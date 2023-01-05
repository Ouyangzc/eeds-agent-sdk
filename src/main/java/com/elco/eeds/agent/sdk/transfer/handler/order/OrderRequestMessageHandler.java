package com.elco.eeds.agent.sdk.transfer.handler.order;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.elco.eeds.agent.sdk.core.common.enums.ErrorEnum;
import com.elco.eeds.agent.sdk.core.connect.ThingsConnectionHandler;
import com.elco.eeds.agent.sdk.core.connect.manager.ConnectManager;
import com.elco.eeds.agent.sdk.core.exception.SdkException;
import com.elco.eeds.agent.sdk.transfer.beans.message.order.OrderRequestMessage;
import com.elco.eeds.agent.sdk.transfer.beans.message.order.SubOrderRequestMessage;
import com.elco.eeds.agent.sdk.transfer.handler.IReceiverMessageHandler;
import com.elco.eeds.agent.sdk.transfer.service.order.OrderConfirmMqService;
import com.elco.eeds.agent.sdk.transfer.service.order.OrderResultMqService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName OrderRequestMessageHandler
 * @Description 指令下发
 * @Author ytl
 * @Date 2022/12/19 14:31
 */
public class OrderRequestMessageHandler implements IReceiverMessageHandler {

    private static Logger logger = LoggerFactory.getLogger(OrderRequestMessageHandler.class);

    @Override
    public void handleRecData(String topic, String recData) {
        OrderRequestMessage message = JSON.parseObject(recData, OrderRequestMessage.class);
        logger.info("接收到指令下发消息，topic:{},data:{}", topic, JSON.toJSONString(message));
        SubOrderRequestMessage data = message.getData();
        // 发送指令下发确认报文
        OrderConfirmMqService.send(data.getThingsId(),data.getMsgSeqNo());

        ThingsConnectionHandler handler = ConnectManager.getHandler(data.getThingsId());
        if(ObjectUtil.isEmpty(handler)){
            throw new SdkException(ErrorEnum.THINGS_CONNECT_NOT_EXIST);
        }
        // 发送成功：true; 发送失败：false;
        // 失败需要发送--下发报文结果报文
        if (!handler.write(data.getProperties(),data.getMsgSeqNo())) {
            OrderResultMqService.sendFail(data.getThingsId(),data.getMsgSeqNo(), "【失败】客户端发送网关消息失败");
        }
    }
}
