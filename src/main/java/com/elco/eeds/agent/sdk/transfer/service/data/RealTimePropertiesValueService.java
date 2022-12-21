package com.elco.eeds.agent.sdk.transfer.service.data;

import com.elco.eeds.agent.mq.nats.plugin.NatsPlugin;
import com.elco.eeds.agent.mq.plugin.MQPluginManager;
import com.elco.eeds.agent.mq.plugin.MQServicePlugin;
import com.elco.eeds.agent.sdk.core.bean.agent.Agent;
import com.elco.eeds.agent.sdk.core.bean.properties.PropertiesValue;
import com.elco.eeds.agent.sdk.transfer.beans.message.data.realTime.DataRealTimePropertiesMessage;
import com.elco.eeds.agent.sdk.transfer.handler.data.sync.DataSyncCancelMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @ClassName RealTimePropertiesValueService
 * @Description 实时数据类
 * @Author OUYANG
 * @Date 2022/12/20 13:39
 */
public class RealTimePropertiesValueService {
    private static Logger logger = LoggerFactory.getLogger(RealTimePropertiesValueService.class);
    /**
     *
     * @param message 原始报文
     * @param thingsId 数据源ID
     * @param collectTime 采集时间戳
     * @param propertiesValueList 解析数据
     */
    public static void recRealTimePropertiesValue(String message, String thingsId,Long collectTime, List<PropertiesValue> propertiesValueList) {
        //存储原始数据，并推送，调用统计接口
        MQServicePlugin mqPlugin = MQPluginManager.getMQPlugin(NatsPlugin.class.getName());
        String postMsg = DataRealTimePropertiesMessage.getMessage(propertiesValueList);
        String agentId = Agent.getInstance().getAgentBaseInfo().getAgentId();
        String topic = DataRealTimePropertiesMessage.getTopic(agentId, thingsId);
        logger.info("实时数据推送：topic:{}, msg:{}", topic, postMsg);
        mqPlugin.publish(topic, postMsg, null);
    }

}
