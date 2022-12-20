package com.elco.eeds.agent.sdk.transfer.service.things;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.elco.eeds.agent.sdk.core.bean.agent.Agent;
import com.elco.eeds.agent.sdk.core.bean.agent.AgentBaseInfo;
import com.elco.eeds.agent.sdk.core.bean.properties.PropertiesContext;
import com.elco.eeds.agent.sdk.core.common.constant.ConstantThings;
import com.elco.eeds.agent.sdk.core.common.constant.http.ConstantHttpApiPath;
import com.elco.eeds.agent.sdk.core.connect.ThingsConnection;
import com.elco.eeds.agent.sdk.core.connect.manager.ConnectManager;
import com.elco.eeds.agent.sdk.core.util.ThingsFileUtils;
import com.elco.eeds.agent.sdk.transfer.beans.message.things.SubThingsSyncIncrMessage;
import com.elco.eeds.agent.sdk.transfer.beans.things.EedsProperties;
import com.elco.eeds.agent.sdk.transfer.beans.things.EedsThings;
import com.elco.eeds.agent.sdk.transfer.beans.things.ThingsDriverContext;
import com.elco.eeds.agent.sdk.transfer.beans.things.ThingsSyncRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @ClassName ThingsSyncServiceImpl
 * @Description 数据源同步逻辑类
 * @Author OUYANG
 * @Date 2022/12/16 13:27
 */
public class ThingsSyncServiceImpl implements ThingsSyncService {

    private static final Logger logger = LoggerFactory.getLogger(ThingsSyncServiceImpl.class);

    public static final Map<String, List<PropertiesContext>> CONTEXTS_MAP = new ConcurrentHashMap<>();

    public static final Map<String, PropertiesContext> PROPERTIES_CONTEXT_MAP = new ConcurrentHashMap<>();
    public static final Map<String, ThingsDriverContext> THINGS_DRIVER_CONTEXT_MAP = new ConcurrentHashMap<>();

    private ThingsServiceImpl thingsService;

    public ThingsSyncServiceImpl(ThingsServiceImpl thingsService) {
        this.thingsService = thingsService;
    }

    @Override
    public void setupSyncThings() {
        Long thingsChangeTime = thingsService.getThingsChangeTime();
        AgentBaseInfo agentBaseInfo = Agent.getInstance().getAgentBaseInfo();
        String agentId = agentBaseInfo.getAgentId();
        String token = agentBaseInfo.getToken();
        ThingsSyncRequest thingsRequest = new ThingsSyncRequest();
        thingsRequest.setAgentId(Long.valueOf(agentId));
        thingsRequest.setLastTime(thingsChangeTime);
        List<EedsThings> setupSyncData = this.getSetupSyncData(thingsRequest, token);
        List<PropertiesContext> propertiesContextList = this.convertData(setupSyncData);
        this.handleSyncThingsData(setupSyncData, propertiesContextList);
        //重新加载数据源文件
        this.loadThingDriver();
    }

