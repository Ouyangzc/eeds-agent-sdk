package com.elco.eeds.agent.sdk.core.start;

import com.alibaba.fastjson.JSON;
import com.elco.eeds.agent.mq.nats.plugin.NatsPlugin;
import com.elco.eeds.agent.mq.plugin.MQPluginManager;
import com.elco.eeds.agent.mq.plugin.MQServicePlugin;
import com.elco.eeds.agent.sdk.core.bean.agent.Agent;
import com.elco.eeds.agent.sdk.core.bean.agent.AgentMqInfo;
import com.elco.eeds.agent.sdk.core.common.constant.client.ConstantClientType;
import com.elco.eeds.agent.sdk.core.common.constant.message.ConstantTopic;
import com.elco.eeds.agent.sdk.core.common.enums.ErrorEnum;
import com.elco.eeds.agent.sdk.core.exception.SdkException;
import com.elco.eeds.agent.sdk.core.util.AgentFileUtils;
import com.elco.eeds.agent.sdk.core.util.http.IpUtil;
import com.elco.eeds.agent.sdk.transfer.beans.http.request.AgentTokenRequest;
import com.elco.eeds.agent.sdk.transfer.handler.agent.AgentConfigGlobalMessageHandler;
import com.elco.eeds.agent.sdk.transfer.handler.agent.AgentConfigLocalMessageHandler;
import com.elco.eeds.agent.sdk.transfer.handler.agent.AgentHeartMessageHandler;
import com.elco.eeds.agent.sdk.transfer.handler.agent.AgentTokenMessageHandler;
import com.elco.eeds.agent.sdk.transfer.service.AgentRequestHttpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @title: AgentRegisterService
 * @Author wl
 * @Date: 2022/12/6 11:25
 * @Version 1.0
 * @Description: 客户端注册逻辑处理类
 */
public class AgentRegisterService implements IAgentRegisterService {

    private static Logger logger = LoggerFactory.getLogger(AgentRegisterService.class);

    private AgentTokenMessageHandler agentTokenMessageHandler = new AgentTokenMessageHandler();
    private AgentHeartMessageHandler agentHeartMessageHandler = new AgentHeartMessageHandler();
    private AgentConfigGlobalMessageHandler agentConfigGlobalMessageHandler = new AgentConfigGlobalMessageHandler();
    private AgentConfigLocalMessageHandler agentConfigLocalMessageHandler = new AgentConfigLocalMessageHandler();

    private AgentRequestHttpService agentRequestHttpService = new AgentRequestHttpService();

    @Override
    public boolean register(String serverUrl, String name, String port, String token) throws Exception {
        try {
            // 获取IP
            String clientIp = IpUtil.getLocalIpAddress();
            // TODO 待完成
            Agent agent = Agent.getInstance();
            // TODO 如果配置文件中的端口号和使用这调用init方法时传入的端口号不一致时，如何处理？
            // 调用http接口的register方法
            agent = agentRequestHttpService.register(clientIp, port, name, token, ConstantClientType.TYPE_EDGE_GATEWAY);
            // 刷新token
            saveAgentFile(agent);
            // 回调更新客户端Token生效时间
            AgentTokenRequest agentTokenRequest = new AgentTokenRequest(Long.parseLong(agent.getAgentBaseInfo().getAgentId()), agent.getAgentBaseInfo().getToken());
            agentRequestHttpService.updateAgentEffectTime(agentTokenRequest);
            // 加载Nats组件
            loadMq(agent.getAgentMqInfo());
            // 数据源同步
            // TODO 数据源同步
            // 更新配置
            // TODO 更新配置
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new SdkException(ErrorEnum.CLIENT_REGISTER_ERROR.code());
        }
    }

    @Override
    public void saveAgentFile(Agent agentInfo) throws SdkException {
        // 目前只保存token字段
        try {
            AgentFileUtils.strogeLocalAgentFile(agentInfo.getAgentBaseInfo().getToken().toString());
        } catch (IOException ioException) {
            ioException.printStackTrace();
            logger.error("保存agent.json异常", ioException);
            throw new SdkException(ErrorEnum.WRITE_AGENT_FILE_ERROR.code());
        }
    }

    @Override
    public void loadMq(AgentMqInfo mqInfo) throws Exception {
        try {
            String mqInfoStr = JSON.toJSONString(mqInfo);
            // 加载插件
            MQPluginManager.loadPlugins(mqInfoStr);
            // 实例化MQ插件
            MQServicePlugin natsClient = MQPluginManager.getMQPlugin(NatsPlugin.class.getName());
            // 订阅 更新token报文topic
            // TODO 需要传入clientId
            natsClient.syncSub(ConstantTopic.TOPIC_AGENT_TOKEN, agentTokenMessageHandler);
            // 订阅 客户端心跳报文topic
            natsClient.syncSub(ConstantTopic.TOPIC_AGENT_HEART_REQ, agentHeartMessageHandler);
            // 订阅 基础配置修改（全局）topic
            natsClient.syncSub(ConstantTopic.TOPIC_AGENT_CONFIG_GLOBAL, agentConfigGlobalMessageHandler);
            // 订阅 基础配置修改（私有）topic
            natsClient.syncSub(ConstantTopic.TOPIC_AGENT_CONFIG_LOCAL, agentConfigLocalMessageHandler);
            // 订阅其他topic...
            // 待补充
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("加载Nats组件异常：{}", e);
            throw new SdkException(ErrorEnum.NATS_LOAD_ERROR.code());
        }

    }

    @Override
    public void close(String msg) {
        // TODO 有疑问？？？

    }
}
