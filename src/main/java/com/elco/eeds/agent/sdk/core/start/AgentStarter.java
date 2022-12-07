package com.elco.eeds.agent.sdk.core.start;

import com.elco.eeds.agent.sdk.core.util.read.parameterfile.AgentConfigYamlReader;
import com.elco.eeds.agent.sdk.core.util.read.parameterfile.ResourceLoader;
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

    private static AgentRegisterService registerService = new AgentRegisterService();

    private AgentConfigYamlReader configReader;

    public static void init(AgentStartProperties properties) throws Exception {

    }

    /**
     * 客户端初始化方法
     * @param serverUrl     server地址
     * @param name          客户端名称
     * @param port          客户端端口
     * @param token         客户端token
     * @param baseFolder    存储文件目录
     * @param ymlPath       配置文件全路径
     * @throws Exception
     */
    public static void init(String serverUrl, String name, String port, String token, String baseFolder, String ymlPath) throws Exception {
        // TODO 通过baseFolder，读取配置文件，拿到token，然后调用register方法
        // TODO 为什么调用程序要传token???
        logger.info("开始初始化方法...");
        logger.info("传入参数为：服务器地址：{}，客户端名称：{}，客户端端口：{}，token：{}，文件存储路径：{}，配置文件全路径：{}",
                serverUrl, name, port, token, baseFolder, ymlPath);
        // 从yml配置文件读取配置，赋值给AgentStartProperties
        AgentConfigYamlReader agentConfigYamlReader = new AgentConfigYamlReader(new ResourceLoader());
        AgentStartProperties agentStartProperties = agentConfigYamlReader.parseYaml(ymlPath);
        logger.info("读取配置文件成功：{}", agentStartProperties.toString());
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

    }
    public static void init(String location) throws Exception {


    }



}
