package com.elco.eeds.agent.sdk.transfer.service.data;

import cn.hutool.json.JSONUtil;
import com.elco.eeds.agent.mq.nats.plugin.NatsPlugin;
import com.elco.eeds.agent.mq.plugin.MQPluginManager;
import com.elco.eeds.agent.mq.plugin.MQServicePlugin;
import com.elco.eeds.agent.sdk.core.bean.agent.Agent;
import com.elco.eeds.agent.sdk.core.bean.agent.AgentBaseInfo;
import com.elco.eeds.agent.sdk.core.bean.properties.PropertiesContext;
import com.elco.eeds.agent.sdk.core.bean.properties.PropertiesValue;
import com.elco.eeds.agent.sdk.core.util.RealTimeDataMessageFileUtils;
import com.elco.eeds.agent.sdk.transfer.beans.data.OriginalPropertiesValueMessage;
import com.elco.eeds.agent.sdk.transfer.beans.data.count.ThingsDataCount;
import com.elco.eeds.agent.sdk.transfer.beans.message.data.realTime.DataRealTimePropertiesMessage;
import com.elco.eeds.agent.sdk.transfer.handler.properties.VirtualPropertiesHandle;
import com.elco.eeds.agent.sdk.transfer.service.data.count.DataCountServiceImpl;
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


    public DataCountServiceImpl dataCountService;

    /**
     * @param message             原始报文
     * @param thingsId            数据源ID
     * @param collectTime         采集时间戳
     * @param propertiesValueList 解析数据
     * @param propertiesContextList 变量信息
     */
    public static void recRealTimePropertiesValue(String message, String thingsId, Long collectTime, List<PropertiesValue> propertiesValueList, List<PropertiesContext> propertiesContextList) {
        if (!propertiesValueList.isEmpty()) {
            AgentBaseInfo agentBaseInfo = Agent.getInstance().getAgentBaseInfo();
            String agentId = agentBaseInfo.getAgentId();
            //存储原始数据
            OriginalPropertiesValueMessage originalPropertiesValueMessage = new OriginalPropertiesValueMessage();
            originalPropertiesValueMessage.setCollectTime(collectTime);
            originalPropertiesValueMessage.setMessage(message);
            RealTimeDataMessageFileUtils.writeAppend(thingsId, JSONUtil.toJsonStr(originalPropertiesValueMessage));

            // 计算虚拟变量
            VirtualPropertiesHandle.creatVirtualProperties(propertiesContextList, propertiesValueList, collectTime);

            //调用统计接口
            ThingsDataCount dataCount = new ThingsDataCount();
            dataCount.setThingsId(thingsId);
            dataCount.setSize(propertiesValueList.size());
            dataCount.setCollectTime(collectTime);
            dataCount.setStartTime(collectTime);
            dataCount.setEndTime(collectTime);
            DataCountServiceImpl.recRealTimeData(agentId, collectTime, dataCount);

            //推送数据
            MQServicePlugin mqPlugin = MQPluginManager.getMQPlugin(NatsPlugin.class.getName());
            String postMsg = DataRealTimePropertiesMessage.getMessage(propertiesValueList);

            String topic = DataRealTimePropertiesMessage.getTopic(agentId, thingsId);
            logger.debug("实时数据推送：采集时间:{} topic:{}, msg:{}", collectTime, topic, postMsg);
            mqPlugin.publish(topic, postMsg, null);
        }
    }

}
