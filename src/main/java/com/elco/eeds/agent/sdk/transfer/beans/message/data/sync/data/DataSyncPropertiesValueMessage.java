package com.elco.eeds.agent.sdk.transfer.beans.message.data.sync.data;

import cn.hutool.core.bean.BeanUtil;
import com.elco.eeds.agent.sdk.core.bean.properties.PropertiesValue;
import com.elco.eeds.agent.sdk.core.common.constant.ConstantCommon;
import com.elco.eeds.agent.sdk.core.common.constant.message.ConstantMethod;
import com.elco.eeds.agent.sdk.core.common.constant.message.ConstantTopic;
import com.elco.eeds.agent.sdk.core.util.DateUtils;
import com.elco.eeds.agent.sdk.transfer.beans.message.BaseMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName DataSyncPropertiesMessage
 * @Description 数据同步--同步数据报文
 * @Author OUYANG
 * @Date 2022/12/9 14:31
 */
public class DataSyncPropertiesValueMessage extends BaseMessage<List<SubDataSyncPropertiesValueMessage>> {

    public static DataSyncPropertiesValueMessage getMessage(List<PropertiesValue> data) {
        DataSyncPropertiesValueMessage message = new DataSyncPropertiesValueMessage();
        message.setMethod(ConstantMethod.METHOD_DATA_SYNC_DATA);
        message.setTimestamp(DateUtils.getTimestamp());
        ArrayList<SubDataSyncPropertiesValueMessage> subMsgs = new ArrayList<>();
        BeanUtil.copyProperties(data, subMsgs);
        message.setData(subMsgs);
        return message;
    }

    public static String getTopic(String agentId, String thingsId) {
        return ConstantTopic.TOPIC_SED_DATA_SYNC_DATA.replace(ConstantCommon.TOPIC_SUFFIX_AGENTID, agentId).replace(ConstantCommon.TOPIC_SUFFIX_THINGSID, thingsId);
    }
}
