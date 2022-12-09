package com.elco.eeds.agent.sdk.core.bean.agent;

/**
 * @title: AgentMqAuthInfo
 * @Author wl
 * @Date: 2022/12/6 9:37
 * @Version 1.0
 * @Description: 客户端mq认证类
 */
public class AgentMqAuthInfo {

    // token
    private String token;

    // 用户名
    private String userName;

    // 密码
    private String password;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
