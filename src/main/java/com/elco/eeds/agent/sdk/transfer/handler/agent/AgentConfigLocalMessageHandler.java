package com.elco.eeds.agent.sdk.transfer.handler.agent;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.elco.eeds.agent.sdk.core.bean.agent.Agent;
import com.elco.eeds.agent.sdk.transfer.beans.message.config.AgentConfigMessage;
import com.elco.eeds.agent.sdk.transfer.handler.IReceiverMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @title: AgentLocalConfigMessageHandler
 * @Author wl
 * @Date: 2022/12/6 9:08
 * @Version 1.0
 * @Description: 客户端私有配置报文处理类
 */
public class AgentConfigLocalMessageHandler implements IReceiverMessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(AgentConfigLocalMessageHandler.class);

    @Override
    public void handleRecData(String topic, String recData) {
        logger.info("收到客户端token报文：topic: {}, msg: {}", topic, recData);

        AgentConfigMessage message = JSON.parseObject(recData, AgentConfigMessage.class);
        Agent agent = Agent.getInstance();
        if (!StrUtil.isEmpty(message.getData().getDataCacheFileSize())) {
            agent.getAgentBaseInfo().setDataCacheFileSize(message.getData().getDataCacheFileSize());
        }
        if (!StrUtil.isEmpty(message.getData().getDataCacheCycle())) {
            agent.getAgentBaseInfo().setDataCacheCycle(message.getData().getDataCacheCycle());
        }
        if (!StrUtil.isEmpty(message.getData().getSyncPeriod())) {
            agent.getAgentBaseInfo().setSyncPeriod(message.getData().getSyncPeriod());
        }

        logger.info("客户端更新私有配置成功，新的全局配置为：{}", agent.getAgentBaseInfo().toString());
    }
}
