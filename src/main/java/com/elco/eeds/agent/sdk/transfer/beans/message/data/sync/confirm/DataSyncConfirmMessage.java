package com.elco.eeds.agent.sdk.transfer.beans.message.data.sync.confirm;

import cn.hutool.json.JSONUtil;
import com.elco.eeds.agent.sdk.core.common.constant.ConstantCommon;
import com.elco.eeds.agent.sdk.core.common.constant.message.ConstantMethod;
import com.elco.eeds.agent.sdk.core.common.constant.message.ConstantTopic;
import com.elco.eeds.agent.sdk.core.util.DateUtils;
import com.elco.eeds.agent.sdk.transfer.beans.message.BaseMessage;

import java.io.Serializable;

/**
 * @ClassName DataSyncConfirmMessage
 * @Description 数据同步--确认报文
 * @Author OUYANG
 * @Date 2022/12/9 14:22
 */
public class DataSyncConfirmMessage extends BaseMessage<SubDataSyncConfirmMessage> implements Serializable {
    public static String getMsg(String agentId, String queueId) {
        DataSyncConfirmMessage message = new DataSyncConfirmMessage();
        message.setMethod(ConstantMethod.METHOD_DATA_SYNC_CONFIRM);
        message.setTimestamp(DateUtils.getTimestamp());
        SubDataSyncConfirmMessage subMsg = new SubDataSyncConfirmMessage();
        subMsg.setAgentId(agentId);
        subMsg.setQueueId(queueId);
        message.setData(subMsg);
        return JSONUtil.toJsonStr(message);
    }

    public static String getTopic(String agentId) {
        return ConstantTopic.TOPIC_SED_DATA_SYNC_CONFRIM.replace(ConstantCommon.TOPIC_SUFFIX_AGENTID,agentId);
    }
}
