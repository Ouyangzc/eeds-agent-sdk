package com.elco.eeds.agent.sdk.core.start;

import com.elco.eeds.agent.sdk.core.bean.agent.Agent;
import com.elco.eeds.agent.sdk.core.bean.agent.AgentBaseInfo;
import com.elco.eeds.agent.sdk.core.util.read.parameterfile.AgentConfigYamlReader;
import com.elco.eeds.agent.sdk.core.util.read.parameterfile.ResourceLoader;
import com.elco.eeds.agent.sdk.transfer.handler.things.ThingsSyncIncrMessageHandler;
import com.elco.eeds.agent.sdk.transfer.service.things.ThingsService;
import com.elco.eeds.agent.sdk.transfer.service.things.ThingsServiceImpl;
import com.elco.eeds.agent.sdk.transfer.service.things.ThingsSyncService;
import com.elco.eeds.agent.sdk.transfer.service.things.ThingsSyncServiceImpl;
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

    private static ThingsServiceImpl thingsService = new ThingsServiceImpl();
    private static ThingsSyncService thingsSyncService = new ThingsSyncServiceImpl(thingsService);

    private static ThingsSyncIncrMessageHandler thingsSyncIncrMessageHandler = new ThingsSyncIncrMessageHandler(thingsSyncService);

    private static AgentRegisterService registerService = new AgentRegisterService(thingsSyncService,thingsSyncIncrMessageHandler);

    private AgentConfigYamlReader configReader;

    private static void init(AgentStartProperties agentStartProperties) throws Exception {
        try {
            Agent.getInstance().setAgentBaseInfo(new AgentBaseInfo(agentStartProperties));
            // 注册
            registerService.register(agentStartProperties.getServerUrl(), agentStartProperties.getName(),
                    agentStartProperties.getPort(), agentStartProperties.getToken());
            // 加载数据文件
            // TODO 加载数据文件
            // 根据协议加载数据源信息
            // TODO 根据协议加载数据源信息
            // 加载统计
            // TODO 加载统计
            // 统计定时任务
            // TODO 统计定时任务
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("客户端注册异常", e);
        }

    }

    /**
     * 客户端手动启动方法
     * @param serverUrl     server地址
     * @param name          客户端名称
     * @param port          客户端端口
     * @param token         客户端token
     * @param baseFolder    存储文件目录
     * @throws Exception
     */
    public static void init(String serverUrl, String name, String port, String token, String baseFolder) throws Exception {
        // TODO 通过baseFolder，读取配置文件，拿到token，然后调用register方法
        logger.info("开始手动初始化方法...");
        logger.info("传入参数为：服务器地址：{}，客户端名称：{}，客户端端口：{}，token：{}，文件存储路径：{}",
                serverUrl, name, port, token, baseFolder);
        // 封装启动参数类
        AgentStartProperties agentStartProperties = new AgentStartProperties();
        agentStartProperties.setServerUrl(serverUrl);
        agentStartProperties.setName(name);
        agentStartProperties.setPort(port);
        agentStartProperties.setToken(token);
        agentStartProperties.setBaseFolder(baseFolder);
        // 调用私有init方法
        init(agentStartProperties);
    }

    /**
     * 客户端手动启动方法：一个参数（从给定的路径取yml文件）
     * @param ymlPath
     */
    public static void init(String ymlPath) throws Exception {
        logger.info("开始手动初始化方法...");
        logger.info("yml文件路径参数为：{}", ymlPath);
        // 从yml配置文件读取配置，赋值给AgentStartProperties
        AgentConfigYamlReader agentConfigYamlReader = new AgentConfigYamlReader(new ResourceLoader());
        AgentStartProperties agentStartProperties = agentConfigYamlReader.parseYaml(ymlPath);
        logger.info("读取配置文件成功：{}", agentStartProperties.toString());
        // 调用私有init方法
        init(agentStartProperties);
    }

    /**
     * 客户端手动启动方法：空参init方法（从默认的两个位置取yml）
     */
    public static void init() throws Exception {
        logger.info("开始手动初始化方法...");
        logger.info("开始从默认的位置读取yml文件...");
        // 从yml配置文件读取配置，赋值给AgentStartProperties
        AgentConfigYamlReader agentConfigYamlReader = new AgentConfigYamlReader(new ResourceLoader());
        AgentStartProperties agentStartProperties = agentConfigYamlReader.parseYaml("application.yaml");
        logger.info("读取配置文件成功：{}", agentStartProperties.toString());
        // 调用私有init方法
        init(agentStartProperties);


    }
}
