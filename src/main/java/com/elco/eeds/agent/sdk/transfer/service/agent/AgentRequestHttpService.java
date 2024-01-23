package com.elco.eeds.agent.sdk.transfer.service.agent;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.elco.eeds.agent.sdk.common.entity.ResponseResult;
import com.elco.eeds.agent.sdk.common.enums.SysCodeEnum;
import com.elco.eeds.agent.sdk.core.bean.agent.Agent;
import com.elco.eeds.agent.sdk.core.bean.agent.AgentBaseInfo;
import com.elco.eeds.agent.sdk.core.bean.agent.AgentClusterProperties;
import com.elco.eeds.agent.sdk.core.bean.agent.AgentMqAuthInfo;
import com.elco.eeds.agent.sdk.core.bean.agent.AgentMqInfo;
import com.elco.eeds.agent.sdk.core.bean.agent.AgentMqSecurityInfo;
import com.elco.eeds.agent.sdk.core.bean.agent.BaseConfigEntity;
import com.elco.eeds.agent.sdk.core.bean.agent.ConfigBase;
import com.elco.eeds.agent.sdk.core.common.constant.http.ConstantHttpApiPath;
import com.elco.eeds.agent.sdk.core.exception.EedsHttpRequestException;
import com.elco.eeds.agent.sdk.core.exception.SdkException;
import com.elco.eeds.agent.sdk.core.util.AgentFileExtendUtils;
import com.elco.eeds.agent.sdk.core.util.AgentResourceUtils;
import com.elco.eeds.agent.sdk.core.util.JsonUtil;
import com.elco.eeds.agent.sdk.core.util.MapUtils;
import com.elco.eeds.agent.sdk.core.util.ReflectUtils;
import com.elco.eeds.agent.sdk.core.util.http.HttpClientUtil;
import com.elco.eeds.agent.sdk.transfer.beans.agent.AgentRegisterRequest;
import com.elco.eeds.agent.sdk.transfer.beans.agent.AgentTokenRequest;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @title: AgentRequestHttpService
 * @Author wl
 * @Date: 2022/12/6 11:35
 * @Version 1.0
 * @Description: 客户端请求服务端服务类
 */
public class AgentRequestHttpService {

  private static final Logger logger = LoggerFactory.getLogger(AgentRequestHttpService.class);

  private Agent agent = Agent.getInstance();

  public AgentRequestHttpService() {
  }

  /**
   * （注册方法）
   *
   * @param host  主机IP
   * @param port  主机端口
   * @param name  客户端名称
   * @param token token
   * @return 返回agent对象
   */
  public Agent register(String host, String port, String name, String token, String clientType) {
    AgentRegisterRequest agentRegisterRequest = new AgentRegisterRequest(name, host, port, token,
        clientType);
    AgentClusterProperties cluster = AgentResourceUtils.getAgentConfigCluster();
    String servicePrefix = ConstantHttpApiPath.STANDALONE_PREFIX;
    if (cluster.getEnable()) {
      servicePrefix = ConstantHttpApiPath.CLUSTER_PREFIX;
    }
    String requestUrl = agent.getAgentBaseInfo().getServerUrl() + servicePrefix
        + ConstantHttpApiPath.AGENT_REGISTER;
    try {
      String response = HttpClientUtil.post(requestUrl, token,
          JSONUtil.toJsonStr(agentRegisterRequest));
      if (!JSONUtil.isJson(response)) {
        logger.error("request rpc register error,msg:{}", response);
      }
      ResponseResult responseResult = JSONUtil.toBean(response, ResponseResult.class);
      if (SysCodeEnum.SUCCESS.getCode().equals(responseResult.getCode())) {
        // 将server-config反馈的data赋值给Agent对象
        agent = copyFieldToAgent(JSONUtil.toJsonStr(responseResult.getData()));
        logger.info("rpc register interfaces,result:{}", response);
        return agent;
      } else {
        String message = "{\"code\":\\"+responseResult.getCode()+",\"msg\":\\"+responseResult.getMsg()+",\"data\":null}";
        throw new EedsHttpRequestException(message);
      }
    } catch (Exception e) {
      logger.error("调用server自动注册接口异常, 请求地址为：{}，形参为：{}", requestUrl,
          agentRegisterRequest);

      throw new EedsHttpRequestException(e.getMessage(),e);
    }
  }

  private void initializeAgent() {
    this.agent = Agent.getInstance();
    if (agent.getAgentMqInfo() == null) {
      AgentMqInfo agentMqInfo = new AgentMqInfo();
      agent.setAgentMqInfo(agentMqInfo);
    }
    if (agent.getAgentBaseInfo() == null) {
      AgentBaseInfo agentBaseInfo = new AgentBaseInfo();
      agent.setAgentBaseInfo(agentBaseInfo);
    }
    if (agent.getAgentMqInfo().getAuthInfo() == null) {
      AgentMqAuthInfo agentMqAuthInfo = new AgentMqAuthInfo();
      agent.getAgentMqInfo().setAuthInfo(agentMqAuthInfo);
    }
    if (agent.getAgentMqInfo().getMqSecurityInfo() == null) {
      AgentMqSecurityInfo agentMqSecurityInfo = new AgentMqSecurityInfo();
      agent.getAgentMqInfo().setMqSecurityInfo(agentMqSecurityInfo);
    }
  }

