package com.elco.eeds.agent.sdk.transfer.beans.data.sync;

/**
 * @ClassName DataSyncFinishResult
 * @Description 数据同步完成报文结构体--数据源结果
 * @Author OUYANG
 * @Date 2022/12/9 14:35
 */
public class DataSyncFinishResult {
    /**
     * 数据源ID
     */
    private String thingsId;
    /**
     * 同步数据量
     */
    private Integer size;
    /**
     * 同步数据开始时间
     */
    private Long startTime;
    /**
     * 同步数据结束时间
     */
    private Long endTime;

    public String getThingsId() {
        return thingsId;
    }

    public void setThingsId(String thingsId) {
        this.thingsId = thingsId;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }
}
