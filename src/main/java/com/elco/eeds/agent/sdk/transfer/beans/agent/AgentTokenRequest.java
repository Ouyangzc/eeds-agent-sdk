package com.elco.eeds.agent.sdk.transfer.beans.agent;

/**
 * @title: AgentTokenRequest
 * @Author wl
 * @Date: 2022/12/6 14:29
 * @Version 1.0
 */
public class AgentTokenRequest {

    private Long id;

    private String currentToken;

    public AgentTokenRequest(Long id, String currentToken) {
        this.id = id;
        this.currentToken = currentToken;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCurrentToken() {
        return currentToken;
    }

    public void setCurrentToken(String currentToken) {
        this.currentToken = currentToken;
    }

    @Override
    public String toString() {
        return "AgentTokenRequest{" +
                "id=" + id +
                ", currentToken='" + currentToken + '\'' +
                '}';
    }
}
