package com.elco.eeds.agent.sdk.core.exception;

/**
 * @ClassName EedsHttpRequestException
 * @Description http请求异常
 * @Author OuYang
 * @Date 2024/1/23 10:45
 * @Version 1.0
 */
public class EedsHttpRequestException extends RuntimeException {

  private String code;

  public EedsHttpRequestException(String message) {
    super(message);
  }

  public EedsHttpRequestException(String code, String message) {
    super(message);

  }


  public EedsHttpRequestException(String message, Throwable cause) {
    super(message, cause);
  }

  public String getCode() {
    return code;
  }
}
