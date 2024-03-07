package com.elco.eeds.agent.sdk.core.common.enums;

/**
 * @ClassName HttpStatusError
 * @Description http 类型枚举
 * 规则：H + 类型 + 业务码
 * 类型：0：自定义异常，1：http code异常
 * 业务码：
 * @Author OuYang
 * @Date 2024/1/29 13:32
 * @Version 1.0
 */
public enum HttpStatusError {
  REQUEST_UNKNOWN_ERROR("H001","http请求发生未知异常")
  ;

  private String code;
  private String message;

  HttpStatusError(String code, String message) {
    this.code = code;
    this.message = message;
  }

  public String getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }
}
