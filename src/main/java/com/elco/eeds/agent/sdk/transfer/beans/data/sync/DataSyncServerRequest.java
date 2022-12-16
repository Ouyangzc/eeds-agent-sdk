package com.elco.eeds.agent.sdk.transfer.beans.data.sync;

import com.elco.eeds.agent.sdk.core.bean.properties.PropertiesContext;

import java.util.List;

/**
 * @ClassName DataSyncServerRequest
 * @Description 数据同步请求--数据源
 * @Author OUYANG
 * @Date 2022/12/9 14:17
 */
public class DataSyncServerRequest {
    /**
     * 数据源ID
     */
    private String thingsId;
    /**
     * 同步数据开始时间
     */
    private Long startTime;

    /**
     * 同步数据结束时间
     */
    private Long endTime;

    /**
     * 需过滤变量点
     */
    private List<PropertiesContext> properties;

    public String getThingsId() {
        return thingsId;
    }

    public void setThingsId(String thingsId) {
        this.thingsId = thingsId;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public List<PropertiesContext> getProperties() {
        return properties;
    }

    public void setProperties(List<PropertiesContext> properties) {
        this.properties = properties;
    }
}
