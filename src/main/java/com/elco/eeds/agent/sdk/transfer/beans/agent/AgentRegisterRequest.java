package com.elco.eeds.agent.sdk.transfer.beans.agent;

import cn.hutool.json.JSONUtil;

import java.io.Serializable;

/**
 * @title: AgentRegisterRequest
 * @Author wl
 * @Date: 2022/12/7 13:36
 * @Version 1.0
 */
public class AgentRegisterRequest implements Serializable {

    /**
     * 客户端名称
     */
    private String name;

    /**
     * 主机地址
     */
    private String host;
    /**
     * 端口
     */
    private String port;
    /**
     * token
     */
    private String token;

    /**
     * 客户端类型
     */
    private String type;

    private String nodeName;

    public AgentRegisterRequest(String name, String host, String port, String token, String type) {
        this.name = name;
        this.host = host;
        this.port = port;
        this.token = token;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    @Override
    public String toString() {
        return JSONUtil.toJsonStr(this);
    }
}
