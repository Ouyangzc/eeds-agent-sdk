package com.elco.eeds.agent.sdk.transfer.beans.things;

import java.io.Serializable;

/**
 * @ClassName BaseProperties
 * @Description 数据源变量基类
 * @Author OUYANG
 * @Date 2022/12/16 9:11
 */
public class BaseProperties implements Serializable {
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
     * 1.实际变量  2.虚拟变量
     */
    private int isVirtual;
    /**
     * 虚拟变量默认值
     */
    private String defaultValue;
    /**
     * 表达式
     */
    private String expression;
    /**
     * 实际变量ID集合，逗号分割
     */
    private String relationIds;
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


    public int getIsVirtual() {
        return isVirtual;
    }

    public void setIsVirtual(int isVirtual) {
        this.isVirtual = isVirtual;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getRelationIds() {
        return relationIds;
    }

    public void setRelationIds(String relationIds) {
        this.relationIds = relationIds;
    }

}
