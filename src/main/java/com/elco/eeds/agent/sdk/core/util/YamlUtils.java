package com.elco.eeds.agent.sdk.core.util;

import com.elco.eeds.agent.sdk.core.common.enums.BasicTypeReflectEnum;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
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
            } else if (property instanceof List) {
              // 处理List<对象>逻辑
              Class<?>[] pt = method.getParameterTypes();
              if (null != pt && pt.length > 0) {
                Class<?> parameterType = pt[0];
                if (List.class.isAssignableFrom(parameterType)) {
                  // 获取List的实际类型
                  Class<?> genericType = getGenericType(method);
                  if (genericType != null) {
                    List<Object> list = (List<Object>) property;
                    List<Object> newList = new ArrayList<>();
                    for (Object item : list) {
                      if (item instanceof Map) {
                        Object nestedObject = genericType.newInstance();
                        propertiesToObject((Map<String, Object>) item, nestedObject, "");
                        newList.add(nestedObject);
                      } else {
                        newList.add(item);
                      }
                    }
                    method.invoke(object, newList);
                  }
                }
              }

            } else {
              String strProperty = property.toString();
              Class<?>[] pt = method.getParameterTypes();
              if (null != pt & pt.length > 0) {
                String simpleName = pt[0].getSimpleName();
                Object arg = null;
                if (BasicTypeReflectEnum.INT.getBasicKey().equals(simpleName)|| BasicTypeReflectEnum.INT.getWrapperKey().equals(simpleName)) {
                  arg = Integer.parseInt(strProperty);
                } else if (BasicTypeReflectEnum.LONG.getBasicKey().equals(simpleName) || BasicTypeReflectEnum.LONG.getWrapperKey().equals(simpleName)) {
                  arg = Long.parseLong(strProperty);
                } else if (BasicTypeReflectEnum.DOUBLE.getBasicKey().equals(simpleName) || BasicTypeReflectEnum.DOUBLE.getWrapperKey().equals(simpleName)) {
                  arg = Double.parseDouble(strProperty);
                } else if (BasicTypeReflectEnum.BOOLEAN.getBasicKey().equals(simpleName) || BasicTypeReflectEnum.BOOLEAN.getWrapperKey().equals(simpleName)) {
                  arg = Boolean.parseBoolean(strProperty);
                } else if (BasicTypeReflectEnum.FLOAT.getBasicKey().equals(simpleName) || BasicTypeReflectEnum.FLOAT.getWrapperKey().equals(simpleName)) {
                  arg = Float.parseFloat(strProperty);
                } else if (BasicTypeReflectEnum.STRING.getBasicKey().equals(simpleName)) {
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


  private static Class<?> getGenericType(Method method) {
    try {
      java.lang.reflect.Type[] genericParameterTypes = method.getGenericParameterTypes();
      if (genericParameterTypes.length > 0) {
        java.lang.reflect.Type genericParameterType = genericParameterTypes[0];
        if (genericParameterType instanceof java.lang.reflect.ParameterizedType) {
          java.lang.reflect.ParameterizedType parameterizedType = (java.lang.reflect.ParameterizedType) genericParameterType;
          java.lang.reflect.Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
          if (actualTypeArguments.length > 0) {
            return (Class<?>) actualTypeArguments[0];
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }


}
