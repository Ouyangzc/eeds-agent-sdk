package com.elco.eeds.agent.sdk.transfer.beans.message.heart;

import com.elco.eeds.agent.sdk.transfer.beans.message.BaseMessage;
import com.elco.eeds.agent.sdk.core.common.constant.message.ConstantMethod;
import com.elco.eeds.agent.sdk.core.util.DateUtils;

import java.io.Serializable;

/**
 * @title: AgentHeartMessage
 * @Author wl
 * @Date: 2022/12/5 16:45
 * @Version 1.0
 * @Description: 客户端心跳报文结构
 */
public class AgentHeartMessage extends BaseMessage<SubAgentHeartMessage> implements Serializable {

    public static AgentHeartMessage getRespMessage() {
        AgentHeartMessage message = new AgentHeartMessage();
        message.setMethod(ConstantMethod.METHOD_AGENT_HEART_RSP);
        message.setTimestamp(DateUtils.getTimestamp());
        message.setData(SubAgentHeartMessage.getResponseMessage());
        return message;
    }
}
