package com.elco.eeds.agent.sdk.transfer.handler.agent;

import com.alibaba.fastjson.JSON;
import com.elco.eeds.agent.sdk.core.bean.agent.Agent;
import com.elco.eeds.agent.sdk.core.exception.SdkException;
import com.elco.eeds.agent.sdk.core.util.AgentFileExtendUtils;
import com.elco.eeds.agent.sdk.transfer.beans.http.request.AgentTokenRequest;
import com.elco.eeds.agent.sdk.transfer.beans.message.token.AgentTokenMessage;
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
        try {
            logger.info("收到客户端token报文：topic: {}, msg: {}", topic, recData);
            AgentTokenMessage message = JSON.parseObject(recData, AgentTokenMessage.class);
            // 保存到本地agent.json文件
            AgentFileExtendUtils.setTokenToLocalAgentFile(message.getData().getToken());
            // 保存成功后，更新到内存中
            Agent agent = Agent.getInstance();
            agent.getAgentBaseInfo().setToken(message.getData().getToken());
            // 调用服务端http接口
            AgentTokenRequest agentTokenRequest = new AgentTokenRequest(Long.parseLong(agent.getAgentBaseInfo().getAgentId()), message.getData().getToken());
            agentRequestHttpService.updateAgentEffectTime(agentTokenRequest);
        } catch (SdkException e) {
            logger.error("客户端token报文处理异常", e);
            e.printStackTrace();
        }
    }
}
