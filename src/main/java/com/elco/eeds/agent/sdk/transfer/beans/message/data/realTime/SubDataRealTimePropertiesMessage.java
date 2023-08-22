package com.elco.eeds.agent.sdk.transfer.beans.message.data.realTime;

import java.io.Serializable;

/**
 * @ClassName SubDataRealTimePropertiesMessage
 * @Description 实时变量数据结构体
 * @Author OUYANG
 * @Date 2022/12/9 14:27
 */
public class SubDataRealTimePropertiesMessage implements Serializable {
    /**
     * 客户端Id
     */
    private String agentId;
    /**
     * 数据源Id
     */
    private String thingsId;
    /**
     * 变量ID
     */
    private String propertiesId;
    /**
     * 变量地址
     */
    private String address;
    /**
     * 变量数据类型
     */
    private String type;
    /**
     * 变量值
     */
    private String value;

    /**
     * 1实际变量  2虚拟变量
     */
    private String isVirtual;

    /**
     * 采集时间戳
     */
    private Long timestamp;

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

    public String getPropertiesId() {
        return propertiesId;
    }

    public void setPropertiesId(String propertiesId) {
        this.propertiesId = propertiesId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getIsVirtual() {
        return isVirtual;
    }

    public void setIsVirtual(String isVirtual) {
        this.isVirtual = isVirtual;
    }
}
