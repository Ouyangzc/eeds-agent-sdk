package com.elco.eeds.agent.sdk.core.bean.agent;

import com.elco.eeds.agent.sdk.core.common.constant.ConstantCommon;
import com.elco.eeds.agent.sdk.core.common.enums.RegexEnum;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName AgentClusterProperties
 * @Description 集群配置参数
 * @Author OuYang
 * @Date 2023/11/16 9:43
 * @Version 1.0
 */
public class AgentClusterProperties implements Serializable {

  private static final Logger logger = LoggerFactory.getLogger(AgentClusterProperties.class);
  private Boolean enable;

  private String nodeName;

  private List<String> serverUrls;

  public AgentClusterProperties() {
    this.enable = false;
  }

  public Boolean getEnable() {
    return enable;
  }

  public void setEnable(Boolean enable) {
    this.enable = enable;
  }

  public String getNodeName() {
    return nodeName;
  }

  public void setNodeName(String nodeName) {
    this.nodeName = nodeName;
  }

  public List<String> getServerUrls() {
    return serverUrls;
  }

  public void setServerUrls(String strUrls) {
    String[] serverUrls = strUrls.split(ConstantCommon.SYMBOL_COMMAS);
    Pattern pattern = Pattern.compile(RegexEnum.HTTP_HTTPS.getRegexStr());
    List<String> urls = Arrays.stream(serverUrls)
        .map(s -> {
          if (pattern.matcher(s).matches()) {
            return s;
          }
          logger.warn("Invalid Http Ulr: " + s);
          return null;
        })
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
    this.serverUrls = urls;
  }
}
