package com.elco.eeds.agent.sdk.transfer.beans.message.things;

import com.elco.eeds.agent.sdk.transfer.beans.message.BaseMessage;

import java.time.Instant;

/**
 * @ClassName ThingsSyncIncrMessage
 * @Description 数据源连接状态报文
 * @Author OUYANG
 * @Date 2022/12/19 14:29
 */
public class ThingsConnectStatusMessage extends BaseMessage<SubThingsConnectStatusMessage> {
    public static ThingsConnectStatusMessage create(String thingsId,String connectStatus,String message) {
        ThingsConnectStatusMessage msg = new ThingsConnectStatusMessage();
        msg.setMethod("things_connectStatus");
        msg.setTimestamp(Instant.now().toEpochMilli());
        msg.setData(new SubThingsConnectStatusMessage(thingsId, connectStatus,message));
        return msg;
    }

}
