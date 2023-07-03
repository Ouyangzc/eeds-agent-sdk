package com.elco.eeds.agent.sdk.transfer.beans.message.agent.linktest;

import java.io.Serializable;

/**
 * @title: SubAgentLinkTestThingsData
 * @Author wl
 * @Date: 2022/12/20 15:59
 * @Version 1.0
 */
public class SubAgentLinkTestThingsData implements Serializable {

    /**
     * 变量地址
     */
    private String address;
    /**
     * 采集客户端ID
     */
    private String agentId;
    /**
     * 变量Id
     */
    private String propertiesId;
    /**
     * 主数据源ID
     */
    private String thingsId;
    /**
     * 时间戳
     */
    private String timestamp;
    /**
     * 变量数据类型
     */
    private String type;
    /**
     * 值
     */
    private String value;
    /**
     * 1实际变量  2虚拟变量
     */
    private String isVirtual;

    public SubAgentLinkTestThingsData(String address, String agentId, String propertiesId, String thingsId, String timestamp, String type, String value,String isVirtual) {
        this.address = address;
        this.agentId = agentId;
        this.propertiesId = propertiesId;
        this.thingsId = thingsId;
        this.timestamp = timestamp;
        this.type = type;
        this.value = value;
        this.isVirtual = isVirtual;
    }

    public static SubAgentLinkTestThingsData getTestData() {
        return new SubAgentLinkTestThingsData("27", "1606121268447412224", "1611308151217455109", "1611308151204872192", "1673001588047", "int32", "117","1");
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getPropertiesId() {
        return propertiesId;
    }

    public void setPropertiesId(String propertiesId) {
        this.propertiesId = propertiesId;
    }

    public String getThingsId() {
        return thingsId;
    }

    public void setThingsId(String thingsId) {
        this.thingsId = thingsId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
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

    public String getIsVirtual() {
        return isVirtual;
    }

    public void setIsVirtual(String isVirtual) {
        this.isVirtual = isVirtual;
    }
}
