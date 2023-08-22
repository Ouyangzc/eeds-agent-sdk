package com.elco.eeds.agent.sdk.transfer.beans.message.token;

import java.io.Serializable;

/**
 * @title: SubAgentTokenRspMessage
 * @Author wl
 * @Date: 2022/12/6 17:05
 * @Version 1.0
 */
public class SubAgentTokenRspMessage implements Serializable {

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
