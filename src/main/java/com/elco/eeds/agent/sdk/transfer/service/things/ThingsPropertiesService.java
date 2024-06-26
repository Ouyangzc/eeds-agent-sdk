package com.elco.eeds.agent.sdk.transfer.service.things;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.elco.eeds.agent.sdk.core.bean.agent.Agent;
import com.elco.eeds.agent.sdk.core.bean.properties.PropertiesContext;
import com.elco.eeds.agent.sdk.core.util.MapstructUtils;
import com.elco.eeds.agent.sdk.core.util.ThingsFileUtils;
import com.elco.eeds.agent.sdk.transfer.beans.things.EedsProperties;
import com.elco.eeds.agent.sdk.transfer.beans.things.EedsThings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
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
            logger.error("存储数据源文件异常,信息:", e);
        }
    }

    public void addThings(EedsThings addThings) {
        currentThingsList.add(addThings);
    }

    public void delThings(String delThingsId) {
        if (ObjectUtil.isNotEmpty(currentThingsList) && currentThingsList.size() > 0 && currentThingsList.stream().anyMatch(t -> t.getThingsId().equals(delThingsId))) {
            currentThingsList = currentThingsList.stream().filter(things -> !things.getThingsId().equals(delThingsId)).collect(Collectors.toList());
        }
    }

    public void editThings(EedsThings editThings) {
        Optional<EedsThings> first = currentThingsList.stream().filter(things -> things.getThingsId().equals(editThings.getThingsId())).findFirst();
        if (first.isPresent()) {
            EedsThings sourceThings = first.get();
            EedsThings eedsThings = MapstructUtils.thingsToThings(editThings);
            //原有点位
            List<EedsProperties> sourceProperties = sourceThings.getProperties();
            eedsThings.setProperties(sourceProperties);
            currentThingsList = currentThingsList.stream().filter(things -> !things.getThingsId().equals(eedsThings.getThingsId())).collect(Collectors.toList());
            currentThingsList.add(eedsThings);
        }
    }

    public void addProperties(String thingsId, EedsProperties addProperties) {
        for (EedsThings currentThings : currentThingsList) {
            if (thingsId.equals(currentThings.getThingsId())) {
                List<EedsProperties> properties = currentThings.getProperties();
                String agentId = Agent.getInstance().getAgentBaseInfo().getAgentId();
                String thingsType = currentThings.getThingsType();
                PropertiesContext propertiesContext =MapstructUtils.syncPropToContext(addProperties,agentId,thingsId,thingsType);
                ThingsSyncNewServiceImpl.PROPERTIES_CONTEXT_MAP.put(addProperties.getPropertiesId(), propertiesContext);
                if (ObjectUtil.isEmpty(properties)) {
                    ArrayList<EedsProperties> eedsProperties = new ArrayList<>();
                    eedsProperties.add(addProperties);
                    currentThings.setProperties(eedsProperties);
                } else {
                    Optional<EedsProperties> optional = properties.stream().filter(p -> p.getPropertiesId().equals(addProperties.getPropertiesId())).findAny();
                    if (!optional.isPresent()){
                        properties.add(addProperties);
                    }
                }
            }
        }
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
    }

    public Long getThingsChangeTime() {
        try {
            String localThings = getThingsFile();
            if (StrUtil.isEmpty(localThings)) {
                return 0L;
            }
            Long maxTimeStamp = 0L;
            List<EedsThings> eedsThings = JSON.parseArray(localThings, EedsThings.class);
            Optional<Long> optional = eedsThings.stream().map(EedsThings::getTimestamp).max(Long::compareTo);
            if (!optional.isPresent()) {
                return maxTimeStamp;
            }
            maxTimeStamp = optional.get();
            for (EedsThings things : eedsThings) {
                List<EedsProperties> properties = things.getProperties();
                if (ObjectUtil.isNotEmpty(properties)) {
                    Optional<Long> longOptional = properties.stream().map(EedsProperties::getTimestamp).max(Long::compareTo);
                    if (!longOptional.isPresent()){
                        return maxTimeStamp;
                    }
                    Long timeStamp = optional.get();
                    if (timeStamp > maxTimeStamp) {
                        maxTimeStamp = timeStamp;
                    }
                }
            }
            return maxTimeStamp;
        } catch (Exception e) {
            logger.error("获取数据源文件失败，异常：", e);
            return 0L;
        }
    }

    public boolean checkThingsExist(String thingsId) {
        Optional<EedsThings> optional = currentThingsList.stream().filter(eedsThings -> eedsThings.getThingsId().equals(thingsId)).findAny();
        return optional.isPresent();
    }

    public List<EedsThings> getCurrentThingsList() {
        return currentThingsList;
    }
}