    @Override
    public void incrSyncThings(SubThingsSyncIncrMessage message) {
        Long thingsChangeTime = thingsService.getThingsChangeTime();
        AgentBaseInfo agentBaseInfo = Agent.getInstance().getAgentBaseInfo();
        String agentId = agentBaseInfo.getAgentId();
        String token = agentBaseInfo.getToken();
        ThingsSyncRequest thingsRequest = new ThingsSyncRequest();
        thingsRequest.setTaskId(message.getTaskId());
        thingsRequest.setTableSearch(message.getTableSearch());
        thingsRequest.setAgentId(Long.valueOf(agentId));
        thingsRequest.setLastTime(thingsChangeTime);
        List<EedsThings> setupSyncData = this.getSetupSyncData(thingsRequest, token);
        List<PropertiesContext> propertiesContextList = this.convertData(setupSyncData);
        this.handleSyncThingsData(setupSyncData, propertiesContextList);
        if ("1".equals(message.getTableSearch())) {
            for (EedsThings eedsThings : setupSyncData) {
                String thingsId = eedsThings.getThingsId();
                boolean addResult = eedsThings.getProperties().stream().allMatch(things -> things.getOperatorType().equals(ConstantThings.P_OPERATOR_TYPE_ADD));
                if (addResult) {
                    //增量新增数据源
                    ThingsDriverContext driverContext = new ThingsDriverContext();
                    BeanUtil.copyProperties(eedsThings, driverContext);
                    THINGS_DRIVER_CONTEXT_MAP.put(thingsId, driverContext);
                    //todo 加载数据源连接
                    ThingsConnection connection = ConnectManager.getConnection("10312");
                    connection.connect(driverContext);

                }
                boolean delResult = eedsThings.getProperties().stream().allMatch(things -> things.getOperatorType().equals(ConstantThings.P_OPERATOR_TYPE_DEL));
                if (delResult) {
                    //增量新增数据源
                    ThingsDriverContext driverContext = new ThingsDriverContext();
                    BeanUtil.copyProperties(eedsThings, driverContext);
                    THINGS_DRIVER_CONTEXT_MAP.remove(thingsId);
                    //todo 断开数据源连接
                    ThingsConnection connection = ConnectManager.getConnection("10312");
                    connection.disconnect();
                }
                boolean editResult = eedsThings.getProperties().stream().allMatch(things -> things.getOperatorType().equals(ConstantThings.P_OPERATOR_TYPE_EDIT));
                if (editResult) {
                    ThingsDriverContext driverContext = new ThingsDriverContext();
                    BeanUtil.copyProperties(eedsThings, driverContext);
                    THINGS_DRIVER_CONTEXT_MAP.put(thingsId, driverContext);
                    //todo 数据源重新连接
                }
            }
        }
    }


    public List<EedsThings> getSetupSyncData(ThingsSyncRequest request, String token) {
        String datas = ThingsRequestHttpService.getThingsSyncData(request, token, ConstantHttpApiPath.THINGS_SETUP_SYNC_API);
        if (datas != null) {
            List<EedsThings> edgeThings = JSON.parseArray(datas, EedsThings.class);
            return edgeThings;
        }
        return null;
    }


