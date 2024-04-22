package com.elco.eeds.agent.sdk.transfer.beans.message.storage;

import com.elco.eeds.agent.sdk.core.util.JSONUtils;
import com.elco.eeds.agent.sdk.transfer.beans.message.BaseMessage;
import com.elco.storage.enums.DbTypeEnum;
import com.elco.storage.enums.SecurityTypeEnum;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.Serializable;

/**
 * @ClassName StorageDbChangeMessage
 * @Description 存储数据库切换报文
 * @Author OuYang
 * @Date 2024/4/19 9:01
 * @Version 1.0
 */
public class StorageDbChangeMessage implements Serializable {

  /**
   * 数据库存储主键ID
   */
  private long pkStorage;
  /**
   *  地址 host:port
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
   * @see com.elco.storage.enums.DbTypeEnum
   */
  private DbTypeEnum dbTypeEnum;
  /**
   *加密方式 ANONYMOUS或ACCOUNT
   * @see SecurityTypeEnum
   */
  private SecurityTypeEnum securityTypeEnum;
  /**
   * 集群名称
   */
  private String cluster;

  public long getPkStorage() {
    return pkStorage;
  }

  public void setPkStorage(long pkStorage) {
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

  public static void main(String[] args) {
    String json = "{\n"
        + "    \"method\": \"storage_change_event\",\n"
        + "    \"timestamp\": 1713230909000,\n"
        + "    \"data\": {\n"
        + "        \"pkStorage\": 1610209993691365376,\n"
        + "        \"tcpUrl\": \"192.168.0.47:3306\",\n"
        + "        \"userName\": \"root\",\n"
        + "        \"password\": \"cm9vdA==\",\n"
        + "        \"certStr\": \"\",\n"
        + "        \"dbTypeEnum\": \"MYSQL\",\n"
        + "        \"securityTypeEnum\": \"ACCOUNT\",\n"
        + "        \"cluster\": \"\"\n"
        + "    }\n"
        + "}";
    BaseMessage<StorageDbChangeMessage> storageDbChangeMessage = JSONUtils.toBeanReference(
        json, new TypeReference<BaseMessage<StorageDbChangeMessage>>() {
        });
    System.out.println(storageDbChangeMessage.toJson());
  }
}
