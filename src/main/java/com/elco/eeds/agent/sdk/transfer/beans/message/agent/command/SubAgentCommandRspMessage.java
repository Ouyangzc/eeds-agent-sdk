package com.elco.eeds.agent.sdk.transfer.beans.message.agent.command;

import java.io.Serializable;

/**
 * @title: SubAgentCommandRspMessage
 * @Author wl
 * @Date: 2022/12/22 13:52
 * @Version 1.0
 */
public class SubAgentCommandRspMessage implements Serializable {

    private String msgSeqNo;

    public String getMsgSeqNo() {
        return msgSeqNo;
    }

    public void setMsgSeqNo(String msgSeqNo) {
        this.msgSeqNo = msgSeqNo;
    }
}
