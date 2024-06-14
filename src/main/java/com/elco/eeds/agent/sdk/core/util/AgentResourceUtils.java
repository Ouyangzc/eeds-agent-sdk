package com.elco.eeds.agent.sdk.core.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.elco.eeds.agent.sdk.core.bean.agent.AgentClusterProperties;
import com.elco.eeds.agent.sdk.core.bean.agent.AgentLoggingFileProperties;
import com.elco.eeds.agent.sdk.core.common.constant.ConstantCommon;
import com.elco.eeds.agent.sdk.core.common.enums.AgentRunningModelEnum;
import com.elco.eeds.agent.sdk.core.config.Config;
import com.elco.eeds.agent.sdk.core.config.ConfigLoader;
import com.elco.eeds.agent.sdk.core.start.AgentStartProperties;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

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
  private static AgentResourceUtils instance;

  private AgentResourceUtils() {
    agentStartProperties = loadYaml();
  }

  public static AgentResourceUtils getInstance() {
    if (instance == null) {
      synchronized (AgentResourceUtils.class) {
        if (instance == null) {
          instance = new AgentResourceUtils();
        }
      }
    }
    return instance;
  }

  public static String getAgentConfigLocalIp() {
    AgentResourceUtils instance = getInstance();
    return instance.getAgentStartProperties().getLocalIp();
  }

  public static boolean getAgentConfigLocalCache() {
    AgentResourceUtils instance = getInstance();
    return instance.getAgentStartProperties().isLocalCache();
  }

  public static AgentLoggingFileProperties getLoggingFileSize() {
    AgentResourceUtils instance = getInstance();
    return instance.getAgentStartProperties().getLoggingFile();
  }

  public static AgentClusterProperties getAgentConfigCluster() {
    Config config = ConfigLoader.getConfig();
    AgentClusterProperties cluster = config.getCluster();
    if (ObjectUtil.isEmpty(cluster)) {
      AgentClusterProperties clusterProperties = new AgentClusterProperties();
      clusterProperties.setServerUrls(config.getServerUrl());
      clusterProperties.setEnable(false);
      return clusterProperties;
    } else {
      cluster.setServerUrls(config.getServerUrl());
      return cluster;
    }
  }

  /**
   * 是否为SLIM版本Model
   *
   * @return
   */
  public static boolean isSlimModle() {
    return getAgentRunningModle().equals(AgentRunningModelEnum.SLIM.getRunningModel());
  }

  public static String getAgentRunningModle() {
    AgentResourceUtils instance = getInstance();
    String runningModel = instance.getAgentStartProperties().getRunningModel();
    return StrUtil.isEmpty(runningModel) ? AgentRunningModelEnum.SINGLE.getRunningModel()
        : runningModel.toUpperCase();
  }

  public AgentStartProperties loadYaml() {
    Yaml yaml = new Yaml();
    InputStream inputStream = null;
    AgentStartProperties properties = new AgentStartProperties();

    String jarPath = getPath();
    try {
      inputStream = getInnerConfigYaml();
      loadInputStream(yaml, inputStream, properties);
      if (null != jarPath) {
        // 构建文件路径，将外部文件名与Jar包所在目录进行拼接
        String externalFilePath = jarPath + File.separator + ConstantCommon.CONFIG_FILE_NAME_YAML;
        if (cn.hutool.core.io.FileUtil.exist(externalFilePath)) {
          inputStream = FileUtil.getInputStream(externalFilePath);
          loadInputStream(yaml, inputStream, properties);
        }
      }
    } catch (IOException e) {
      logger.error("读取本地配置文件错误,e: ", e);
      return new AgentStartProperties();
    }
    return properties;
  }


  private void loadInputStream(Yaml yaml, InputStream inputStream,
      AgentStartProperties properties) {
    Map<String, Object> yamlParams = yaml.load(inputStream);
    if (yamlParams.containsKey(ConstantCommon.YAML_AGENT_KEY)) {
      Map<String, Object> serverParams = (Map<String, Object>) yamlParams.get(
          ConstantCommon.YAML_AGENT_KEY);
      YamlUtils.propertiesToObject(serverParams, properties);
    }
    if (yamlParams.containsKey(ConstantCommon.YAML_SERVER_KEY)) {
      Map<String, Object> serverParams = (Map<String, Object>) yamlParams.get(
          ConstantCommon.YAML_SERVER_KEY);
      YamlUtils.propertiesToObject(serverParams, properties);
    }
  }

  private InputStream getInnerConfigYaml() throws IOException {
    URL resource = Thread.currentThread().getContextClassLoader()
        .getResource(ConstantCommon.CONFIG_FILE_NAME_YAML);
    return resource.openStream();
  }

  public String getPath() {
    String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
    if (path.contains(ConstantCommon.SYMBOL_EXCLAMATION_POINT)) {
      //jar包
      path = path.split(ConstantCommon.SYMBOL_EXCLAMATION_POINT)[0];
    }
    if (System.getProperty(ConstantCommon.SYSTEM_OS_NAME_KEY)
        .contains(ConstantCommon.WINDOWS_OS_KEY)) {
      path = path.substring(6);
    }
    if (path.contains(ConstantCommon.FILE_FORMAT_JAR)) {
      File jarFile = new File(path);
      String jarCatalog = jarFile.getParentFile().getPath();
      return jarCatalog;
    }
    return null;
  }

  public AgentStartProperties getAgentStartProperties() {
    return agentStartProperties;
  }
}
