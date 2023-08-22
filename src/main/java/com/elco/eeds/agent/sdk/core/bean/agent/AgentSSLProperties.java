package com.elco.eeds.agent.sdk.core.bean.agent;

import java.io.Serializable;

/**
 * @ClassName AgentSSLProperties
 * @Description SSL配置信息
 * @Author OuYang
 * @Date 2023/7/17 13:13
 * @Version 1.0
 */
public class AgentSSLProperties implements Serializable {

    private Boolean enable;


    private String keystorePath;

    private String type;

    private String password;


    public String getKeystorePath() {
        return keystorePath;
    }

    public void setKeystorePath(String keystorePath) {
        this.keystorePath = keystorePath;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }
}
