package com.elco.eeds.agent.sdk.transfer.service.data.realtime;

import com.elco.eeds.agent.sdk.core.bean.agent.Agent;
import com.elco.eeds.agent.sdk.core.bean.properties.PropertiesContext;
import com.elco.eeds.agent.sdk.core.bean.properties.PropertiesValue;
import com.elco.eeds.agent.sdk.core.disruptor.RealTimeDataDisruptorServer;
import com.elco.eeds.agent.sdk.core.util.AgentResourceUtils;
import com.elco.eeds.agent.sdk.transfer.service.data.count.RealTimeDataStatisticsDeque;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName AbstractRealTimeService
 * @Description 实时数据抽象类
 * @Author OuYang
 * @Date 2024/4/7 16:35
 * @Version 1.0
 */
public abstract class AbstractRealTimeService implements RealTimeService {

  private static Logger logger = LoggerFactory.getLogger(AbstractRealTimeService.class);
  private boolean isLocalCache;


  private boolean runningModel;

  private String agentId;

  public AbstractRealTimeService() {
    this.isLocalCache = AgentResourceUtils.getAgentConfigLocalCache();
    this.runningModel = AgentResourceUtils.isSlimModle();
    this.agentId = Agent.getInstance().getAgentBaseInfo().getAgentId();
    if(this.runningModel){
      RealTimeDataDisruptorServer disruptorServer = RealTimeDataDisruptorServer.getInstance2();
      disruptorServer.doStart();
    }
  }

  /**
   * @param message               原始报文
   * @param thingsId              数据源ID
   * @param collectTime           采集时间戳
   * @param propertiesValueList   解析数据
   * @param propertiesContextList 变量信息
   */
  public void proccess(String message, String thingsId, Long collectTime,
      List<PropertiesValue> propertiesValueList, List<PropertiesContext> propertiesContextList) {
    if (propertiesValueList.isEmpty()) {
      return;
    }
    if (!checkCollectTimeValid(collectTime)) {
      return;
    }
    if (isLocalCache) {
      localCache(agentId, thingsId, message, propertiesContextList, propertiesValueList,
          collectTime);
    } else {
      noNeedLocalCache(propertiesContextList, propertiesValueList, collectTime);
    }
    if (runningModel) {
      pushDataToLocally(propertiesValueList);
    }
    pushDataToMQ(agentId, thingsId, propertiesValueList, collectTime);
  }

  /**
   * 本地存储数据
   *
   * @param propertiesValueList
   */
  protected abstract void pushDataToLocally(List<PropertiesValue> propertiesValueList);

  /**
   * 推送数据到MQ
   *
   * @param agentId
   * @param thingsId
   * @param propertiesValueList
   * @param collectTime
   */
  protected abstract void pushDataToMQ(String agentId, String thingsId,
      List<PropertiesValue> propertiesValueList, Long collectTime);

  /**
   * 本地存储：关
   *
   * @param propertiesContextList
   * @param propertiesValueList
   * @param collectTime
   */
  protected abstract void noNeedLocalCache(List<PropertiesContext> propertiesContextList,
      List<PropertiesValue> propertiesValueList, Long collectTime);

  /**
   * 本地存储：开
   *
   * @param agentId
   * @param thingsId
   * @param message
   * @param propertiesContextList
   * @param propertiesValueList
   * @param collectTime
   */
  protected abstract void localCache(String agentId, String thingsId, String message,
      List<PropertiesContext> propertiesContextList, List<PropertiesValue> propertiesValueList,
      Long collectTime);


  @Override
  public boolean checkCollectTimeValid(Long collectTime) {
    long startCollectTime = RealTimeDataStatisticsDeque.collectionStartTime.get();
    if (0==startCollectTime){
      return false;
    }
    if (collectTime<startCollectTime){
      logger.debug("丢弃该消息,时间戳：{},统计开始时间:{}", collectTime, startCollectTime);
      return false;
    }
    return true;
  }
}
