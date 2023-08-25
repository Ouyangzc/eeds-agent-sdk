package com.elco.eeds.agent.sdk.transfer.beans.message.cmd;

import java.io.Serializable;

/**
 * @ClassName SubCmdResultMessage
 * @Description 指令下发结果内容报文
 * @Author OuYang
 * @Date 2023/8/16 10:57
 * @Version 1.0
 */
public class SubCmdResultMessage implements Serializable {

    private String result;
    private String errMsg;
    private String thingsId;
    private String msgSeqNo;


    public SubCmdResultMessage(String thingsId,String msgSeqNo,String result, String errMsg) {
        this.thingsId = thingsId;
        this.msgSeqNo = msgSeqNo;
        this.result = result;
        this.errMsg = errMsg;
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
