package com.elco.eeds.agent.sdk.transfer.beans.message.agent.command;

import com.elco.eeds.agent.sdk.transfer.beans.message.BaseMessage;

/**
 * @title: AgentCommandMessage
 * @Author wl
 * @Date: 2022/12/21 17:22
 * @Version 1.0
 * @Description: 客户端指令下发消息结构
 */
public class AgentCommandMessage extends BaseMessage<SubAgentCommandMessage> {

    private SubAgentCommandRspMessage getResponseMsg(AgentCommandMessage agentCommandMessage) {
        SubAgentCommandRspMessage subAgentCommandRspMessage = new SubAgentCommandRspMessage();
        // TODO

        return subAgentCommandRspMessage;
    }

}
