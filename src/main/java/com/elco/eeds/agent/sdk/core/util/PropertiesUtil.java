package com.elco.eeds.agent.sdk.core.util;

import com.elco.eeds.agent.sdk.core.common.constant.ConstantFilePath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

/**
 * @title: PropertiesUtil
 * @Author wl
 * @Date: 2022/12/19 20:50
 * @Version 1.0
 */
public class PropertiesUtil {
    /**
     * 值可变，引用不可变
     */
    private static final Properties PROPERTIES = new Properties();
    private static final String FILE_NAME = ConstantFilePath.YML_NAME;
    private static final Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

    /**
     * 判断文件是否存在
     * @return
     */
    public static boolean isExistFile() {
        //获取当前目录
        String property = System.getProperty("user.dir");
        //默认是linux os
        String fileName = "/" + FILE_NAME;
        //判断是否是windows os
        if(System.getProperty ("os.name").contains("Windows")) {
            fileName = "\\" + FILE_NAME;
        }
        // 读取当前目录下conf配置文件
        File file = new File(property + fileName);
        if(!file.exists()) {
            return false;
        }
        return true;
    }

    /**
     * @param key
     * @param defaultValue
     * @return
     */
    public static String get(String key,String defaultValue) {
        if(PROPERTIES.isEmpty()) {
            throw new UnsupportedOperationException("配置未加载");
        }
        return PROPERTIES.getProperty(key, defaultValue);
    }
    /**
     * 获取默认配置的值，这个值可以动态修改
     * @param key
     * @return
     */
    public static String get(String key) {
        return get(key, "");
    }
    public static void initProperties() {
        InputStream is = null;
        try {
            //获取当前目录
            String property = System.getProperty("user.dir");
            //默认是linux os
            String fileName = "/" + FILE_NAME;
            //判断是否是windows os
            if(System.getProperty ("os.name").contains("Windows")) {
                fileName = "\\" + FILE_NAME;
            }
            // 读取当前目录下conf配置文件
            File file = new File(property + fileName);
            PROPERTIES.clear();
            is = new FileInputStream(file);
            PROPERTIES.load(new InputStreamReader(is,"UTF-8"));
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
    public static void main(String[] args) {
        System.out.println(PropertiesUtil.get("name"));
    }
}
