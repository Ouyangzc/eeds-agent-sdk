package com.elco.eeds.agent.sdk.core.bean.agent;

import java.io.Serializable;

/**
 * @title: AgentMqSecurityInfo
 * @Author wl
 * @Date: 2022/12/6 9:30
 * @Version 1.0
 * @Description: 客户端mq安全类
 */
public class AgentMqSecurityInfo implements Serializable {

    private String keystore;

    private String trustStore;

    private String storePassword;

    private String keyPassword;

    private String algorithm;

    private String rootCer;

    public String getKeystore() {
        return keystore;
    }

    public void setKeystore(String keystore) {
        this.keystore = keystore;
    }

    public String getTrustStore() {
        return trustStore;
    }

    public void setTrustStore(String trustStore) {
        this.trustStore = trustStore;
    }

    public String getStorePassword() {
        return storePassword;
    }

    public void setStorePassword(String storePassword) {
        this.storePassword = storePassword;
    }

    public String getKeyPassword() {
        return keyPassword;
    }

    public void setKeyPassword(String keyPassword) {
        this.keyPassword = keyPassword;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getRootCer() {
        return rootCer;
    }

    public void setRootCer(String rootCer) {
        this.rootCer = rootCer;
    }
}
