package com.elco.eeds.agent.sdk.transfer.beans.things;

import java.io.Serializable;

/**
 * @ClassName BaseThings
 * @Description 数据源基类
 * @Author OUYANG
 * @Date 2022/12/16 9:07
 */
public class BaseThings implements Serializable {
    /**
     * 客户端ID
     */
    private String agentId;
    /**
     * 数据源ID
     */
    private String thingsId;
    /**
     * 设备类型
     * {@link }
     */
    private String thingsType;
    /**
     * 是否支持监控
     * {@link }
     */
    private String isMonitoring;

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getThingsId() {
        return thingsId;
    }

    public void setThingsId(String thingsId) {
        this.thingsId = thingsId;
    }

    public String getThingsType() {
        return thingsType;
    }

    public void setThingsType(String thingsType) {
        this.thingsType = thingsType;
    }

    public String getIsMonitoring() {
        return isMonitoring;
    }

    public void setIsMonitoring(String isMonitoring) {
        this.isMonitoring = isMonitoring;
    }
}
