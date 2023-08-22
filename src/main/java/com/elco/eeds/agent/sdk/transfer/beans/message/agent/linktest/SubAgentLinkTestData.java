package com.elco.eeds.agent.sdk.transfer.beans.message.agent.linktest;

import java.io.Serializable;
import java.util.List;

/**
 * @title: SubAgentLinkTestData
 * @Author wl
 * @Date: 2022/12/20 15:57
 * @Version 1.0
 */
public class SubAgentLinkTestData implements Serializable {

    private SubAgentLinkTestStatus agentStatus;

    private List<SubAgentLinkTestThingsData> thingsData;

    public SubAgentLinkTestStatus getAgentStatus() {
        return agentStatus;
    }

    public void setAgentStatus(SubAgentLinkTestStatus agentStatus) {
        this.agentStatus = agentStatus;
    }

    public List<SubAgentLinkTestThingsData> getThingsData() {
        return thingsData;
    }

    public void setThingsData(List<SubAgentLinkTestThingsData> thingsData) {
        this.thingsData = thingsData;
    }
}
