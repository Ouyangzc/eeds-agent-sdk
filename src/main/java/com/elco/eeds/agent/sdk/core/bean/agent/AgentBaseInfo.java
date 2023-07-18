package com.elco.eeds.agent.sdk.core.bean.agent;

import com.elco.eeds.agent.sdk.core.start.AgentStartProperties;

import java.io.Serializable;

/**
 * @title: AgentBaseInfo
 * @Author wl
 * @Date: 2022/12/6 9:24
 * @Version 1.0
 * @Description: 客户端基础信息类
 */
public class AgentBaseInfo implements Serializable {

    public AgentBaseInfo(){}

    public AgentBaseInfo(AgentStartProperties startProperties){
        this.baseFolder = startProperties.getBaseFolder();
        this.token =startProperties.getToken();
        this.port = startProperties.getPort();
        this.serverUrl = startProperties.getServerUrl();
        this.name = startProperties.getName();
        this.ssl = startProperties.getSsl();
    }



    // 客户端ID
    private String agentId;
    // 客户端名称
    private String name;
    // 客户端token
    private String token;
    // server地址
    private String serverUrl;
    // 文件存储路径
    private String baseFolder;
    // 同步上报间隔
    private String syncPeriod;
    // 缓存大小
    private String dataCacheFileSize;
    // 缓存周期
    private String dataCacheCycle;

    private AgentSSLProperties ssl;

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    /**
     * 客户端端口
     */
    private String port;

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getBaseFolder() {
        return baseFolder;
    }

    public void setBaseFolder(String baseFolder) {
        this.baseFolder = baseFolder;
    }

    public String getSyncPeriod() {
        return syncPeriod;
    }

    public void setSyncPeriod(String syncPeriod) {
        this.syncPeriod = syncPeriod;
    }

    public String getDataCacheFileSize() {
        return dataCacheFileSize;
    }

    public void setDataCacheFileSize(String dataCacheFileSize) {
        this.dataCacheFileSize = dataCacheFileSize;
    }

    public String getDataCacheCycle() {
        return dataCacheCycle;
    }

    public void setDataCacheCycle(String dataCacheCycle) {
        this.dataCacheCycle = dataCacheCycle;
    }

    public AgentSSLProperties getSsl() {
        return ssl;
    }

    public void setSsl(AgentSSLProperties ssl) {
        this.ssl = ssl;
    }

    @Override
    public String toString() {
        return "AgentBaseInfo{" +
                "agentId='" + agentId + '\'' +
                ", name='" + name + '\'' +
                ", token='" + token + '\'' +
                ", serverUrl='" + serverUrl + '\'' +
                ", baseFolder='" + baseFolder + '\'' +
                ", syncPeriod='" + syncPeriod + '\'' +
                ", dataCacheFileSize='" + dataCacheFileSize + '\'' +
                ", dataCacheCycle='" + dataCacheCycle + '\'' +
                ", port='" + port + '\'' +
                '}';
    }
}
