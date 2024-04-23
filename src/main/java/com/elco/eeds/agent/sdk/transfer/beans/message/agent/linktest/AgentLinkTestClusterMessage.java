package com.elco.eeds.agent.sdk.transfer.beans.message.agent.linktest;

import com.elco.eeds.agent.sdk.core.common.enums.MessageMethod;
import com.elco.eeds.agent.sdk.core.util.DateUtils;
import com.elco.eeds.agent.sdk.transfer.beans.message.BaseMessage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName AgentLinkTestClusterMessage
 * @Description 客户端链路测试
 * @Author OuYang
 * @Date 2023/12/21 11:21
 * @Version 1.0
 */
public class AgentLinkTestClusterMessage extends BaseMessage<SubAgentLinkTestClusterMessage> implements Serializable {

    public static AgentLinkTestClusterMessage getRspMessage(String nodeName, String nodeIp,String sessionId) {
        AgentLinkTestClusterMessage message = new AgentLinkTestClusterMessage();
        message.setMethod(MessageMethod.AGENT_LINK_TEST.getMethod());
        message.setTimestamp(DateUtils.getTimestamp());
        // 准备数据
        SubAgentLinkTestClusterMessage subMsg = new SubAgentLinkTestClusterMessage();
        subMsg.setNodeName(nodeName);
        subMsg.setNodeIp(nodeIp);
        subMsg.setSessionId(sessionId);
        SubAgentLinkTestData subAgentLinkTestData = new SubAgentLinkTestData();
        subAgentLinkTestData.setAgentStatus(new SubAgentLinkTestStatus("客户端连接成功..."));
        List<SubAgentLinkTestThingsData> list = new ArrayList<>();
        Map<String, Object> filter = new HashMap<String, Object>();
        filter.put("filter", "");
        list.add(SubAgentLinkTestThingsData.getTestData());
        subAgentLinkTestData.setThingsData(list);
        subMsg.setData(subAgentLinkTestData);

        message.setData(subMsg);
        return message;
    }
}
