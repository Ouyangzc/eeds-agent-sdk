package com.elco.eeds.agent.sdk.core.exception;

/**
 * @ClassName EedsConnectException
 * @Description 数据源连接异常
 * @Author OuYang
 * @Date 2023/6/20 15:55
 * @Version 1.0
 */
public class EedsConnectException extends RuntimeException {
    public EedsConnectException(String message) {
        super(message);
    }

    public EedsConnectException(String message, Throwable cause) {
        super(message, cause);
    }


    /**
     * 连接超时异常
     *
     * @return
     */
    public static EedsConnectException socketTimeOutException() {
        throw new EedsConnectException("连接超时");
    }

    /**
     * 用户名密码错误异常
     *
     * @return
     */
    public static EedsConnectException userNamePasswordException() {
        throw new EedsConnectException("认证失败,请检查用户名密码");
    }


    /**
     * 未知主机异常
     *
     * @return
     */
    public static EedsConnectException unknownHostException() {
        throw new EedsConnectException("主机名不存在或网络不可用");
    }
}
