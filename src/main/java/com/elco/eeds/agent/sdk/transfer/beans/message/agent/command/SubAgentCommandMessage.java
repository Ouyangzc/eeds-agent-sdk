package com.elco.eeds.agent.sdk.transfer.beans.message.agent.command;

import java.io.Serializable;

/**
 * @title: SubAgentCommandMessage
 * @Author wl
 * @Date: 2022/12/21 17:23
 * @Version 1.0
 * @Description: 客户端指令下发子报文结构
 */
public class SubAgentCommandMessage implements Serializable {

    /**
     * 消息流水号
     */
    private String msgSeqNo;

    private SubAgentCommandContent command;

    public String getMsgSeqNo() {
        return msgSeqNo;
    }

    public void setMsgSeqNo(String msgSeqNo) {
        this.msgSeqNo = msgSeqNo;
    }

    public SubAgentCommandContent getCommand() {
        return command;
    }

    public void setCommand(SubAgentCommandContent command) {
        this.command = command;
    }
}
