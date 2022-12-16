package com.elco.eeds.agent.sdk.transfer.beans.things;

import java.util.Map;

/**
 * @ClassName BaseProperties
 * @Description 数据源变量基类
 * @Author OUYANG
 * @Date 2022/12/16 9:11
 */
public class BaseProperties {
    /**
     * 变量ID
     */
    private String propertiesId;
    /**
     * 变量名称
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
     * 同步类型
     * {@link }
     */
    private String operatorType;
    /**
     * 修改时间戳
     */
    private Long timestamp;


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

    public String getOperatorType() {
        return operatorType;
    }

    public void setOperatorType(String operatorType) {
        this.operatorType = operatorType;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }


}
