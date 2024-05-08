package com.elco.eeds.agent.sdk.transfer.service.things;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.elco.eeds.agent.sdk.core.common.constant.ConstantThings;
import com.elco.eeds.agent.sdk.core.connect.ThingsConnection;
import com.elco.eeds.agent.sdk.core.connect.manager.ConnectManager;
import com.elco.eeds.agent.sdk.core.start.AgentStartProperties;
import com.elco.eeds.agent.sdk.core.util.MapstructUtils;
import com.elco.eeds.agent.sdk.core.util.ThingsFileUtils;
import com.elco.eeds.agent.sdk.transfer.beans.things.EedsProperties;
import com.elco.eeds.agent.sdk.transfer.beans.things.EedsThings;
import com.elco.eeds.agent.sdk.transfer.beans.things.ThingsDriverContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @ClassName ThingsServiceImpl
 * @Description 数据源逻辑处理类
 * @Author OUYANG
 * @Date 2022/12/19 9:04
 */
public class ThingsServiceImpl implements ThingsService {

    private static final Logger logger = LoggerFactory.getLogger(ThingsServiceImpl.class);
    private List<EedsThings> currentThingsList;

    @Override
    public String getThingsFile() throws IOException {
        String thingsFile = ThingsFileUtils.readLocalThingsFile();
        currentThingsList = JSON.parseArray(thingsFile, EedsThings.class);
        return thingsFile;
    }

    @Override
    public void saveThingsFile(String data) {
        try {
            ThingsFileUtils.saveThingsFileToLocal(data);
        } catch (IOException e) {
            logger.error("存储数据源文件异常,信息:", e);
        }
    }

    @Override
    public void addThings(EedsThings addThings) {
        currentThingsList.add(addThings);
        saveThingsFile(JSONUtil.toJsonStr(currentThingsList));
    }

    @Override
    public void delThings(EedsThings delThings) {
        currentThingsList = currentThingsList.stream().filter(things -> !things.getThingsId().equals(delThings.getThingsId())).collect(Collectors.toList());
        saveThingsFile(JSONUtil.toJsonStr(currentThingsList));
    }

    @Override
    public void editThings(EedsThings editThings) {
        currentThingsList = currentThingsList.stream().filter(things -> !things.getThingsId().equals(editThings.getThingsId())).collect(Collectors.toList());
        currentThingsList.add(editThings);
        saveThingsFile(JSONUtil.toJsonStr(currentThingsList));
    }

    @Override
    public void addProperties(String thingsId, EedsProperties addProperties, List<EedsThings> syncThingsList) {
        boolean flag = true;
        for (EedsThings currentThings : currentThingsList) {
            if (thingsId.equals(currentThings.getThingsId())) {
                List<EedsProperties> properties = currentThings.getProperties();
                Optional<EedsProperties> optional = properties.stream().filter(p -> p.getPropertiesId().equals(addProperties.getPropertiesId())).findFirst();
                if (optional.isPresent()) {
                    delProperties(thingsId, addProperties);
                    properties.add(addProperties);
                } else {
                    properties.add(addProperties);
                }
                flag = false;
            }
        }
        if (flag) {
            //新增things
            Optional<EedsThings> optional = syncThingsList.stream().filter(eedsThings -> eedsThings.getThingsId().equals(thingsId)).findFirst();
            if (!optional.isPresent()){
                return;
            }
            EedsThings things = optional.get();
            List<EedsProperties> eedsPropertiesList = things.getProperties().stream().filter(p -> p.getOperatorType().equals(ConstantThings.P_OPERATOR_TYPE_ADD)).collect(Collectors.toList());
            things.setProperties(eedsPropertiesList);
            this.addThings(things);
            ThingsConnection connection = ConnectManager.getConnection(AgentStartProperties.getInstance().getAgentClientType());
            if (connection.checkThingsConnectParams(things)) {
                //增量新增数据源
                ThingsDriverContext driverContext =  MapstructUtils.syncThingsToThingsDriver(things);
                ThingsSyncServiceImpl.THINGS_DRIVER_CONTEXT_MAP.put(thingsId, driverContext);
                ConnectManager.delConnection(thingsId);
                ConnectManager.create(driverContext, AgentStartProperties.getInstance().getAgentClientType());
            }
        } else {
            //新增点位
            saveThingsFile(JSONUtil.toJsonStr(currentThingsList));
        }
    }

    @Override
    public void delProperties(String thingsId, EedsProperties delProperties) {
        Iterator<EedsThings> thingsIterators = currentThingsList.iterator();
        while (thingsIterators.hasNext()) {
            EedsThings currentThings = thingsIterators.next();
            if (thingsId.equals(currentThings.getThingsId())) {
                List<EedsProperties> currentThingsProperties = currentThings.getProperties();
                Iterator<EedsProperties> propertiesIterators = currentThingsProperties.iterator();
                while (propertiesIterators.hasNext()) {
                    EedsProperties eedsProperties = propertiesIterators.next();
                    if (delProperties.getPropertiesId().equals(eedsProperties.getPropertiesId())) {
                        propertiesIterators.remove();
                    }
                }
                if (currentThingsProperties.isEmpty()) {
                    thingsIterators.remove();
                    //增量新增数据源
                    if (!checkThingsExist(thingsId)) {
                        //该数据源不存在，则删除数据源
                        ThingsSyncServiceImpl.THINGS_DRIVER_CONTEXT_MAP.remove(thingsId);
                        ConnectManager.delConnection(thingsId);
                    }
                }
            }
        }
        if (currentThingsList.size() > 0) {
            saveThingsFile(JSONUtil.toJsonStr(currentThingsList));
        } else {
            saveThingsFile("");
        }

    }

    @Override
    public void editProperties(EedsThings things) {
        String thingsId = things.getThingsId();
        Iterator<EedsThings> thingsIterators = currentThingsList.iterator();
        while (thingsIterators.hasNext()) {
            EedsThings currentThings = thingsIterators.next();
            if (thingsId.equals(currentThings.getThingsId())) {
                thingsIterators.remove();
            }
        }
        this.addThings(things);
        saveThingsFile(JSONUtil.toJsonStr(currentThingsList));
    }

    /**
     * 获取数据源最新变动时间戳
     *
     * @return
     */
    public Long getThingsChangeTime() {
        try {
            String localThings = getThingsFile();
            if (StrUtil.isEmpty(localThings)) {
                return 0L;
            }
            Long maxTimeStamp = 0L;
            List<EedsThings> eedsThings = JSON.parseArray(localThings, EedsThings.class);
            for (EedsThings things : eedsThings) {
                List<EedsProperties> properties = things.getProperties();
                Optional<Long> optional = properties.stream().map(EedsProperties::getTimestamp).max(Long::compareTo);
                if (!optional.isPresent()){
                    return maxTimeStamp;
                }
                Long timeStamp = optional.get();
                if (timeStamp > maxTimeStamp) {
                    maxTimeStamp = timeStamp;
                }
            }
            return maxTimeStamp;
        } catch (Exception e) {
            logger.error("获取数据源文件失败，异常：{}", e);
            return 0L;
        }
    }

    public boolean checkThingsExist(String thingsId){
        Optional<EedsThings> optional = currentThingsList.stream().filter(eedsThings -> eedsThings.getThingsId().equals(thingsId)).findAny();
        return optional.isPresent();
    }
}
