package com.elco.eeds.agent.sdk.transfer.service.things;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.elco.eeds.agent.sdk.core.bean.properties.PropertiesContext;
import com.elco.eeds.agent.sdk.core.common.constant.ConstantThings;
import com.elco.eeds.agent.sdk.core.common.constant.http.ConstantHttpApiPath;
import com.elco.eeds.agent.sdk.core.util.ThingsFileUtils;
import com.elco.eeds.agent.sdk.transfer.beans.things.EedsProperties;
import com.elco.eeds.agent.sdk.transfer.beans.things.EedsThings;
import com.elco.eeds.agent.sdk.transfer.beans.things.ThingsSyncRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @ClassName ThingsSyncServiceImpl
 * @Description TODO
 * @Author OUYANG
 * @Date 2022/12/16 13:27
 */
public class ThingsSyncServiceImpl implements ThingsSyncService {

    private static final Logger logger = LoggerFactory.getLogger(ThingsSyncServiceImpl.class);

    public static final Map<String, List<PropertiesContext>> CONTEXTS_MAP = new ConcurrentHashMap<>();

    @Override
    public List<EedsThings> getSetupSyncData(ThingsSyncRequest request, String token) {
        String datas = ThingsRequestHttpService.getThingsSyncData(request, token, ConstantHttpApiPath.THINGS_SETUP_SYNC_API);
        if (datas != null) {
            List<EedsThings> edgeThings = JSON.parseArray(datas, EedsThings.class);
            return edgeThings;
        }
        return null;
    }

    @Override
    public List<EedsThings> getSyncData(ThingsSyncRequest request, String token) {
        String datas = ThingsRequestHttpService.getThingsSyncData(request, token, ConstantHttpApiPath.THINGS_INCR_SYNC_API);
        if (datas != null) {
            List<EedsThings> edgeThings = JSON.parseArray(datas, EedsThings.class);
            return edgeThings;
        }
        return null;
    }

    @Override
    public List<PropertiesContext> convertData(List<EedsThings> convertData) {
        List<PropertiesContext> propertiesContextList = new ArrayList<>();
        for (EedsThings edgeThings : convertData) {
            String agentId = edgeThings.getAgentId();
            String thingsId = edgeThings.getThingsId();
            List<EedsProperties> properties = edgeThings.getProperties();
            for (EedsProperties p : properties) {
                PropertiesContext propertiesContext = new PropertiesContext();
                BeanUtil.copyProperties(p, propertiesContext);
                propertiesContext.setAgentId(agentId);
                propertiesContext.setThingsId(thingsId);
                propertiesContext.setThingsType(edgeThings.getThingsType());
                propertiesContextList.add(propertiesContext);
            }
        }
        return propertiesContextList;
    }

    @Override
    public boolean saveToLocalFile(String thingsData) throws Exception {

        ThingsFileUtils.saveThingsFileToLocal(thingsData);
        return true;
    }

    @Override
    public String getLocalThings() throws IOException {

        return ThingsFileUtils.readLocalThingsFile();
    }

