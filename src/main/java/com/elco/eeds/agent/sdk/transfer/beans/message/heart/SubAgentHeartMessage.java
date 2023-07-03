package com.elco.eeds.agent.sdk.transfer.beans.message.heart;

import com.elco.eeds.agent.sdk.core.common.constant.message.ConstantOperation;

import java.io.Serializable;

/**
 * @title: SubAgentHeartMessage
 * @Author wl
 * @Date: 2022/12/5 16:47
 * @Version 1.0
 * @Discription: 客户端心跳子报文结构
 */
public class SubAgentHeartMessage implements Serializable {

    private String operation;

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public static SubAgentHeartMessage getResponseMessage() {
        SubAgentHeartMessage message = new SubAgentHeartMessage();
        message.setOperation(ConstantOperation.OPERATION_AGENT_HEART_RSP);
        return message;
    }
}