  /**
   * 将调用注册接口之后，将返回值赋值到Agent对象中
   *
   * @param data
   * @return
   */
  public Agent copyFieldToAgent(String data) throws SdkException {
    initializeAgent();
    this.agent = Agent.getInstance();
    AgentBaseInfo agentBaseInfo = this.agent.getAgentBaseInfo();
    AgentMqInfo agentMqInfo = this.agent.getAgentMqInfo();
    AgentMqAuthInfo agentMqAuthInfo;
    AgentMqSecurityInfo agentMqSecurityInfo;
    ConfigBase configGlobal = null;
    ConfigBase configPrivate = null;
    Map<String, Object> map = (Map<String, Object>) JSON.parse(data);

    for (Map.Entry<String, Object> entry : map.entrySet()) {
      // 反射赋值
      if (ReflectUtils.isContainKey(agentBaseInfo, entry.getKey())) {
        ReflectUtils.invokeSet(agentBaseInfo, entry.getKey(), entry.getValue());
      } else if ("mqConfig".equals(entry.getKey())) {
        Map<String, Object> mapMqConfig = JSON.parseObject(entry.getValue().toString(), Map.class);
        for (Map.Entry<String, Object> entryMqConfig : mapMqConfig.entrySet()) {
          if (ReflectUtils.isContainKey(agentMqInfo, entryMqConfig.getKey()) && !"authInfo".equals(
              entryMqConfig.getKey())) {
            if ("urls".equals(entryMqConfig.getKey())) {
              ReflectUtils.invokeSet(agentMqInfo, entryMqConfig.getKey(),
                  JsonUtil.jsonArray2StringArray((JSONArray) entryMqConfig.getValue()));
            } else {
              ReflectUtils.invokeSet(agentMqInfo, entryMqConfig.getKey(), entryMqConfig.getValue());
            }
          } else if ("authInfo".equals(entryMqConfig.getKey())) {
            agentMqAuthInfo = JSON.parseObject(String.valueOf(entryMqConfig.getValue()),
                AgentMqAuthInfo.class);
            this.agent.getAgentMqInfo().setAuthInfo(agentMqAuthInfo);
          } else if ("tlsInfo".equals(entryMqConfig.getKey())) {
            agentMqSecurityInfo = JSON.parseObject(String.valueOf(entryMqConfig.getValue()),
                AgentMqSecurityInfo.class);
            this.agent.getAgentMqInfo().setMqSecurityInfo(agentMqSecurityInfo);
          }
        }
      } else if ("baseConfigCache".equals(entry.getKey())) {
        configGlobal = JSON.parseObject(String.valueOf(entry.getValue()), ConfigBase.class);
      } else if ("privateConfigCache".equals(entry.getKey())) {
        configPrivate = JSON.parseObject(String.valueOf(entry.getValue()), ConfigBase.class);
      }
    }

    // 保存token
    AgentFileExtendUtils.setTokenToLocalAgentFile(this.agent.getAgentBaseInfo().getToken());

    // 反射得到Object的属性名和属性值
    assert configGlobal != null;
    Map<String, String> mapConfigGlobal = ReflectUtils.reflectObjectToMap(configGlobal);
    assert configPrivate != null;
    Map<String, String> mapConfigPrivate = ReflectUtils.reflectObjectToMap(configPrivate);
    // 遍历map，组装存入agent.json的config字段
    JSONArray jsonArray = MapUtils.mapToJsonConfig(mapConfigGlobal, mapConfigPrivate);
    // 将返回的两个json处理后存储至agent.json中
    AgentFileExtendUtils.setConfigToLocalAgentFile(jsonArray);
    // 反射给agent赋值
    for (Object o : jsonArray) {
      BaseConfigEntity baseConfigEntity = (BaseConfigEntity) o;
      if (ReflectUtils.isContainKey(this.agent.getAgentBaseInfo(),
          baseConfigEntity.getConfigFieldName())) {
        ReflectUtils.invokeSet(this.agent.getAgentBaseInfo(), baseConfigEntity.getConfigFieldName(),
            baseConfigEntity.getConfigFieldValue());
      }
    }

    return this.agent;
  }

  /**
   * 更新客户端TOKEN生效时间
   *
   * @param agentTokenRequest 客户端请求对象
   */
  public void updateAgentEffectTime(AgentTokenRequest agentTokenRequest) {
    AgentClusterProperties cluster = AgentResourceUtils.getAgentConfigCluster();
    String servicePrefix = ConstantHttpApiPath.STANDALONE_PREFIX;
    if (cluster.getEnable()) {
      servicePrefix = ConstantHttpApiPath.CLUSTER_PREFIX;
    }
    String requestUrl = this.agent.getAgentBaseInfo().getServerUrl() + servicePrefix
        + ConstantHttpApiPath.AGENT_TOKEN;
    try {
      String response = HttpClientUtil.post(requestUrl, agentTokenRequest.getCurrentToken(),
          JSONUtil.toJsonStr(agentTokenRequest));
      logger.debug("调用token接口返回值为：{}", response);
    } catch (Exception e) {
      logger.error("更新客户端TOKEN生效时间接口异常, 形参为：{}",
          JSONUtil.toJsonStr(agentTokenRequest));
      logger.error("请求地址为：{}", requestUrl);
      e.printStackTrace();
    }
  }

}
