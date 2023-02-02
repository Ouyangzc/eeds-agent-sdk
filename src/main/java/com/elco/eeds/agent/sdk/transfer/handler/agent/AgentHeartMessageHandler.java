package com.elco.eeds.agent.sdk.transfer.handler.agent;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.elco.eeds.agent.sdk.core.bean.agent.Agent;
import com.elco.eeds.agent.sdk.core.common.constant.message.ConstantTopic;
import com.elco.eeds.agent.sdk.transfer.beans.message.heart.AgentHeartMessage;
import com.elco.eeds.agent.sdk.transfer.handler.IReceiverMessageHandler;
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

    private Agent agent = Agent.getInstance();

    @Override
    public void handleRecData(String topic, String recData) {
        logger.debug("收到客户端心跳报文：topic: {}, msg: {}", topic, recData);
        try {
            // 序列化 判断格式是否正确，抛出异常即不正确
            AgentHeartMessage message = JSON.parseObject(recData, AgentHeartMessage.class);
            // 收到ping，发送pong
            AgentHeartMessage rspMessage = AgentHeartMessage.getRespMessage();
            String rspTopic = ConstantTopic.TOPIC_AGENT_HEART_RSP.replace("{agentId}", agent.getAgentBaseInfo().getAgentId());
            this.publishMessage(rspTopic, JSONUtil.toJsonStr(rspMessage));
        } catch (Exception e) {
            logger.error("客户端心跳报文处理异常", e);
            e.printStackTrace();
        }
    }
}
