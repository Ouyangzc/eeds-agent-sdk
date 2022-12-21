package com.elco.eeds.agent.sdk.transfer.service.things;

import com.elco.eeds.agent.mq.nats.plugin.NatsPlugin;
import com.elco.eeds.agent.mq.plugin.MQPluginManager;
import com.elco.eeds.agent.mq.plugin.MQServicePlugin;
import com.elco.eeds.agent.sdk.core.common.constant.message.ConstantTopic;
import com.elco.eeds.agent.sdk.transfer.beans.message.things.ThingsHeartBeatMessage;

/**
 * @author ：ytl
 * @date ：Created in 2022/12/21 11:46
 * @description：
 */
public class ThingsHeartBeatMqService {

    private static final String CONNECT = "2";
    private static final String DIS_CONNECT = "3";

    private static void send(String thingsId, String status) {
        MQServicePlugin mqPlugin = MQPluginManager.getMQPlugin(NatsPlugin.class.getName());
        mqPlugin.publish(ConstantTopic.TOPIC_THINGS_HEARTBEAT_REQUEST + thingsId
                , ThingsHeartBeatMessage.create(thingsId, status).toJson(), null);
    }

    public static void sendConnectMsg(String thingsId) {
        send(thingsId, CONNECT);
    }

    public static void sendDisConnectMsg(String thingsId) {
        send(thingsId, DIS_CONNECT);
    }
}
