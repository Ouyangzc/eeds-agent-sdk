package com.elco.eeds.agent.sdk.transfer.config;

import java.io.Serializable;

/**
 * @title: TransferApi
 * @Author wl
 * @Date: 2022/12/6 11:51
 * @Version 1.0
 */
public class TransferApi implements Serializable {

    public String method;

    public String api;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

}
