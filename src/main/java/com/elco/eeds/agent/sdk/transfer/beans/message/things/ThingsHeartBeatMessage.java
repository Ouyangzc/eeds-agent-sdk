package com.elco.eeds.agent.sdk.transfer.beans.message.things;

import com.elco.eeds.agent.sdk.transfer.beans.message.BaseMessage;

import java.io.Serializable;

/**
 * @ClassName ThingsHeartBeatMessage
 * @Description 数据源心跳报文
 * @Author OUYANG
 * @Date 2022/12/19 14:29
 */
public class ThingsHeartBeatMessage extends BaseMessage<SubThingsHeartBeatMessage> implements Serializable {

    public static ThingsHeartBeatMessage create(String thingsId, String status) {
        ThingsHeartBeatMessage msg = new ThingsHeartBeatMessage();
        msg.setMethod("things_heartBeat");
        msg.setTimestamp(null);
        msg.setData(new SubThingsHeartBeatMessage(thingsId, status));
        return msg;
    }
}
