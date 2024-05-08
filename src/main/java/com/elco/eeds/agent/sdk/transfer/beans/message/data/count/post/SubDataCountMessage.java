package com.elco.eeds.agent.sdk.transfer.beans.message.data.count.post;

import com.elco.eeds.agent.sdk.core.util.MapstructUtils;
import com.elco.eeds.agent.sdk.transfer.beans.data.count.PostDataCount;
import com.elco.eeds.agent.sdk.transfer.beans.data.count.ThingsDataCount;
import java.io.Serializable;
import java.util.List;

/**
 * @ClassName SubDataCountMessage
 * @Description 统计报文发送结构体
 * @Author OUYANG
 * @Date 2022/12/9 13:22
 */
public class SubDataCountMessage implements Serializable {

  /**
   * 客户端ID
   */
  private String agentId;
  /**
   * 统计ID
   */
  private String countId;
  /**
   * 统计开始时间
   */
  private Long startTime;
  /**
   * 统计结束时间
   */
  private Long endTime;
  /**
   * 统计集合
   */
  private List<ThingsDataCount> thingsCountList;

  public String getAgentId() {
    return agentId;
  }

  public void setAgentId(String agentId) {
    this.agentId = agentId;
  }

  public String getCountId() {
    return countId;
  }

  public void setCountId(String countId) {
    this.countId = countId;
  }

  public Long getStartTime() {
    return startTime;
  }

  public void setStartTime(Long startTime) {
    this.startTime = startTime;
  }

  public Long getEndTime() {
    return endTime;
  }

  public void setEndTime(Long endTime) {
    this.endTime = endTime;
  }

  public List<ThingsDataCount> getThingsCountList() {
    return thingsCountList;
  }

  public void setThingsCountList(List<ThingsDataCount> thingsCountList) {
    this.thingsCountList = thingsCountList;
  }

  /**
   * 获取子消息
   *
   * @param dataCount
   * @return
   */
  public static SubDataCountMessage getSubMsg(PostDataCount dataCount) {
    SubDataCountMessage subMsg = MapstructUtils.dataCountToMsg(dataCount);
    return subMsg;
  }
}
