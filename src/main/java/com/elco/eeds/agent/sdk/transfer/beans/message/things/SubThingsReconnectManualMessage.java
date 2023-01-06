package com.elco.eeds.agent.sdk.transfer.beans.message.things;

/**
 * @title: SubThingsReconnectManualMessage
 * @Author wl
 * @Date: 2023/1/6 14:31
 * @Version 1.0
 */
public class SubThingsReconnectManualMessage {

    private String agentId;

    private String thingsId;

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
}
