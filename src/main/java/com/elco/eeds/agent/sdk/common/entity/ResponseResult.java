package com.elco.eeds.agent.sdk.common.entity;

import cn.hutool.json.JSONUtil;
import com.elco.eeds.agent.sdk.common.enums.SysCodeEnum;

import java.io.Serializable;

public class ResponseResult<T> implements Serializable {
    private static final long serialVersionUID = -5371548220309172397L;

    private String code = "";

    private String msg = "";

    private T data;

    public ResponseResult() {
    }

    public ResponseResult(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ResponseResult(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static <T> ResponseResult<T> SUCCESS() {
        return new ResponseResult<T>(SysCodeEnum.SUCCESS.getCode(), SysCodeEnum.SUCCESS.getMessage());
    }

    public static <T> ResponseResult<T> SUCCESS(T data) {
        return new ResponseResult<T>(SysCodeEnum.SUCCESS.getCode(), SysCodeEnum.SUCCESS.getMessage(), data);
    }

    public ResponseResult(SysCodeEnum sysCodeEnum) {
        this.code = sysCodeEnum.getCode();
        this.msg = sysCodeEnum.getMessage(new Object[0]);
    }

    public ResponseResult(SysCodeEnum sysCodeEnum, Object... params) {
        this.code = sysCodeEnum.getCode();
        this.msg = sysCodeEnum.getMessage(params);
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String toString() {
        return JSONUtil.toJsonPrettyStr(this);
    }

}
