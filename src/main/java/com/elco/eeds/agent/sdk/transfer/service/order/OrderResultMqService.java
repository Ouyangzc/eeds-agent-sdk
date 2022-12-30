package com.elco.eeds.agent.sdk.transfer.service.order;

import cn.hutool.core.thread.ThreadUtil;
import com.alibaba.fastjson.JSON;
import com.elco.eeds.agent.mq.nats.plugin.NatsPlugin;
import com.elco.eeds.agent.mq.plugin.MQPluginManager;
import com.elco.eeds.agent.mq.plugin.MQServicePlugin;
import com.elco.eeds.agent.sdk.core.common.constant.message.ConstantTopic;
import com.elco.eeds.agent.sdk.transfer.beans.message.order.OrderResultMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @title: OrderResultMqService
 * @Author wl
 * @Date: 2022/12/29 11:26
 * @Version 1.0
 */
public class OrderResultMqService {

    public static final Logger logger = LoggerFactory.getLogger(OrderResultMqService.class);

    public static void send(String message) {
        ThreadUtil.execute(new Runnable() {
            @Override
            public void run() {
                OrderResultMessage orderResultMessage = JSON.parseObject(message, OrderResultMessage.class);
                String topic = ConstantTopic.TOPIC_AGENT_AGENT_ORDER_RESPOND + orderResultMessage.getData().getThingsId();
                MQServicePlugin mqPlugin = MQPluginManager.getMQPlugin(NatsPlugin.class.getName());
                mqPlugin.publish(topic, message, null);
                logger.info("发送指令下发结果报文：topic: {}; message: {}", topic, message);
            }
        });
    }

}
