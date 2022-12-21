package com.elco.eeds.agent.sdk.transfer.handler.data.count;

import com.alibaba.fastjson.JSON;
import com.elco.eeds.agent.sdk.transfer.beans.message.data.count.confirm.DataCountConfirmMessage;
import com.elco.eeds.agent.sdk.transfer.beans.message.data.count.confirm.SubDataCountConfirmMessage;
import com.elco.eeds.agent.sdk.transfer.handler.IReceiverMessageHandler;
import com.elco.eeds.agent.sdk.transfer.service.data.count.DataCountServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName DataCountConfirmMessageHandler
 * @Description 数据统计确认报文逻辑
 * @Author OUYANG
 * @Date 2022/12/9 13:43
 */
public class DataCountConfirmMessageHandler implements IReceiverMessageHandler {
    private static Logger logger = LoggerFactory.getLogger(DataCountConfirmMessageHandler.class);

    @Override
    public void handleRecData(String topic, String recData) {
        logger.info("数据统计--收到确认报文,主题:{},内容:{}", topic, recData);
        DataCountConfirmMessage confirmMessage = JSON.parseObject(recData, DataCountConfirmMessage.class);
        SubDataCountConfirmMessage message = confirmMessage.getData();
        DataCountServiceImpl.recConfirmMsg(message.getCountId());
    }
}
