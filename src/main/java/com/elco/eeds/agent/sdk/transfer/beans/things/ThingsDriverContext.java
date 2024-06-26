package com.elco.eeds.agent.sdk.transfer.beans.things;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.Map;

/**
 * @ClassName ThingsDriverContext
 * @Description 数据源驱动上下文
 * @Author OUYANG
 * @Date 2022/12/19 11:00
 */
public class ThingsDriverContext implements Serializable {
    /**
     * 数据源ID
     */
    private String thingsId;
    /**
     * 设备类型
     * {@link }
     */
    private String thingsType;

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
     * 同步类型
     * {@link }
     */
    private String operatorType;
    /**
     * 是否支持监控
     */
    private String isMonitoring;

    /**
     * 扩展map
     */
    private Map<String, String> extraMap;

    /**
     * 客户端从此Map中获取数据
     */
    private String extendFieldMap;

    public String getThingsId() {
        return thingsId;
    }

    public void setThingsId(String thingsId) {
        this.thingsId = thingsId;
    }

    public String getThingsType() {
        return thingsType;
    }

    public void setThingsType(String thingsType) {
        this.thingsType = thingsType;
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

    public String getOperatorType() {
        return operatorType;
    }

    public void setOperatorType(String operatorType) {
        this.operatorType = operatorType;
    }

    public Map<String, String> getExtraMap() {
        return extraMap;
    }

    public void setExtraMap(Map<String, String> extraMap) {
        this.extraMap = extraMap;
    }

    public String getIsMonitoring() {
        return isMonitoring;
    }

    public void setIsMonitoring(String isMonitoring) {
        this.isMonitoring = isMonitoring;
    }

    public String getExtendFieldMap() {
        return extendFieldMap;
    }

    public void setExtendFieldMap(String extendFieldMap) {
        this.extendFieldMap = extendFieldMap;
        // 将动态连接信息中的字段赋值给原先的固定字段
        this.extraMap = JSON.parseObject(this.extendFieldMap, Map.class);
    }

    @Override
    public String toString() {
        return JSONUtil.toJsonStr(this);
    }
}
