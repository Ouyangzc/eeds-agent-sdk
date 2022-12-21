package com.elco.eeds.agent.sdk.transfer.service.things;

import com.elco.eeds.agent.mq.nats.plugin.NatsPlugin;
import com.elco.eeds.agent.mq.plugin.MQPluginManager;
import com.elco.eeds.agent.mq.plugin.MQServicePlugin;
import com.elco.eeds.agent.sdk.core.common.constant.message.ConstantTopic;
import com.elco.eeds.agent.sdk.transfer.beans.message.things.ThingsConnectStatusMessage;

/**
 * @author ：ytl
 * @date ：Created in 2022/12/21 11:46
 * @description：
 */
public class ThingsConnectStatusMqService {

    private static final String CONNECT = "2";
    private static final String DIS_CONNECT = "3";

    private static void send(String thingsId, String status,String msg) {
        MQServicePlugin mqPlugin = MQPluginManager.getMQPlugin(NatsPlugin.class.getName());
        mqPlugin.publish(ConstantTopic.TOPIC_THINGS_CONNECTSTATUS_REQUEST + thingsId
                , ThingsConnectStatusMessage.create(thingsId, status,msg).toJson(), null);
    }

    public static void sendConnectMsg(String thingsId) {
        send(thingsId, CONNECT,"数据源已连接");
    }

    public static void sendDisConnectMsg(String thingsId) {
        send(thingsId, DIS_CONNECT,"数据源已断开");
    }
}
