package com.elco.eeds.agent.sdk.core.quartz.bean;

import com.elco.eeds.agent.sdk.core.common.enums.QuartzEnum;
import com.elco.eeds.agent.sdk.core.util.JSONUtils;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName CmdTimeOutBean
 * @Description 下发指定超时job bean
 * @Author OuYang
 * @Date 2024/4/24 15:26
 * @Version 1.0
 */
public class CmdTimeOutBean extends JobBeanBase implements Serializable {

  String msgSeqNo;
  String thingsId;
  Integer timeout;

  public CmdTimeOutBean(String msgSeqNo, String thingsId, Integer timeout) {
    this.msgSeqNo = msgSeqNo;
    this.thingsId = thingsId;
    this.timeout = timeout;
    this.extraMap = new HashMap<>();
    this.jobGroup = QuartzEnum.CMD_TIME_OUT_GROUP.getValue();
    this.jobName = QuartzEnum.CMD_TIME_OUT_JOB.getValue()+msgSeqNo;
  }

  public String getMsgSeqNo() {
    return msgSeqNo;
  }

  public void setMsgSeqNo(String msgSeqNo) {
    this.msgSeqNo = msgSeqNo;
  }

  public String getThingsId() {
    return thingsId;
  }

  public void setThingsId(String thingsId) {
    this.thingsId = thingsId;
  }

  public Integer getTimeout() {
    return timeout;
  }

  public void setTimeout(Integer timeout) {
    this.timeout = timeout;
  }

  @Override
  public String toString() {
    return JSONUtils.toJsonStr(this);
  }


  public static final class CmdTimeOutBeanBaseBuilder {

    private String msgSeqNo;
    private String thingsId;
    private Integer timeout;
    private String jobName;
    private String jobGroup;
    private Map<String, Object> extraMap;

    private CmdTimeOutBeanBaseBuilder() {
    }

    public static CmdTimeOutBeanBaseBuilder create() {
      return new CmdTimeOutBeanBaseBuilder();
    }

    public CmdTimeOutBeanBaseBuilder msgSeqNo(String msgSeqNo) {
      this.msgSeqNo = msgSeqNo;
      return this;
    }

    public CmdTimeOutBeanBaseBuilder thingsId(String thingsId) {
      this.thingsId = thingsId;
      return this;
    }

    public CmdTimeOutBeanBaseBuilder timeout(Integer timeout) {
      this.timeout = timeout;
      return this;
    }

    public CmdTimeOutBeanBaseBuilder extraMap(Map<String, Object> extraMap) {
      this.extraMap = extraMap;
      return this;
    }

    public CmdTimeOutBean build() {
      CmdTimeOutBean cmdTimeOutBean = new CmdTimeOutBean(msgSeqNo, thingsId, timeout);
      cmdTimeOutBean.setJobGroup(QuartzEnum.CMD_TIME_OUT_GROUP.getValue());
      cmdTimeOutBean.setJobName(QuartzEnum.CMD_TIME_OUT_JOB.getValue()+msgSeqNo);
      return cmdTimeOutBean;
    }
  }
}
