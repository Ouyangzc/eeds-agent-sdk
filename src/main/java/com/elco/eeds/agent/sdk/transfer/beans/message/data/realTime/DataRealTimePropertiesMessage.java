package com.elco.eeds.agent.sdk.transfer.beans.message.data.realTime;

import com.elco.eeds.agent.sdk.core.bean.properties.PropertiesValue;
import com.elco.eeds.agent.sdk.core.common.constant.ConstantCommon;
import com.elco.eeds.agent.sdk.core.common.constant.message.ConstantTopic;
import com.elco.eeds.agent.sdk.core.common.enums.MessageMethod;
import com.elco.eeds.agent.sdk.core.util.DateUtils;
import com.elco.eeds.agent.sdk.core.util.JSONUtils;
import com.elco.eeds.agent.sdk.core.util.MapstructUtils;
import com.elco.eeds.agent.sdk.transfer.beans.message.BaseMessage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName DataRealTimePropertiesMessage
 * @Description 实时变量数据报文
 * @Author OUYANG
 * @Date 2022/12/9 14:26
 */
public class DataRealTimePropertiesMessage extends
    BaseMessage<List<SubDataRealTimePropertiesMessage>> implements Serializable {

  public static String getMessage(List<PropertiesValue> propertiesValueList) {
    DataRealTimePropertiesMessage message = new DataRealTimePropertiesMessage();
    message.setMethod(MessageMethod.DATA_REALTIME_DATA.getMethod());
    message.setTimestamp(DateUtils.getTimestamp());
    List<SubDataRealTimePropertiesMessage> subMsgs = new ArrayList<>();
    for (PropertiesValue pv : propertiesValueList) {
      SubDataRealTimePropertiesMessage subMsg = MapstructUtils.pvToMsg(pv);
      subMsgs.add(subMsg);
    }
    message.setData(subMsgs);
    return JSONUtils.toJsonStr(message);
  }

  public static String getTopic(String agentId, String thingsId) {
    return ConstantTopic.TOPIC_SED_DATA_REALTIME_PROPERTIES.replace(
            ConstantCommon.TOPIC_SUFFIX_AGENTID, agentId)
        .replace(ConstantCommon.TOPIC_SUFFIX_THINGSID, thingsId);
  }

}
