package com.elco.eeds.agent.sdk.transfer.service.data.sync;

import com.alibaba.fastjson.JSON;
import com.elco.eeds.agent.mq.nats.plugin.NatsPlugin;
import com.elco.eeds.agent.mq.plugin.MQPluginManager;
import com.elco.eeds.agent.mq.plugin.MQServicePlugin;
import com.elco.eeds.agent.sdk.core.bean.properties.PropertiesContext;
import com.elco.eeds.agent.sdk.core.bean.properties.PropertiesValue;
import com.elco.eeds.agent.sdk.transfer.beans.data.sync.DataSyncFinishResult;
import com.elco.eeds.agent.sdk.transfer.beans.data.sync.DataSyncServerRequest;
import com.elco.eeds.agent.sdk.transfer.beans.message.data.sync.confirm.DataSyncConfirmMessage;
import com.elco.eeds.agent.sdk.transfer.beans.message.data.sync.data.DataSyncPropertiesValueMessage;
import com.elco.eeds.agent.sdk.transfer.beans.message.data.sync.finish.DataSyncFinishMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName DataSyncService
 * @Description 数据同步处理类
 * @Author OUYANG
 * @Date 2022/12/15 10:02
 */
public class DataSyncService {
    public static final Logger logger = LoggerFactory.getLogger(DataSyncService.class);
    private Boolean syncFlag;

    private Boolean status;

    private String queueId;

    public DataSyncService() {
        syncFlag = false;
        status = true;
    }

    public Boolean getSyncFlag() {
        return syncFlag;
    }

    public void setSyncFlag(Boolean syncFlag) {
        this.syncFlag = syncFlag;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getQueueId() {
        return queueId;
    }

    public void setQueueId(String queueId) {
        this.queueId = queueId;
    }



    /**
     * 获取数据源同步数据
     *
     * @param request
     */
    public List<PropertiesValue> getThingsSyncDatas(DataSyncServerRequest request) {
        List<PropertiesValue> syncData = new ArrayList<>();
        String thingsId = request.getThingsId();
        Long startTime = request.getStartTime();
        Long endTime = request.getEndTime();
        //本地文件最新缓存时间大于server端，进行同步请求
        //todo 调用解析数据方法
        //List<ReportDataMessage> syncDatas = reader.getFileData(thingsId, startTime, endTime);
        //logger.info("数据同步：同步数据源id:{},开始时间:{},结束时间：{},获取同步原始报文大小:{}", thingsId, startTime, endTime, syncDatas.size());
        //if (syncDatas.size() > 0) {
        //    //过滤数据
        //    List<PropertiesContext> properties = request.getProperties();
        //    List<PropertiesContext> propertiesContexts = getPropertiesContext(thingsId, properties);
        //    if (!CollectionUtil.isEmpty(propertiesContexts)) {
        //        syncData = getSyncData(syncDatas, propertiesContexts);
        //        this.setSyncFlag(true);
        //    }
        //}
        return syncData;
    }





}
