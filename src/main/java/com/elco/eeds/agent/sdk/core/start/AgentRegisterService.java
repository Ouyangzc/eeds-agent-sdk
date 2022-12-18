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
import com.elco.eeds.agent.sdk.core.util.AgentFileExtendUtils;
import com.elco.eeds.agent.sdk.core.util.ReplaceTopicAgentId;
import com.elco.eeds.agent.sdk.core.util.http.IpUtil;
import com.elco.eeds.agent.sdk.transfer.beans.agent.AgentTokenRequest;
import com.elco.eeds.agent.sdk.transfer.handler.agent.AgentConfigGlobalMessageHandler;
import com.elco.eeds.agent.sdk.transfer.handler.agent.AgentConfigLocalMessageHandler;
import com.elco.eeds.agent.sdk.transfer.handler.agent.AgentHeartMessageHandler;
import com.elco.eeds.agent.sdk.transfer.handler.agent.AgentTokenMessageHandler;
import com.elco.eeds.agent.sdk.transfer.service.agent.AgentRequestHttpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @title: AgentRegisterService
 * @Author wl
 * @Date: 2022/12/6 11:25
 * @Version 1.0
 * @Description: 客户端注册逻辑处理类
 */
public class AgentRegisterService implements IAgentRegisterService {

    private static final Logger logger = LoggerFactory.getLogger(AgentRegisterService.class);

    private final AgentTokenMessageHandler agentTokenMessageHandler = new AgentTokenMessageHandler();
    private final AgentHeartMessageHandler agentHeartMessageHandler = new AgentHeartMessageHandler();
    private final AgentConfigGlobalMessageHandler agentConfigGlobalMessageHandler = new AgentConfigGlobalMessageHandler();
    private final AgentConfigLocalMessageHandler agentConfigLocalMessageHandler = new AgentConfigLocalMessageHandler();

    private final AgentRequestHttpService agentRequestHttpService = new AgentRequestHttpService();

    @Override
    public boolean register(String serverUrl, String name, String port, String token) throws Exception {
        try {
            // 获取IP
            String clientIp = IpUtil.getLocalIpAddress();
            // TODO 待完成
            Agent agent = Agent.getInstance();
            // 调用http接口的register方法
            agent = agentRequestHttpService.register(clientIp, port, name, token, ConstantClientType.TYPE_EDGE_GATEWAY);
            if (agent == null) {
                throw new SdkException(ErrorEnum.CLIENT_REGISTER_ERROR.code());
            }
            // 刷新token
            // saveAgentFile(agent);
            // 回调更新客户端Token生效时间
            agentRequestHttpService.updateAgentEffectTime(new AgentTokenRequest(Long.parseLong(agent.getAgentBaseInfo().getAgentId()), agent.getAgentBaseInfo().getToken()));
            // 加载Nats组件
            loadMq(agent.getAgentMqInfo());
            // 数据源同步
            // TODO 数据源同步
            // 更新配置
            // TODO 更新配置
            logger.error("客户端注册流程成功！");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            // throw new SdkException(ErrorEnum.CLIENT_REGISTER_ERROR.code());
            logger.error("客户端注册流程失败！{}", e);
            this.close(e.getMessage());
            return false;
        }
    }

    @Override
    public void saveAgentFile(Agent agentInfo) throws SdkException {
        // 目前只保存token字段
        AgentFileExtendUtils.setTokenToLocalAgentFile(agentInfo.getAgentBaseInfo().getToken());
    }

    @Override
    public void loadMq(AgentMqInfo mqInfo) throws Exception {
        try {
            Agent agent = Agent.getInstance();
            String agentId = agent.getAgentBaseInfo().getAgentId();
            String mqInfoStr = JSON.toJSONString(mqInfo);
            // 将mqSecurityInfo 替换为 tlsInfo
            mqInfoStr = mqInfoStr.replace("mqSecurityInfo", "tlsInfo");
            // 加载插件
            MQPluginManager.loadPlugins(mqInfoStr);
            // 实例化MQ插件
            MQServicePlugin natsClient = MQPluginManager.getMQPlugin(NatsPlugin.class.getName());
            // 订阅 更新token报文topic
            // TODO 需要传入clientId
            natsClient.syncSub(ReplaceTopicAgentId.getTopicWithRealAgentId(ConstantTopic.TOPIC_AGENT_TOKEN, agentId), agentTokenMessageHandler);
            // 订阅 客户端心跳报文topic
            natsClient.syncSub(ReplaceTopicAgentId.getTopicWithRealAgentId(ConstantTopic.TOPIC_AGENT_HEART_REQ, agentId), agentHeartMessageHandler);
            // 订阅 基础配置修改（全局）topic
            natsClient.syncSub(ReplaceTopicAgentId.getTopicWithRealAgentId(ConstantTopic.TOPIC_AGENT_CONFIG_GLOBAL, agentId), agentConfigGlobalMessageHandler);
            // 订阅 基础配置修改（私有）topic
            natsClient.syncSub(ReplaceTopicAgentId.getTopicWithRealAgentId(ConstantTopic.TOPIC_AGENT_CONFIG_LOCAL, agentId), agentConfigLocalMessageHandler);
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
        // 关闭客户端
        logger.error("客户端即将关闭：{}", msg);
        //注册失败，退出程序
        System.exit(500);

    }
}
