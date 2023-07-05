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
     * 功能标识,供客户端识别
     */
    private String identifier;

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

    public String getThingsId() {
        return thingsId;
    }

    public void setThingsId(String thingsId) {
        this.thingsId = thingsId;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
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
}
