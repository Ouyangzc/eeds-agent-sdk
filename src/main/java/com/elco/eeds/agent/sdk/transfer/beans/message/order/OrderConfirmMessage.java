package com.elco.eeds.agent.sdk.transfer.beans.message.order;

import com.elco.eeds.agent.sdk.transfer.beans.message.BaseMessage;

/**
 * @ClassName ThingsSyncIncrMessage
 * @Description 数据源连接状态报文
 * @Author OUYANG
 * @Date 2022/12/19 14:29
 */
public class OrderConfirmMessage extends BaseMessage<SubOrderConfirmMessage> {
    public static OrderConfirmMessage create(String thingsId,  String msgSeqNo) {
        OrderConfirmMessage msg = new OrderConfirmMessage();
        msg.setMethod("agent_order_confirm");
        msg.setTimestamp(null);
        msg.setData(new SubOrderConfirmMessage(thingsId, msgSeqNo));
        return msg;
    }

}
