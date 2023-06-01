package com.elco.eeds.agent.sdk.transfer.beans.data.sync;

import com.elco.eeds.agent.sdk.core.bean.properties.PropertiesContext;

import java.util.List;

/**
 * @ClassName DataSyncServerRequest
 * @Description 数据同步请求--数据源
 * @Author OUYANG
 * @Date 2022/12/9 14:17
 */
public class DataSyncServerRequest {
    /**
     * 数据源ID
     */
    private String thingsId;
    /**
     * 同步数据开始时间
     */
    private Long startTime;

    /**
     * 同步数据结束时间
     */
    private Long endTime;

    /**
     * 需过滤变量点
     */
    private List<PropertiesContext> properties;

    /**
     * 设备编号
     */
    private String thingsCode;

    /**
     * broker的IP
     */
    private String ip;
    /**
     * broker的端口
     */
    private String port;
    /**
     * broker安全设置（0 匿名 1 账密)
     */
    private String safety;
    /**
     * 账户名
     */
    private String account;
    /**
     * 密码
     */
    private String pwd;
    /**
     * CA认证文件
     */
    private String caCertFile;
    /**
     * 重连次数
     */
    private String reconnectNum;
    /**
     * 重连时间间隔,单位秒
     */
    private String reconnectInterval;

    /**
     * 动态连接字段
     */
    private String extendFieldMap;


    /**
     * 设备类型
     * {@link }
     */
    private String thingsType;
    /**
     * 是否支持监控
     * {@link }
     */
    private String isMonitoring;



    public String getThingsId() {
        return thingsId;
    }

    public void setThingsId(String thingsId) {
        this.thingsId = thingsId;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public List<PropertiesContext> getProperties() {
        return properties;
    }

    public String getThingsCode() {
        return thingsCode;
    }

    public void setThingsCode(String thingsCode) {
        this.thingsCode = thingsCode;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getSafety() {
        return safety;
    }

    public void setSafety(String safety) {
        this.safety = safety;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getCaCertFile() {
        return caCertFile;
    }

    public void setCaCertFile(String caCertFile) {
        this.caCertFile = caCertFile;
    }

    public String getReconnectNum() {
        return reconnectNum;
    }

    public void setReconnectNum(String reconnectNum) {
        this.reconnectNum = reconnectNum;
    }

    public String getReconnectInterval() {
        return reconnectInterval;
    }

    public void setReconnectInterval(String reconnectInterval) {
        this.reconnectInterval = reconnectInterval;
    }

    public String getExtendFieldMap() {
        return extendFieldMap;
    }

    public void setExtendFieldMap(String extendFieldMap) {
        this.extendFieldMap = extendFieldMap;
    }

    public String getThingsType() {
        return thingsType;
    }

    public void setThingsType(String thingsType) {
        this.thingsType = thingsType;
    }

    public String getIsMonitoring() {
        return isMonitoring;
    }

    public void setIsMonitoring(String isMonitoring) {
        this.isMonitoring = isMonitoring;
    }

    public void setProperties(List<PropertiesContext> properties) {
        this.properties = properties;
    }
}
