package com.elco.eeds.agent.sdk.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @title: ReflectUtils
 * @Author wl
 * @Date: 2022/12/8 18:30
 * @Version 1.0
 */
public class ReflectUtils {

    private static Logger logger = LoggerFactory.getLogger(ReflectUtils.class);

    private ReflectUtils(){}

    /**
     * 通过反射，返回对象的map<字段名，字段值>
     * @param o
     * @return
     */
    public static Map reflectObjectToMap(Object o) {
        Map<String, String> map = new HashMap<>();
        Field[] fields = o.getClass().getDeclaredFields();

        for (Field item : fields) {
            String name = item.getName();
            String typeName = item.getGenericType().getTypeName();
            logger.debug("属性名：{}， 类型：{}", name, typeName);
            String value = (String) ReflectUtils.invokeGet(o, name);
            map.put(name, value);
        }
        return map;
    }

    /**
     * java反射bean的get方法
     *
     * @param objectClass objectClass
     * @param fieldName fieldName
     * @return Method
     * @throws RuntimeException
     */
    public static Method getGetMethod(Class<?> objectClass, String fieldName) {
        StringBuilder sb = new StringBuilder();
        sb.append("get");
        sb.append(fieldName.substring(0, 1).toUpperCase(Locale.ROOT));
        sb.append(fieldName.substring(1));

        try {
            return objectClass.getMethod(sb.toString());
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Reflect error!");
        }
    }

    /**
     * java反射bean的set方法
     *
     * @param objectClass objectClass
     * @param fieldName fieldName
     * @return Method
     * @throws RuntimeException
     */
    public static Method getSetMethod(Class<?> objectClass, String fieldName) {
        try {
            Class<?>[] parameterTypes = new Class<?>[1];
            Field field = objectClass.getDeclaredField(fieldName);
            parameterTypes[0] = field.getType();
            StringBuilder sb = new StringBuilder();
            sb.append("set");
            sb.append(fieldName.substring(0, 1).toUpperCase(Locale.ROOT));
            sb.append(fieldName.substring(1));
            return objectClass.getMethod(sb.toString(), parameterTypes);
        } catch (NoSuchFieldException | NoSuchMethodException e) {
            throw new RuntimeException("Reflect error!");
        }
    }

    /**
     * 执行set方法
     *
     * @param obj 执行对象
     * @param fieldName 属性
     * @param value 值
     * @throws RuntimeException
     */
    public static void invokeSet(Object obj, String fieldName, Object value) {
        Method method = getSetMethod(obj.getClass(), fieldName);
        try {
            method.invoke(obj, value);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Reflect error!");
        }
    }

    /**
     * 执行get方法
     *
     * @param obj 执行对象
     * @param fieldName 属性
     * @return Object
     * @throws RuntimeException
     */
    public static Object invokeGet(Object obj, String fieldName) {
        Method method = getGetMethod(obj.getClass(), fieldName);
        try {
            return method.invoke(obj);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Reflect error!");
        }
    }
}
