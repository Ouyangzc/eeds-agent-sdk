package com.elco.eeds.agent.sdk.transfer.beans.message.cmd;

import java.io.Serializable;

/**
 * @ClassName WriteResult
 * @Description 下发执行结果
 * @Author OuYang
 * @Date 2023/5/17 9:18
 * @Version 1.0
 */
public class CmdResult implements Serializable {
    private String thingsId;
    private String msgSeqNo;
    private boolean result;
    private String resultMsg;

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

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }
}
