package com.elco.eeds.agent.sdk.transfer.beans.message.agent.linktest;

import java.io.Serializable;

/**
 * @title: SubAgentLinkTestMessage
 * @Author wl
 * @Date: 2022/12/20 13:12
 * @Version 1.0
 * @Description: 客户端链路测试子报文
 */
public class SubAgentLinkTestMessage implements Serializable {

    /**
     * 用户ID
     */
    private String pkUser;

    /**
     * websocket链接ID
     */
    private String socketId;

    private SubAgentLinkTestData data;

    public String getPkUser() {
        return pkUser;
    }

    public void setPkUser(String pkUser) {
        this.pkUser = pkUser;
    }

    public String getSocketId() {
        return socketId;
    }

    public void setSocketId(String socketId) {
        this.socketId = socketId;
    }

    public SubAgentLinkTestData getData() {
        return data;
    }

    public void setData(SubAgentLinkTestData data) {
        this.data = data;
    }
}
