package com.elco.eeds.agent.sdk.transfer.beans.message.cmd;

import cn.hutool.json.JSONUtil;

/**
 * @ClassName WriteResult
 * @Description 下发执行结果
 * @Author OuYang
 * @Date 2023/5/17 9:18
 * @Version 1.0
 */
public class CmdResult {
    private final String thingsId;
    private final String msgSeqNo;
    private final boolean result;
    private String resultMsg;

    public CmdResult(String thingsId, String msgSeqNo, boolean result) {
        this(thingsId, msgSeqNo, result, "");
    }

    public CmdResult(String thingsId, String msgSeqNo, boolean result, String resultMsg) {
        this.thingsId = thingsId;
        this.msgSeqNo = msgSeqNo;
        this.result = result;
        this.resultMsg = resultMsg;
    }

    public String getThingsId() {
        return thingsId;
    }


    public String getMsgSeqNo() {
        return msgSeqNo;
    }


    public boolean isResult() {
        return result;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    @Override
    public String toString() {
        return JSONUtil.toJsonStr(this);
    }
}
