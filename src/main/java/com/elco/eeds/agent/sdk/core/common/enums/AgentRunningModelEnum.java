package com.elco.eeds.agent.sdk.core.common.enums;

/**
 * @ClassName AgentRunningModelEnum
 * @Description 客户端运行模式
 * @Author OuYang
 * @Date 2024/4/1 8:52
 * @Version 1.0
 */
public enum AgentRunningModelEnum {
  SLIM("SLIM", "瘦身版"),
  CLUSTER("CLUSTER", "集群版"),

  SINGLE("SINGLE", "单机版");

  private String runningModel;
  private String desc;

  AgentRunningModelEnum(String runningModel, String desc) {
    this.runningModel = runningModel;
    this.desc = desc;
  }

  public String getRunningModel() {
    return runningModel;
  }
}
