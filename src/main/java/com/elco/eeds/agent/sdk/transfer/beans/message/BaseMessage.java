package com.elco.eeds.agent.sdk.transfer.beans.message;

import cn.hutool.json.JSONUtil;

import com.elco.eeds.agent.sdk.core.common.enums.MessageMethod;
import java.io.Serializable;

/**
 * @title: BaseMessage
 * @Author wl
 * @Date: 2022/12/5 16:21
 * @Version 1.0
 */
public class BaseMessage<T> implements Serializable {

    /**
     * 报文类型
     */
    private String method;
    /**
     * 时间戳
     */
    private Long timestamp;

    /**
     * 子报文数据体
     */
    private T data;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


    public String toJson() {
        return JSONUtil.toJsonStr(this);
    }
}
