package com.elco.eeds.agent.sdk.transfer.handler;

import com.alibaba.fastjson.JSON;
import com.elco.eeds.agent.mq.nats.plugin.NatsPlugin;
import com.elco.eeds.agent.mq.plugin.MQPluginManager;
import com.elco.eeds.agent.mq.plugin.MQServicePlugin;
import com.elco.eeds.agent.mq.plugin.ReceiverMessagerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @title: IReceiverMessageHandler
 * @Author wl
 * @Date: 2022/12/6 10:53
 * @Version 1.0
 * @Description: MQ消息回调接口
 */
public interface IReceiverMessageHandler extends ReceiverMessagerHandler {

    Logger logger = LoggerFactory.getLogger(IReceiverMessageHandler.class);

    /**
     * 推送消息
     * @param topic        主题
     * @param messageJson  消息体
     */
    default void publishMessage(String topic, String messageJson) {
        MQServicePlugin mqPlugin = MQPluginManager.getMQPlugin(NatsPlugin.class.getName());
        String msg = JSON.toJSONString(messageJson);
        logger.info("发送报文：topic:{}, msg:{}", topic, msg);
        mqPlugin.publish(topic, msg, null);
    }

    /**
     * 日志打印
     * @param messageType   消息类型
     * @param topic         topic
     * @param msg           消息
     */
    default void printLog(String messageType, String topic, String msg) {
        logger.info("topic:{}, messageType:{}, msg:{}", topic, messageType, msg);
    }

}
