package com.elco.eeds.agent.sdk.core.common.enums;

/**
 * @ClassName QuartzGroupEnum
 * @Description quartz分组枚举，业务含义
 * @Author OuYang
 * @Date 2023/7/13 9:18
 * @Version 1.0
 */
public enum QuartzEnum {
    REALTIME_PROPERTIES_READ_GROUP("REALTIME_READ_GROUP","组--实时数据读取"),
    REALTIME_PROPERTIES_READ_JOB("REALTIME_READ_JOB","任务--实时数据读取"),

    CMD_TIME_OUT_GROUP("CMD_TIMEOUT_GROUP","组--指令下发超时"),
    CMD_TIME_OUT_JOB("CMD_TIMEOUT_JOB","任务--指令下发超时"),

    DATA_COUNT_GROUP("DATA_COUNT_GROUP","组--数据统计"),
    DATA_COUNT_JOB("DATA_COUNT_JOB","任务--数据统计"),

    EXPIRE_DATA_FILE_GROUP("EXPIRE_DATA_FILE_GROUP","组--数据文件过期"),
    EXPIRE_DATA_FILE_JOB("EXPIRE_DATA_FILE_JOB","任务--数据文件过期"),



    ;



    private final String value;

    private final String desc;

    QuartzEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }
}
