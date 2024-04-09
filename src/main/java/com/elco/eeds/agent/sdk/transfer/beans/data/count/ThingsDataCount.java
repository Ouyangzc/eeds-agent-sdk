package com.elco.eeds.agent.sdk.transfer.beans.data.count;

import java.io.Serializable;

/**
 * @ClassName ThingsDataCount
 * @Description 数据源发送数据统计
 * @Author OUYANG
 * @Date 2022/12/9 10:11
 */
public class ThingsDataCount implements Serializable {
    /**
     * 数据源id
     */
    private String thingsId;
    /**
     * 统计数量
     */
    private Integer size;

    /**
     * 采集时间
     */
    private Long collectTime;
    /**
     * 开始时间
     */
    private Long startTime;
    /**
     * 结束时间
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

    public Long getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Long collectTime) {
        this.collectTime = collectTime;
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


    public static final class ThingsDataCountBuilder {

        private String thingsId;
        private Integer size;
        private Long collectTime;
        private Long startTime;
        private Long endTime;

        private ThingsDataCountBuilder() {
        }

        public static ThingsDataCountBuilder create() {
            return new ThingsDataCountBuilder();
        }

        public ThingsDataCountBuilder thingsId(String thingsId) {
            this.thingsId = thingsId;
            return this;
        }

        public ThingsDataCountBuilder size(Integer size) {
            this.size = size;
            return this;
        }

        public ThingsDataCountBuilder collectTime(Long collectTime) {
            this.collectTime = collectTime;
            return this;
        }

        public ThingsDataCountBuilder startTime(Long startTime) {
            this.startTime = startTime;
            return this;
        }

        public ThingsDataCountBuilder endTime(Long endTime) {
            this.endTime = endTime;
            return this;
        }

        public ThingsDataCount build() {
            ThingsDataCount thingsDataCount = new ThingsDataCount();
            thingsDataCount.setThingsId(thingsId);
            thingsDataCount.setSize(size);
            thingsDataCount.setCollectTime(collectTime);
            thingsDataCount.setStartTime(startTime);
            thingsDataCount.setEndTime(endTime);
            return thingsDataCount;
        }
    }
}
