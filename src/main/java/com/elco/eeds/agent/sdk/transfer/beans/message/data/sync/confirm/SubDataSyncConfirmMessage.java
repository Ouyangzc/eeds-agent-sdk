package com.elco.eeds.agent.sdk.transfer.beans.message.data.sync.confirm;

import java.io.Serializable;

/**
 * @ClassName SubDataSyncConfirmMessage
 * @Description 数据同步--确认报文--结构体
 * @Author OUYANG
 * @Date 2022/12/9 14:23
 */
public class SubDataSyncConfirmMessage implements Serializable {
    /**
     * 同步队列ID
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

    public static SubDataSyncConfirmMessage getSubMsg(String agentId, String queueId) {
        SubDataSyncConfirmMessage subMsg = new SubDataSyncConfirmMessage();
        subMsg.setAgentId(agentId);
        subMsg.setQueueId(queueId);
        return subMsg;
    }
}
