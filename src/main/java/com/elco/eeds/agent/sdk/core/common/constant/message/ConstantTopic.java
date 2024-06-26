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
     * 客户端链接测试
     */
    public static final String TOPIC_AGENT_LINK_TEST_REQ = "server.agent.linkTest.request.{agentId}";
    public static final String TOPIC_AGENT_LINK_TEST_RSP = "agent.agent.linkTest.respond.{agentId}";

    /**
     * 客户端链接测试(集群)
     */
    public static final String TOPIC_AGENT_LINK_TEST_CLUSTER_REQ = "server.agent.linkTestCluster.request.{agentId}";
    public static final String TOPIC_AGENT_LINK_TEST_CLUSTER_RSP = "agent.agent.linkTestCluster.respond.{agentId}";

    /**
     * 客户端下发报文
     */
    public static final String TOPIC_AGENT_COMMAND_DISTRIBUTE_REQ = "server.agent.order.request.{agentId}.{thingsId}";
    public static final String TOPIC_AGENT_COMMAND_DISTRIBUTE_RSP = "agent.agent.order.respond.{agentId}.{thingsId}";

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
     * 数据--同步-取消
     */
    public static final String TOPIC_REC_DATA_SYNC_CANCEL = "server.data.syncData.cancel.{agentId}";
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


    /**
     * 数据--实时--变量值
     */
    public static final String TOPIC_SED_DATA_REALTIME_PROPERTIES = "agent.data.realTime.value.{agentId}.{thingsId}";

    /**
     * 数据源--同步--增量同步
     */
    public static final String TOPIC_REC_THINGS_SYNC_INCR = "server.things.sync.incr.{agentId}";


    /**
     * 数据源健康状态（心跳）
     * agent.things.heartBeat.request.{agentId}
     */
    public static final String TOPIC_THINGS_HEARTBEAT_REQUEST = "agent.things.heartBeat.request.";

    /**
     * 数据源连接状态
     * agent.things.connectStatus.request.{thingsId}
     */
    public static final String TOPIC_THINGS_CONNECTSTATUS_REQUEST = "agent.things.connectStatus.request.";

    /**
     * 数据源手动重连
     * server.things.reconnect.manual.{agentId}
     */
    public static final String TOPIC_SERVER_THINGS_RECONNECT_MANUAL = "server.things.reconnect.manual.{agentId}";

    /**
     * 指令下发请求报文
     * server.agent.order.request.{agentId}
     */
    public static final String TOPIC_SERVER_AGENT_ORDER_REQUEST = "server.agent.order.request.{agentId}";

    /**
     * 指令下发请求报文(功能)
     * server.cmd.service.request.{agentId}
     */
    public static final String TOPIC_SERVER_CMD_SERVICE_REQUEST = "server.cmd.service.request.{agentId}";

    /**
     * 指令下发确认报文
     */
    public static final String TOPIC_AGENT_CMD_CONFIRM_RESPOND = "agent.agent.instruct.confirm.{thingsId}";

    /**
     * 指令下发结果报文
     */
    public static final String TOPIC_AGENT_CMD_RESULT_RESPOND = "agent.agent.instruct.respond.{thingsId}";


    /**
     * 指令下发确认报文
     * agent.agent.order.confirm.{thingsId}
     */
    public static final String TOPIC_AGENT_AGENT_ORDER_CONFIRM = "agent.agent.order.confirm.";

    /**
     * 下发指令返回结果报文
     * agent.agent.order.respond.{thingsId}
     */
    public static final String TOPIC_AGENT_AGENT_ORDER_RESPOND = "agent.agent.order.respond.";

    /**
     * 数据库切换报文
     */
    public static final String TOPIC_STORAGE_DB_CHANGE = "server.base.storage.change";
    /**
     * 数据库切换结果报文
     */
    public static final String TOPIC_STORAGE_DB_CHANGE_RESULT = "server.base.storage.change.result";

}
