package com.elco.eeds.agent.sdk.core.start;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ReflectUtil;
import com.elco.eeds.agent.sdk.core.bean.agent.Agent;
import com.elco.eeds.agent.sdk.core.bean.agent.AgentBaseInfo;
import com.elco.eeds.agent.sdk.core.common.constant.ConstantFilePath;
import com.elco.eeds.agent.sdk.core.common.enums.AgentStatus;
import com.elco.eeds.agent.sdk.core.common.enums.ErrorEnum;
import com.elco.eeds.agent.sdk.core.connect.ThingsConnection;
import com.elco.eeds.agent.sdk.core.connect.ThingsConnectionHandler;
import com.elco.eeds.agent.sdk.core.connect.init.InitConnectFactory;
import com.elco.eeds.agent.sdk.core.exception.SdkException;
import com.elco.eeds.agent.sdk.core.util.read.parameterfile.AgentConfigYamlReader;
import com.elco.eeds.agent.sdk.core.util.read.parameterfile.ResourceLoader;
import com.elco.eeds.agent.sdk.transfer.handler.things.ThingsSyncIncrMessageHandler;
import com.elco.eeds.agent.sdk.transfer.quartz.CountScheduler;
import com.elco.eeds.agent.sdk.transfer.service.data.count.DataCountServiceImpl;
import com.elco.eeds.agent.sdk.transfer.service.things.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * @title: AgentStarter
 * @Author wl
 * @Date: 2022/12/6 11:18
 * @Version 1.0
 * @Description: 客户端启动类
 */
public class AgentStarter {

  private static Logger logger = LoggerFactory.getLogger(AgentStarter.class);

  private static ThingsServiceImpl thingsService = new ThingsServiceImpl();
  private static ThingsPropertiesService thingsPropertiesService = new ThingsPropertiesService();
  private static ThingsSyncService thingsSyncService = new ThingsSyncServiceImpl(thingsService);

  private static ThingsSyncNewServiceImpl thingsSyncNewService = new ThingsSyncNewServiceImpl(thingsPropertiesService);

  private static ThingsSyncIncrMessageHandler thingsSyncIncrMessageHandler = new ThingsSyncIncrMessageHandler(
          thingsSyncNewService);

  private static AgentRegisterService registerService = new AgentRegisterService(thingsSyncNewService,
      thingsSyncIncrMessageHandler);

  private static CountScheduler countScheduler = new CountScheduler();
  private AgentConfigYamlReader configReader;

  private static void init(AgentStartProperties agentStartProperties) throws Exception {
    // 实例化Agent对象
    Agent agent = Agent.getInstance();
    try {
      // 存储解析对象实例
      ThingsConnection connection = ReflectUtil.newInstance(agentStartProperties.getProtocolPackage());
      ThingsConnectionHandler handler = (ThingsConnectionHandler) connection;
      agent.setDataParsing(handler.getParsing());

      // 将协议注入
      InitConnectFactory.addConnectPackagePath(agentStartProperties.getProtocolPackage());
      InitConnectFactory.initConnect();

      agent.setAgentBaseInfo(new AgentBaseInfo(agentStartProperties));
      // 注册
      registerService.register(agentStartProperties.getServerUrl(), agentStartProperties.getName(),
          agentStartProperties.getPort(), agentStartProperties.getToken(),
          agentStartProperties.getAgentClientType());
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
    // 调用私有init方法
    init(agentStartProperties);
  }

  /**
   * 客户端手动启动方法：一个参数（从给定的路径取yml文件）
   *
   * @param ymlPath yml的绝对路径
   */
  public static void init(String ymlPath) throws Exception {
    logger.debug("开始手动初始化方法...");
    logger.debug("yml文件全路径参数为：{}", ymlPath);
    // 从yml配置文件读取配置，赋值给AgentStartProperties
    AgentStartProperties agentStartProperties = AgentStartProperties.getInstance();
    AgentConfigYamlReader agentConfigYamlReader = new AgentConfigYamlReader(new ResourceLoader());
    agentStartProperties = agentConfigYamlReader.parseYaml(ymlPath, true);
    logger.info("读取配置文件成功：{}", agentStartProperties.toString());
    // 调用私有init方法
    init(agentStartProperties);
  }

  /**
   * 客户端手动启动方法：空参init方法（从默认的两个位置取yml）
   */
  public static void init() throws Exception {
    logger.debug("开始手动初始化方法...");
    logger.debug("开始从默认的位置读取yml文件...");
    // 从yml配置文件读取配置，赋值给AgentStartProperties
    AgentConfigYamlReader agentConfigYamlReader = new AgentConfigYamlReader(new ResourceLoader());
    AgentStartProperties agentStartProperties = AgentStartProperties.getInstance();

    String fileName = getJarSamePathYml();
    if (FileUtil.exist(new File(fileName))) {
      // 默认在jar包相同路径下读取agent-sdk-config.yaml
      agentStartProperties = agentConfigYamlReader.parseYaml(fileName, true);
      logger.debug("jar包同级路径配置文件读取成功");
      logger.info("读取配置文件成功：{}", agentStartProperties.toString());
    } else {
      // jar包中resource文件下的agent-sdk-config.yaml
      logger.debug("jar包同级路径配置文件不存在，即将开始读取jar包中resource文件下的配置文件");
      agentStartProperties = agentConfigYamlReader
          .parseYaml("/" + ConstantFilePath.YML_NAME, false);
      if (agentStartProperties == null) {
        logger.debug("读取配置文件失败");
        throw new SdkException(ErrorEnum.READ_CONFIG_FILE_ERROR.code());
      }
      logger.debug("jar包中resource文件下的配置文件读取成功");
      logger.info("读取配置文件成功：{}", agentStartProperties.toString());
    }
    // 调用私有init方法
    init(agentStartProperties);
  }

  /**
   * 获取jar包同路径下的规定名称的配置文件
   *
   * @return
   */
  private static String getJarSamePathYml() {
    //获取当前目录
    String property = System.getProperty("user.dir");
    //默认是linux os
    String fileName = "/" + ConstantFilePath.YML_NAME;
    //判断是否是windows os
    if (System.getProperty("os.name").contains("Windows")) {
      fileName = "\\" + ConstantFilePath.YML_NAME;
    }
    // 读取当前目录下conf配置文件
    return property + fileName;
  }
}
