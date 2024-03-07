package com.elco.eeds.agent.sdk.transfer.handler.agent;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.elco.eeds.agent.sdk.core.bean.agent.Agent;
import com.elco.eeds.agent.sdk.core.common.constant.message.ConstantTopic;
import com.elco.eeds.agent.sdk.transfer.beans.message.agent.linktest.AgentLinkTestClusterMessage;
import com.elco.eeds.agent.sdk.transfer.beans.message.agent.linktest.AgentLinkTestMessage;
import com.elco.eeds.agent.sdk.transfer.handler.IReceiverMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName AgentLinkTestClusterMessageHandler
 * @Description 客户端链接测试报文处理类--集群
 * @Author OuYang
 * @Date 2023/12/21 11:16
 * @Version 1.0
 */
public class AgentLinkTestClusterMessageHandler implements IReceiverMessageHandler {
    private static final Logger logger = LoggerFactory.getLogger(AgentLinkTestClusterMessageHandler.class);

    private Agent agent = Agent.getInstance();
    @Override
    public void handleRecData(String topic, String recData) {

        try {
            logger.info("收到客户端链接测(集群)试报文：topic: {}, msg: {}", topic, recData);
            AgentLinkTestClusterMessage message = JSON.parseObject(recData, AgentLinkTestClusterMessage.class);
            AgentLinkTestClusterMessage rspMessage = AgentLinkTestClusterMessage.getRspMessage(message.getData().getNodeName(), message.getData().getNodeIp(),message.getData().getSessionId());
            String rspTopic = ConstantTopic.TOPIC_AGENT_LINK_TEST_CLUSTER_RSP.replace("{agentId}", agent.getAgentBaseInfo().getAgentId());
            this.publishMessage(rspTopic, JSONUtil.toJsonStr(rspMessage));
        }catch (Exception e) {
            logger.error("客户端链接测试报文(集群)处理异常", e);
            e.printStackTrace();
        }
    }
}
