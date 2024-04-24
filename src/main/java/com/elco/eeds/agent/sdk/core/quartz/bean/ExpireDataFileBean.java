package com.elco.eeds.agent.sdk.core.quartz.bean;

import com.elco.eeds.agent.sdk.core.common.enums.QuartzEnum;
import java.io.Serializable;
import java.util.HashMap;

/**
 * @ClassName DelExpireDataFileBean
 * @Description 过期文件删除
 * @Author OuYang
 * @Date 2024/4/24 16:09
 * @Version 1.0
 */
public class ExpireDataFileBean extends JobBeanBase implements Serializable {

  /**
   * 默认1小时
   */
  private Integer period = Integer.valueOf(1);

  public ExpireDataFileBean() {
    this.period = period;
    this.extraMap = new HashMap<>();
    this.jobGroup = QuartzEnum.EXPIRE_DATA_FILE_GROUP.getValue();
    this.jobName = QuartzEnum.EXPIRE_DATA_FILE_JOB.getValue();
  }

  public Integer getPeriod() {
    return period;
  }
}
