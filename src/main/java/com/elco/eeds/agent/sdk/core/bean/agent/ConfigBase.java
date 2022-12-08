package com.elco.eeds.agent.sdk.core.bean.agent;

import java.lang.reflect.Field;

/**
 * @title: GlobalConfig
 * @Author wl
 * @Date: 2022/12/8 17:04
 * @Version 1.0
 * @Description: 配置实体类
 */
public class ConfigBase {

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

    public static void main(String[] args) {
        //可以使用getDeclaredFields()方法获取对象的所有属性
        ConfigBase configBase = new ConfigBase();		// 先初始化一个类
        Field[] fields = configBase.getClass().getDeclaredFields();	// 获取对象的所有属性
        for (Field item : fields) {
            String name = item.getName();	// 获取对象属性名
            String typeName = item.getGenericType().getTypeName();	// 获取对象属性的类型
            System.out.printf("属性名：%s,类型：%s\n", name, typeName);
        }

    }
}
