package com.elco.eeds.agent.sdk.core.config;

import static com.elco.eeds.agent.sdk.core.common.constant.ConstantFilePath.BASE_FOLDER;

import com.elco.eeds.agent.sdk.core.bean.agent.AgentClusterProperties;
import com.elco.eeds.agent.sdk.core.bean.agent.AgentLoggingFileProperties;
import com.elco.eeds.agent.sdk.core.bean.agent.AgentSSLProperties;
import com.elco.eeds.agent.sdk.core.common.constant.ConstantFilePath;
import com.elco.eeds.agent.sdk.core.common.enums.AgentRunningModelEnum;
import com.elco.eeds.agent.sdk.core.util.http.IpUtil;
import java.io.Serializable;

/**
 * @ClassName Config
 * @Description 配置信息
 * @Author OuYang
 * @Date 2024/4/22 10:28
 * @Version 1.0
 */
public class Config implements Serializable {
  /**
   * 默认server端地址
   */
  private String serverUrl = "http://localhost:8080";
  /**
   * 默认客户端名称
   */
  private String name = "采集客户端";
  /**
   * 默认客户端端口
   */
  private int port = 9090;
  /**
   * token
   */
  private String token;
  /**
   * 默认文件存储路径
   */
  private String baseFolder = ConstantFilePath.BASE_FOLDER;
  /**
   * 协议包地址
   */
  private String protocolPackage;
  /**
   * 协议类型
   */
  private String clientType;
  /**
   * 客户端IP
   */
  private String localIp = IpUtil.getLocalIp();

  /**
   * 运行模式
   */
  private String runningModel = AgentRunningModelEnum.SINGLE.getRunningModel();

  /**
   * 本地缓存开关
   */
  private boolean localCache = true;

  /**
   * ssl配置
   */
  private AgentSSLProperties ssl = new AgentSSLProperties();

  /**
   * 日志配置
   */
  private AgentLoggingFileProperties loggingFile=new AgentLoggingFileProperties();

  /**
   * 集群配置
   */
  private AgentClusterProperties cluster = new AgentClusterProperties();


  public String getServerUrl() {
    return serverUrl;
  }

  public void setServerUrl(String serverUrl) {
    this.serverUrl = serverUrl;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getBaseFolder() {
    return baseFolder;
  }

  public void setBaseFolder(String baseFolder) {
    this.baseFolder = baseFolder;
  }

  public String getProtocolPackage() {
    return protocolPackage;
  }

  public void setProtocolPackage(String protocolPackage) {
    this.protocolPackage = protocolPackage;
  }

  public String getClientType() {
    return clientType;
  }

  public void setClientType(String clientType) {
    this.clientType = clientType;
  }

  public String getLocalIp() {
    return localIp;
  }

  public void setLocalIp(String localIp) {
    this.localIp = localIp;
  }

  public String getRunningModel() {
    return runningModel;
  }

  public void setRunningModel(String runningModel) {
    this.runningModel = runningModel;
  }

  public boolean isLocalCache() {
    return localCache;
  }

  public void setLocalCache(boolean localCache) {
    this.localCache = localCache;
  }

  public AgentSSLProperties getSsl() {
    return ssl;
  }

  public void setSsl(AgentSSLProperties ssl) {
    this.ssl = ssl;
  }

  public AgentLoggingFileProperties getLoggingFile() {
    return loggingFile;
  }

  public void setLoggingFile(
      AgentLoggingFileProperties loggingFile) {
    this.loggingFile = loggingFile;
  }

  public AgentClusterProperties getCluster() {
    return cluster;
  }

  public void setCluster(AgentClusterProperties cluster) {
    this.cluster = cluster;
  }
}
