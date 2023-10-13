package com.elco.eeds.agent.sdk.core.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONUtil;
import com.elco.eeds.agent.sdk.core.common.constant.ConstantCommon;
import com.elco.eeds.agent.sdk.core.start.AgentStartProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

/**
 * @ClassName AgentResourceUtils
 * @Description 客户端配置工具类
 * @Author OuYang
 * @Date 2023/10/11 9:08
 * @Version 1.0
 */
public class AgentResourceUtils {

    private static final Logger logger = LoggerFactory.getLogger(AgentResourceUtils.class);

    private AgentStartProperties agentStartProperties;


    private AgentResourceUtils() {
        agentStartProperties = loadYaml();
    }

    public static String getAgentConfigLocalIp() {
        AgentResourceUtils agentResourceUtils = new AgentResourceUtils();
        return agentResourceUtils.agentStartProperties.getLocalIp();
    }

    public static boolean getAgentConfigLocalCache() {
        AgentResourceUtils agentResourceUtils = new AgentResourceUtils();
        return agentResourceUtils.agentStartProperties.isLocalCache();
    }

    public AgentStartProperties loadYaml() {
        Yaml yaml = new Yaml();
        String jarPath = getPath();
        InputStream inputStream = null;
        try {
            if (null != jarPath) {
                // 构建文件路径，将外部文件名与Jar包所在目录进行拼接
                String externalFilePath = jarPath + File.separator + ConstantCommon.CONFIG_FILE_NAME_YAML;
                if (cn.hutool.core.io.FileUtil.exist(externalFilePath)) {
                    inputStream = FileUtil.getInputStream(externalFilePath);
                } else {
                    URL resource = Thread.currentThread().getContextClassLoader().getResource(ConstantCommon.CONFIG_FILE_NAME_YAML);
                    inputStream = resource.openStream();
                }
            } else {
                URL resource = Thread.currentThread().getContextClassLoader().getResource(ConstantCommon.CONFIG_FILE_NAME_YAML);
                inputStream = resource.openStream();
            }
        } catch (IOException e) {
            logger.error("读取本地配置文件错误,e: ", e);
            return new AgentStartProperties();
        }
        Map<String, Object> yamlParams = yaml.load(inputStream);
        Object serverParams = yamlParams.get(ConstantCommon.YAML_AGENT_KEY);
        AgentStartProperties properties = JSONUtil.toBean(JSONUtil.toJsonStr(serverParams), AgentStartProperties.class);
        return properties;
    }

    public String getPath() {
        String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        if (path.contains(ConstantCommon.SYMBOL_EXCLAMATION_POINT)) {
            //jar包
            path = path.split(ConstantCommon.SYMBOL_EXCLAMATION_POINT)[0];
        }
        if (System.getProperty(ConstantCommon.SYSTEM_OS_NAME_KEY).contains(ConstantCommon.WINDOWS_OS_KEY)) {
            path = path.substring(6);
        }
        if (path.contains(ConstantCommon.FILE_FORMAT_JAR)) {
            File jarFile = new File(path);
            String jarCatalog = jarFile.getParentFile().getPath();
            return jarCatalog;
        }
        return null;
    }
}
