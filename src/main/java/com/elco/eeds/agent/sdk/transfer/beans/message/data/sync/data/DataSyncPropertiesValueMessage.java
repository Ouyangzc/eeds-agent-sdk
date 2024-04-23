package com.elco.eeds.agent.sdk.transfer.beans.message.data.sync.data;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.elco.eeds.agent.sdk.core.bean.properties.PropertiesValue;
import com.elco.eeds.agent.sdk.core.common.constant.ConstantCommon;
import com.elco.eeds.agent.sdk.core.common.constant.message.ConstantTopic;
import com.elco.eeds.agent.sdk.core.common.enums.MessageMethod;
import com.elco.eeds.agent.sdk.core.util.DateUtils;
import com.elco.eeds.agent.sdk.transfer.beans.message.BaseMessage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName DataSyncPropertiesMessage
 * @Description 数据同步--同步数据报文
 * @Author OUYANG
 * @Date 2022/12/9 14:31
 */
public class DataSyncPropertiesValueMessage extends BaseMessage<List<SubDataSyncPropertiesValueMessage>> implements Serializable {

    public static String getMessage(List<PropertiesValue> datas) {
        DataSyncPropertiesValueMessage message = new DataSyncPropertiesValueMessage();
        message.setMethod(MessageMethod.DATA_SYNC_DATA.getMethod());
        message.setTimestamp(DateUtils.getTimestamp());
        List<SubDataSyncPropertiesValueMessage> subMsgs = new ArrayList<>();
        for (PropertiesValue pv : datas) {
            SubDataSyncPropertiesValueMessage valueMessage = new SubDataSyncPropertiesValueMessage();
            BeanUtil.copyProperties(pv, valueMessage);
            subMsgs.add(valueMessage);
        }
        message.setData(subMsgs);
        return JSONUtil.toJsonStr(message);
    }

    public static String getTopic(String agentId, String thingsId) {
        return ConstantTopic.TOPIC_SED_DATA_SYNC_DATA.replace(ConstantCommon.TOPIC_SUFFIX_AGENTID, agentId).replace(ConstantCommon.TOPIC_SUFFIX_THINGSID, thingsId);
    }
}
