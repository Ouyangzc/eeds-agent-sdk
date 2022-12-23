package com.elco.eeds.agent.sdk.transfer.beans.message.order;

/**
 * @author ：ytl
 * @date ：Created in 2022/12/23 13:38
 * @description：下发指令确认报文
 */
public class SubOrderConfirmMessage {

    public SubOrderConfirmMessage(String thingsId, String msgSeqNo) {
        this.thingsId = thingsId;
        this.msgSeqNo = msgSeqNo;
    }

    /**
     * 数据源ID
     */
    private String thingsId;

    /**
     * 消息流水号
     */
    private String msgSeqNo;

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
