package com.elco.eeds.agent.sdk.transfer.handler.things;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.elco.eeds.agent.sdk.core.connect.ThingsConnectionHandler;
import com.elco.eeds.agent.sdk.core.connect.manager.ConnectManager;
import com.elco.eeds.agent.sdk.core.start.AgentStartProperties;
import com.elco.eeds.agent.sdk.transfer.beans.message.things.ThingsReconnectManualMessage;
import com.elco.eeds.agent.sdk.transfer.beans.things.ThingsDriverContext;
import com.elco.eeds.agent.sdk.transfer.handler.IReceiverMessageHandler;
import com.elco.eeds.agent.sdk.transfer.service.things.ThingsSyncServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @title: ThingsReconnectManualMessageHandler
 * @Author wl
 * @Date: 2023/1/6 14:24
 * @Version 1.0
 * @Description: 数据源手动重连处理类
 */
public class ThingsReconnectManualMessageHandler implements IReceiverMessageHandler {

  private static final Logger logger = LoggerFactory
      .getLogger(ThingsReconnectManualMessageHandler.class);

  @Override
  public void handleRecData(String topic, String recData) {
    logger.info("收到数据源手动重连报文：topic: {}, msg: {}", topic, recData);

    try {
      // 校验报文是否格式是否满足
      ThingsReconnectManualMessage message = JSON
          .parseObject(recData, ThingsReconnectManualMessage.class);
      String thingsId = message.getData().getThingsId();
      // 获取handler
      ThingsConnectionHandler handler = ConnectManager.getHandler(thingsId);
      if (ObjectUtil.isEmpty(handler)) {
        //获取不到数据源Handler，重新连接
        ThingsDriverContext thingsDriverContext = ThingsSyncServiceImpl.THINGS_DRIVER_CONTEXT_MAP
            .get(thingsId);
        ConnectManager
            .create(thingsDriverContext, AgentStartProperties.getInstance().getAgentClientType());
      }
      // 数据源重连
      handler.reconnect();
    } catch (Exception e) {
      logger.error("数据源手动重连报文处理异常：", e);
      e.printStackTrace();
    }
  }
}
