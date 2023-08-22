package com.elco.eeds.agent.sdk.core.connect.scheduler.dynamic;

/**
 * @ClassName QuartzGroupEnum
 * @Description quartz分组枚举，业务含义
 * @Author OuYang
 * @Date 2023/7/13 9:18
 * @Version 1.0
 */
public enum QuartzGroupEnum {
    READ_PROPERTIES("READ_PROPERTIES");

    private final String value;

    QuartzGroupEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
