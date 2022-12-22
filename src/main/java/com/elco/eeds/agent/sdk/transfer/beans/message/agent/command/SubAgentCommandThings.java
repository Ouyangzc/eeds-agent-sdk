package com.elco.eeds.agent.sdk.transfer.beans.message.agent.command;

/**
 * @title: SubAgentCommandThings
 * @Author wl
 * @Date: 2022/12/22 8:52
 * @Version 1.0
 * @Description: 下发报文子报文中的things属性
 */
public class SubAgentCommandThings {

    /**
     * 数据源名称
     */
    private String name;

    /**
     * 数据源ID
     */
    private String thingsId;

    /**
     * 协议类型
     */
    private String thingTypes;

    /**
     * 客户端ID
     */
    private String agentId;

    /**
     * 数据源code
     */
    private String thingCode;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThingsId() {
        return thingsId;
    }

    public void setThingsId(String thingsId) {
        this.thingsId = thingsId;
    }

    public String getThingTypes() {
        return thingTypes;
    }

    public void setThingTypes(String thingTypes) {
        this.thingTypes = thingTypes;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getThingCode() {
        return thingCode;
    }

    public void setThingCode(String thingCode) {
        this.thingCode = thingCode;
    }
}
