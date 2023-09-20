package com.elco.eeds.agent.sdk.transfer.beans.message.cmd;

/**
 * @ClassName SubCmdConfirmMessage
 * @Description 确认报文内容
 * @Author OuYang
 * @Date 2023/8/16 10:44
 * @Version 1.0
 */
public class SubCmdConfirmMessage {
    private String thingsId;
    private String msgSeqNo;

    public SubCmdConfirmMessage(String thingsId, String msgSeqNo) {
        this.thingsId = thingsId;
        this.msgSeqNo = msgSeqNo;
    }

    public String getThingsId() {
        return thingsId;
    }

    public void setThingsId(String thingsId) {
        this.thingsId = thingsId;
    }

    public String getMsgSeqNo() {
        return msgSeqNo;
    }

    public void setMsgSeqNo(String msgSeqNo) {
        this.msgSeqNo = msgSeqNo;
    }
}
