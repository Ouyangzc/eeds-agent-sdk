package com.elco.eeds.agent.sdk.core.start;

import com.elco.eeds.agent.sdk.core.bean.agent.Agent;
import com.elco.eeds.agent.sdk.core.bean.agent.AgentMqInfo;
import com.elco.eeds.agent.sdk.core.exception.SdkException;

/**
 * @title: IAgentRegisterService
 * @Author wl
 * @Date: 2022/12/6 11:26
 * @Version 1.0
 * @Description: 客户端接口
 */
public interface IAgentRegisterService {

    /**
     * 注册客户端
     * @param url
     * @param name
     * @param token
     * @return
     * @throws Exception
     */
    boolean register(String url,String name,String port, String token, String clientType) throws Exception;

    /**
     * 存储客户端文件
     * @param agentInfo
     */
    void saveAgentFile(Agent agentInfo) throws SdkException;

    /**
     * 加载mq
     * @param mqInfo
     * @throws Exception
     */
    void loadMq(AgentMqInfo mqInfo) throws Exception;

    /**
     * 关闭客户端
     * @param msg
     */
    void close(String msg);
}
