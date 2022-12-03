package com.elco.eeds.agent.sdk.core.common;

/**
 * @Description eeds错误异常枚举
 * @Author Administrator
 * @Date 2022/12/3 9:39
 **/
public enum EedsErrorEnum {
    /**
     * 客户端本身错误，以CL开头
     */
    CLIENT_START_ERROR("CL0101001", "客户端启动失败"),
    /**
     * 数据源异常，以DE开头
     */
    CONNECT_MQTT_ERROR("MT0202001", "连接Mqtt失败");

    /**
     * 数据异常，以DA开头
     */

    /**
     * 同步异常，以SY开头
     */
    private String code;
    private String message;

    EedsErrorEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String code() {
        return this.code;
    }

    public String message() {
        return this.message;
    }
    /**
     * 根据消息值返回编码
     *
     * @param value
     * @return
     */
    public static String getCode(String value) {
        for (EedsErrorEnum eu : EedsErrorEnum.values()) {
            if (eu.name().equals(value)) {
                return eu.code();
            }
        }
        return "";
    }

    /**
     * 根据编码返回提示消息
     *
     * @param code
     * @return
     */
    public static String getMessage(String code) {
        for (EedsErrorEnum eu : EedsErrorEnum.values()) {
            if (eu.code().equals(code)) {
                return eu.message();
            }
        }
        return null;
    }
}
