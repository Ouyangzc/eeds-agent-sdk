package com.elco.eeds.agent.sdk.core.bean.agent;

import java.io.Serializable;

/**
 * @title: BaseConfigEntity
 * @Author wl
 * @Date: 2022/12/8 11:33
 * @Version 1.0
 * @Description: 配置通用类
 */
public class BaseConfigEntity implements Serializable {

    /**
     * 字段名称
     */
    private String configFieldName;

    /**
     * 字段值
     */
    private String configFieldValue;

    /**
     * 字段类型（配置的类型：0 全局配置；1 私有配置）
     */
    private String configFieldType;

    public BaseConfigEntity(String configFieldName, String configFieldValue, String configFieldType) {
        this.configFieldName = configFieldName;
        this.configFieldValue = configFieldValue;
        this.configFieldType = configFieldType;
    }

    public String getConfigFieldName() {
        return configFieldName;
    }

    public void setConfigFieldName(String configFieldName) {
        this.configFieldName = configFieldName;
    }

    public String getConfigFieldValue() {
        return configFieldValue;
    }

    public void setConfigFieldValue(String configFieldValue) {
        this.configFieldValue = configFieldValue;
    }

    public String getConfigFieldType() {
        return configFieldType;
    }

    public void setConfigFieldType(String configFieldType) {
        this.configFieldType = configFieldType;
    }

    @Override
    public String toString() {
        return "BaseConfigEntity{" +
                "configFieldName='" + configFieldName + '\'' +
                ", configFieldValue='" + configFieldValue + '\'' +
                ", configFieldType='" + configFieldType + '\'' +
                '}';
    }
}
