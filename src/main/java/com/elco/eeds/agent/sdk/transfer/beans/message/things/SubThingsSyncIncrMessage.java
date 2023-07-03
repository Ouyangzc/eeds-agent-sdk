package com.elco.eeds.agent.sdk.transfer.beans.message.things;

import java.io.Serializable;

/**
 * @ClassName SubThingsSyncIncrMessage
 * @Description 数据源增量同步报文结构体
 * @Author OUYANG
 * @Date 2022/12/19 14:29
 */
public class SubThingsSyncIncrMessage implements Serializable {
    /**
     * 同步任务ID
     */
    private Long taskId;
    /**
     * 客户端ID
     */
    private Long agentId;
    /**
     * 查表标识
     * 1:things 2:properties
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

    public String getTableSearch() {
        return tableSearch;
    }

    public void setTableSearch(String tableSearch) {
        this.tableSearch = tableSearch;
    }
}
