package com.elco.eeds.agent.sdk.transfer.beans.things;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * @ClassName EedsThings
 * @Description 数据源
 * @Author OUYANG
 * @Date 2022/12/16 9:08
 */
public class EedsThings extends BaseThings{
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
     * 点位信息
     */
    private List<EedsProperties> properties;

    /**
     * 动态连接字段
     */
    private String extendFieldMap;

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

    public List<EedsProperties> getProperties() {
        return properties;
    }

    public void setProperties(List<EedsProperties> properties) {
        this.properties = properties;
    }

    public String getExtendFieldMap() {
        return this.extendFieldMap;
    }

    public void setExtendFieldMap(String extendFieldMap) throws Exception {
        this.extendFieldMap = extendFieldMap;
    }

}
