package com.elco.eeds.agent.sdk.transfer.beans.agent;

/**
 * @title: UpdateAgentsFinishToken 客户端更新token成功后 更新生效时间 入参
 * @Date: 2022/11/1 14:11
 * @Version 1.0
 */
public class UpdateAgentsFinishToken {

  /**
   * 客户端表ID不能为空
   */
  private Long id;
  /**
   * 客户端当前token
   */
  private String currentToken;


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCurrentToken() {
    return currentToken;
  }

  public void setCurrentToken(String currentToken) {
    this.currentToken = currentToken;
  }
}
