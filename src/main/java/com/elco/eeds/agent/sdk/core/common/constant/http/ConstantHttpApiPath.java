package com.elco.eeds.agent.sdk.core.common.constant.http;

/**
 * @title: ConstantHttpTag
 * @Author wl
 * @Date: 2022/12/6 15:24
 * @Version 1.0
 * @Description: 客户端需要调用的服务端接口的请求地址（不包含IP）
 */
public class ConstantHttpApiPath {

    /**
     * 单机前缀
     */
    public static final String STANDALONE_PREFIX = "/eeds-sys-config";

    /**
     * 集群前缀
     */
    public static final String CLUSTER_PREFIX = "/eeds-base-server";

    /**
     * 客户端注册 url路径
     */
    public static final String AGENT_REGISTER = "/agents/autoAddAgents";

    /**
     * 客户端更新token url路径
     */
    public static final String AGENT_TOKEN = "/agents/updateAfterFinishToken";

    /**
     * 数据源启动同步url路径
     */
    public static final String THINGS_SETUP_SYNC_API = "/things/getThingsBySync";

    /**
     * 数据源启动同步url路径
     */
    public static final String THINGS_SETUP_SYNC_NEW_API = "/things/getThingsSyncInfo";
    /**
     * 数据源增量同步url路径
     */
    public static final String THINGS_INCR_SYNC_API = "/things/saveAgentsByConfirm";



}
