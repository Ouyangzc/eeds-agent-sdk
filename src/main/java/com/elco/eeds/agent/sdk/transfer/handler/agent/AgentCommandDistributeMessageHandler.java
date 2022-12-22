package com.elco.eeds.agent.sdk.transfer.handler.agent;

import com.alibaba.fastjson.JSON;
import com.elco.eeds.agent.sdk.transfer.beans.message.agent.command.AgentCommandMessage;
import com.elco.eeds.agent.sdk.transfer.handler.IReceiverMessageHandler;

/**
 * @title: CommandDistributeMessageHandler
 * @Author wl
 * @Date: 2022/12/21 17:16
 * @Version 1.0
 * @Description: 客户端指令下发处理类
 */
public class AgentCommandDistributeMessageHandler implements IReceiverMessageHandler {


    @Override
    public void handleRecData(String topic, String recData) {
        logger.info("收到客户端全局配置报文：topic: {}, msg: {}", topic, recData);
        AgentCommandMessage agentCommandMessage = JSON.parseObject(recData, AgentCommandMessage.class);

        // TODO

    }
}
