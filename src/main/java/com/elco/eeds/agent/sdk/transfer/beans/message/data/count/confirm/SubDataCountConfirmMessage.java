package com.elco.eeds.agent.sdk.transfer.beans.message.data.count.confirm;

import java.io.Serializable;

/**
 * @ClassName SubDataCountConfirmMessage
 * @Description 数据统计确认结构体
 * @Author OUYANG
 * @Date 2022/12/9 13:33
 */
public class SubDataCountConfirmMessage implements Serializable {
    /**
     * 客户端id
     */
    private Long agentId;

    /**
     * 数据统计id
     */
    private String countId;

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public String getCountId() {
        return countId;
    }

    public void setCountId(String countId) {
        this.countId = countId;
    }
}
