package com.elco.eeds.agent.sdk.transfer.handler.data.sync;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.elco.eeds.agent.sdk.core.bean.agent.Agent;
import com.elco.eeds.agent.sdk.core.bean.agent.AgentBaseInfo;
import com.elco.eeds.agent.sdk.core.bean.properties.PropertiesValue;
import com.elco.eeds.agent.sdk.core.disruptor.RealTimeDataDisruptorServer;
import com.elco.eeds.agent.sdk.core.util.AgentResourceUtils;
import com.elco.eeds.agent.sdk.core.util.FileUtil;
import com.elco.eeds.agent.sdk.core.util.MqPluginUtils;
import com.elco.eeds.agent.sdk.core.util.RealTimeDataMessageFileUtils;
import com.elco.eeds.agent.sdk.transfer.beans.data.sync.DataSyncFinishResult;
import com.elco.eeds.agent.sdk.transfer.beans.data.sync.DataSyncServerRequest;
import com.elco.eeds.agent.sdk.transfer.beans.message.data.sync.confirm.DataSyncConfirmMessage;
import com.elco.eeds.agent.sdk.transfer.beans.message.data.sync.data.DataSyncPropertiesValueMessage;
import com.elco.eeds.agent.sdk.transfer.beans.message.data.sync.finish.DataSyncFinishMessage;
import com.elco.eeds.agent.sdk.transfer.beans.message.data.sync.request.DataSyncRequestMessage;
import com.elco.eeds.agent.sdk.transfer.beans.message.data.sync.request.SubDataSyncRequestMessage;
import com.elco.eeds.agent.sdk.transfer.handler.IReceiverMessageHandler;
import com.elco.eeds.agent.sdk.transfer.service.data.sync.DataSyncService;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName DataSyncRequestMessageHandler
 * @Description 数据同步请求报文逻辑
 * @Author OUYANG
 * @Date 2022/12/15 10:00
 */
public class DataSyncRequestMessageHandler extends IReceiverMessageHandler {

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
    this.postConfirMsg(agentId, queueId);
    dataSyncService.setQueueId(queueId);
    dataSyncService.setStatus(true);
    dataSyncService.setSyncFlag(false);
    List<DataSyncServerRequest> dataThingsList = data.getDataThingsList();
    for (DataSyncServerRequest request : dataThingsList) {
      String thingsId = request.getThingsId();
      Long startTime = request.getStartTime();
      Long endTime = request.getEndTime();
      //重新加载文件
      FileUtil.getLastDataFile();
      List<PropertiesValue> syncDatas = RealTimeDataMessageFileUtils.getFileData(request, startTime,
          endTime, request.getProperties());
      //判断是否取消同步
      if (!dataSyncService.getStatus()) {
        //取消状态，跳出此次循环
        dataSyncService.setStatus(true);
        break;
      }
      if (syncDatas.size() > 0) {
        dataSyncService.setSyncFlag(true);
        List<List<PropertiesValue>> split = CollectionUtil.split(syncDatas, 2000);
        // 循环发送同步点位数据
        split.stream().forEach(t -> {
//                    List<PropertiesValue> propertiesData = new ArrayList<>();
//                    propertiesData.add(t);
          //发送数据
          this.postPropertiesValueMsg(t, agentId, thingsId);
        });
      }
      //发送该数据源同步完成
      this.postDataSyncFinishMessage(agentId, queueId, dataSyncService.getSyncFlag(), thingsId,
          syncDatas.size(), startTime, endTime);
    }
  }


  /**
   * 返回确认报文
   */
  private void postConfirMsg(String agentId, String queueId) {
    String confirmMessage = DataSyncConfirmMessage.getMsg(agentId, queueId);
    String confirmTopic = DataSyncConfirmMessage.getTopic(agentId);
    logger.info("数据同步,同步确认,队列ID:{}", queueId);
    MqPluginUtils.sendDataSyncConfirmMsg(confirmTopic, confirmMessage);
  }

  /**
   * 推送变量同步数据
   *
   * @param propertiesValues
   * @param agentId
   * @param thingsId
   */
  private void postPropertiesValueMsg(List<PropertiesValue> propertiesValues, String agentId,
      String thingsId) {
    String message = DataSyncPropertiesValueMessage.getMessage(propertiesValues);
    String topic = DataSyncPropertiesValueMessage.getTopic(agentId, thingsId);
    if (AgentResourceUtils.isSlimModle()){
      RealTimeDataDisruptorServer disruptorServer = RealTimeDataDisruptorServer.getInstance2();
      disruptorServer.sendData(propertiesValues);
    }
    MqPluginUtils.sendDataSyncPropertiesValueMsg(topic, message);
    logger.debug("数据同步,推送数据,topic:{},msg:{}", topic, message);
  }

  private void postDataSyncFinishMessage(String agentId, String queueId, Boolean syncFlag,
      String thingsId, Integer size, Long startTime, Long endTime) {
    List<DataSyncFinishResult> results = new ArrayList<>();
    DataSyncFinishResult result = new DataSyncFinishResult();
    result.setSize(size);
    result.setStartTime(startTime);
    result.setEndTime(endTime);
    result.setThingsId(thingsId);
    results.add(result);
    String message = DataSyncFinishMessage.getMessage(queueId, syncFlag, results);
    String topic = DataSyncFinishMessage.getTopic(agentId);
    logger.info("数据同步，同步完成，topic:{},msg:{}", topic, message);
    MqPluginUtils.sendDataSyncFinishMsg(topic, message);
  }
}
