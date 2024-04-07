package com.elco.eeds.agent.sdk.transfer.handler;

import com.elco.eeds.agent.mq.nats.plugin.NatsPlugin;
import com.elco.eeds.agent.mq.plugin.MQPluginManager;
import com.elco.eeds.agent.mq.plugin.MQServicePlugin;
import com.elco.eeds.agent.mq.plugin.ReceiverMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @title: IReceiverMessageHandler
 * @Author wl
 * @Date: 2022/12/6 10:53
 * @Version 1.0
 * @Description: MQ消息回调接口
 */
public abstract class IReceiverMessageHandler implements ReceiverMessageHandler {

  public static final Logger logger = LoggerFactory.getLogger(IReceiverMessageHandler.class);

  /**
   * 推送消息
   *
   * @param topic       主题
   * @param messageJson 消息体
   */
  public void publishMessage(String topic, String messageJson) {
    MQServicePlugin mqPlugin = MQPluginManager.getMQPlugin(NatsPlugin.class.getName());
    logger.debug("发送报文：topic:{}, msg:{}", topic, messageJson);
    mqPlugin.publish(topic, messageJson, null);
  }

  /**
   * 日志打印
   *
   * @param messageType 消息类型
   * @param topic       topic
   * @param msg         消息
   */
  public void printLog(String messageType, String topic, String msg) {
    logger.debug("topic:{}, messageType:{}, msg:{}", topic, messageType, msg);
  }

}
