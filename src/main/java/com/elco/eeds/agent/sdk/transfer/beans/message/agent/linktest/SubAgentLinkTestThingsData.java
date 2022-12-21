package com.elco.eeds.agent.sdk.transfer.beans.message.agent.linktest;

import java.util.Map;

/**
 * @title: SubAgentLinkTestThingsData
 * @Author wl
 * @Date: 2022/12/20 15:59
 * @Version 1.0
 */
public class SubAgentLinkTestThingsData {

    /**
     * 采集客户端ID
     */
    private String agentId;
    /**
     * 主数据源ID
     */
    private String thingsId;
    /**
     * 设备mac地址或imei号
     */
    private String thingsCode;
    /**
     * 协议类型
     */
    private String thingTypes;
    /**
     * 变量Id
     */
    private String propertiesId;
    /**
     * 变量名
     */
    private String name;
    /**
     * 变量地址
     */
    private String address;
    /**
     * 变量数据类型
     */
    private String type;
    /**
     * 值
     */
    private String value;
    /**
     * 原始值
     */
    private String rawValue;
    /**
     * 是否必需
     */
    private String require;
    /**
     *
     */
    private Map<String, Object> extraMap;

    public SubAgentLinkTestThingsData(String agentId, String thingsId, String thingsCode, String thingTypes, String propertiesId, String name, String address, String type, String value, String rawValue, String require, Map<String, Object> extraMap) {
        this.agentId = agentId;
        this.thingsId = thingsId;
        this.thingsCode = thingsCode;
        this.thingTypes = thingTypes;
        this.propertiesId = propertiesId;
        this.name = name;
        this.address = address;
        this.type = type;
        this.value = value;
        this.rawValue = rawValue;
        this.require = require;
        this.extraMap = extraMap;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getThingsId() {
        return thingsId;
    }

    public void setThingsId(String thingsId) {
        this.thingsId = thingsId;
    }

    public String getThingsCode() {
        return thingsCode;
    }

    public void setThingsCode(String thingsCode) {
        this.thingsCode = thingsCode;
    }

    public String getThingTypes() {
        return thingTypes;
    }

    public void setThingTypes(String thingTypes) {
        this.thingTypes = thingTypes;
    }

    public String getPropertiesId() {
        return propertiesId;
    }

    public void setPropertiesId(String propertiesId) {
        this.propertiesId = propertiesId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getRawValue() {
        return rawValue;
    }

    public void setRawValue(String rawValue) {
        this.rawValue = rawValue;
    }

    public String getRequire() {
        return require;
    }

    public void setRequire(String require) {
        this.require = require;
    }

    public Map<String, Object> getExtraMap() {
        return extraMap;
    }

    public void setExtraMap(Map<String, Object> extraMap) {
        this.extraMap = extraMap;
    }
}
