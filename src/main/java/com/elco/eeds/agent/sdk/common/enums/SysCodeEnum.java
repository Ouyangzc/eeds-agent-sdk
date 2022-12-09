package com.elco.eeds.agent.sdk.common.enums;

import java.text.MessageFormat;

public enum SysCodeEnum {

    SUCCESS("000000", "执行成功"),
    ILLEGAL_PARAM("000001", "非法参数"),
    BUSINESS_ERROR("000002", "业务异常"),
    GET_SEQ_FAILURE("000003", "数据序列失败"),
    TIME_OUT("000004", "内部请求超时"),
    REQUEST_ERROR("000005", "内部请求错误"),
    NO_DATA("000006", "结果集为空"),
    INTERNAL_ERROR("000009", "系统内部错误"),
    PARAM_FORMAT_ERROR("000010", "参数格式化错误"),
    UPLOAD_ERROR("000011", "上传文件失败"),
    UPLOAD_BINARY_ERROR("000012", "二进制文件必须设置文件名"),
    MQ_QUEUE_NAME_ILLEGAL("000013", "MQ队列名称不符合规则：{0}"),
    GET_USER_ERR("000014", "获取当前用户失败"),
    THREAD_EXECUTE_ERR("000015", "执行线程失败"),
    COPY_BEAN_ERR("000016", "拷贝数据失败"),
    UNQUALIFIED_BEAN("000017", "不支持当前Bean的拷贝"),
    PAGE_HELP_ERROR("000018", "系统分页失败,{0}"),
    FILE_EXCEED_LIMIT("000019", "文件大小超出限制，最大：{0}"),
    JSON_FORMAT_ERROR("000023", "JSON格式化错误"),
    INVOICE_STATUS_CHANGE_ERROR("800000", "系统繁忙,请稍后重试"),
    MIN_IO_TCP_ERROR("000024","文件服务器连接异常"),
    //=====================================
    USER_ERROR("A0001","用户端错误"),
    USER_LOGIN_ERROR("A0200","用户登录异常"),

    USER_ACCOUNT_NOT_EXIST("A0201","用户账户不存在"),
    USER_ACCOUNT_LOCKED("A0202","用户账户被冻结"),
    USER_ACCOUNT_INVALID("A0203","用户账户已作废"),

    USERNAME_OR_PASSWORD_ERROR("A0210","用户名或密码错误"),
    INPUT_PASSWORD_EXCEED_LIMIT("A0211","用户输入密码次数超限"),
    CLIENT_AUTHENTICATION_FAILED("A0212","客户端认证失败"), // *
    TOKEN_INVALID_OR_EXPIRED("A0230","token无效或已过期"),

    USER_AUTHORIZED_ERROR ("A0300","访问权限异常"),
    USER_ACCESS_UNAUTHORIZED ("A0301","访问未授权"),
    //====================================
    LOGIN_TIMEOUT("999", "登录超时");


    private String code;
    private String message;

    private SysCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage(Object... params) {
        return MessageFormat.format(this.message, params);
    }
}
