package com.elco.eeds.agent.sdk.core.common.constant;

/**
 * @ClassName CmdConstant
 * @Description 指令下发常量
 * @Author OuYang
 * @Date 2023/6/12 8:53
 * @Version 1.0
 */
public class CmdConstant {

    public static final String ORDER_TYPE_SERIAL = "11001";
    public static final String ORDER_TYPE_RESPONSE = "11002";
    /**
     * 指令类型对照:
     *  顺序下发 : 11001 -- SERIAL
     *  响应    : 11002 -- RESPONSE
     *  无响应  : 11003 -- NO_RESPONSE
     */
    public static final String ORDER_TYPE_NO_RESPONSE = "11003";

    public static final Integer WAITING = 0;

    public static final Integer READY = 1;
}
