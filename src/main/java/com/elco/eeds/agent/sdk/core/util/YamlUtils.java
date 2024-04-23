package com.elco.eeds.agent.sdk.core.util;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @ClassName YamlUtils
 * @Description yaml工具类
 * @Author OuYang
 * @Date 2024/4/22 11:27
 * @Version 1.0
 */
public class YamlUtils {

  public static void propertiesToObject(final Map<String, Object> p, final Object object) {
    propertiesToObject(p, object, "");
  }

  private static void propertiesToObject(Map<String, Object> p, Object object, String prefix) {
    Method[] methods = object.getClass().getMethods();
    for (Method method : methods) {
      String methodName = method.getName();
      if (methodName.startsWith("set")) {
        try {
          String tmp = methodName.substring(4);
          String first = methodName.substring(3, 4);
          String key = prefix + first.toLowerCase() + tmp;
          Object property = p.get(key);
          if (null != property) {
            if (property instanceof Map) {
              // 嵌套对象赋值逻辑
              Class<?>[] pt = method.getParameterTypes();
              if (null != pt && pt.length > 0) {
                Class<?> parameterType = pt[0];
                // 实例化嵌套对象
                Object nestedObject = parameterType.newInstance();
                // 递归赋值
                propertiesToObject((Map<String, Object>) property, nestedObject, "");
                // 调用方法设置嵌套对象
                method.invoke(object, nestedObject);
              }
            } else {
              String strProperty = property.toString();
              Class<?>[] pt = method.getParameterTypes();
              if (null != pt & pt.length > 0) {
                String simpleName = pt[0].getSimpleName();
                Object arg = null;
                if (simpleName.equals("int") || simpleName.equals("Integer")) {
                  arg = Integer.parseInt(strProperty);
                } else if (simpleName.equals("long") || simpleName.equals("Long")) {
                  arg = Long.parseLong(strProperty);
                } else if (simpleName.equals("double") || simpleName.equals("Double")) {
                  arg = Double.parseDouble(strProperty);
                } else if (simpleName.equals("boolean") || simpleName.equals("Boolean")) {
                  arg = Boolean.parseBoolean(strProperty);
                } else if (simpleName.equals("float") || simpleName.equals("Float")) {
                  arg = Float.parseFloat(strProperty);
                } else if (simpleName.equals("String")) {
                  arg = property;
                } else {
                  continue;
                }
                method.invoke(object, arg);
              }
            }
          }
        } catch (Exception e) {

        }
      }
    }
  }

}
