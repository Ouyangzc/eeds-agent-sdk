package com.elco.eeds.agent.sdk.transfer.beans.message.agent.command;

import java.io.Serializable;
import java.util.Map;

/**
 * @title: SubAgentCommandProperties
 * @Author wl
 * @Date: 2022/12/22 13:44
 * @Version 1.0
 */
public class SubAgentCommandProperties implements Serializable {

    /**
     * 点位名称
     */
    private String name;
    /**
     * 点位名称
     */
    private String dataType;
    /**
     * 点位类型 0 模拟量 1 开关量
     */
    private String type;
    /**
     * 点位地址
     */
    private String address;
    /**
     * 设定的值
     */
    private String value;
    /**
     * 扩展字段
     */
    private Map extendedMap;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Map getExtendedMap() {
        return extendedMap;
    }

    public void setExtendedMap(Map extendedMap) {
        this.extendedMap = extendedMap;
    }
}
