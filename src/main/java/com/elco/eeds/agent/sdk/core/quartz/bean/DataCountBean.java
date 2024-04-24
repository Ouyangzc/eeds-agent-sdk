package com.elco.eeds.agent.sdk.core.quartz.bean;

import com.elco.eeds.agent.sdk.core.bean.agent.Agent;
import com.elco.eeds.agent.sdk.core.common.enums.QuartzEnum;
import java.io.Serializable;
import java.util.HashMap;

/**
 * @ClassName DataCountBean
 * @Description 数据统计Job bean
 * @Author OuYang
 * @Date 2024/4/24 15:50
 * @Version 1.0
 */
public class DataCountBean extends JobBeanBase implements Serializable {

  private Long countPeriod;

  public DataCountBean() {
    this.countPeriod = Long.valueOf(Agent.getInstance().getAgentBaseInfo().getSyncPeriod()) / 1000L;

    this.extraMap = new HashMap<>();
    this.jobGroup = QuartzEnum.DATA_COUNT_GROUP.getValue();
    this.jobName = QuartzEnum.DATA_COUNT_JOB.getValue();
  }

  public Long getCountPeriod() {
    return countPeriod;
  }
}
