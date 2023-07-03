package com.elco.eeds.agent.sdk.transfer.config;

import cn.hutool.core.collection.CollectionUtil;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @title: TransferApiProperties
 * @Author wl
 * @Date: 2022/12/6 11:47
 * @Version 1.0
 * @Description: 客户端请求Server Api类
 */
public class TransferApiProperties implements Serializable {

    private String serverUrl;

    private List<TransferApi> apis;

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public List<TransferApi> getApis() {
        return apis;
    }

    public void setApis(List<TransferApi> apis) {
        this.apis = apis;
    }

    /**
     * 获取请求接口
     * @param methods
     * @return
     */
    public String getRequestUrl(String methods) {
        String api = getApi(methods);
        String requestUrl = serverUrl + api;
        return requestUrl;
    }

    /**
     * 获取请求API
     * @param methods
     * @return
     */
    private String getApi(String methods) {
        List<TransferApi> apiList = apis.stream().filter(transferApi -> transferApi.getMethod().equals(methods)).collect(Collectors.toList());
        return CollectionUtil.isNotEmpty(apiList) ? apiList.get(0).getApi() : "";
    }
}
