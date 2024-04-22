package com.elco.eeds.agent.sdk.core.start;

import cn.hutool.core.util.ObjectUtil;
import com.elco.eeds.agent.sdk.core.bean.agent.Agent;
import com.elco.eeds.agent.sdk.core.bean.agent.AgentMqInfo;
import com.elco.eeds.agent.sdk.core.common.enums.AgentStatus;
import com.elco.eeds.agent.sdk.core.common.enums.ErrorEnum;
import com.elco.eeds.agent.sdk.core.exception.SdkException;
import com.elco.eeds.agent.sdk.core.util.AgentFileExtendUtils;
import com.elco.eeds.agent.sdk.core.util.http.IpUtil;
import com.elco.eeds.agent.sdk.transfer.beans.agent.AgentTokenRequest;
import com.elco.eeds.agent.sdk.transfer.handler.agent.AgentConfigGlobalMessageHandler;
import com.elco.eeds.agent.sdk.transfer.handler.agent.AgentConfigLocalMessageHandler;
import com.elco.eeds.agent.sdk.transfer.handler.agent.AgentHeartMessageHandler;
import com.elco.eeds.agent.sdk.transfer.handler.agent.AgentLinkTestClusterMessageHandler;
import com.elco.eeds.agent.sdk.transfer.handler.agent.AgentLinkTestMessageHandler;
import com.elco.eeds.agent.sdk.transfer.handler.agent.AgentTokenMessageHandler;
import com.elco.eeds.agent.sdk.transfer.handler.cmd.CmdRequestMessageHandler;
import com.elco.eeds.agent.sdk.transfer.handler.data.count.DataCountConfirmMessageHandler;
import com.elco.eeds.agent.sdk.transfer.handler.data.sync.DataSyncCancelMessageHandler;
import com.elco.eeds.agent.sdk.transfer.handler.data.sync.DataSyncRequestMessageHandler;
import com.elco.eeds.agent.sdk.transfer.handler.order.OrderRequestMessageHandler;
import com.elco.eeds.agent.sdk.transfer.handler.storage.StorageDbChangeMessageHandler;
import com.elco.eeds.agent.sdk.transfer.handler.things.ThingsReconnectManualMessageHandler;
import com.elco.eeds.agent.sdk.transfer.handler.things.ThingsSyncIncrMessageHandler;
import com.elco.eeds.agent.sdk.transfer.service.agent.AgentRequestHttpService;
import com.elco.eeds.agent.sdk.transfer.service.cmd.CmdRequestManager;
import com.elco.eeds.agent.sdk.transfer.service.cmd.CmdService;
import com.elco.eeds.agent.sdk.transfer.service.data.sync.DataSyncService;
import com.elco.eeds.agent.sdk.transfer.service.things.ThingsPropertiesService;
import com.elco.eeds.agent.sdk.transfer.service.things.ThingsSyncNewServiceImpl;
import com.elco.eeds.agent.sdk.transfer.service.things.ThingsSyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName AbstractAgentRegisterService
 * @Description 注册抽象
 * @Author OuYang
 * @Date 2024/4/1 13:17
 * @Version 1.0
 */
public abstract class AbstractAgentRegisterService implements IAgentRegisterService{
  private static Logger logger = LoggerFactory.getLogger(AbstractAgentRegisterService.class);

  protected AgentTokenMessageHandler agentTokenMessageHandler = new AgentTokenMessageHandler();
  protected OrderRequestMessageHandler orderRequestMessageHandler = new OrderRequestMessageHandler();
  protected AgentHeartMessageHandler agentHeartMessageHandler = new AgentHeartMessageHandler();
  protected AgentConfigGlobalMessageHandler agentConfigGlobalMessageHandler = new AgentConfigGlobalMessageHandler();
  protected AgentConfigLocalMessageHandler agentConfigLocalMessageHandler = new AgentConfigLocalMessageHandler();
  protected AgentLinkTestMessageHandler agentLinkTestMessageHandler = new AgentLinkTestMessageHandler();

