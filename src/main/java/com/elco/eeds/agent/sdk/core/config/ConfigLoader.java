package com.elco.eeds.agent.sdk.core.config;

import cn.hutool.core.io.FileUtil;
import com.elco.eeds.agent.sdk.core.bean.agent.AgentClusterProperties;
import com.elco.eeds.agent.sdk.core.common.constant.ConstantCommon;
import com.elco.eeds.agent.sdk.core.util.JSONUtils;
import com.elco.eeds.agent.sdk.core.util.ObjectsUtils;
import com.elco.eeds.agent.sdk.core.util.YamlUtils;
import com.google.common.base.Optional;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

/**
 * @ClassName ConfigLoader
 * @Description 配置加载类
 * @Author OuYang
 * @Date 2024/4/22 10:34
 * @Version 1.0
 */
public class ConfigLoader {

  private static Logger logger = LoggerFactory.getLogger(ConfigLoader.class);

  private static final ConfigLoader INSTANCE = new ConfigLoader();
  private Config config;

  public ConfigLoader() {
  }

  public static ConfigLoader getInstance() {
    return INSTANCE;
  }

  public static Config getConfig() {
    return INSTANCE.config;
  }

  /**
   * 加载配置方法 优先级高的覆盖优先级低的 运行参数>jvm参数>环境变量>配置文件>配置对象默认值
   *
   * @param args
   * @return
   */
  public Config load(String[] args) {
    //配置对象默认值
    config = new Config();
    //配置文件 application.yaml
    loadFromConfigFile();
    //环境变量  port=2222
//    loadFromEnv();
    //jvm参数  -Dport=1234
//    loadFromJvm();
    //运行参数 --port=1234
//    loadFromRuntimeArgs(args);
    populateCluster();
    return config;
  }

  private void populateCluster() {
    if (ObjectsUtils.isNull(config.getCluster())) {
      AgentClusterProperties clusterProperties = new AgentClusterProperties();
      clusterProperties.setServerUrls(config.getServerUrl());
      clusterProperties.setEnable(false);
      config.setCluster(clusterProperties);
    } else {
      AgentClusterProperties cluster = config.getCluster();
      cluster.setServerUrls(config.getServerUrl());
    }
  }

  private void loadFromConfigFile() {
    loadYaml();
  }

  private void loadYaml() {
    Yaml yaml = new Yaml();
    InputStream inputStream = null;

    Optional<String> jarPathOptional = getPath();
    try {
      inputStream = getInnerConfigYaml();
      loadInputStream(yaml, inputStream);
      if (jarPathOptional.isPresent()) {
        String jarPath = jarPathOptional.get();
        // 构建文件路径，将外部文件名与Jar包所在目录进行拼接
        String externalFilePath = jarPath + File.separator + ConstantCommon.CONFIG_FILE_NAME_YAML;
        if (cn.hutool.core.io.FileUtil.exist(externalFilePath)) {
          inputStream = FileUtil.getInputStream(externalFilePath);
          loadInputStream(yaml, inputStream);
        }
      }
    } catch (IOException e) {
      logger.error("读取本地配置文件错误,e: ", e);
    }
  }

  private void loadInputStream(Yaml yaml, InputStream inputStream) {
    Map<String, Object> yamlParams = yaml.load(inputStream);
    if (yamlParams.containsKey(ConstantCommon.YAML_AGENT_KEY)) {
      Map<String, Object> serverParams = (Map<String, Object>) yamlParams.get(
          ConstantCommon.YAML_AGENT_KEY);
      YamlUtils.propertiesToObject(serverParams, config);
    }
    if (yamlParams.containsKey(ConstantCommon.YAML_SERVER_KEY)) {
      Map<String, Object> serverParams = (Map<String, Object>) yamlParams.get(
          ConstantCommon.YAML_SERVER_KEY);
      YamlUtils.propertiesToObject(serverParams, config);
    }
  }

  private InputStream getInnerConfigYaml() throws IOException {
    URL resource = Thread.currentThread().getContextClassLoader()
        .getResource(ConstantCommon.CONFIG_FILE_NAME_YAML);
    return resource.openStream();
  }


  public Optional<String> getPath() {
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
      return Optional.of(jarCatalog);
    }
    return Optional.absent();
  }

}
