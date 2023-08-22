package com.elco.eeds.agent.sdk.transfer.beans.message.data.sync.cancel;

import cn.hutool.json.JSONUtil;

import java.io.Serializable;

/**
 * @ClassName SubDataSyncCancelMessage
 * @Description 数据同步取消报文结构体
 * @Author OUYANG
 * @Date 2022/12/9 14:38
 */
public class SubDataSyncCancelMessage implements Serializable {
    /**
     * 队列ID
     */
    private String queueId;
    /**
     * 客户端ID
     */
    private String agentId;

    public String getQueueId() {
        return queueId;
    }

    public void setQueueId(String queueId) {
        this.queueId = queueId;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    @Override
    public String toString() {
        return JSONUtil.toJsonStr(this);
    }
}
