package com.elco.eeds.agent.sdk.core.quartz.bean;

import java.io.Serializable;
import java.util.Map;

/**
 * @ClassName JobBeanBase
 * @Description job基础bean
 * @Author OuYang
 * @Date 2024/4/24 15:27
 * @Version 1.0
 */
public class JobBeanBase implements Serializable {
  protected String jobName;

  protected String jobGroup;

  protected Map<String, Object> extraMap;

  public String getJobName() {
    return jobName;
  }

  public void setJobName(String jobName) {
    this.jobName = jobName;
  }

  public String getJobGroup() {
    return jobGroup;
  }

  public void setJobGroup(String jobGroup) {
    this.jobGroup = jobGroup;
  }

  public Map<String, Object> getExtraMap() {
    return extraMap;
  }

  public void setExtraMap(Map<String, Object> extraMap) {
    this.extraMap = extraMap;
  }
}
