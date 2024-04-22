package com.elco.eeds.agent.sdk.transfer.beans.message.storage;

import com.elco.eeds.agent.sdk.core.bean.agent.Agent;
import java.io.Serializable;

/**
 * @ClassName StorageDbChangeResultMessage
 * @Description 切换数据库 结果报文
 * @Author OuYang
 * @Date 2024/4/19 9:52
 * @Version 1.0
 */
public class SubStorageDbChangeResultMessage implements Serializable {

  /**
   * 数据库存储主键ID
   */
  private long pkStorage;
  /**
   * 客户端ID
   */
  private String agentId;
  /**
   * 切换结果
   */
  private String result;

  public SubStorageDbChangeResultMessage(long pkStorage, String result) {
    this.pkStorage = pkStorage;
    this.agentId = Agent.getInstance().getAgentBaseInfo().getAgentId();
    this.result = result;
  }

  public long getPkStorage() {
    return pkStorage;
  }

  public void setPkStorage(long pkStorage) {
    this.pkStorage = pkStorage;
  }

  public String getAgentId() {
    return agentId;
  }

  public void setAgentId(String agentId) {
    this.agentId = agentId;
  }

  public String getResult() {
    return result;
  }

  public void setResult(String result) {
    this.result = result;
  }
}
