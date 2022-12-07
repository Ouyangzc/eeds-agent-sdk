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




}
