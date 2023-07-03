package com.elco.eeds.agent.sdk.transfer.beans.message.token;

import java.io.Serializable;

/**
 * @title: SubAgentTokenMessage
 * @Author wl
 * @Date: 2022/12/5 16:54
 * @Version 1.0
 * @Description: 客户端token子报文结构
 */
public class SubAgentTokenMessage implements Serializable {

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
