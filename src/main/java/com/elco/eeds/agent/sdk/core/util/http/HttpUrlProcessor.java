package com.elco.eeds.agent.sdk.core.util.http;

import cn.hutool.json.JSONUtil;
import com.elco.eeds.agent.sdk.core.bean.agent.AgentClusterProperties;
import com.elco.eeds.agent.sdk.core.common.constant.http.ConstantHttpApiPath;
import com.elco.eeds.agent.sdk.core.exception.EedsHttpRequestException;
import com.elco.eeds.agent.sdk.core.util.AgentResourceUtils;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName HttpUrlProcessor
 * @Description url处理类
 * @Author OuYang
 * @Date 2024/1/29 13:23
 * @Version 1.0
 */
public class HttpUrlProcessor {

  private static final Logger logger = LoggerFactory.getLogger(HttpUrlProcessor.class);
  private String servicePrefix;

  private String apiPath;

  private AgentClusterProperties clusterProperties;

  private int maxAttempts;

  private static String successUrl;

  public HttpUrlProcessor(String apiPath) {
    this.clusterProperties = AgentResourceUtils.getAgentConfigCluster();;
    this.servicePrefix = getAgentServicePrefix();
    this.apiPath = apiPath;
    this.maxAttempts = clusterProperties.getEnable() ? clusterProperties.getServerUrls().size() : 1;
  }



  public String processRequest(String token, String content) {
    if (Objects.nonNull(successUrl)) {
      try {
        String requestUrl = successUrl + servicePrefix + apiPath;
        String response = HttpClientUtil.post(requestUrl, token,
            content);
        return response;
      } catch (Exception e) {
        //tryProcessRequest will process
      }
    }
    return tryProcessRequest(token, content);
  }

  private String tryProcessRequest(String token, String content) {
    String exceptionMsg = "";
    Exception causeExp = null;
    for (int attempt = 1; attempt <= maxAttempts; attempt++) {
      String serverUlr = clusterProperties.getServerUrls().get(attempt - 1);
      String requestUrl = serverUlr + servicePrefix + apiPath;
      try {
        String response = HttpClientUtil.post(requestUrl, token,
            content);
        if (JSONUtil.isJson(response)) {
          this.setSuccesUrl(serverUlr);
          return response;
        }
      } catch (Exception e) {
        exceptionMsg = e.getMessage();
        causeExp = e;
        if (clusterProperties.getEnable() && attempt < maxAttempts - 1) {
          logger.info("将尝试切换到下一个URL进行重试");
          // 如果是集群环境且还有其他URL可以尝试，则继续循环进行重试
          continue;
        }
      }
    }
    throw new EedsHttpRequestException(exceptionMsg, causeExp);
  }





  public String getServicePrefix() {
    return servicePrefix;
  }

  public String getApiPath() {
    return apiPath;
  }

  public String getSuccesUrl() {
    return successUrl;
  }

  public void setSuccesUrl(String succesUrl) {
    successUrl = succesUrl;
  }

  public AgentClusterProperties getClusterProperties() {
    return clusterProperties;
  }
  private String getAgentServicePrefix() {
    AgentClusterProperties cluster = AgentResourceUtils.getAgentConfigCluster();
    String servicePrefix = ConstantHttpApiPath.STANDALONE_PREFIX;
    if (cluster.getEnable()||AgentResourceUtils.isSlimModle()) {
      servicePrefix = ConstantHttpApiPath.CLUSTER_PREFIX;
    }
    return servicePrefix;
  }
}
