package com.elco.eeds.agent.sdk.transfer.beans.message.heart;

import com.elco.eeds.agent.sdk.core.common.enums.MessageMethod;
import com.elco.eeds.agent.sdk.core.util.DateUtils;
import com.elco.eeds.agent.sdk.transfer.beans.message.BaseMessage;
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
        message.setMethod(MessageMethod.AGENT_HEART_RSP);
        message.setTimestamp(DateUtils.getTimestamp());
        message.setData(SubAgentHeartMessage.getResponseMessage());
        return message;
    }
}
