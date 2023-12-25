package com.elco.eeds.agent.sdk.transfer.beans.message.agent.linktest;

import java.io.Serializable;

/**
 * @ClassName SubAgentLinkTestClusterMessage
 * @Description 客户端链路测试子报文
 * @Author OuYang
 * @Date 2023/12/21 11:17
 * @Version 1.0
 */
public class SubAgentLinkTestClusterMessage implements Serializable {
    private String nodeName;
    private String nodeIp;
    private String sessionId;

    private SubAgentLinkTestData data;

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getNodeIp() {
        return nodeIp;
    }

    public void setNodeIp(String nodeIp) {
        this.nodeIp = nodeIp;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public SubAgentLinkTestData getData() {
        return data;
    }

    public void setData(SubAgentLinkTestData data) {
        this.data = data;
    }
}
