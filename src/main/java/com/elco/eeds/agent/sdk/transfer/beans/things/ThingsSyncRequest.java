package com.elco.eeds.agent.sdk.transfer.beans.things;

import java.io.Serializable;

/**
 * @ClassName ThingsSyncRequest
 * @Description 数据源同步请求类
 * @Author OUYANG
 * @Date 2022/12/16 9:05
 */
public class ThingsSyncRequest implements Serializable {
    /**
     * 任务ID
     */
    private Long taskId;
    /**
     * 客户端ID
     */
    private Long agentId;
    /**
     * 最后操作的时间戳
     */
    private Long lastTime;
    /**
     * 查表字段
     * 1:things
     * 2:properties
     */
    private String tableSearch;

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public Long getLastTime() {
        return lastTime;
    }

    public void setLastTime(Long lastTime) {
        this.lastTime = lastTime;
    }

    public String getTableSearch() {
        return tableSearch;
    }

    public void setTableSearch(String tableSearch) {
        this.tableSearch = tableSearch;
    }
}
