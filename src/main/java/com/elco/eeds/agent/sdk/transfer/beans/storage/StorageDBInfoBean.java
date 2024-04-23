package com.elco.eeds.agent.sdk.transfer.beans.storage;

import com.elco.storage.enums.DbTypeEnum;
import com.elco.storage.enums.SecurityTypeEnum;
import java.io.Serializable;

/**
 * @ClassName StorageDBInfoMessage
 * @Description 存储数据库信息
 * @Author OuYang
 * @Date 2024/4/22 14:41
 * @Version 1.0
 */
public class StorageDBInfoBean implements Serializable{
  /**
   * 数据库存储主键ID
   */
  private Long pkStorage;

  /**
   * 地址 host:port
   */
  private String tcpUrl;

  /**
   * 用户名
   */
  private String userName;

  /**
   * 密码
   */
  private String password;

  /**
   * ca文件内容
   */
  private String certStr;

  /**
   * 数据库类型
   */
  private DbTypeEnum dbTypeEnum;

  /**
   * 加密方式
   */
  private SecurityTypeEnum securityTypeEnum;

  /**
   * 集群名称
   */
  private String cluster;

  /**
   * 切换状态 1正常 2异常 3切换中
   */
  private String status;

  public Long getPkStorage() {
    return pkStorage;
  }

  public void setPkStorage(Long pkStorage) {
    this.pkStorage = pkStorage;
  }

  public String getTcpUrl() {
    return tcpUrl;
  }

  public void setTcpUrl(String tcpUrl) {
    this.tcpUrl = tcpUrl;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getCertStr() {
    return certStr;
  }

  public void setCertStr(String certStr) {
    this.certStr = certStr;
  }

  public DbTypeEnum getDbTypeEnum() {
    return dbTypeEnum;
  }

  public void setDbTypeEnum(DbTypeEnum dbTypeEnum) {
    this.dbTypeEnum = dbTypeEnum;
  }

  public SecurityTypeEnum getSecurityTypeEnum() {
    return securityTypeEnum;
  }

  public void setSecurityTypeEnum(SecurityTypeEnum securityTypeEnum) {
    this.securityTypeEnum = securityTypeEnum;
  }

  public String getCluster() {
    return cluster;
  }

  public void setCluster(String cluster) {
    this.cluster = cluster;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
