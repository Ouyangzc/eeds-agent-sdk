package com.elco.eeds.agent.sdk.transfer.handler.agent;

import com.alibaba.fastjson.JSON;
import com.elco.eeds.agent.sdk.core.bean.agent.Agent;
import com.elco.eeds.agent.sdk.core.common.constant.message.ConstantTopic;
import com.elco.eeds.agent.sdk.transfer.beans.http.request.AgentTokenRequest;
import com.elco.eeds.agent.sdk.transfer.beans.message.token.AgentTokenMessage;
import com.elco.eeds.agent.sdk.transfer.beans.message.token.AgentTokenRspMessage;
import com.elco.eeds.agent.sdk.transfer.handler.IReceiverMessageHandler;
import com.elco.eeds.agent.sdk.transfer.service.AgentRequestHttpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @title: TokenMessageHandler
 * @Author wl
 * @Date: 2022/12/6 9:03
 * @Version 1.0
 * @Description: 客户端token报文处理类
 */
public class AgentTokenMessageHandler implements IReceiverMessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(AgentTokenMessageHandler.class);

    private AgentRequestHttpService agentRequestHttpService = new AgentRequestHttpService();

    @Override
    public void handleRecData(String topic, String recData) {
        logger.info("收到客户端token报文：topic: {}, msg: {}", topic, recData);
        AgentTokenMessage message = JSON.parseObject(recData, AgentTokenMessage.class);
        // 将token持久化到本地文件
        // TODO 保存成功后，更新到内存中
        Agent agent = Agent.getInstance();
        agent.getAgentBaseInfo().setToken(message.getData().getToken());
        // 调用服务端http接口
        AgentTokenRequest agentTokenRequest = new AgentTokenRequest(Long.parseLong(agent.getAgentBaseInfo().getAgentId()), message.getData().getToken());
        agentRequestHttpService.updateAgentEffectTime(agentTokenRequest);
        // 发送反馈报文
        AgentTokenRspMessage rspMessage;
        if(true) {
            rspMessage = AgentTokenRspMessage.getSuccessMsg(message);
        }else {
            rspMessage = AgentTokenRspMessage.getFailMsg(message);
        }
        this.publishMessage(ConstantTopic.TOPIC_AGENT_TOKEN, JSON.toJSONString(rspMessage));
    }
}
