package com.elco.eeds.agent.sdk.core.common.enums;

/**
 * @Description eeds错误异常枚举
 * @Author Administrator
 * @Date 2022/12/3 9:39
 **/
public enum ErrorEnum {
    /**
     * 客户端本身错误，以CL开头
     */
    CLIENT_START_ERROR("CL0101001", "客户端启动失败"),
    CLIENT_REGISTER_ERROR("CL0101002", "客户端注册错误"),
    RESOURCE_OBTAIN_ERROR("CL0101003", "配置文件获取Resource失败"),
    WRITE_AGENT_FILE_ERROR("CL0101004", "保存agent.json异常"),
    SAVE_TOKEN_ERROR("CL0101005", "保存token至agent.json异常"),
    SAVE_CONFIG_ERROR("CL0101006", "保存config至agent.json异常"),
    READ_TOKEN_ERROR("CL0101007", "从agent.json读取token信息异常"),
    READ_CONFIG_ERROR("CL0101008", "从agent.json读取config信息异常"),
    /**
     * 数据源异常，以DE开头
     */
    CONNECT_MQTT_ERROR("MT0102001", "连接Mqtt失败"),

    /**
     * 数据异常，以DA开头
     */


    /**
     * 同步异常，以SY开头
     */

    /**
     * 第三方服务或者插件异常，以TP：third-party
     */
    HTTP_REQUEST_ERROR("TP0301001", "http请求异常"),
    NATS_LOAD_ERROR("TP0301002", "加载Nats组件异常");

    /**
     * 配置类异常, 以PR
     */


    private String code;
    private String message;

    ErrorEnum(String code, String message) {
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
        for (ErrorEnum eu : ErrorEnum.values()) {
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
        for (ErrorEnum eu : ErrorEnum.values()) {
            if (eu.code().equals(code)) {
                return eu.message();
            }
        }
        return null;
    }
}
