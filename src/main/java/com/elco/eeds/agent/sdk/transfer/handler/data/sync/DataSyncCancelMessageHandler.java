package com.elco.eeds.agent.sdk.transfer.handler.data.sync;

import com.alibaba.fastjson.JSON;
import com.elco.eeds.agent.sdk.transfer.beans.message.data.sync.cancel.DataSyncCancelMessage;
import com.elco.eeds.agent.sdk.transfer.beans.message.data.sync.cancel.SubDataSyncCancelMessage;
import com.elco.eeds.agent.sdk.transfer.handler.IReceiverMessageHandler;
import com.elco.eeds.agent.sdk.transfer.service.data.sync.DataSyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName DataSyncCancelMessageHandler
 * @Description 数据同步取消报文逻辑处理
 * @Author OUYANG
 * @Date 2022/12/15 13:36
 */
public class DataSyncCancelMessageHandler implements IReceiverMessageHandler {
    private static Logger logger = LoggerFactory.getLogger(DataSyncCancelMessageHandler.class);
    private DataSyncService dataSyncService;

    public DataSyncCancelMessageHandler(DataSyncService dataSyncService) {
        this.dataSyncService = dataSyncService;
    }

    @Override
    public void handleRecData(String topic, String recData) {
        DataSyncCancelMessage message = JSON.parseObject(recData, DataSyncCancelMessage.class);
        SubDataSyncCancelMessage subMsg = message.getData();
        String queueId = subMsg.getQueueId();
        if (queueId.equals(dataSyncService.getQueueId())) {
            logger.debug("数据同步--取消报文，主题:{},消息内容:{}", topic, JSON.toJSONString(subMsg));
            dataSyncService.setStatus(false);
        }
    }
}
