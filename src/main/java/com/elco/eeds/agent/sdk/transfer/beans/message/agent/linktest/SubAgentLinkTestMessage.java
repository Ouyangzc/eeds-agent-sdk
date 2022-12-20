package com.elco.eeds.agent.sdk.transfer.beans.message.agent.linktest;

/**
 * @title: SubAgentLinkTestMessage
 * @Author wl
 * @Date: 2022/12/20 13:12
 * @Version 1.0
 * @Description: 客户端链路测试子报文
 */
public class SubAgentLinkTestMessage {

    /**
     * 用户ID
     */
    private String pkUser;

    /**
     * websocket链接ID
     */
    private String socketId;

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
}
