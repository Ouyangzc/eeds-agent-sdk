package com.elco.eeds.agent.sdk.transfer.service.data.sync;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName DataSyncService
 * @Description 数据同步处理类
 * @Author OUYANG
 * @Date 2022/12/15 10:02
 */
public class DataSyncService {

  public static final Logger logger = LoggerFactory.getLogger(DataSyncService.class);
  private Boolean syncFlag;

  private Boolean status;

  private String queueId;

  public DataSyncService() {
    syncFlag = false;
    status = true;
  }

  public Boolean getSyncFlag() {
    return syncFlag;
  }

  public void setSyncFlag(Boolean syncFlag) {
    this.syncFlag = syncFlag;
  }

  public Boolean getStatus() {
    return status;
  }

  public void setStatus(Boolean status) {
    this.status = status;
  }

  public String getQueueId() {
    return queueId;
  }

  public void setQueueId(String queueId) {
    this.queueId = queueId;
  }


}
