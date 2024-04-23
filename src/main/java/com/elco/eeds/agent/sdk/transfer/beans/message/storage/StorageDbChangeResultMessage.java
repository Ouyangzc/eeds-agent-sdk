package com.elco.eeds.agent.sdk.transfer.beans.message.storage;

import com.elco.eeds.agent.sdk.core.common.enums.MessageMethod;
import com.elco.eeds.agent.sdk.core.util.JSONUtils;
import com.elco.eeds.agent.sdk.transfer.beans.message.BaseMessage;
import java.io.Serializable;

/**
 * @ClassName StorageDbChangeResultMessage
 * @Description 结果报文
 * @Author OuYang
 * @Date 2024/4/19 9:55
 * @Version 1.0
 */
public class StorageDbChangeResultMessage extends BaseMessage<SubStorageDbChangeResultMessage> implements
    Serializable {


  public static StorageDbChangeResultMessage getResult(long pkStorage,String result) {
    StorageDbChangeResultMessage msg = new StorageDbChangeResultMessage();
    msg.setMethod(MessageMethod.DB_CHANGE_RESULT.getMethod());
    msg.setTimestamp(System.currentTimeMillis());
    msg.setData(new SubStorageDbChangeResultMessage(pkStorage,result));
    return msg;
  }

  @Override
  public String toString() {
    return JSONUtils.toJsonStr(this);
  }
}
