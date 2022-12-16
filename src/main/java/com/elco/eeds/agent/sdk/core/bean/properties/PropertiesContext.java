package com.elco.eeds.agent.sdk.core.bean.properties;

import java.util.Map;

/**
 * @ClassName PropertiesContext
 * @Description 变量点位上下文
 * @Author OUYANG
 * @Date 2022/12/9 9:47
 */
public class PropertiesContext {
    /**
     * 客户端Id
     */
    private String agentId;
    /**
     * 主数据源Id
     */
    private String thingsId;
    /**
     * 变量ID
     */
    private String propertiesId;
    /**
     * 变量地址
     */
    private String address;
    /**
     * 同步类型
     * {@link }
     */
    private String operatorType;

    /**
     */
    private String thingsType;
    /**
     * 变量数据类型
     */
    private String type;
    /**
     * 扩展字段
     */
    private Map<String, Map<String, String>> attributes;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, Map<String, String>> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Map<String, String>> attributes) {
        this.attributes = attributes;
    }

    public String getOperatorType() {
        return operatorType;
    }

    public void setOperatorType(String operatorType) {
        this.operatorType = operatorType;
    }

    public String getThingsType() {
        return thingsType;
    }

    public void setThingsType(String thingsType) {
        this.thingsType = thingsType;
    }
}
