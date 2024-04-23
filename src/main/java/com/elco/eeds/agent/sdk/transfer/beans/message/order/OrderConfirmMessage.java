package com.elco.eeds.agent.sdk.transfer.beans.message.order;

import com.elco.eeds.agent.sdk.core.common.enums.MessageMethod;
import com.elco.eeds.agent.sdk.transfer.beans.message.BaseMessage;

import java.io.Serializable;

/**
 * @ClassName ThingsSyncIncrMessage
 * @Description 数据源连接状态报文
 * @Author OUYANG
 * @Date 2022/12/19 14:29
 */
public class OrderConfirmMessage extends BaseMessage<SubOrderConfirmMessage> implements Serializable {
    public static OrderConfirmMessage create(String thingsId,  String msgSeqNo) {
        OrderConfirmMessage msg = new OrderConfirmMessage();
        msg.setMethod(MessageMethod.AGENT_ORDER_CONFIRM.getMethod());
        msg.setTimestamp(System.currentTimeMillis());
        msg.setData(new SubOrderConfirmMessage(thingsId, msgSeqNo));
        return msg;
    }

}
