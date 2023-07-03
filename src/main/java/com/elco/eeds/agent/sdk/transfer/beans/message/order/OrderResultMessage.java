package com.elco.eeds.agent.sdk.transfer.beans.message.order;

import com.elco.eeds.agent.sdk.transfer.beans.message.BaseMessage;

import java.io.Serializable;

/**
 * @title: OrderResultMessage
 * @Author wl
 * @Date: 2022/12/29 11:33
 * @Version 1.0
 */
public class OrderResultMessage extends BaseMessage<SubOrderResultMessage> implements Serializable {

    public static final String SUCCESS = "success";
    public static final String FAIL = "fail";

    public static OrderResultMessage createSuccess(String thingsId, String msgSeqNo) {
        OrderResultMessage msg = new OrderResultMessage();
        msg.setMethod("agent_order_respond");
        msg.setTimestamp(System.currentTimeMillis());
        msg.setData(new SubOrderResultMessage(thingsId, msgSeqNo, SUCCESS, ""));
        return msg;
    }

    public static OrderResultMessage createFail(String thingsId, String msgSeqNo, String errMsg) {
        OrderResultMessage msg = new OrderResultMessage();
        msg.setMethod("agent_order_respond");
        msg.setTimestamp(System.currentTimeMillis());
        msg.setData(new SubOrderResultMessage(thingsId, msgSeqNo, FAIL, errMsg));
        return msg;
    }

}
