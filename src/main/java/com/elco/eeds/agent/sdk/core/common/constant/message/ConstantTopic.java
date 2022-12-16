package com.elco.eeds.agent.sdk.core.common.constant.message;

/**
 * @title: topic
 * @Author wl
 * @Date: 2022/12/5 22:35
 * @Version 1.0
 */
public class ConstantTopic {

    /**
     * 客户端心跳请求
     */
    public static final String TOPIC_AGENT_HEART_REQ = "server.agent.heartBeat.request.{agentId}";
    /**
     * 客户端心跳回应
     */
    public static final String TOPIC_AGENT_HEART_RSP = "agent.agent.heartBeat.respond.{agentId}";
    /**
     * 客户端全局配置
     */
    public static final String TOPIC_AGENT_CONFIG_GLOBAL = "server.agent.config.globalConfig";
    /**
     * 客户端局部配置
     */
    public static final String TOPIC_AGENT_CONFIG_LOCAL = "server.agent.config.localConfig.{agentId}";
    /**
     * 客户端token
     */
    public static final String TOPIC_AGENT_TOKEN = "server.agent.config.token.{agentId}";


    /**
     * 接收--数据统计确认报文
     */
    public static final String TOPIC_REC_DATA_COUNT_CONFIRM = "server.data.count.confirm.{agentId}";
    /**
     * 发送--数据统计记录发送
     */
    public static final String TOPIC_SED_DATA_COUNT_POST = "agent.data.count.post.{agentId}";


    /**
     * 数据--同步--请求--接收
     */
    public static final String TOPIC_REC_DATA_SYNC_REQ = "server.data.syncData.request.{agentId}";
    /**
     * 数据--同步--确认--发送
     */
    public static final String TOPIC_SED_DATA_SYNC_CONFRIM = "agent.data.syncData.confirm.{agentId}";
    /**
     * 数据--同步--同步数据--发送
     */
    public static final String TOPIC_SED_DATA_SYNC_DATA = "agent.data.syncData.value.{agentId}.{thingsId}";
    /**
     * 数据--同步--同步数据--同步完成
     */
    public static final String TOPIC_SED_DATA_SYNC_FINISH = "agent.data.syncData.respond.{agentId}";
}
