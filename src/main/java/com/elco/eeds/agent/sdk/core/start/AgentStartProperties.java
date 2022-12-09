package com.elco.eeds.agent.sdk.core.start;

/**
 * @title: AgentStartProperties
 * @Author wl
 * @Date: 2022/12/6 11:21
 * @Version 1.0
 * @Description: 客户端启动参数
 */
public class AgentStartProperties {

    //TODO 是否需要单例模式

    /**
     * server地址
     */
    private String serverUrl;

    /**
     * 客户端名称
     */
    private String name;

    /**
     * 客户端端口
     */
    private String port;

    /**
     * 客户端token
     */
    private String token;

    /**
     * 存储文件目录
     */
    private String baseFolder;

    private String protocolPath;

    public String getProtocolPath() {
        return protocolPath;
    }

    public void setProtocolPath(String protocolPath) {
        this.protocolPath = protocolPath;
    }

    private static volatile AgentStartProperties agentStartProperties;

    public static AgentStartProperties getInstance() {
        if(agentStartProperties == null) {
            synchronized (AgentStartProperties.class) {
                if(agentStartProperties == null) {
                    agentStartProperties = new AgentStartProperties();
                }
            }
        }
        return agentStartProperties;
    }

    /*private AgentStartProperties(String serverUrl, String name, String port, String token, String baseFolder) {
        this.serverUrl = serverUrl;
        this.name = name;
        this.port = port;
        this.token = token;
        this.baseFolder = baseFolder;
    }*/

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getBaseFolder() {
        return baseFolder;
    }

    public void setBaseFolder(String baseFolder) {
        this.baseFolder = baseFolder;
    }

    @Override
    public String toString() {
        return "AgentStartProperties{" +
                "serverUrl='" + serverUrl + '\'' +
                ", name='" + name + '\'' +
                ", port='" + port + '\'' +
                ", token='" + token + '\'' +
                ", baseFolder='" + baseFolder + '\'' +
                '}';
    }
}
