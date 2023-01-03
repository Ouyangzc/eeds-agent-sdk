package com.elco.eeds.agent.sdk.transfer.beans.message.order;

import com.elco.eeds.agent.sdk.transfer.beans.message.BaseMessage;

/**
 * @title: OrderResultMessage
 * @Author wl
 * @Date: 2022/12/29 11:33
 * @Version 1.0
 */
public class OrderResultMessage extends BaseMessage<SubOrderResultMessage> {

    public static final String SUCCESS_CHARACTER = "成功";
    public static final String SUCCESS = "success";
    public static final String FAIL = "fail";

    public static OrderResultMessage create(String thingsId, String msgSeqNo, String result) {
        OrderResultMessage msg = new OrderResultMessage();
        msg.setMethod("agent_order_respond");
        msg.setTimestamp(System.currentTimeMillis());
        msg.setData(new SubOrderResultMessage(thingsId, msgSeqNo, SUCCESS_CHARACTER.equals(result) ? SUCCESS : FAIL));
        return msg;
    }

}
