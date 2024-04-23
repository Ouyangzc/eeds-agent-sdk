package com.elco.eeds.agent.sdk.core.util;

/**
 * @ClassName StrUtils
 * @Description 字符串工具类
 * @Author OuYang
 * @Date 2024/4/22 16:37
 * @Version 1.0
 */
public class StrUtils {

  /**
   * 字符串是否为空，空的定义如下:<br>
   * 1、为null <br>
   * 2、为""<br>
   *
   * @param str 被检测的字符串
   * @return 是否为空
   */
  public static boolean isEmpty(CharSequence str) {
    return str == null || str.length() == 0;
  }

  /**
   * 字符串是否为非空白 空白的定义如下： <br>
   * 1、不为null <br>
   * 2、不为""<br>
   *
   * @param str 被检测的字符串
   * @return 是否为非空
   */
  public static boolean isNotEmpty(CharSequence str) {
    return false == isEmpty(str);
  }
}
