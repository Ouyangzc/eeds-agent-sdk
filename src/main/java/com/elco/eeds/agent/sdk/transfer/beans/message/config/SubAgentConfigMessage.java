package com.elco.eeds.agent.sdk.transfer.beans.message.config;

/**
 * @title: SubAgentConfigMessage
 * @Author wl
 * @Date: 2022/12/5 17:05
 * @Version 1.0
 * @Description: 客户端配置子报文结构
 */
public class SubAgentConfigMessage {

    private String dataCacheFileSize;

    private String dataCacheCycle;

    private String syncPeriod;

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

    public String getSyncPeriod() {
        return syncPeriod;
    }

    public void setSyncPeriod(String syncPeriod) {
        this.syncPeriod = syncPeriod;
    }
}
