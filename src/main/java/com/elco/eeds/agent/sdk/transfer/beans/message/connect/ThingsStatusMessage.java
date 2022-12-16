package com.elco.eeds.agent.sdk.transfer.beans.message.connect;

import cn.hutool.json.JSONUtil;
import com.elco.eeds.agent.mq.nats.plugin.NatsPlugin;
import com.elco.eeds.agent.mq.plugin.MQPluginManager;
import com.elco.eeds.agent.mq.plugin.MQServicePlugin;
import com.elco.eeds.agent.sdk.core.util.DateUtils;
import com.elco.eeds.agent.sdk.transfer.beans.message.BaseMessage;

/**
 * @title: AgentHeartMessage
 * @Author wl
 * @Date: 2022/12/5 16:45
 * @Version 1.0
 * @Description: 客户端心跳报文结构
 */
public class ThingsStatusMessage extends BaseMessage<SubThingsConnectStatusMessage> {

    public ThingsStatusMessage(String thingsId,String status,String msg){
        this.setMethod("things_connect_status");
        this.setTimestamp(DateUtils.getTimestamp());
        this.setData(new SubThingsConnectStatusMessage(thingsId,status,msg));
    }


    //1未连接 2已连接 3已断开 4不支持
    private final static  String CONNECT ="2";

    private final static  String DISCONNECT ="3";

    private final static String TOPIC ="agent.things.connectStatus.up.{agentId}.{thingsId}";


    /**
     * 连接状态信息上报---数据源连接成功
     *
     * @param thingsId
     * @return
     */
    public static ThingsStatusMessage connect(String thingsId) {
        return new ThingsStatusMessage(thingsId,CONNECT,"数据源连接成功");
    }

//    /**
//     * 连接状态信息上报--- 重新连接
//     *
//     * @param thingsId
//     * @return
//     */
//    public static ThingsStatusMessage retryConnect(String thingsId) {
//        return new ThingsStatusMessage(thingsId,CONNECT,"数据源已重新连接");
//    }

    /**
     * 连接状态信息上报--- 断开连接
     *
     * @param thingsId
     * @return
     */
    public static ThingsStatusMessage disConnect(String thingsId) {
        return new ThingsStatusMessage(thingsId,DISCONNECT,"数据源已断开");
    }

    /**
     * 获取topic
     *
     * @param agentId
     * @return
     */
    public static String getTopic(String agentId, String thingsId) {
        String topic = TOPIC.replace("{agentId}", agentId).replace("{thingsId}", thingsId);
        return topic;
    }

    /**
     * 发送首次连接消息
     * @param agentId
     * @param thingsId
     */
    public static void sendConnectMsg(String agentId, String thingsId){
        MQServicePlugin mqPlugin = MQPluginManager.getMQPlugin(NatsPlugin.class.getName());
        mqPlugin.publish(getTopic(agentId,thingsId), JSONUtil.toJsonStr(connect(thingsId)), null);
    }
//    /**
//     * 发送重新连接消息
//     * @param agentId
//     * @param thingsId
//     */
//    public static void sendRetryConnectMsg(String agentId, String thingsId){
//        MQServicePlugin mqPlugin = MQPluginManager.getMQPlugin(NatsPlugin.class.getName());
//        mqPlugin.publish(getTopic(agentId,thingsId), JSONUtil.toJsonStr(retryConnect(thingsId)), null);
//    }


    /**
     * 发送重新连接消息
     * @param agentId
     * @param thingsId
     */
    public static void sendDisConnectMsg(String agentId, String thingsId){
        MQServicePlugin mqPlugin = MQPluginManager.getMQPlugin(NatsPlugin.class.getName());
        mqPlugin.publish(getTopic(agentId,thingsId), JSONUtil.toJsonStr(disConnect(thingsId)), null);
    }
}
