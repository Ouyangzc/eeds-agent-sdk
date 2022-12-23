package com.elco.eeds.agent.sdk.transfer.beans.message.order;

import java.util.Map;

/**
 * @author ：ytl
 * @date ：Created in 2022/12/23 14:45
 * @description：
 */
public class OrderPropertiesValue {
    private String name;
    private String dataType;
    private String type;

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

    public Map<String, String> getExtendedMap() {
        return extendedMap;
    }

    public void setExtendedMap(Map<String, String> extendedMap) {
        this.extendedMap = extendedMap;
    }

    private String address;
    private String value;
    private Map<String,String> extendedMap;
}
