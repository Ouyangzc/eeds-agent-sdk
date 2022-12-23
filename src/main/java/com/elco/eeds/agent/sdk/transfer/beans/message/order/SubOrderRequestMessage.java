package com.elco.eeds.agent.sdk.transfer.beans.message.order;

import java.util.List;

/**
 * @author ：ytl
 * @date ：Created in 2022/12/23 13:38
 * @description：下发指令确认报文
 */
public class SubOrderRequestMessage {
    private String msgSeqNo;

    public String getMsgSeqNo() {
        return msgSeqNo;
    }

    public void setMsgSeqNo(String msgSeqNo) {
        this.msgSeqNo = msgSeqNo;
    }

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

    public List<OrderPropertiesValue> getProperties() {
        return properties;
    }

    public void setProperties(List<OrderPropertiesValue> properties) {
        this.properties = properties;
    }

    private String name;
    private String thingsId;
    private String thingTypes;
    private String agentId;
    private String thingCode;
    List<OrderPropertiesValue>properties;
}

//
//class PropertiesEntity{
//
//
//}
