package com.elco.eeds.agent.sdk.transfer.beans.data;

/**
 * @ClassName OriginalMessage
 * @Description 原始变量报文
 * @Author OUYANG
 * @Date 2022/12/21 10:54
 */
public class OriginalPropertiesValueMessage {
    /**
     * 采集时间
     */
    private Long collectTime;
    /**
     * 原始报文
     */
    private String message;

    public Long getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Long collectTime) {
        this.collectTime = collectTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
