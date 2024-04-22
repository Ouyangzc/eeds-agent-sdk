package com.elco.eeds.agent.sdk.transfer.handler.storage;

import com.elco.eeds.agent.sdk.core.common.constant.message.ConstantTopic;
import com.elco.eeds.agent.sdk.core.util.JSONUtils;
import com.elco.eeds.agent.sdk.core.util.MqPluginUtils;
import com.elco.eeds.agent.sdk.core.util.ObjectsUtils;
import com.elco.eeds.agent.sdk.transfer.beans.message.BaseMessage;
import com.elco.eeds.agent.sdk.transfer.beans.message.storage.StorageDbChangeMessage;
import com.elco.eeds.agent.sdk.transfer.beans.message.storage.StorageDbChangeResultMessage;
import com.elco.eeds.agent.sdk.transfer.handler.IReceiverMessageHandler;
import com.elco.storage.domain.ChangeResult;
import com.elco.storage.service.StorageService;
import com.elco.storage.utils.SpringUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName StorageDbChangeHandler
 * @Description 存储数据库切换
 * @Author OuYang
 * @Date 2024/4/19 8:54
 * @Version 1.0
 */
public class StorageDbChangeMessageHandler extends IReceiverMessageHandler {

  public static final String SUCCESS = "SUCCESS";

  public static final String FAIL = "FAIL";

  private static Logger logger = LoggerFactory.getLogger(StorageDbChangeMessageHandler.class);

  @Override
  public void handleRecData(String topic, String recData) throws Exception {
    logger.info("数据存储--收到切换数据库报文,主题:{},内容:{}", topic, recData);
    BaseMessage<StorageDbChangeMessage> dbChangeMessage = JSONUtils.toBeanReference(
        recData, new TypeReference<BaseMessage<StorageDbChangeMessage>>() {
        });
    StorageService storageService = SpringUtils.getBean(StorageService.class);
    if (ObjectsUtils.isNull(storageService)) {
      logger.warn("-收到切换数据库,未找到StorageService bean对象");
      return;
    }
    StorageDbChangeMessage message = dbChangeMessage.getData();
    ChangeResult changeResult = storageService.changeStorageDb(message.getTcpUrl(),
        message.getUserName(), message.getPassword(), message.getCertStr(), message.getDbTypeEnum(),
        message.getSecurityTypeEnum(), message.getCluster());
    String result = changeResult.getIsSuccess() ? SUCCESS : FAIL;
    StorageDbChangeResultMessage resultMsg = StorageDbChangeResultMessage.getResult(
        message.getPkStorage(), result);
    String postTopic = ConstantTopic.TOPIC_STORAGE_DB_CHANGE_RESULT;
    MqPluginUtils.sendStorageDbChangeResultMsg(postTopic,resultMsg.toString());
  }
}
