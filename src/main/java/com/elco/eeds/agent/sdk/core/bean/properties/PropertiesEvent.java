package com.elco.eeds.agent.sdk.core.bean.properties;

import com.elco.eeds.agent.sdk.core.common.constant.ConstantThings;

import java.util.Map;

/**
 * @ClassName PropertiesEvent
 * @Description 变量变动实体
 * @Author ouyang
 * @Date 2023/1/28 9:45
 * @Version 1.0
 */
public class PropertiesEvent {
	/**
	 * 主数据源Id
	 */
	private String thingsId;
	/**
	 * 变量ID
	 */
	private String propertiesId;
	/**
	 * 1.实际变量  2.虚拟变量
	 */
	private int isVirtual;
	/**
	 * 变量地址
	 */
	private String address;
	/**
	 * 变量变动类型
	 * {@link ConstantThings}
	 */
	private String operatorType;
	/**
	 * 扩展字段
	 */
	private Map<String, Map<String, String>> attributes;
	
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
	
	public String getOperatorType() {
		return operatorType;
	}
	
	public void setOperatorType(String operatorType) {
		this.operatorType = operatorType;
	}
	
	public Map<String, Map<String, String>> getAttributes() {
		return attributes;
	}
	
	public void setAttributes(Map<String, Map<String, String>> attributes) {
		this.attributes = attributes;
	}

	public int getIsVirtual() {
		return isVirtual;
	}

	public void setIsVirtual(int isVirtual) {
		this.isVirtual = isVirtual;
	}
}
