package com.elco.eeds.agent.sdk.core.connect.scheduler.dynamic;

import com.elco.eeds.agent.sdk.core.common.constant.ReadTypeEnums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName SysJob
 * @Description 任务实体，用来保存创建的任务，可以自定义
 * @Author OuYang
 * @Date 2023/7/12 14:23
 * @Version 1.0
 */
public class SysJob implements Serializable {

    private String thingsId;

    private ReadTypeEnums readTypeEnums;

    /**
     * 表达式
     */
    private String cron;

    private String jobName;

    private QuartzGroupEnum jobGroup;

    private Map<String, Object> extraMap;

    public SysJob(String thingsId, ReadTypeEnums readTypeEnums, String cron, QuartzGroupEnum groupEnum) {
        this.thingsId = thingsId;
        this.readTypeEnums = readTypeEnums;
        this.cron = cron;
        this.extraMap = new HashMap<>();
        this.jobGroup = groupEnum;
        this.jobName = "read_job" + thingsId;
    }

    public String getThingsId() {
        return thingsId;
    }

    public ReadTypeEnums getReadTypeEnums() {
        return readTypeEnums;
    }

    public String getJobName() {
        return jobName;
    }

    public QuartzGroupEnum getJobGroup() {
        return jobGroup;
    }

    public Map<String, Object> getExtraMap() {
        return extraMap;
    }

    public void setExtraMap(Map<String, Object> extraMap) {
        this.extraMap = extraMap;
    }

    public String getCron() {
        return cron;
    }
}
