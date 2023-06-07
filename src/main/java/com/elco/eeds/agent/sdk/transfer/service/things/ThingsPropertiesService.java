package com.elco.eeds.agent.sdk.transfer.service.things;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.elco.eeds.agent.sdk.core.bean.properties.PropertiesContext;
import com.elco.eeds.agent.sdk.core.common.constant.ConstantThings;
import com.elco.eeds.agent.sdk.core.connect.manager.ConnectManager;
import com.elco.eeds.agent.sdk.core.util.ThingsFileUtils;
import com.elco.eeds.agent.sdk.transfer.beans.things.EedsProperties;
import com.elco.eeds.agent.sdk.transfer.beans.things.EedsThings;
import com.elco.eeds.agent.sdk.transfer.beans.things.ThingsDriverContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName ThingsPropertiesService
 * @Description 数据源变量操作类
 * @Author OuYang
 * @Date 2023/5/29 11:03
 * @Version 1.0
 */
public class ThingsPropertiesService {
    private static final Logger logger = LoggerFactory.getLogger(ThingsPropertiesService.class);
    private List<EedsThings> currentThingsList;


    public String getThingsFile() throws IOException {
        String thingsFile = ThingsFileUtils.readLocalThingsFile();
        currentThingsList = JSON.parseArray(thingsFile, EedsThings.class);
        if (ObjectUtil.isEmpty(currentThingsList)) {
            currentThingsList = new ArrayList<>();
        }
        return thingsFile;
    }

    public void saveToFile(String data) {
        try {
            ThingsFileUtils.saveThingsFileToLocal(data);
        } catch (IOException e) {
            logger.error("存储数据源文件异常,信息:{}", e);
        }
    }

    public void addThings(EedsThings addThings) {
        currentThingsList.add(addThings);
        saveToFile(JSONUtil.toJsonStr(currentThingsList));
    }

    public void delThings(String delThingsId) {
        if (ObjectUtil.isNotEmpty(currentThingsList) && currentThingsList.size() > 0 && currentThingsList.stream().anyMatch(t -> t.getThingsId().equals(delThingsId))) {
            currentThingsList = currentThingsList.stream().filter(things -> !things.getThingsId().equals(delThingsId)).collect(Collectors.toList());
            saveToFile(JSONUtil.toJsonStr(currentThingsList));
        }
    }

    public void editThings(EedsThings editThings) {
        Optional<EedsThings> first = currentThingsList.stream().filter(things -> things.getThingsId().equals(editThings.getThingsId())).findFirst();
        if (first.isPresent()) {
            EedsThings sourceThings = first.get();
            //原有点位
            List<EedsProperties> sourceProperties = sourceThings.getProperties();

            Map<String, List<EedsProperties>> operatorTypeMap = editThings.getProperties().stream().collect(Collectors.groupingBy(EedsProperties::getOperatorType));
            for (String operatorType : operatorTypeMap.keySet()) {
                List<EedsProperties> editProperties = operatorTypeMap.get(operatorType);
                if (ConstantThings.P_OPERATOR_TYPE_ADD.equals(operatorType)) {
                    //新增点位
                    sourceProperties.addAll(editProperties);
                } else {
                    //删除点位，匹配删除
                    for (EedsProperties properties : editProperties) {
                        sourceProperties = sourceProperties.stream().filter(p -> !p
                                .getPropertiesId().equals(properties.getPropertiesId())).collect(Collectors.toList());
                    }
                }
            }
            editThings.setProperties(sourceProperties);
            currentThingsList = currentThingsList.stream().filter(things -> !things.getThingsId().equals(editThings.getThingsId())).collect(Collectors.toList());
            currentThingsList.add(editThings);
            saveToFile(JSONUtil.toJsonStr(currentThingsList));
        }
    }

    public void addProperties(String thingsId, EedsProperties addProperties) {
        for (EedsThings currentThings : currentThingsList) {
            if (thingsId.equals(currentThings.getThingsId())) {
                List<EedsProperties> properties = currentThings.getProperties();
                PropertiesContext propertiesContext = new PropertiesContext();
                BeanUtil.copyProperties(addProperties, propertiesContext);
                propertiesContext.setThingsId(thingsId);
                ThingsSyncNewServiceImpl.PROPERTIES_CONTEXT_MAP.put(addProperties.getPropertiesId(), propertiesContext);
                if (ObjectUtil.isEmpty(properties)) {
                    ArrayList<EedsProperties> eedsProperties = new ArrayList<>();
                    eedsProperties.add(addProperties);
                    currentThings.setProperties(eedsProperties);
                } else {
                    properties.add(addProperties);
                }
            }
        }
        saveToFile(JSONUtil.toJsonStr(currentThingsList));
    }

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
            }
        }
        if (currentThingsList.size() > 0) {
            saveToFile(JSONUtil.toJsonStr(currentThingsList));
        } else {
            saveToFile("");
        }
    }

    public Long getThingsChangeTime() {
        try {
            String localThings = getThingsFile();
            if (StrUtil.isEmpty(localThings)) {
                return 0L;
            }
            Long maxTimeStamp = 0L;
            List<EedsThings> eedsThings = JSON.parseArray(localThings, EedsThings.class);
            maxTimeStamp = eedsThings.stream().map(EedsThings::getTimestamp).max(Long::compareTo).get();
            for (EedsThings things : eedsThings) {
                List<EedsProperties> properties = things.getProperties();
                if (ObjectUtil.isNotEmpty(properties)) {
                    Long timeStamp = properties.stream().map(EedsProperties::getTimestamp).max(Long::compareTo).get();
                    if (timeStamp > maxTimeStamp) {
                        maxTimeStamp = timeStamp;
                    }
                }
            }
            return maxTimeStamp;
        } catch (Exception e) {
            logger.error("获取数据源文件失败，异常：{}", e);
            return 0L;
        }
    }

    public boolean checkThingsExist(String thingsId) {
        Optional<EedsThings> optional = currentThingsList.stream().filter(eedsThings -> eedsThings.getThingsId().equals(thingsId)).findAny();
        return optional.isPresent();
    }

}
