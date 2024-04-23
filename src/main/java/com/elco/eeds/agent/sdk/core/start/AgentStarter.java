package com.elco.eeds.agent.sdk.core.start;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.json.JSONUtil;
import com.elco.eeds.agent.sdk.core.bean.agent.Agent;
import com.elco.eeds.agent.sdk.core.bean.agent.AgentBaseInfo;
import com.elco.eeds.agent.sdk.core.bean.agent.AgentSSLProperties;
import com.elco.eeds.agent.sdk.core.common.enums.AgentStatus;
import com.elco.eeds.agent.sdk.core.config.Config;
import com.elco.eeds.agent.sdk.core.config.ConfigLoader;
import com.elco.eeds.agent.sdk.core.connect.ThingsConnection;
import com.elco.eeds.agent.sdk.core.connect.ThingsConnectionHandler;
import com.elco.eeds.agent.sdk.core.connect.init.InitConnectFactory;
import com.elco.eeds.agent.sdk.core.util.AgentResourceUtils;
import com.elco.eeds.agent.sdk.transfer.quartz.CountScheduler;
import com.elco.eeds.agent.sdk.transfer.service.data.count.DataCountServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @title: AgentStarter
 * @Author wl
 * @Date: 2022/12/6 11:18
 * @Version 1.0
 * @Description: 客户端启动类
 */
public class AgentStarter {

  private static Logger logger = LoggerFactory.getLogger(AgentStarter.class);

  private static AbstractAgentRegisterService registerService =
      AgentResourceUtils.isSlimModle() ? new AgentSlimRegisterService()
          : new AgentRegisterService();

  private static CountScheduler countScheduler = new CountScheduler();

  private static void init(AgentStartProperties agentStartProperties) throws Exception {
    // 实例化Agent对象
    Agent agent = Agent.getInstance();
    agent.setAgentStatus(AgentStatus.INTI);
    try {
      // 存储解析对象实例
      ThingsConnection connection = ReflectUtil.newInstance(
          agentStartProperties.getProtocolPackage());
      ThingsConnectionHandler handler = (ThingsConnectionHandler) connection;
      agent.setDataParsing(handler.getParsing());

      // 将协议注入
      InitConnectFactory.addConnectPackagePath(agentStartProperties.getProtocolPackage());
      InitConnectFactory.initConnect();

      agent.setAgentBaseInfo(new AgentBaseInfo(agentStartProperties));
      // 注册
      logger.info("init,agentStartProperties:{}", JSONUtil.toJsonStr(agentStartProperties));
      registerService.processRegister(agentStartProperties.getName(),
          agentStartProperties.getPort(), agentStartProperties.getToken());
      // 加载数据文件
      com.elco.eeds.agent.sdk.core.util.FileUtil.getLastDataFile();
      // 定时任务
      countScheduler.scheduleJobs();
      agent.setAgentStatus(AgentStatus.LOAD);

      // 加载统计
      DataCountServiceImpl.createCountMapSection(null);
      DataCountServiceImpl.setUp();
      logger.info(Logo.logo);
      agent.setAgentStatus(AgentStatus.RUNNING);
    } catch (Exception e) {
      agent.setAgentStatus(AgentStatus.ERROR);
      e.printStackTrace();
      logger.error("客户端注册异常", e);
    }


  }

  /**
   * 客户端手动启动方法
   *
   * @param serverUrl  server地址
   * @param name       客户端名称
   * @param port       客户端端口
   * @param token      客户端token
   * @param baseFolder 存储文件目录
   * @throws Exception
   */
  public static void init(String serverUrl
      , String name
      , String port
      , String token
      , String baseFolder
      , String clientType
      , String protocolPackage
  ) throws Exception {
    logger.debug("开始手动初始化方法...");
    logger.debug("传入参数为：服务器地址：{}，客户端名称：{}，客户端端口：{}，token：{}，文件存储路径：{}",
        serverUrl, name, port, token, baseFolder);
    // 封装启动参数类
    AgentStartProperties agentStartProperties = AgentStartProperties.getInstance();
    agentStartProperties.setServerUrl(serverUrl);
    agentStartProperties.setName(name);
    agentStartProperties.setPort(port);
    agentStartProperties.setToken(token);
    agentStartProperties.setBaseFolder(baseFolder);
    agentStartProperties.setAgentClientType(clientType);
    agentStartProperties.setProtocolPackage(protocolPackage);
    AgentSSLProperties sslProperties = new AgentSSLProperties();
    sslProperties.setEnable(false);
    agentStartProperties.setSsl(sslProperties);
    // 调用私有init方法
    init(agentStartProperties);
  }

  /**
   * 客户端手动启动方法：空参init方法（从默认的两个位置取yml）
   */
  public static void init() throws Exception {
    Config config = ConfigLoader.getInstance().load(new String[]{});
    // 封装启动参数类
    AgentStartProperties agentStartProperties = AgentStartProperties.getInstance();
    agentStartProperties.setServerUrl(config.getServerUrl());
    agentStartProperties.setName(config.getName());
    agentStartProperties.setPort(String.valueOf(config.getPort()));
    agentStartProperties.setToken(config.getToken());
    agentStartProperties.setBaseFolder(config.getBaseFolder());
    agentStartProperties.setAgentClientType(config.getClientType());
    agentStartProperties.setProtocolPackage(config.getProtocolPackage());
    agentStartProperties.setLocalIp(config.getLocalIp());
    agentStartProperties.setRunningModel(config.getRunningModel());
    agentStartProperties.setLocalCache(config.isLocalCache());
    agentStartProperties.setSsl(config.getSsl());
    agentStartProperties.setLoggingFile(config.getLoggingFile());
    agentStartProperties.setCluster(config.getCluster());
    // 调用私有init方法
    init(agentStartProperties);
  }
}
