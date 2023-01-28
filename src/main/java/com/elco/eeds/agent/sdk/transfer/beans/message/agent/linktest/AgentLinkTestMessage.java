package com.elco.eeds.agent.sdk.transfer.beans.message.agent.linktest;

import cn.hutool.core.util.IdUtil;
import com.elco.eeds.agent.sdk.core.common.constant.message.ConstantMethod;
import com.elco.eeds.agent.sdk.core.util.DateUtils;
import com.elco.eeds.agent.sdk.transfer.beans.message.BaseMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @title: AgentLinkTestMessage
 * @Author wl
 * @Date: 2022/12/20 13:11
 * @Version 1.0
 * @Description: 客户端链路测试
 */
public class AgentLinkTestMessage extends BaseMessage<SubAgentLinkTestMessage> {

    public static AgentLinkTestMessage getRspMessage(String pkUser, String socketId) {
        AgentLinkTestMessage message = new AgentLinkTestMessage();
        message.setMethod(ConstantMethod.METHOD_AGENT_LINK_TEST);
        message.setTimestamp(DateUtils.getTimestamp());
        // 准备数据
        SubAgentLinkTestMessage subAgentLinkTestMessage = new SubAgentLinkTestMessage();
        subAgentLinkTestMessage.setPkUser(pkUser);
        subAgentLinkTestMessage.setSocketId(socketId);
        SubAgentLinkTestData subAgentLinkTestData = new SubAgentLinkTestData();
        subAgentLinkTestData.setAgentStatus(new SubAgentLinkTestStatus("客户端连接成功..."));
        List<SubAgentLinkTestThingsData> list = new ArrayList<>();
        Map<String, Object> filter = new HashMap<String, Object>();
        filter.put("filter", "");
        list.add(SubAgentLinkTestThingsData.getTestData());
        subAgentLinkTestData.setThingsData(list);
        subAgentLinkTestMessage.setData(subAgentLinkTestData);

        message.setData(subAgentLinkTestMessage);
        return message;
    }

}
