package com.elco.eeds.agent.sdk.core.start;

import cn.hutool.json.JSONUtil;
import com.elco.eeds.agent.mq.plugin.MQPluginManager;
import com.elco.eeds.agent.mq.plugin.MQServicePlugin;
import com.elco.eeds.agent.sdk.core.bean.agent.Agent;
import com.elco.eeds.agent.sdk.core.bean.agent.AgentMqInfo;
import com.elco.eeds.agent.sdk.core.common.constant.message.ConstantTopic;
import com.elco.eeds.agent.sdk.core.common.enums.ErrorEnum;
import com.elco.eeds.agent.sdk.core.exception.SdkException;
import com.elco.eeds.agent.sdk.core.util.ReplaceTopicAgentId;
import com.elco.eeds.agent.sdk.transfer.beans.agent.AgentTokenRequest;
import com.elco.eeds.mq.eventbus.plugin.EventBusPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName SilmAgentRegisterService
 * @Description SLIM模式下的注册
 * @Author OuYang
 * @Date 2024/4/1 13:15
 * @Version 1.0
 */
public class AgentSlimRegisterService extends AbstractAgentRegisterService {

  private static Logger logger = LoggerFactory.getLogger(AgentSlimRegisterService.class);

  public AgentSlimRegisterService() {
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
  protected void syncThings() {
    thingsSyncService.setupSyncThings();
  }



  @Override
  public void loadMq(AgentMqInfo mqInfo){
    try {
      Agent agent = Agent.getInstance();
      String agentId = agent.getAgentBaseInfo().getAgentId();
      String mqInfoStr = JSONUtil.toJsonStr(mqInfo);
      // 将mqSecurityInfo 替换为 tlsInfo
      mqInfoStr = mqInfoStr.replace("mqSecurityInfo", "tlsInfo");
      // 加载插件
      MQPluginManager.loadPlugins(mqInfoStr);
      // 实例化MQ插件
      MQServicePlugin eventBusClient = MQPluginManager.getMQPlugin(EventBusPlugin.class.getName());

      // 订阅 更新token报文topic
      eventBusClient.subscribe(
          ReplaceTopicAgentId.getTopicWithRealAgentId(ConstantTopic.TOPIC_AGENT_TOKEN, agentId),
          agentTokenMessageHandler);
      // 订阅 客户端心跳报文topic
      eventBusClient.subscribe(
          ReplaceTopicAgentId.getTopicWithRealAgentId(ConstantTopic.TOPIC_AGENT_HEART_REQ, agentId),
          agentHeartMessageHandler);
      // 订阅 基础配置修改（全局）topic
      eventBusClient.subscribe(
          ReplaceTopicAgentId.getTopicWithRealAgentId(ConstantTopic.TOPIC_AGENT_CONFIG_GLOBAL,
              agentId), agentConfigGlobalMessageHandler);
      // 订阅 基础配置修改（私有）topic
      eventBusClient.subscribe(
          ReplaceTopicAgentId.getTopicWithRealAgentId(ConstantTopic.TOPIC_AGENT_CONFIG_LOCAL,
              agentId), agentConfigLocalMessageHandler);
      eventBusClient.subscribe(
          ReplaceTopicAgentId.getTopicWithRealAgentId(ConstantTopic.TOPIC_AGENT_LINK_TEST_REQ,
              agentId), agentLinkTestMessageHandler);
      eventBusClient.subscribe(ReplaceTopicAgentId.getTopicWithRealAgentId(
              ConstantTopic.TOPIC_AGENT_LINK_TEST_CLUSTER_REQ, agentId),
          agentLinkTestClusterMessageHandler);

      //数据源--增量同步
      eventBusClient.subscribe(
          ReplaceTopicAgentId.getTopicWithRealAgentId(ConstantTopic.TOPIC_REC_THINGS_SYNC_INCR,
              agentId), thingsSyncIncrMessageHandler);

      //统计--统计确认
      eventBusClient.subscribe(
          ReplaceTopicAgentId.getTopicWithRealAgentId(ConstantTopic.TOPIC_REC_DATA_COUNT_CONFIRM,
              agentId), dataCountConfirmMessageHandler);

      //同步--数据--请求
      eventBusClient.subscribe(
          ReplaceTopicAgentId.getTopicWithRealAgentId(ConstantTopic.TOPIC_REC_DATA_SYNC_REQ,
              agentId), dataSyncRequestMessageHandler);
      //同步--数据--取消
      eventBusClient.subscribe(
          ReplaceTopicAgentId.getTopicWithRealAgentId(ConstantTopic.TOPIC_REC_DATA_SYNC_CANCEL,
              agentId), dataSyncCancelMessageHandler);

      //指令下发请求
      eventBusClient.subscribe(ReplaceTopicAgentId.getTopicWithRealAgentId(
          ConstantTopic.TOPIC_SERVER_AGENT_ORDER_REQUEST, agentId), orderRequestMessageHandler);

      // 数据源重新连接
      eventBusClient.subscribe(ReplaceTopicAgentId.getTopicWithRealAgentId(
              ConstantTopic.TOPIC_SERVER_THINGS_RECONNECT_MANUAL, agentId),
          thingsReconnectManualMessageHandler);

      // 指令下发--功能
      eventBusClient.subscribe(ReplaceTopicAgentId.getTopicWithRealAgentId(
          ConstantTopic.TOPIC_SERVER_CMD_SERVICE_REQUEST, agentId), cmdRequestMessageHandler);

      // 订阅其他topic...
      // 待补充
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("加载Nats组件异常：", e);
      throw new SdkException(ErrorEnum.NATS_LOAD_ERROR.code());
    }
  }


}
