package com.elco.eeds.agent.sdk.transfer.handler.agent;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.elco.eeds.agent.sdk.core.bean.agent.Agent;
import com.elco.eeds.agent.sdk.core.bean.agent.AgentBaseInfo;
import com.elco.eeds.agent.sdk.core.bean.agent.BaseConfigEntity;
import com.elco.eeds.agent.sdk.core.common.constant.ConstantCommon;
import com.elco.eeds.agent.sdk.core.exception.SdkException;
import com.elco.eeds.agent.sdk.core.util.AgentFileExtendUtils;
import com.elco.eeds.agent.sdk.core.util.ReflectUtils;
import com.elco.eeds.agent.sdk.transfer.beans.message.config.AgentConfigMessage;
import com.elco.eeds.agent.sdk.transfer.handler.IReceiverMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @title: AgentLocalConfigMessageHandler
 * @Author wl
 * @Date: 2022/12/6 9:08
 * @Version 1.0
 * @Description: 客户端私有配置报文处理类
 */
public class AgentConfigLocalMessageHandler extends IReceiverMessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(AgentConfigLocalMessageHandler.class);

    @Override
    public void handleRecData(String topic, String recData) {
        logger.info("收到客户端私有配置报文：topic: {}, msg: {}", topic, recData);
        AgentConfigMessage message = JSON.parseObject(recData, AgentConfigMessage.class);
        Agent agent = Agent.getInstance();
        AgentBaseInfo agentBaseInfo = agent.getAgentBaseInfo();
        List<BaseConfigEntity> waitWriteJsonList = new ArrayList<>();
        // 从agent.json中取出config信息
        try {
            String config = AgentFileExtendUtils.getConfigFromLocalAgentFile();
            JSONArray jsonArray = JSON.parseArray(config);
            List<BaseConfigEntity> list = JSON.parseArray(jsonArray.toJSONString(), BaseConfigEntity.class);
            List<BaseConfigEntity> listTemp = list.stream().collect(Collectors.toList());
            listTemp.forEach(e -> {
                String configFieldName = e.getConfigFieldName();
                // 通过反射，判断SubAgentConfigMessage类中是否含有同名的Field
                boolean containKey = ReflectUtils.isContainKey(message.getData(), configFieldName);
                // 有，更新该值
                if(containKey) {
                    if (ReflectUtils.isContainKey(agentBaseInfo, configFieldName)) {
                        // 更新
                        String value = (String) ReflectUtils.invokeGet(message.getData(), configFieldName);
                        if(!"".equals(value)) {
                            ReflectUtils.invokeSet(agentBaseInfo, configFieldName, value);
                            // 准备要写入json的数据
                            waitWriteJsonList.add(new BaseConfigEntity(configFieldName, value, ConstantCommon.ONE));
                        }else {
                            // 如果报文中value为空字符串，则将该字段的公私属性置为"公有"
                            waitWriteJsonList.add(new BaseConfigEntity(e.getConfigFieldName(), e.getConfigFieldValue(), ConstantCommon.ZERO));
                        }
                    }else {
                        waitWriteJsonList.add(e);
                    }
                }else {
                    waitWriteJsonList.add(e);
                }
            });
            agent.setAgentBaseInfo(agentBaseInfo);
            // 将新的客户端生效的配置，写入agent.json
            JSONArray writeAgentFileJsonArray = JSON.parseArray(JSONUtil.toJsonStr(waitWriteJsonList));
            AgentFileExtendUtils.setConfigToLocalAgentFile(writeAgentFileJsonArray);
            logger.debug("客户端更新私有配置成功，新的客户端配置为：{}", agent.getAgentBaseInfo());
        } catch (SdkException e) {
            logger.error("客户端私有配置报文处理异常:", e);
            e.printStackTrace();
        }

    }
}
