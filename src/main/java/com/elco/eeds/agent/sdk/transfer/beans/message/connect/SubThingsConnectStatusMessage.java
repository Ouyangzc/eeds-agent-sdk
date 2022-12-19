package com.elco.eeds.agent.sdk.transfer.beans.message.connect;

/**
 * @Projectname: eeds-agent-core
 * @Filename: SubConnectStatusInfoDataMessage
 * @Author: hhf
 * @Data:2022/11/18 9:29
 * @Description: 连接状态信息上报,子报文结构
 */
public class SubThingsConnectStatusMessage {
    /**
     * 数据源ID
     */
    private String thingsId;

    public SubThingsConnectStatusMessage(String thingsId, String connectStatus, String msg) {
        this.thingsId = thingsId;
        this.connectStatus = connectStatus;
        this.msg = msg;
    }

    /**
     * 状态（0：已断开；1：已连接）
     */
    private String connectStatus;

    /**
     * 消息
     */

    private String msg;



    public String getThingsId() {
        return thingsId;
    }

    public void setThingsId(String thingsId) {
        this.thingsId = thingsId;
    }

    public String getConnectStatus() {
        return connectStatus;
    }

    public void setConnectStatus(String connectStatus) {
        this.connectStatus = connectStatus;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
