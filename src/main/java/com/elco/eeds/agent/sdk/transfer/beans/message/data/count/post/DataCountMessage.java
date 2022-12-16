package com.elco.eeds.agent.sdk.transfer.beans.message.data.count.post;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.elco.eeds.agent.sdk.core.common.constant.message.ConstantMethod;
import com.elco.eeds.agent.sdk.transfer.beans.data.count.PostDataCount;
import com.elco.eeds.agent.sdk.transfer.beans.message.BaseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName DataCountMessage
 * @Description 数据统计报文
 * @Author OUYANG
 * @Date 2022/12/9 13:27
 */
public class DataCountMessage extends BaseMessage<SubDataCountMessage> {
    public static final Logger logger = LoggerFactory.getLogger(DataCountMessage.class);

    /**
     * 获取统计报文
     *
     * @param dataCount
     * @return
     */
    public static String getMsg(PostDataCount dataCount) {
        DataCountMessage message = new DataCountMessage();
        message.setMethod(ConstantMethod.METHOD_DATA_COUNT_POST);
        message.setTimestamp(DateUtil.currentSeconds());
        message.setData(SubDataCountMessage.getSubMsg(dataCount));
        String msg = JSON.toJSONString(message);
        return msg;
    }
}
