package com.elco.eeds.agent.sdk.core.common.constant.message;

/**
 * @title: ConstantMethod
 * @Author wl
 * @Date: 2022/12/5 22:36
 * @Version 1.0
 */
public class ConstantMethod {

    /**
     * 客户端心跳请求
     */
    public static final String METHOD_AGENT_HEART_REQ = "agent_heartBeat_request";
    /**
     * 客户端心跳回应
     */
    public static final String METHOD_AGENT_HEART_RSP = "agent_heartBeat_respond";
    /**
     * 客户端全局配置
     */
    public static final String METHOD_AGENT_CONFIG_GLOBAL = "agent_global_config";
    /**
     * 客户端局部配置
     */
    public static final String METHOD_AGENT_CONFIG_LOCALCONFIG = "agent_local_config";
    /**
     * 客户端token
     */
    public static final String METHOD_AGENT_TOKEN = "agent_update_token";


    /**
     * 数据--统计--发送统计记录报文
     */
    public static final String METHOD_DATA_COUNT_POST = "data_count_post";

    /**
     * 数据--同步--同步确认
     */
    public static final String METHOD_DATA_SYNC_CONFIRM = "data_sync_confirm";
    /**
     * 数据--同步--同步数据
     */
    public static final String METHOD_DATA_SYNC_DATA = "data_sync_properties";

    /**
     * 数据--同步--同步完成
     */
    public static final String METHOD_DATA_SYNC_FINISH = "data_sync_finish";

}
