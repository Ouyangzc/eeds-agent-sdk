package com.elco.eeds.agent.sdk.core.util;

import com.google.common.base.Objects;

/**
 * @ClassName ObjectsUtils
 * @Description 对象工具类
 * @Author OuYang
 * @Date 2024/4/22 9:53
 * @Version 1.0
 */
public class ObjectsUtils {

  private ObjectsUtils() {
  }

  /**
   *  比较两个对象是否相等。 相同的条件有两个，满足其一即可：
   *  obj1 == null && obj2 == null
   *  obj1.equals(obj2)
   *
   * @param obj1 对象1
   * @param obj2 对象2
   * @return 是否相等
   */
  public static boolean equal(Object obj1, Object obj2) {
    return Objects.equal(obj1,obj2);
  }

  /**
   * 检查对象是否为null<br>
   * 判断标准为：
   *
   * <pre>
   * 1. == null
   * 2. equals(null)
   * </pre>
   *
   * @param obj 对象
   * @return 是否为null
   */
  public static boolean isNull(Object obj) {
    //noinspection ConstantConditions
    return null == obj || obj.equals(null);
  }

  /**
   * 检查对象是否不为null
   *
   * @param obj 对象
   * @return 是否为null
   */
  public static boolean isNotNull(Object obj) {
    //noinspection ConstantConditions
    return null != obj && false == obj.equals(null);
  }
}
