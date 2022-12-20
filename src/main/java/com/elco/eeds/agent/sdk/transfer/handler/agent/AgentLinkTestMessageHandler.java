package com.elco.eeds.agent.sdk.transfer.handler.agent;

import com.alibaba.fastjson.JSON;
import com.elco.eeds.agent.sdk.transfer.beans.message.agent.linktest.AgentLinkTestMessage;
import com.elco.eeds.agent.sdk.transfer.handler.IReceiverMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @title: AgentLinkTestMessageHandler
 * @Author wl
 * @Date: 2022/12/19 11:54
 * @Version 1.0
 * @Description: 客户端链接测试报文处理类
 */
public class AgentLinkTestMessageHandler implements IReceiverMessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(AgentLinkTestMessageHandler.class);

    @Override
    public void handleRecData(String topic, String recData) {
        logger.info("收到客户端链接测试报文：topic: {}, msg: {}", topic, recData);
        AgentLinkTestMessage agentLinkTestMessage = JSON.parseObject(recData, AgentLinkTestMessage.class);


    }
}
