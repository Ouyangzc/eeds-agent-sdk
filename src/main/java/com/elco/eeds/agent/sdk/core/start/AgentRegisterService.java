package com.elco.eeds.agent.sdk.core.start;

import cn.hutool.json.JSONUtil;
import com.elco.eeds.agent.mq.nats.plugin.NatsPlugin;
import com.elco.eeds.agent.mq.plugin.MQPluginManager;
import com.elco.eeds.agent.mq.plugin.MQServicePlugin;
import com.elco.eeds.agent.sdk.core.bean.agent.Agent;
import com.elco.eeds.agent.sdk.core.bean.agent.AgentMqInfo;
import com.elco.eeds.agent.sdk.core.common.constant.message.ConstantTopic;
import com.elco.eeds.agent.sdk.core.common.enums.ErrorEnum;
import com.elco.eeds.agent.sdk.core.exception.SdkException;
import com.elco.eeds.agent.sdk.core.util.ReplaceTopicAgentId;
import com.elco.eeds.agent.sdk.transfer.beans.agent.AgentTokenRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @title: AgentRegisterService
 * @Author wl
 * @Date: 2022/12/6 11:25
 * @Version 1.0
 * @Description: 客户端注册逻辑处理类
 */
public class AgentRegisterService extends AbstractAgentRegisterService {

  private static Logger logger = LoggerFactory.getLogger(AgentRegisterService.class);
  public AgentRegisterService() {
  }

  @Override
  protected Agent requestServer(String clientIp, String port, String name, String token,
      String agentClientType) {
    Agent agent = requestHttpService.register(clientIp, port, name, token,
        AgentStartProperties.getInstance().getAgentClientType());
    return agent;
  }

  @Override
  protected void updateAgentEffect(AgentTokenRequest agentTokenRequest) {
    requestHttpService.updateAgentEffectTime(agentTokenRequest);
  }

  @Override
  public void loadMq(AgentMqInfo mqInfo) {
    try {
      Agent agent = Agent.getInstance();
      String agentId = agent.getAgentBaseInfo().getAgentId();
      String mqInfoStr = JSONUtil.toJsonStr(mqInfo);
      // 将mqSecurityInfo 替换为 tlsInfo
      mqInfoStr = mqInfoStr.replace("mqSecurityInfo", "tlsInfo");
      // 加载插件
      MQPluginManager.loadPlugins(mqInfoStr);
      // 实例化MQ插件
      MQServicePlugin natsClient = MQPluginManager.getMQPlugin(NatsPlugin.class.getName());
      ;
      // 订阅 更新token报文topic
      natsClient.subscribe(
          ReplaceTopicAgentId.getTopicWithRealAgentId(ConstantTopic.TOPIC_AGENT_TOKEN, agentId),
          agentTokenMessageHandler);
      // 订阅 客户端心跳报文topic
      natsClient.subscribe(
          ReplaceTopicAgentId.getTopicWithRealAgentId(ConstantTopic.TOPIC_AGENT_HEART_REQ, agentId),
          agentHeartMessageHandler);
      // 订阅 基础配置修改（全局）topic
      natsClient.subscribe(
          ReplaceTopicAgentId.getTopicWithRealAgentId(ConstantTopic.TOPIC_AGENT_CONFIG_GLOBAL,
              agentId), agentConfigGlobalMessageHandler);
      // 订阅 基础配置修改（私有）topic
      natsClient.subscribe(
          ReplaceTopicAgentId.getTopicWithRealAgentId(ConstantTopic.TOPIC_AGENT_CONFIG_LOCAL,
              agentId), agentConfigLocalMessageHandler);
      natsClient.subscribe(
          ReplaceTopicAgentId.getTopicWithRealAgentId(ConstantTopic.TOPIC_AGENT_LINK_TEST_REQ,
              agentId), agentLinkTestMessageHandler);
      natsClient.subscribe(ReplaceTopicAgentId.getTopicWithRealAgentId(
              ConstantTopic.TOPIC_AGENT_LINK_TEST_CLUSTER_REQ, agentId),
          agentLinkTestClusterMessageHandler);

      //数据源--增量同步
      natsClient.subscribe(
          ReplaceTopicAgentId.getTopicWithRealAgentId(ConstantTopic.TOPIC_REC_THINGS_SYNC_INCR,
              agentId), thingsSyncIncrMessageHandler);

      //统计--统计确认
      natsClient.subscribe(
          ReplaceTopicAgentId.getTopicWithRealAgentId(ConstantTopic.TOPIC_REC_DATA_COUNT_CONFIRM,
              agentId), dataCountConfirmMessageHandler);

      //同步--数据--请求
      natsClient.subscribe(
          ReplaceTopicAgentId.getTopicWithRealAgentId(ConstantTopic.TOPIC_REC_DATA_SYNC_REQ,
              agentId), dataSyncRequestMessageHandler);
      //同步--数据--取消
      natsClient.subscribe(
          ReplaceTopicAgentId.getTopicWithRealAgentId(ConstantTopic.TOPIC_REC_DATA_SYNC_CANCEL,
              agentId), dataSyncCancelMessageHandler);

      //指令下发请求
      natsClient.subscribe(ReplaceTopicAgentId.getTopicWithRealAgentId(
          ConstantTopic.TOPIC_SERVER_AGENT_ORDER_REQUEST, agentId), orderRequestMessageHandler);

      // 数据源重新连接
      natsClient.subscribe(ReplaceTopicAgentId.getTopicWithRealAgentId(
              ConstantTopic.TOPIC_SERVER_THINGS_RECONNECT_MANUAL, agentId),
          thingsReconnectManualMessageHandler);

      // 指令下发--功能
      natsClient.subscribe(ReplaceTopicAgentId.getTopicWithRealAgentId(
          ConstantTopic.TOPIC_SERVER_CMD_SERVICE_REQUEST, agentId), cmdRequestMessageHandler);

      //数据库切换
      natsClient.subscribe(ConstantTopic.TOPIC_STORAGE_DB_CHANGE,dbChangeMessageHandler);

      // 订阅其他topic...
      // 待补充
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("加载Nats组件异常：", e);
      throw new SdkException(ErrorEnum.NATS_LOAD_ERROR.code());
    }
  }

  @Override
  protected void syncThings() {
    thingsSyncService.setupSyncThings();
  }




}
