package com.elco.eeds.agent.sdk.transfer.beans.message.token;

import com.elco.eeds.agent.sdk.core.common.constant.ConstantCommon;
import com.elco.eeds.agent.sdk.core.util.DateUtils;
import com.elco.eeds.agent.sdk.transfer.beans.message.BaseMessage;

import java.io.Serializable;

/**
 * @title: AgentTokenRspMessage
 * @Author wl
 * @Date: 2022/12/6 17:05
 * @Version 1.0
 * @Description: 客户端更新token后反馈的报文
 */
public class AgentTokenRspMessage extends BaseMessage<SubAgentTokenRspMessage>  implements Serializable {

    /**
     * 返回更新token成功的报文
     * @param agentTokenMessage
     * @return
     */
    public static AgentTokenRspMessage getSuccessMsg(AgentTokenMessage agentTokenMessage) {
        AgentTokenRspMessage agentTokenRspMessage = new AgentTokenRspMessage();
        SubAgentTokenRspMessage subAgentTokenRspMessage = new SubAgentTokenRspMessage();
        subAgentTokenRspMessage.setStatus(ConstantCommon.SUCCESS);
        // 属性拷贝
        agentTokenRspMessage.setMethod(agentTokenMessage.getMethod());
        agentTokenRspMessage.setTimestamp(DateUtils.getTimestamp());
        agentTokenRspMessage.setData(subAgentTokenRspMessage);

        return agentTokenRspMessage;

    }

    /**
     * 返回更新token失败的报文
     * @param agentTokenMessage
     * @return
     */
    public static AgentTokenRspMessage getFailMsg(AgentTokenMessage agentTokenMessage) {
        AgentTokenRspMessage agentTokenRspMessage = new AgentTokenRspMessage();
        SubAgentTokenRspMessage subAgentTokenRspMessage = new SubAgentTokenRspMessage();
        subAgentTokenRspMessage.setStatus(ConstantCommon.FAIL);
        // 属性拷贝
        agentTokenRspMessage.setMethod(agentTokenMessage.getMethod());
        agentTokenRspMessage.setTimestamp(DateUtils.getTimestamp());
        agentTokenRspMessage.setData(subAgentTokenRspMessage);

        return agentTokenRspMessage;
    }
}
