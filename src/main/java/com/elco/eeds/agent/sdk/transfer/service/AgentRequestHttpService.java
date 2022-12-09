package com.elco.eeds.agent.sdk.transfer.service;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.elco.eeds.agent.sdk.common.entity.ResponseResult;
import com.elco.eeds.agent.sdk.common.enums.SysCodeEnum;
import com.elco.eeds.agent.sdk.core.bean.agent.*;
import com.elco.eeds.agent.sdk.core.common.constant.http.ConstantHttpApiPath;
import com.elco.eeds.agent.sdk.core.exception.SdkException;
import com.elco.eeds.agent.sdk.core.util.AgentFileExtendUtils;
import com.elco.eeds.agent.sdk.core.util.JsonUtil;
import com.elco.eeds.agent.sdk.core.util.MapUtils;
import com.elco.eeds.agent.sdk.core.util.ReflectUtils;
import com.elco.eeds.agent.sdk.core.util.http.HttpClientUtil;
import com.elco.eeds.agent.sdk.transfer.beans.http.request.AgentRegisterRequest;
import com.elco.eeds.agent.sdk.transfer.beans.http.request.AgentTokenRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

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
     * @param host
     * @param port
     * @param name
     * @param token
     * @return
     */
    public Agent register(String host, String port, String name, String token, String clientType) {
        // TODO
        AgentRegisterRequest agentRegisterRequest = new AgentRegisterRequest(name, host, port, token, clientType);
        String requestUrl = agent.getAgentBaseInfo().getServerUrl() + ConstantHttpApiPath.AGENT_REGISTER;
        try {
            String response = HttpClientUtil.post(requestUrl, token, JSON.toJSONString(agentRegisterRequest));
            if(!JSONUtil.isJson(response)){
                logger.error("request rpc register error,msg:{}", response);
            }
            ResponseResult responseResult = JSONUtil.toBean(response, ResponseResult.class);
            if(SysCodeEnum.SUCCESS.getCode().equals(responseResult.getCode())){
                // 将server-config反馈的data赋值给Agent对象
                Agent agent = copyFieldToAgent(JSONUtil.toJsonStr(responseResult.getData()));
                logger.info("rpc register interfaces,result:{}", JSON.toJSONString(response));
                return agent;
            }else{
                logger.error("request rpc register error,msg:{}", JSON.toJSONString(response));
                return null;
            }
        } catch (Exception e) {
            logger.error("调用server自动注册接口异常, 形参为：{}", JSON.toJSONString(agentRegisterRequest));
            logger.error("请求地址为：{}", requestUrl);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将调用注册接口之后，将返回值赋值到Agent对象中
     * @param data
     * @return
     */
    public Agent copyFieldToAgent(String data) throws SdkException {
        JSONObject jsonObject = JSON.parseObject(data, JSONObject.class);
        Agent agent = Agent.getInstance();
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

        agent.getAgentBaseInfo().setAgentId((String) jsonObject.get("agentId"));
        agent.getAgentBaseInfo().setName((String) jsonObject.get("name"));
        agent.getAgentBaseInfo().setToken((String) jsonObject.get("token"));
        // AgentMqInfo
        JSONObject agentMqInfoJsonObject = JSON.parseObject(String.valueOf(jsonObject.get("mqConfig")), JSONObject.class);
        agent.getAgentMqInfo().setMqType((String) agentMqInfoJsonObject.get("mqType"));

        JSONArray urls = (JSONArray) agentMqInfoJsonObject.get("urls");
        agent.getAgentMqInfo().setUrls(JsonUtil.jsonArray2StringArray(urls));

        AgentMqAuthInfo agentMqAuthInfo = JSONObject.parseObject(String.valueOf(agentMqInfoJsonObject.get("authInfo")), AgentMqAuthInfo.class);
        agent.getAgentMqInfo().getAuthInfo().setAuthType((String) agentMqInfoJsonObject.get("authType"));
        AgentMqSecurityInfo agentMqSecurityInfo = JSONObject.parseObject(String.valueOf(agentMqInfoJsonObject.get("tlsInfo")), AgentMqSecurityInfo.class);

        agent.getAgentMqInfo().setAuthInfo(agentMqAuthInfo);
        agent.getAgentMqInfo().setMqSecurityInfo(agentMqSecurityInfo);

        // 保存token
        AgentFileExtendUtils.setTokenToLocalAgentFile(agent.getAgentBaseInfo().getToken().toString());

        // 新的配置逻辑，server-config把公有变量和私有变量分开传输
        ConfigBase configGlobal = JSONObject.parseObject(String.valueOf(jsonObject.get("baseConfigCache")), ConfigBase.class);
        ConfigBase configPrivate = JSONObject.parseObject(String.valueOf(jsonObject.get("privateConfigCache")), ConfigBase.class);
        // 反射得到Object的属性名和属性值
        Map mapConfigGlobal = ReflectUtils.reflectObjectToMap(configGlobal);
        Map mapConfigPrivate = ReflectUtils.reflectObjectToMap(configPrivate);
        // 遍历map，组装存入agent.json的config字段
        JSONArray jsonArray = MapUtils.mapToJsonConfig(mapConfigGlobal, mapConfigPrivate);
        // 将返回的两个json处理后存储至agent.json中
        AgentFileExtendUtils.setConfigToLocalAgentFile(jsonArray);

        return agent;
    }

    /**
     * 更新客户端TOKEN生效时间
     * @param agentTokenRequest
     */
    public void updateAgentEffectTime(AgentTokenRequest agentTokenRequest) {
        String requestUrl = agent.getAgentBaseInfo().getServerUrl() + ConstantHttpApiPath.AGENT_TOKEN;
        try {
            HttpClientUtil.post(requestUrl, agentTokenRequest.getCurrentToken(), JSON.toJSONString(agentTokenRequest));
        } catch (Exception e) {
            logger.error("更新客户端TOKEN生效时间接口异常, 形参为：{}", JSON.toJSONString(agentTokenRequest));
            logger.error("请求地址为：{}", requestUrl);
            e.printStackTrace();
        }
    }

    /**
     * 启动请求数据源同步
     * @param request
     * @param token
     * @return
     */
    /*public String getSetupSyncThingsData(SyncThingsRequest request,String token) {
        // TODO
        return null;
    }*/

    /**
     * 请求数据源同步
     */
    /*public String getSyncThingsData(SyncThingsRequest request,String token) {
        // TODO
        return null;
    }*/

    public static void main(String[] args) throws SdkException {
        String baseConfigCache = "{\"baseConfigCache\": \"{\\\"type\\\":\\\"cache_config\\\",\\\"dataCacheFileSize\\\":\\\"1\\\",\\\"dataCacheCycle\\\":\\\"7\\\",\\\"syncPeriod\\\":\\\"10000\\\"}\"}";
        // String baseConfigCache = "{\"baseConfigCache\": \"{\\\"dataCacheFileSize\\\":\\\"1\\\",\\\"dataCacheCycle\\\":\\\"7\\\",\\\"syncPeriod\\\":\\\"10000\\\"}\"}";
        JSONObject jsonObjectBaseConfigCache = JSONObject.parseObject(baseConfigCache);
        ConfigBase configGlobal = JSONObject.parseObject(jsonObjectBaseConfigCache.get("baseConfigCache").toString(), ConfigBase.class);

//        String privateConfigCache = "{\"privateConfigCache\": \"{\\\"type\\\":null,\\\"dataCacheFileSize\\\":null,\\\"dataCacheCycle\\\":null,\\\"syncPeriod\\\":\\\"10000\\\"}\"}";
        String privateConfigCache = "{\"privateConfigCache\": \"{\\\"syncPeriod\\\":\\\"20000\\\"}\"}";

        JSONObject jsonObjectPrivateConfigCache = JSONObject.parseObject(privateConfigCache);
        ConfigBase configPrivate = JSONObject.parseObject(jsonObjectPrivateConfigCache.get("privateConfigCache").toString(), ConfigBase.class);

        Map mapConfigPrivate = ReflectUtils.reflectObjectToMap(configPrivate);
        Map mapConfigGlobal = ReflectUtils.reflectObjectToMap(configGlobal);
        System.out.println(mapConfigGlobal);
        System.out.println(mapConfigPrivate);

        // 组装
        JSONObject jsonConfig = new JSONObject();
        jsonConfig.put("config", MapUtils.mapToJsonConfig(mapConfigGlobal, mapConfigPrivate));
        System.out.println(jsonConfig);
        AgentFileExtendUtils.setConfigToLocalAgentFile(MapUtils.mapToJsonConfig(mapConfigGlobal, mapConfigPrivate));

    }

}
