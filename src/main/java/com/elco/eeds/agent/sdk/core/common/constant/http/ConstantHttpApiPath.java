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
     * 客户端注册 url路径
     */
    public static final String AGENT_REGISTER = "/eeds-sys-config/agents/autoAddAgents";

    /**
     * 客户端更新token url路径
     */
    public static final String AGENT_TOKEN = "/eeds-sys-config/agents/updateAfterFinishToken";



}
