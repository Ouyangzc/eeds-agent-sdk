package com.elco.eeds.agent.sdk.core.bean.agent;

import java.util.Arrays;

/**
 * @title: AgentMqInfo
 * @Author wl
 * @Date: 2022/12/6 9:28
 * @Version 1.0
 * @Description: 客户端mq配置信息
 */
public class AgentMqInfo {

    private String mqType;

    private String[] urls;

    private AgentMqAuthInfo authInfo;

    private AgentMqSecurityInfo mqSecurityInfo;

    public String getMqType() {
        return mqType;
    }

    public void setMqType(String mqType) {
        this.mqType = mqType;
    }

    public String[] getUrls() {
        return urls;
    }

    public void setUrls(String[] urls) {
        this.urls = urls;
    }

    public AgentMqAuthInfo getAuthInfo() {
        return authInfo;
    }

    public void setAuthInfo(AgentMqAuthInfo authInfo) {
        this.authInfo = authInfo;
    }

    public AgentMqSecurityInfo getMqSecurityInfo() {
        return mqSecurityInfo;
    }

    public void setMqSecurityInfo(AgentMqSecurityInfo mqSecurityInfo) {
        this.mqSecurityInfo = mqSecurityInfo;
    }

    @Override
    public String toString() {
        return "AgentMqInfo{" +
                "mqType='" + mqType + '\'' +
                ", urls=" + Arrays.toString(urls) +
                ", authInfo=" + authInfo +
                ", mqSecurityInfo=" + mqSecurityInfo +
                '}';
    }
}
