package com.elco.eeds.agent.sdk.core.quartz.bean;

import com.elco.eeds.agent.sdk.core.common.constant.ReadTypeEnums;
import com.elco.eeds.agent.sdk.core.common.enums.QuartzEnum;
import com.elco.eeds.agent.sdk.core.util.JSONUtils;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName RealTimeReadBean
 * @Description 实时数据读取bean
 * @Author OuYang
 * @Date 2024/4/24 14:53
 * @Version 1.0
 */
public class RealTimeReadBean extends JobBeanBase implements Serializable {
  private String thingsId;

  private ReadTypeEnums readTypeEnums;

  /**
   * 表达式
   */
  private String cron;


  public RealTimeReadBean(String thingsId, ReadTypeEnums readTypeEnums, String cron) {
    this.thingsId = thingsId;
    this.readTypeEnums = readTypeEnums;
    this.cron = cron;
    this.extraMap = new HashMap<>();
    this.jobGroup = QuartzEnum.REALTIME_PROPERTIES_READ_GROUP.getValue();
    this.jobName = QuartzEnum.REALTIME_PROPERTIES_READ_JOB.getValue()+thingsId;
  }

  public String getThingsId() {
    return thingsId;
  }

  public void setThingsId(String thingsId) {
    this.thingsId = thingsId;
  }

  public ReadTypeEnums getReadTypeEnums() {
    return readTypeEnums;
  }

  public void setReadTypeEnums(ReadTypeEnums readTypeEnums) {
    this.readTypeEnums = readTypeEnums;
  }

  public String getCron() {
    return cron;
  }

  public void setCron(String cron) {
    this.cron = cron;
  }

  @Override
  public String toString() {
    return JSONUtils.toJsonStr(this);
  }

  public static final class RealTimeReadBeanBuilder {

    private String thingsId;
    private ReadTypeEnums readTypeEnums;
    private String cron;
    private String jobName;
    private String jobGroup;
    private Map<String, Object> extraMap;

    private RealTimeReadBeanBuilder() {
    }

    public static RealTimeReadBeanBuilder create() {
      return new RealTimeReadBeanBuilder();
    }

    public RealTimeReadBeanBuilder thingsId(String thingsId) {
      this.thingsId = thingsId;
      return this;
    }

    public RealTimeReadBeanBuilder readTypeEnums(ReadTypeEnums readTypeEnums) {
      this.readTypeEnums = readTypeEnums;
      return this;
    }

    public RealTimeReadBeanBuilder cron(String cron) {
      this.cron = cron;
      return this;
    }

    public RealTimeReadBeanBuilder jobName(String jobName) {
      this.jobName = jobName;
      return this;
    }

    public RealTimeReadBeanBuilder jobGroup(String jobGroup) {
      this.jobGroup = jobGroup;
      return this;
    }

    public RealTimeReadBeanBuilder extraMap(Map<String, Object> extraMap) {
      this.extraMap = extraMap;
      return this;
    }

    public RealTimeReadBean build() {
      RealTimeReadBean realTimeReadBean = new RealTimeReadBean(thingsId, readTypeEnums, cron);
      realTimeReadBean.setJobName(jobName);
      realTimeReadBean.setJobGroup(jobGroup);
      realTimeReadBean.setExtraMap(extraMap);
      return realTimeReadBean;
    }
  }
}
