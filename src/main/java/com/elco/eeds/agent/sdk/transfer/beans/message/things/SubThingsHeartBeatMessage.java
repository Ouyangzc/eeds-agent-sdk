package com.elco.eeds.agent.sdk.transfer.beans.message.things;

/**
 * @Projectname: eeds-agent-core
 * @Filename: SubStatusInfoDataMessage
 * @Author: lyc
 * @Data:2022/9/7 9:29
 * @Description: 状态信息上报,子报文结构
 */
public class SubThingsHeartBeatMessage {

    public SubThingsHeartBeatMessage(String thingsId, String status) {
        this.thingsId = thingsId;
        this.status = status;
    }

    /**
     * 数据源ID
     */
    private String thingsId;
    /**
     * 状态（0：已断开；1：已连接）
     */
    private String status;

    public String getThingsId() {
        return thingsId;
    }

    public void setThingsId(String thingsId) {
        this.thingsId = thingsId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
