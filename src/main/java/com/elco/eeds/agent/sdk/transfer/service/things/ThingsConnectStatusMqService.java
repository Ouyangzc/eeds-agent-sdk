package com.elco.eeds.agent.sdk.transfer.service.things;

import cn.hutool.core.thread.ThreadUtil;
import com.elco.eeds.agent.mq.nats.plugin.NatsPlugin;
import com.elco.eeds.agent.mq.plugin.MQPluginManager;
import com.elco.eeds.agent.mq.plugin.MQServicePlugin;
import com.elco.eeds.agent.sdk.core.common.constant.message.ConstantTopic;
import com.elco.eeds.agent.sdk.transfer.beans.message.things.ThingsConnectStatusMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ：ytl
 * @date ：Created in 2022/12/21 11:46
 * @description：
 */
public class ThingsConnectStatusMqService {

  public static final Logger logger = LoggerFactory.getLogger(ThingsConnectStatusMqService.class);

  private static final String CONNECT = "2";
  private static final String DIS_CONNECT = "3";

  private static final String CONNECTING = "5";

  private static void send(String thingsId, String status, String msg) {
    String json = ThingsConnectStatusMessage.create(thingsId, status, msg).toJson();
    MQServicePlugin mqPlugin = MQPluginManager.getMQPlugin(NatsPlugin.class.getName());
    mqPlugin.publish(ConstantTopic.TOPIC_THINGS_CONNECTSTATUS_REQUEST + thingsId
        , json, null);
    logger.info("发送数据源连接状态报文：{}", json);
  }

  public static void sendConnectMsg(String thingsId) {
    send(thingsId, CONNECT, "数据源已连接");
  }

  public static void sendDisConnectMsg(String thingsId) {
    sendDisConnectMsg(thingsId, "数据源已断开");
  }
  public static void sendDisConnectMsg(String thingsId,String connectMsg) {
    send(thingsId, DIS_CONNECT, connectMsg);
  }

  public static void sendConnectingMsg(String thingsId) {
    send(thingsId, CONNECTING, "数据源连接中");
  }
}
