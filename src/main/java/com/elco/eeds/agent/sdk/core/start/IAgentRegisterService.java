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
     * 存储客户端文件
     * @param agentInfo
     * @throws SdkException
     */
    void saveAgentFile(Agent agentInfo) throws SdkException;

    /**
     * 关闭客户端
     * @param msg
     */
    void close(String msg);
}
