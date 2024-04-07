package com.elco.eeds.agent.sdk.transfer.service.agent;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.elco.eeds.agent.sdk.core.bean.agent.Agent;
import com.elco.eeds.agent.sdk.core.bean.agent.AgentBaseInfo;
import com.elco.eeds.agent.sdk.core.bean.agent.AgentMqAuthInfo;
import com.elco.eeds.agent.sdk.core.bean.agent.AgentMqInfo;
import com.elco.eeds.agent.sdk.core.bean.agent.AgentMqSecurityInfo;
import com.elco.eeds.agent.sdk.core.bean.agent.BaseConfigEntity;
import com.elco.eeds.agent.sdk.core.bean.agent.ConfigBase;
import com.elco.eeds.agent.sdk.core.common.enums.InvokeServiceEnum;
import com.elco.eeds.agent.sdk.core.exception.EedsHttpRequestException;
import com.elco.eeds.agent.sdk.core.exception.SdkException;
import com.elco.eeds.agent.sdk.core.util.AgentFileExtendUtils;
import com.elco.eeds.agent.sdk.core.util.JsonUtil;
import com.elco.eeds.agent.sdk.core.util.MapUtils;
import com.elco.eeds.agent.sdk.core.util.ReflectUtils;
import com.elco.eeds.agent.sdk.transfer.beans.agent.AgentTokenRequest;
import com.elco.eeds.agent.sdk.transfer.beans.agent.AutoAddAgentsDTO;
import com.elco.eeds.agent.sdk.transfer.beans.things.ThingsSyncRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName AgentRequestInvokeService
 * @Description 客户端调用服务端服务类
 * @Author OuYang
 * @Date 2024/4/1 13:29
 * @Version 1.0
 */
public class AgentRequestInvokeService {

  private static final Logger logger = LoggerFactory.getLogger(AgentRequestInvokeService.class);

  private Agent agent = Agent.getInstance();

  public AgentRequestInvokeService() {
  }


  public Agent register(String host, String port, String clientType) {
    AutoAddAgentsDTO agentRegisterRequest = new AutoAddAgentsDTO(host, port, clientType);
    try {
      Object invoke = invokeMethodService(InvokeServiceEnum.REGISTER, agentRegisterRequest);
      agent = copyFieldToAgent(JSONUtil.toJsonStr(invoke));
      logger.info("rpc register interfaces,result:{}", invoke);
      return agent;
    } catch (EedsHttpRequestException e) {
      throw new EedsHttpRequestException(e.getMessage(), e);
    }
  }

  /**
   * 更新token生效时间
   *
   * @param agentTokenRequest
   */
  public void updateAgentEffectTime(AgentTokenRequest agentTokenRequest) {
    try {
      Object invoke = invokeMethodService(InvokeServiceEnum.UPDATE_TOKEN, agentTokenRequest);
      logger.debug("本地调用token接口返回值为：{}", invoke.toString());
    } catch (Exception e) {
      logger.error("更新客户端TOKEN生效时间接口异常, 形参为：{}",
          JSONUtil.toJsonStr(agentTokenRequest));
      e.printStackTrace();
    }
  }

  public String getThingsSyncInfo(ThingsSyncRequest request) {
    try {
      Object invoke = invokeMethodService(InvokeServiceEnum.THINGS_SYNC, request);
      logger.debug("本地调用数据源同步返回值为：{}", invoke.toString());
      return invoke.toString();
    } catch (Exception e) {
      throw new EedsHttpRequestException(e.getMessage(), e);
    }
  }


  public Object invokeMethodService(InvokeServiceEnum invokeServiceEnum, Object paramObj) {
    String className = invokeServiceEnum.getClassName();
    String methodName = invokeServiceEnum.getMethodName();
    String parameterTypes = invokeServiceEnum.getParameterTypes();
    try {
      Class<?> beanClass = Class.forName(className);
      Class<?> parameterClass = Class.forName(parameterTypes);
      //构造对象
      Object instance = beanClass.newInstance();
      Method method = ReflectUtil.getMethod(beanClass, methodName, parameterClass);
      Object invoke = ReflectUtil.invoke(instance, method, paramObj);
      logger.info("invoke method,result:{}", invoke);
      return invoke;
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
      throw new EedsHttpRequestException("调用本地方法失败", e);
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


}
