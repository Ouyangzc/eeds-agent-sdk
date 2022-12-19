package com.elco.eeds.agent.sdk.core.util;

import com.elco.eeds.agent.sdk.core.common.constant.ConstantCommon;

/**
 * @title: ReplaceTopicAgentId
 * @Author wl
 * @Date: 2022/12/9 10:23
 * @Version 1.0
 * @Description: 替换topic中的后缀{agentId}
 */
public class ReplaceTopicAgentId {

    public static String getTopicWithRealAgentId(String topic, String agentId) {
        return topic.replace(ConstantCommon.TOPIC_SUFFIX_AGENTID, agentId);
    }

}
