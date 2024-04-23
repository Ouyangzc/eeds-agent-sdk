package com.elco.eeds.agent.sdk.transfer.beans.message.data.count.post;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.elco.eeds.agent.sdk.core.common.constant.ConstantCommon;
import com.elco.eeds.agent.sdk.core.common.constant.message.ConstantTopic;
import com.elco.eeds.agent.sdk.core.common.enums.MessageMethod;
import com.elco.eeds.agent.sdk.transfer.beans.data.count.PostDataCount;
import com.elco.eeds.agent.sdk.transfer.beans.message.BaseMessage;
import java.io.Serializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName DataCountMessage
 * @Description 数据统计报文
 * @Author OUYANG
 * @Date 2022/12/9 13:27
 */
public class DataCountMessage extends BaseMessage<SubDataCountMessage> implements Serializable {
    public static final Logger logger = LoggerFactory.getLogger(DataCountMessage.class);

    /**
     * 获取统计报文
     *
     * @param dataCount
     * @return
     */
    public static String getMsg(PostDataCount dataCount) {
        DataCountMessage message = new DataCountMessage();
        message.setMethod(MessageMethod.DATA_COUNT_POST.getMethod());
        message.setTimestamp(DateUtil.currentSeconds());
        message.setData(SubDataCountMessage.getSubMsg(dataCount));

        return JSONUtil.toJsonStr(message);
    }

    public static String getTopic(String agentId) {
        return ConstantTopic.TOPIC_SED_DATA_COUNT_POST.replace(ConstantCommon.TOPIC_SUFFIX_AGENTID, agentId);
    }
}
