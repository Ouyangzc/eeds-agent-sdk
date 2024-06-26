package com.elco.eeds.agent.sdk.transfer.handler.things;

import com.alibaba.fastjson.JSON;
import com.elco.eeds.agent.sdk.transfer.beans.message.things.ThingsSyncIncrMessage;
import com.elco.eeds.agent.sdk.transfer.handler.IReceiverMessageHandler;
import com.elco.eeds.agent.sdk.transfer.service.things.ThingsSyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName ThingsSyncIncrMessageHandler
 * @Description 数据源增量同步报文处理类
 * @Author OUYANG
 * @Date 2022/12/19 14:31
 */
public class ThingsSyncIncrMessageHandler extends IReceiverMessageHandler {

  private static Logger logger = LoggerFactory.getLogger(ThingsSyncIncrMessageHandler.class);

  private ThingsSyncService thingsSyncService;

  public ThingsSyncIncrMessageHandler(ThingsSyncService thingsSyncService) {
    this.thingsSyncService = thingsSyncService;
  }

  @Override
  public void handleRecData(String topic, String recData) {
    ThingsSyncIncrMessage message = JSON.parseObject(recData, ThingsSyncIncrMessage.class);
    logger.info("接收到数据源信息同步消息，topic:{},data:{}", topic, message);
    try {
      thingsSyncService.incrSyncThings(message.getData());
    } catch (Exception e) {
      logger.error("数据源同步发生异常,异常", e);
    }
  }
}
