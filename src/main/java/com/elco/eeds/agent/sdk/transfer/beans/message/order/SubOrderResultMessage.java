package com.elco.eeds.agent.sdk.transfer.beans.message.order;

/**
 * @title: SubOrderResultMessage
 * @Author wl
 * @Date: 2022/12/29 11:36
 * @Version 1.0
 */
public class SubOrderResultMessage {

    /**
     * 数据源Id
     */
    private String thingsId;

    /**
     * 消息流水号
     */
    private String msgSeqNo;

    /**
     * success 成功 fail 失败
     */
    private String result;

    /**
     * 错误原因
     */
    private String errMsg;

    public SubOrderResultMessage(String thingsId, String msgSeqNo, String result, String errMsg) {
        this.thingsId = thingsId;
        this.msgSeqNo = msgSeqNo;
        this.result = result;
        this.errMsg = errMsg;
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

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
