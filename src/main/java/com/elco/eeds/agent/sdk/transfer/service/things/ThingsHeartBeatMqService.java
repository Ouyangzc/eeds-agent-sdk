package com.elco.eeds.agent.sdk.transfer.service.things;

import cn.hutool.core.thread.ThreadUtil;
import com.elco.eeds.agent.sdk.core.bean.agent.Agent;
import com.elco.eeds.agent.sdk.core.common.constant.message.ConstantTopic;
import com.elco.eeds.agent.sdk.core.util.MqPluginUtils;
import com.elco.eeds.agent.sdk.transfer.beans.message.things.ThingsHeartBeatMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ：ytl
 * @date ：Created in 2022/12/21 11:46
 * @description：
 */
public class ThingsHeartBeatMqService {

  public static final Logger logger = LoggerFactory.getLogger(ThingsHeartBeatMqService.class);
  private static final String CONNECT = "2";
  private static final String DIS_CONNECT = "3";

  private static void send(String thingsId, String status) {
    ThreadUtil.execute(new Runnable() {
      @Override
      public void run() {
        String message = ThingsHeartBeatMessage.create(thingsId, status).toJson();
        String topic = ConstantTopic.TOPIC_THINGS_HEARTBEAT_REQUEST + Agent.getInstance()
            .getAgentBaseInfo().getAgentId();
        MqPluginUtils.sendThingsHeartBeatMsg(topic, message);
//        logger.debug("发送数据源心跳报文：{}", message);
      }
    });
  }

  public static void sendConnectMsg(String thingsId) {
    send(thingsId, CONNECT);
  }

  public static void sendDisConnectMsg(String thingsId) {
    send(thingsId, DIS_CONNECT);
  }
}
