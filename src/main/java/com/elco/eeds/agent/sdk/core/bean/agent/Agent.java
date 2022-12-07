package com.elco.eeds.agent.sdk.core.bean.agent;

import java.io.Serializable;

/**
 * @title: Agent
 * @Author wl
 * @Date: 2022/12/6 9:16
 * @Version 1.0
 * @Description: 客户端Agent类（单例模式）
 */
public class Agent implements Serializable {

    private static volatile Agent agent;

    private AgentBaseInfo agentBaseInfo;

    private AgentMqInfo agentMqInfo;

    public static Agent getInstance() {
        if(agent == null) {
            synchronized (Agent.class) {
                if(agent == null) {
                    agent = new Agent();
                }
            }
        }
        return agent;
    }

    public AgentBaseInfo getAgentBaseInfo() {
        return agentBaseInfo;
    }

    public void setAgentBaseInfo(AgentBaseInfo agentBaseInfo) {
        this.agentBaseInfo = agentBaseInfo;
    }

    public AgentMqInfo getAgentMqInfo() {
        return agentMqInfo;
    }

    public void setAgentMqInfo(AgentMqInfo agentMqInfo) {
        this.agentMqInfo = agentMqInfo;
    }

    @Override
    public String toString() {
        return "Agent{" +
                "agentBaseInfo=" + agentBaseInfo +
                ", agentMqInfo=" + agentMqInfo +
                '}';
    }
}
