package com.elco.eeds.agent.sdk.transfer.beans.message.cmd;

import java.io.Serializable;

/**
 * @ClassName SubCmdRequestMessage
 * @Description 指令下发内容
 * @Author OuYang
 * @Date 2023/5/17 9:02
 * @Version 1.0
 */
public class SubCmdRequestMessage implements Serializable {
    /**
     * 数据源ID
     */
    private String thingsId;

    /**
     * 指令下发方式
     * 顺序下发: SERIAL 响应:RESPONSE 无响应:NO_RESPONSE
     */
    private String orderType;
    /**
     * 消息ID
     */
    private String msgSeqNo;
    /**
     * 下发内容
     */
    private String inputData;

    /**
     * 指令下发超时时间，单位（秒）
     */
    private int orderTimeOut;

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

    public String getInputData() {
        return inputData;
    }

    public void setInputData(String inputData) {
        this.inputData = inputData;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public int getOrderTimeOut() {
        return orderTimeOut;
    }

    public void setOrderTimeOut(int orderTimeOut) {
        this.orderTimeOut = orderTimeOut;
    }
}
