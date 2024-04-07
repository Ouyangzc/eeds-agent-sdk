package com.elco.eeds.agent.sdk.transfer.service.order;

import cn.hutool.core.thread.ThreadUtil;
import com.elco.eeds.agent.sdk.core.common.constant.message.ConstantTopic;
import com.elco.eeds.agent.sdk.core.util.MqPluginUtils;
import com.elco.eeds.agent.sdk.transfer.beans.message.order.OrderConfirmMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ：ytl
 * @date ：Created in 2022/12/21 11:46
 * @description：
 */
public class OrderConfirmMqService {

  public static final Logger logger = LoggerFactory.getLogger(OrderConfirmMqService.class);

  public static void send(String thingsId, String msgSeqNo) {
    ThreadUtil.execute(new Runnable() {
      @Override
      public void run() {
        String message = OrderConfirmMessage.create(thingsId, msgSeqNo).toJson();
        String topic = ConstantTopic.TOPIC_AGENT_AGENT_ORDER_CONFIRM + thingsId;
        MqPluginUtils.sendOrderConfirmMsg(topic, message);
        logger.info("发送指令下发确认报文：{}", message);
      }
    });
  }
}
