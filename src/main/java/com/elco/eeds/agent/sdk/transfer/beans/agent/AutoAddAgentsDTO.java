package com.elco.eeds.agent.sdk.transfer.beans.agent;

import java.io.Serializable;

/**
 * @ClassName AutoAddAgentsDTO
 * @Description 自动添加 客户端表 入参
 * @Author OuYang
 * @Date 2024/4/1 14:07
 * @Version 1.0
 */
public class AutoAddAgentsDTO implements Serializable {


  private String host;


  private String port;

  private String type;


  private String nodeName;

  public AutoAddAgentsDTO(String host, String port, String type) {
    this.host = host;
    this.port = port;
    this.type = type;
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public String getPort() {
    return port;
  }

  public void setPort(String port) {
    this.port = port;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getNodeName() {
    return nodeName;
  }

  public void setNodeName(String nodeName) {
    this.nodeName = nodeName;
  }
}
