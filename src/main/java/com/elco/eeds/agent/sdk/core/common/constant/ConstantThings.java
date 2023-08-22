package com.elco.eeds.agent.sdk.core.common.constant;

import java.io.Serializable;

/**
 * @ClassName ConstantThings
 * @Description 数据源常量
 * @Author OUYANG
 * @Date 2022/12/16 14:03
 */
public class ConstantThings implements Serializable {
    /**
     * 变量点位操作类型--新增
     */
    public static final String P_OPERATOR_TYPE_ADD = "add";
    /**
     * 变量点位操作类型--编辑
     */
    public static final String P_OPERATOR_TYPE_EDIT = "edit";
    /**
     * 变量点位操作类型--删除
     */
    public static final String P_OPERATOR_TYPE_DEL = "del";

    public static final String P_OPERATOR_TYPE_UNCHANGE = "unchange";
}
