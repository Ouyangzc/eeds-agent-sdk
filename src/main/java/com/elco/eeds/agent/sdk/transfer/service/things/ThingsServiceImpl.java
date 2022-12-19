package com.elco.eeds.agent.sdk.transfer.service.things;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.elco.eeds.agent.sdk.core.bean.properties.PropertiesContext;
import com.elco.eeds.agent.sdk.core.util.ThingsFileUtils;
import com.elco.eeds.agent.sdk.transfer.beans.things.EedsProperties;
import com.elco.eeds.agent.sdk.transfer.beans.things.EedsThings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
            logger.error("存储数据源文件异常,信息:{}", e);
        }
    }

    @Override
    public void addThings(EedsThings addThings) {
        currentThingsList.add(addThings);
        saveThingsFile(JSON.toJSONString(currentThingsList));
    }

    @Override
    public void delThings(EedsThings delThings) {
        currentThingsList = currentThingsList.stream().filter(things -> !things.getThingsId().equals(delThings.getThingsId())).collect(Collectors.toList());
        saveThingsFile(JSON.toJSONString(currentThingsList));
    }

    @Override
    public void editThings(EedsThings editThings) {
        currentThingsList = currentThingsList.stream().filter(things -> !things.getThingsId().equals(editThings.getThingsId())).collect(Collectors.toList());
        currentThingsList.add(editThings);
        saveThingsFile(JSON.toJSONString(currentThingsList));
    }

    @Override
    public void addProperties(String thingsId, EedsProperties addProperties) {
        for (EedsThings currentThings : currentThingsList) {
            if (thingsId.equals(currentThings.getThingsId())) {
                List<EedsProperties> properties = currentThings.getProperties();
                properties.add(addProperties);
            }
        }
        saveThingsFile(JSON.toJSONString(currentThingsList));
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
            }
        }
        saveThingsFile(JSON.toJSONString(currentThingsList));
    }

    @Override
    public void editProperties(String thingsId, EedsProperties editProperties) {
        Iterator<EedsThings> thingsIterators = currentThingsList.iterator();
        while (thingsIterators.hasNext()) {
            EedsThings currentThings = thingsIterators.next();
            if (thingsId.equals(currentThings.getThingsId())) {
                List<EedsProperties> currentThingsProperties = currentThings.getProperties();
                Iterator<EedsProperties> propertiesIterators = currentThingsProperties.iterator();
                while (propertiesIterators.hasNext()) {
                    EedsProperties eedsProperties = propertiesIterators.next();
                    if (editProperties.getPropertiesId().equals(eedsProperties.getPropertiesId())) {
                        propertiesIterators.remove();
                        currentThingsProperties.add(editProperties);
                    }
                }
            }
        }
        saveThingsFile(JSON.toJSONString(currentThingsList));
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
                Long timeStamp = properties.stream().map(EedsProperties::getTimestamp).max(Long::compareTo).get();
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
}
