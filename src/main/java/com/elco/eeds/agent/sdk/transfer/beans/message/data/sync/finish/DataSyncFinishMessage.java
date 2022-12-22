package com.elco.eeds.agent.sdk.transfer.beans.message.data.sync.finish;

import com.alibaba.fastjson.JSON;
import com.elco.eeds.agent.sdk.core.common.constant.ConstantCommon;
import com.elco.eeds.agent.sdk.core.common.constant.message.ConstantMethod;
import com.elco.eeds.agent.sdk.core.common.constant.message.ConstantTopic;
import com.elco.eeds.agent.sdk.core.util.DateUtils;
import com.elco.eeds.agent.sdk.transfer.beans.data.sync.DataSyncFinishResult;
import com.elco.eeds.agent.sdk.transfer.beans.message.BaseMessage;

import java.util.List;

/**
 * @ClassName DataSyncFinishMessage
 * @Description 数据同步完成报文
 * @Author OUYANG
 * @Date 2022/12/9 14:34
 */
public class DataSyncFinishMessage extends BaseMessage<SubDataSyncFinishMessage> {
    public static String getMessage(String queueId, Boolean syncFlag, List<DataSyncFinishResult> datas) {
        DataSyncFinishMessage message = new DataSyncFinishMessage();
        message.setMethod(ConstantMethod.METHOD_DATA_SYNC_FINISH);
        message.setTimestamp(DateUtils.getTimestamp());
        SubDataSyncFinishMessage subMsg = new SubDataSyncFinishMessage();
        subMsg.setQueueId(queueId);
        subMsg.setSyncFlag(syncFlag);
        subMsg.setDatas(datas);
        message.setData(subMsg);
        return JSON.toJSONString(message);
    }

    public static String getTopic(String agentId) {
        return ConstantTopic.TOPIC_SED_DATA_SYNC_FINISH.replace(ConstantCommon.TOPIC_SUFFIX_AGENTID,agentId);
    }
}
