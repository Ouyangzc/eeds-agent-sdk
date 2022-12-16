package com.elco.eeds.agent.sdk.transfer.beans.message.data.sync.finish;

import com.elco.eeds.agent.sdk.transfer.beans.data.sync.DataSyncFinishResult;

import java.util.List;

/**
 * @ClassName SubDataSyncFinishMessage
 * @Description 数据同步完成报文结构体
 * @Author OUYANG
 * @Date 2022/12/9 14:34
 */
public class SubDataSyncFinishMessage {
    /**
     * 同步队列Id
     */
    private String queueId;

    /**
     * 同步是否有数据标识
     */
    private Boolean syncFlag;

    private List<DataSyncFinishResult> datas;

    public String getQueueId() {
        return queueId;
    }

    public void setQueueId(String queueId) {
        this.queueId = queueId;
    }

    public Boolean getSyncFlag() {
        return syncFlag;
    }

    public void setSyncFlag(Boolean syncFlag) {
        this.syncFlag = syncFlag;
    }

    public List<DataSyncFinishResult> getDatas() {
        return datas;
    }

    public void setDatas(List<DataSyncFinishResult> datas) {
        this.datas = datas;
    }
}
