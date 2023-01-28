package com.elco.eeds.agent.sdk.core.script.domain;

/**
 * @author ：ytl
 * @date ：Created in 2023/1/11 11:50
 * @description：
 */
public class PropertiesInfo {
    public String getPropertiesId() {
        return propertiesId;
    }

    public void setPropertiesId(String propertiesId) {
        this.propertiesId = propertiesId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    /**
     * 变量ID
     */
    private String propertiesId;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * 变量地址
     */
    private String address;

    /**
     * 变量值类型
     */
    private String type ="int";

    private String variableName;
}
