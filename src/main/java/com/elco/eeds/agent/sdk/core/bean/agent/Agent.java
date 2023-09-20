package com.elco.eeds.agent.sdk.core.bean.agent;

import com.elco.eeds.agent.sdk.core.common.enums.AgentStatus;
import com.elco.eeds.agent.sdk.core.parsing.DataParsing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * @title: Agent
 * @Author wl
 * @Date: 2022/12/6 9:16
 * @Version 1.0
 * @Description: 客户端Agent类（单例模式）
 */
public class Agent implements Serializable {

  private static Logger logger = LoggerFactory.getLogger(Agent.class);

  private static volatile Agent agent;

  private AgentBaseInfo agentBaseInfo;

  private AgentMqInfo agentMqInfo;

  private AgentStatus agentStatus;

  public DataParsing getDataParsing() {
    return dataParsing;
  }

  public void setDataParsing(DataParsing dataParsing) {
    this.dataParsing = dataParsing;
  }

  private DataParsing dataParsing;

  public static Agent getInstance() {
    if (agent == null) {
      synchronized (Agent.class) {
        if (agent == null) {
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

  public AgentStatus getAgentStatus() {
    return agentStatus;
  }

  public void setAgentStatus(AgentStatus agentStatus) {
		logger.info("客户端状态发生变化,当前状态为:{}", agentStatus);
		this.agentStatus = agentStatus;
  }

  @Override
  public String toString() {
    return "Agent{" +
        "agentBaseInfo=" + agentBaseInfo +
        ", agentMqInfo=" + agentMqInfo +
        '}';
  }
}
