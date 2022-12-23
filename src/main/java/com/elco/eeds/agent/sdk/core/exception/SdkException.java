package com.elco.eeds.agent.sdk.core.exception;

import com.elco.eeds.agent.sdk.core.common.enums.ErrorEnum;

/**
 * @Description SDK异常类
 * @Author ouyang
 * @Date 2022/12/3 9:33
 **/
public class SdkException extends RuntimeException {
    /**
     * 错误码
     * {@link ErrorEnum}
     */
    private String code;
    /**
     * 错误信息描述
     */
    private String message;

    /**
     * 默认构造方法，根据异常码，构建一个异常实例对象
     *
     * @param code
     */
    public SdkException(String code) {
        this.message = ErrorEnum.getMessage(code);
        this.code = code;
    }

    /**
     * 根据异常信息，响应状态码，异常对象构建 一个异常实例对象
     *
     * @param message 异常信息
     * @param code    响应状态码
     * @param e       异常对象
     */
    public SdkException(String code, String message, Throwable e) {
        this.code = code;
        this.message = message;
    }


    public SdkException(ErrorEnum errorEnum) {
        this.code = errorEnum.code();
        this.message = errorEnum.message();
    }
}