  protected AgentLinkTestClusterMessageHandler agentLinkTestClusterMessageHandler = new AgentLinkTestClusterMessageHandler();
  protected DataCountConfirmMessageHandler dataCountConfirmMessageHandler = new DataCountConfirmMessageHandler();
  protected ThingsReconnectManualMessageHandler thingsReconnectManualMessageHandler = new ThingsReconnectManualMessageHandler();

  protected CmdRequestManager cmdRequestManager = new CmdRequestManager();
  protected CmdService cmdService = new CmdService(cmdRequestManager);
  protected CmdRequestMessageHandler cmdRequestMessageHandler = new CmdRequestMessageHandler(
      cmdService);

  protected DataSyncService dataSyncService = new DataSyncService();

  protected DataSyncRequestMessageHandler dataSyncRequestMessageHandler = new DataSyncRequestMessageHandler(
      dataSyncService);
  protected DataSyncCancelMessageHandler dataSyncCancelMessageHandler = new DataSyncCancelMessageHandler(
      dataSyncService);

  protected StorageDbChangeMessageHandler dbChangeMessageHandler = new StorageDbChangeMessageHandler();

  private static ThingsPropertiesService thingsPropertiesService = new ThingsPropertiesService();
  protected static AgentRequestHttpService requestHttpService = new AgentRequestHttpService();

  protected static ThingsSyncService thingsSyncService = new ThingsSyncNewServiceImpl(
      thingsPropertiesService);

  protected ThingsSyncIncrMessageHandler thingsSyncIncrMessageHandler= new ThingsSyncIncrMessageHandler(
      thingsSyncService);;




  public boolean processRegister(String name, String port, String token){
    try {
      //获取本地token
      String localToken = getLocalToken();
      if (null != localToken) {
        token = localToken;
      }
      // 获取IP
      String clientIp = IpUtil.getLocalIpAddress();
      //调用注册接口
      Agent agent = requestServer(clientIp, port, name, token,
          AgentStartProperties.getInstance().getAgentClientType());
      if (agent == null) {
        Agent instance = Agent.getInstance();
        instance.setAgentStatus(AgentStatus.ERROR);
        throw new SdkException(ErrorEnum.CLIENT_REGISTER_ERROR.code());
      }
      //回调token接口,
      AgentTokenRequest agentTokenRequest = new AgentTokenRequest(
          Long.parseLong(agent.getAgentBaseInfo().getAgentId()),
          agent.getAgentBaseInfo().getToken());
      updateAgentEffect(agentTokenRequest);
      //加载MQ
      loadMq(agent.getAgentMqInfo());
      //同步数据源
      syncThings();

      agent.setAgentStatus(AgentStatus.READY);
      return true;
    }catch (Exception e){
      Agent instance = Agent.getInstance();
      instance.setAgentStatus(AgentStatus.ERROR);
      logger.error("客户端注册流程失败", e);
      this.close(e.getMessage());
      return false;
    }
  }

  /**
   * 同步数据源
   */
  protected abstract void syncThings();

  /**
   * 更新Agent token生效
   * @param agentTokenRequest
   */
  protected abstract void updateAgentEffect(AgentTokenRequest agentTokenRequest);

  /**
   * 加载MQ
   * @param agentMqInfo
   */
  protected abstract void loadMq(AgentMqInfo agentMqInfo);

  /**
   * 请求注册
   * @param clientIp
   * @param port
   * @param name
   * @param token
   * @param agentClientType
   * @return
   */
  protected abstract Agent requestServer(String clientIp, String port, String name, String token, String agentClientType);

  @Override
  public void saveAgentFile(Agent agentInfo) throws SdkException {
    // 目前只保存token字段
    AgentFileExtendUtils.setTokenToLocalAgentFile(
        agentInfo.getAgentBaseInfo().getToken().toString());
  }

  @Override
  public void close(String msg) {
    // 关闭客户端
    logger.error("客户端即将关闭：{}", msg);
    //注册失败，退出程序
    System.exit(500);

  }

  protected String getLocalToken() {
    String token = null;
    try {
      String localToken = AgentFileExtendUtils.getTokenFromLocalAgentFile();
      if (ObjectUtil.isNotEmpty(localToken)) {
        token = localToken;
      }
    } catch (SdkException e) {
      logger.warn("获取本地token发生异常,异常信息：{}", e.getMessage());
      return null;
    }
    return token;
  }
}
