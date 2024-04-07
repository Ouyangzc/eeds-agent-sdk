package com.elco.eeds.agent.sdk.core.start;

import cn.hutool.core.util.ObjectUtil;
import com.elco.eeds.agent.sdk.core.bean.agent.Agent;
import com.elco.eeds.agent.sdk.core.exception.SdkException;
import com.elco.eeds.agent.sdk.core.util.AgentFileExtendUtils;
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
import com.elco.eeds.agent.sdk.transfer.handler.things.ThingsReconnectManualMessageHandler;
import com.elco.eeds.agent.sdk.transfer.handler.things.ThingsSyncIncrMessageHandler;
import com.elco.eeds.agent.sdk.transfer.service.cmd.CmdRequestManager;
import com.elco.eeds.agent.sdk.transfer.service.cmd.CmdService;
import com.elco.eeds.agent.sdk.transfer.service.data.sync.DataSyncService;
import com.elco.eeds.agent.sdk.transfer.service.things.ThingsSyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName AbstractAgentRegisterService
 * @Description TODO
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

  protected ThingsSyncService thingsSyncService;

  protected ThingsSyncIncrMessageHandler thingsSyncIncrMessageHandler;

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
