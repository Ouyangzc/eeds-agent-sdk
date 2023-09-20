package com.elco.eeds.agent.sdk.core.util;

/**
 * @ClassName ClassUtils
 * @Description class loader加载类
 * @Author OuYang
 * @Date 2023/7/14 14:00
 * @Version 1.0
 */
public class ClassUtils {

    private ClassUtils() {
    }

    /**
     * 获取默认类加载器
     *
     * @return
     */
    public static ClassLoader getDefaultClassLoader() {
        ClassLoader classLoader = null;
        try {
            classLoader = Thread.currentThread().getContextClassLoader();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null == classLoader) {
            classLoader = ClassUtils.class.getClassLoader();
        }
        return classLoader;
    }

    /**
     * 系统类加载器
     *
     * @return
     */
    public static ClassLoader getSystemClassLoader() {
        return ClassLoader.getSystemClassLoader();
    }

    public static ClassLoader[] getClassLoader() {
        return new ClassLoader[]{ClassLoader.getSystemClassLoader(), Thread.currentThread().getContextClassLoader()};
    }
}
