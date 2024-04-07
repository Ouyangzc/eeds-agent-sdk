package com.elco.eeds.agent.sdk.core.common.enums;

/**
 * @ClassName InvokeServiceEnum
 * @Description 本地调用枚举
 * @Author OuYang
 * @Date 2024/4/1 13:47
 * @Version 1.0
 */
public enum InvokeServiceEnum {
  REGISTER("com.elco.eeds.base.application.impl.AgentsApplicationImpl", "autoAddAgents","com.elco.eeds.base.domain.dto.agents.AutoAddAgentsDTO","com.elco.eeds.base.config.mq.confignats.AutoAgentsVO"),
  UPDATE_TOKEN("com.elco.eeds.base.application.impl.AgentsApplicationImpl",
      "updateAfterFinishToken","com.elco.eeds.base.domain.dto.agents.UpdateAgentsFinishToken",""),
  THINGS_SYNC("com.elco.eeds.base.application.impl.ThingsApplicationImpl","getThingsSyncInfo","com.elco.eeds.base.domain.dto.sync.SyncThingsData","com.elco.eeds.base.config.mq.thingsProperties.MetaData")
      ;

  private String className;

  private String methodName;

  private String parameterTypes;

  private String returnType;

  InvokeServiceEnum(String className, String methodName, String parameterTypes,
      String returnType) {
    this.className = className;
    this.methodName = methodName;
    this.parameterTypes = parameterTypes;
    this.returnType = returnType;
  }

  public String getMethodName() {
    return methodName;
  }

  public String getClassName() {
    return className;
  }

  public String getParameterTypes() {
    return parameterTypes;
  }

  public String getReturnType() {
    return returnType;
  }
}
