package com.elco.eeds.agent.sdk.transfer.beans.message.data.sync.request;

import com.elco.eeds.agent.sdk.transfer.beans.data.sync.DataSyncServerRequest;

import java.util.List;

/**
 * @ClassName SubDataSyncRequestMessage
 * @Description 数据同步请求结构体
 * @Author OUYANG
 * @Date 2022/12/9 14:16
 */
public class SubDataSyncRequestMessage {
    /**
     * 客户端Id
     */
    private String agentId;

    /**
     * 同步队列Id
     */
    private String queueId;
    /**
     * 数据最新时间
     */
    private List<DataSyncServerRequest> dataThingsList;

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getQueueId() {
        return queueId;
    }

    public void setQueueId(String queueId) {
        this.queueId = queueId;
    }

    public List<DataSyncServerRequest> getDataThingsList() {
        return dataThingsList;
    }

    public void setDataThingsList(List<DataSyncServerRequest> dataThingsList) {
        this.dataThingsList = dataThingsList;
    }
}
