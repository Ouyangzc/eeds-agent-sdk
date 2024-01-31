package com.elco.eeds.agent.sdk.core.common.enums;

/**
 * @ClassName RegexEnum
 * @Description 正则枚举
 * @Author OuYang
 * @Date 2024/1/29 9:01
 * @Version 1.0
 */
public enum RegexEnum {
  HTTP_HTTPS(
      "https?://(?:\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}|(?:www\\.)?[\\w-]+\\.\\w{2,3}(?:\\.\\w{2})?)(:\\d{1,5})?\\/?",
      "url填写错误");
  private String regexStr;
  private String msg;

  RegexEnum(String regexStr, String msg) {
    this.regexStr = regexStr;
    this.msg = msg;
  }

  public String getRegexStr() {
    return regexStr;
  }

  public String getMsg() {
    return msg;
  }
}