    public List<EedsThings> getSyncData(ThingsSyncRequest request, String token) {
        String datas = ThingsRequestHttpService.getThingsSyncData(request, token, ConstantHttpApiPath.THINGS_INCR_SYNC_API);
        if (datas != null) {
            List<EedsThings> edgeThings = JSON.parseArray(datas, EedsThings.class);
            return edgeThings;
        }
        return null;
    }


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
                PROPERTIES_CONTEXT_MAP.put(p.getPropertiesId(), propertiesContext);
            }
        }
        return propertiesContextList;
    }


    public boolean saveToLocalFile(String thingsData) throws Exception {
        ThingsFileUtils.saveThingsFileToLocal(thingsData);
        return true;
    }


    public String getLocalThings() throws IOException {
        return ThingsFileUtils.readLocalThingsFile();
    }

    public boolean handleSyncThingsData(List<EedsThings> syncThingsList, List<PropertiesContext> propertiesContexts) {
        List<PropertiesContext> addList = propertiesContexts.stream().filter(p -> p.getOperatorType().equals(ConstantThings.P_OPERATOR_TYPE_ADD)).collect(Collectors.toList());
        List<PropertiesContext> editList = propertiesContexts.stream().filter(p -> p.getOperatorType().equals(ConstantThings.P_OPERATOR_TYPE_EDIT)).collect(Collectors.toList());
        List<PropertiesContext> delList = propertiesContexts.stream().filter(p -> p.getOperatorType().equals(ConstantThings.P_OPERATOR_TYPE_DEL)).collect(Collectors.toList());
        try {
            String localThings = thingsService.getThingsFile();
            if (StrUtil.isEmpty(localThings)) {
                //暂无本地文件，所有数据都是新增
                ArrayList<EedsThings> result = new ArrayList<>();
                //存储数据源，并添加缓存
                if (!addList.isEmpty()) {
                    List<EedsThings> addThings = this.getSyncThings(syncThingsList, addList);
                    result.addAll(addThings);
                }
                if (!editList.isEmpty()) {
                    List<EedsThings> editThings = getSyncThings(syncThingsList, editList);
                    result.addAll(editThings);
                }
                if (!result.isEmpty()) {
                    saveToLocalFile(JSON.toJSONString(result));
                }
            } else {
                //有本地数据
                List<EedsThings> localThingsList = JSON.parseArray(localThings, EedsThings.class);
                //处理删除点位
                for (PropertiesContext delProperties : delList) {
                    //删除
                    EedsProperties eedsProperties = getEedsProperties(localThingsList, delProperties);
                    if (!ObjectUtil.isEmpty(eedsProperties)) {
                        PROPERTIES_CONTEXT_MAP.remove(delProperties.getPropertiesId());
                        thingsService.delProperties(delProperties.getThingsId(), eedsProperties);
                    }
                }
                for (PropertiesContext editProperties : editList) {
                    //编辑
                    PROPERTIES_CONTEXT_MAP.put(editProperties.getPropertiesId(), editProperties);
                    EedsProperties eedsProperties = getEedsProperties(localThingsList, editProperties);
                    thingsService.editProperties(editProperties.getThingsId(), eedsProperties);
                }
                for (PropertiesContext addProperties : addList) {
                    //新增
                    PROPERTIES_CONTEXT_MAP.put(addProperties.getPropertiesId(), addProperties);
                    EedsProperties eedsProperties = getEedsProperties(syncThingsList, addProperties);
                    thingsService.addProperties(addProperties.getThingsId(), eedsProperties);
                }
            }
        } catch (Exception e) {
            logger.error("数据同步处理数据异常，异常信息：{}", e);
            return false;
        }
        return true;
    }

    private List<EedsThings> getSyncThings(List<EedsThings> syncThingsList, List<PropertiesContext> propertiesContextList) {
        //根据数据源id进行分组
        Map<String, List<PropertiesContext>> thingsIdMap = propertiesContextList.stream().collect(Collectors.groupingBy(PropertiesContext::getThingsId));
        for (String thingsId : thingsIdMap.keySet()) {
            //获取该数据源原始数据
            EedsThings things = syncThingsList.stream().filter(syncThings -> syncThings.getThingsId().equals(thingsId)).findFirst().get();
            //获取所有要新增得点位
            List<String> propertiesIdList = thingsIdMap.get(thingsId).stream().map(PropertiesContext::getPropertiesId).collect(Collectors.toList());
            List<EedsProperties> properties = things.getProperties();
            List<EedsProperties> eedsProperties = properties.stream().filter(p -> propertiesIdList.contains(p.getPropertiesId())).collect(Collectors.toList());
            things.setProperties(eedsProperties);
        }
        return syncThingsList;
    }

    private EedsProperties getEedsProperties(List<EedsThings> localThingsList, PropertiesContext delProperties) {
        String thingsId = delProperties.getThingsId();
        Optional<EedsThings> optional = localThingsList.stream().filter(syncThings -> syncThings.getThingsId().equals(thingsId)).findFirst();
        if (optional.isPresent()) {
            EedsThings things = optional.get();
            EedsProperties properties = things.getProperties().stream().filter(eedsProperties -> eedsProperties.getPropertiesId().equals(delProperties.getPropertiesId())).findFirst().get();
            return properties;
        }
        return null;
    }

    private void loadThingDriver() {
        try {
            String localThings = getLocalThings();
            List<EedsThings> eedsThings = JSON.parseArray(localThings, EedsThings.class);
            for (EedsThings things : eedsThings) {
                ThingsDriverContext driverContext = new ThingsDriverContext();
                BeanUtil.copyProperties(things, driverContext);
                String agentId = things.getAgentId();
                String thingsId = things.getThingsId();
                THINGS_DRIVER_CONTEXT_MAP.put(things.getThingsId(), driverContext);
                //todo 调用数据源连接
                ThingsConnection connection = ConnectManager.getConnection("10312");
                connection.connect(driverContext);

                List<EedsProperties> properties = things.getProperties();
                for (EedsProperties p : properties) {
                    PropertiesContext propertiesContext = new PropertiesContext();
                    BeanUtil.copyProperties(p, propertiesContext);
                    propertiesContext.setAgentId(agentId);
                    propertiesContext.setThingsId(thingsId);
                    propertiesContext.setThingsType(things.getThingsType());
                    PROPERTIES_CONTEXT_MAP.put(p.getPropertiesId(), propertiesContext);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
