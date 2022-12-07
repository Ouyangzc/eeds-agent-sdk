package com.elco.eeds.agent.sdk.transfer.handler.agent;

import com.alibaba.fastjson.JSON;
import com.elco.eeds.agent.sdk.core.bean.agent.Agent;
import com.elco.eeds.agent.sdk.transfer.beans.message.config.AgentConfigMessage;
import com.elco.eeds.agent.sdk.transfer.handler.IReceiverMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @title: AgentConfigMessageHandler
 * @Author wl
 * @Date: 2022/12/6 9:05
 * @Version 1.0
 * @Description: 客户端全局配置处理类
 */
public class AgentConfigGlobalMessageHandler implements IReceiverMessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(AgentConfigGlobalMessageHandler.class);

    @Override
    public void handleRecData(String topic, String recData) {
        logger.info("收到客户端token报文：topic: {}, msg: {}", topic, recData);

        AgentConfigMessage message = JSON.parseObject(recData, AgentConfigMessage.class);
        Agent agent = Agent.getInstance();
        agent.getAgentBaseInfo().setDataCacheFileSize(message.getData().getDataCacheFileSize());
        agent.getAgentBaseInfo().setDataCacheCycle(message.getData().getDataCacheCycle());

        logger.info("客户端更新全局配置成功，新的全局配置为：{}", agent.getAgentBaseInfo().toString());
    }
}
