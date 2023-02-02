package com.elco.eeds.agent.sdk.transfer.beans.data.count;

import cn.hutool.json.JSONUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName PostDataCount
 * @Description 数据统计记录
 * @Author OUYANG
 * @Date 2022/12/9 10:13
 */
public class PostDataCount {
    /**
     * 统计id
     */
    private String countId;
    /**
     * 客户端id
     */
    private Long agentId;
    /**
     * 状态
     */
    private String status;
    /**
     * 统计开始时间
     */
    private Long startTime;
    /**
     * 统计结束时间
     */
    private Long endTime;
    /**
     * 统计集合
     */
    private List<ThingsDataCount> thingsCountList;

    public String getCountId() {
        return countId;
    }

    public void setCountId(String countId) {
        this.countId = countId;
    }

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public List<ThingsDataCount> getThingsCountList() {
        return thingsCountList;
    }

    public void setThingsCountList(List<ThingsDataCount> thingsCountList) {
        this.thingsCountList = thingsCountList;
    }
    @Override
    public String toString() {
        return JSONUtil.toJsonStr(this);
    }
    /**
     * 获取新的统计记录
     *
     * @param agentId
     * @param dataCount
     * @return
     */
    public static PostDataCount getNewPostDataCount(String agentId,ThingsDataCount dataCount) {
        PostDataCount postDataCount = new PostDataCount();
        String countId = agentId + System.currentTimeMillis();
        postDataCount.setCountId(countId);
        postDataCount.setAgentId(Long.valueOf(agentId));
        postDataCount.setStartTime(dataCount.getStartTime());
        postDataCount.setEndTime(dataCount.getEndTime());
        List<ThingsDataCount> dataCounts = new ArrayList<>();
        dataCounts.add(dataCount);
        postDataCount.setThingsCountList(dataCounts);
        return postDataCount;
    }
}
