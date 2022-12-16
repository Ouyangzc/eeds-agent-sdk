package com.elco.eeds.agent.sdk.transfer.handler.data.sync;

import com.alibaba.fastjson.JSON;
import com.elco.eeds.agent.sdk.core.bean.agent.Agent;
import com.elco.eeds.agent.sdk.core.bean.agent.AgentBaseInfo;
import com.elco.eeds.agent.sdk.transfer.beans.data.sync.DataSyncServerRequest;
import com.elco.eeds.agent.sdk.transfer.beans.message.data.sync.confirm.DataSyncConfirmMessage;
import com.elco.eeds.agent.sdk.transfer.beans.message.data.sync.request.DataSyncRequestMessage;
import com.elco.eeds.agent.sdk.transfer.beans.message.data.sync.request.SubDataSyncRequestMessage;
import com.elco.eeds.agent.sdk.transfer.handler.IReceiverMessageHandler;
import com.elco.eeds.agent.sdk.transfer.service.data.sync.DataSyncService;

import java.util.List;

/**
 * @ClassName DataSyncRequestMessageHandler
 * @Description 数据同步请求报文逻辑
 * @Author OUYANG
 * @Date 2022/12/15 10:00
 */
public class DataSyncRequestMessageHandler implements IReceiverMessageHandler {

    private DataSyncService dataSyncService;

    public DataSyncRequestMessageHandler(DataSyncService dataSyncService) {
        this.dataSyncService = dataSyncService;
    }

    @Override
    public void handleRecData(String topic, String recData) {
        DataSyncRequestMessage requestMessage = JSON.parseObject(recData, DataSyncRequestMessage.class);
        SubDataSyncRequestMessage data = requestMessage.getData();
        String queueId = data.getQueueId();
        AgentBaseInfo agentBaseInfo = Agent.getInstance().getAgentBaseInfo();
        String agentId = agentBaseInfo.getAgentId();
        //返回确认报文
        String confirmMessage = DataSyncConfirmMessage.getMsg(agentId, queueId);
        String confirmTopic = DataSyncConfirmMessage.getTopic(agentId);
        this.publishMessage(confirmTopic, confirmMessage);
        dataSyncService.setQueueId(queueId);
        dataSyncService.setStatus(true);
        List<DataSyncServerRequest> dataThingsList = data.getDataThingsList();
        for (DataSyncServerRequest request : dataThingsList) {
            // TODO: 2022/12/15 获取同步数据，发送同步数据，同步完成报文
        }
    }
}
