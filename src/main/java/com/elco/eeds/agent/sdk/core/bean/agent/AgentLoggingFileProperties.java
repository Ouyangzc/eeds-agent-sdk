package com.elco.eeds.agent.sdk.core.bean.agent;

import java.io.Serializable;

/**
 * @ClassName AgentLogProperties
 * @Description 日志文件配置，可支持文件大小，及缓存周期
 * @Author OuYang
 * @Date 2024/3/13 13:20
 * @Version 1.0
 */
public class AgentLoggingFileProperties implements Serializable {

  /**
   * 保留天数
   */
  private Integer maxHistory = 7;
  /**
   * 文件大小
   */
  private String maxFileSize = "250MB";
  /**
   * 总大小
   */
  private String totalSizeCap;

  public Integer getMaxHistory() {
    return maxHistory;
  }

  public void setMaxHistory(Integer maxHistory) {
    this.maxHistory = maxHistory;
  }

  public String getMaxFileSize() {
    return maxFileSize;
  }

  public void setMaxFileSize(String maxFileSize) {
    this.maxFileSize = maxFileSize;
  }

  public String getTotalSizeCap() {
    return totalSizeCap;
  }

  public void setTotalSizeCap(String totalSizeCap) {
    this.totalSizeCap = totalSizeCap;
  }
}
