package com.elco.eeds.agent.sdk.transfer.handler.agent;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.elco.eeds.agent.sdk.core.bean.agent.Agent;
import com.elco.eeds.agent.sdk.core.common.constant.message.ConstantTopic;
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

    private Agent agent = Agent.getInstance();

    @Override
    public void handleRecData(String topic, String recData) {
        try {
            logger.info("收到客户端链接测试报文：topic: {}, msg: {}", topic, recData);
            AgentLinkTestMessage agentLinkTestMessage = JSON.parseObject(recData, AgentLinkTestMessage.class);
            AgentLinkTestMessage rspMessage = AgentLinkTestMessage.getRspMessage(agentLinkTestMessage.getData().getPkUser(), agentLinkTestMessage.getData().getSocketId());
            String rspTopic = ConstantTopic.TOPIC_AGENT_LINK_TEST_RSP.replace("{agentId}", agent.getAgentBaseInfo().getAgentId());
            this.publishMessage(rspTopic, JSONUtil.toJsonStr(rspMessage));
        }catch (Exception e) {
            logger.error("客户端链接测试报文处理异常", e);
            e.printStackTrace();
        }
    }
}
