package com.elco.eeds.agent.sdk.core.util;

import cn.hutool.core.date.DateUtil;

/**
 * @author Administrator
 * @version V1.0
 * @className DateUtils
 * @description 时间工具类
 * @date 2022/08/22 13:44
 **/
public class DateUtils {

    /**
     * 获取当前时间戳字符串
     *
     * @return
     */
    public static String getCurrentTimestamp() {
        return String.valueOf(DateUtil.current(true));
    }

    /**
     * 获取当前时间戳
     *
     * @return
     */
    public static Long getTimestamp() {
        return System.currentTimeMillis();
    }


}