    @Override
    public boolean handleSyncThingsData(List<PropertiesContext> propertiesContexts) {
        List<PropertiesContext> addList = propertiesContexts.stream().filter(p -> p.getOperatorType().equals(ConstantThings.P_OPERATOR_TYPE_ADD)).collect(Collectors.toList());
        List<PropertiesContext> editList = propertiesContexts.stream().filter(p -> p.getOperatorType().equals(ConstantThings.P_OPERATOR_TYPE_EDIT)).collect(Collectors.toList());
        List<PropertiesContext> delList = propertiesContexts.stream().filter(p -> p.getOperatorType().equals(ConstantThings.P_OPERATOR_TYPE_DEL)).collect(Collectors.toList());

        try {
            String localThings = getLocalThings();
            if (StrUtil.isEmpty(localThings)) {
                //暂无本地文件，所有数据都是新增
                List<PropertiesContext> propertiesContextList = new ArrayList<>();
                propertiesContextList.addAll(addList);
                propertiesContextList.addAll(editList);
                Map<String, PropertiesContext> propertiesMap = propertiesContextList.stream().collect(Collectors.toMap(PropertiesContext::getPropertiesId, Function.identity()));
                if (propertiesMap.size() > 0) {
                    saveToLocalFile(JSON.toJSONString(propertiesMap));
                    //根据协议进行缓存
                    groupThingsByProtocol(propertiesContextList, ConstantThings.P_OPERATOR_TYPE_ADD);
                }
            } else {
                //有本地数据
                Map<String, PropertiesContext> propertiesMap = JSON.parseObject(localThings, new TypeReference<Map<String, PropertiesContext>>() {
                });
                //处理删除点位
                for (PropertiesContext delProperties : delList) {
                    String propertiesId = delProperties.getPropertiesId();
                    propertiesMap.remove(propertiesId);
                    groupThingsByProtocol(delList, ConstantThings.P_OPERATOR_TYPE_DEL);
                }
                for (PropertiesContext editProperties : editList) {
                    String propertiesId = editProperties.getPropertiesId();
                    propertiesMap.remove(propertiesId);
                    propertiesMap.put(propertiesId, editProperties);
                    groupThingsByProtocol(editList, ConstantThings.P_OPERATOR_TYPE_EDIT);
                }
                for (PropertiesContext addProperties : addList) {
                    String propertiesId = addProperties.getPropertiesId();
                    propertiesMap.put(propertiesId, addProperties);
                    groupThingsByProtocol(addList, ConstantThings.P_OPERATOR_TYPE_ADD);
                }
                if (propertiesMap.size() > 0) {
                    ThingsFileUtils.saveThingsFileToLocal(JSON.toJSONString(propertiesMap));
                } else {
                    ThingsFileUtils.saveThingsFileToLocal("");
                }
            }
        } catch (Exception e) {
            logger.error("数据同步处理数据异常，异常信息：{}", e);
            return false;
        }
        return true;
    }

    /**
     * 根据协议进行缓存处理
     *
     * @param propertiesContextList
     */
    private void groupThingsByProtocol(List<PropertiesContext> propertiesContextList, String operatorType) {
        //根据协议划划分
        Map<String, List<PropertiesContext>> protocolMap = propertiesContextList.stream().collect(Collectors.groupingBy(PropertiesContext::getThingsType));
        //新增处理
        if (operatorType.equals(ConstantThings.P_OPERATOR_TYPE_ADD)) {
            handleAddProperties(protocolMap);
        }
        //删除处理
        if (operatorType.equals(ConstantThings.P_OPERATOR_TYPE_DEL)) {
            handleDelProperties(protocolMap);
        }
        //修改处理
        if (operatorType.equals(ConstantThings.P_OPERATOR_TYPE_EDIT)) {
            //先删除再修改
            handleDelProperties(protocolMap);

            handleAddProperties(protocolMap);
        }

    }

    private void handleAddProperties(Map<String, List<PropertiesContext>> protocolMap) {
        CONTEXTS_MAP.putAll(protocolMap);
    }

    private void handleDelProperties(Map<String, List<PropertiesContext>> protocolMap) {
        for (String protocolKey : protocolMap.keySet()) {
            //取出删除协议变量集合
            List<PropertiesContext> delProperties = protocolMap.get(protocolKey);
            //取出该协议缓存变量集合
            List<PropertiesContext> cacheProperties = CONTEXTS_MAP.get(protocolKey);
            if (!CollectionUtil.isEmpty(cacheProperties) && !CollectionUtil.isEmpty(delProperties)) {
                //取差集
                List<PropertiesContext> diffPropertiesCtx = cacheProperties.stream().filter((item) -> !delProperties.stream().map((item2) -> item2.getPropertiesId()).collect(Collectors.toList()).contains(item.getPropertiesId())).collect(Collectors.toList());
                CONTEXTS_MAP.put(protocolKey, diffPropertiesCtx);
            }
        }
    }
}
