package com.elco.eeds.agent.sdk.core.bean.agent;

import java.io.Serializable;

/**
 * @title: GlobalConfig
 * @Author wl
 * @Date: 2022/12/8 17:04
 * @Version 1.0
 * @Description: 配置实体类
 */
public class ConfigBase implements Serializable {

    private String syncPeriod;

    private String dataCacheCycle;

    private String dataCacheFileSize;

    public String getSyncPeriod() {
        return syncPeriod;
    }

    public void setSyncPeriod(String syncPeriod) {
        this.syncPeriod = syncPeriod;
    }

    public String getDataCacheCycle() {
        return dataCacheCycle;
    }

    public void setDataCacheCycle(String dataCacheCycle) {
        this.dataCacheCycle = dataCacheCycle;
    }

    public String getDataCacheFileSize() {
        return dataCacheFileSize;
    }

    public void setDataCacheFileSize(String dataCacheFileSize) {
        this.dataCacheFileSize = dataCacheFileSize;
    }

    @Override
    public String toString() {
        return "ConfigGlobal{" +
                "syncPeriod='" + syncPeriod + '\'' +
                ", dataCacheCycle='" + dataCacheCycle + '\'' +
                ", dataCacheFileSize='" + dataCacheFileSize + '\'' +
                '}';
    }

}
