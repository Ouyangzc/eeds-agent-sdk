package com.elco.eeds.agent.sdk.transfer.handler.agent;

import com.alibaba.fastjson.JSON;
import com.elco.eeds.agent.sdk.core.common.constant.message.ConstantTopic;
import com.elco.eeds.agent.sdk.transfer.beans.message.heart.SubAgentHeartMessage;
import com.elco.eeds.agent.sdk.transfer.handler.IReceiverMessageHandler;
import com.elco.eeds.agent.sdk.transfer.service.AgentRequestHttpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @title: AgentHeartMessageHandler
 * @Author wl
 * @Date: 2022/12/6 9:05
 * @Version 1.0
 * @Description: 客户端心跳报文处理类
 */
public class AgentHeartMessageHandler implements IReceiverMessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(AgentHeartMessageHandler.class);

    @Override
    public void handleRecData(String topic, String recData) {
        logger.info("收到客户端token报文：topic: {}, msg: {}", topic, recData);
        // 收到ping，发送pong
        SubAgentHeartMessage rspMessage = SubAgentHeartMessage.getResponseMessage();
        this.publishMessage(ConstantTopic.TOPIC_AGENT_TOKEN, JSON.toJSONString(rspMessage));
    }
}
