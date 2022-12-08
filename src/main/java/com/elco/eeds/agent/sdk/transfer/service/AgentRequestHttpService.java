package com.elco.eeds.agent.sdk.transfer.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.elco.eeds.agent.sdk.core.bean.agent.*;
import com.elco.eeds.agent.sdk.core.common.constant.http.ConstantHttpApiPath;
import com.elco.eeds.agent.sdk.core.util.JsonUtil;
import com.elco.eeds.agent.sdk.core.util.http.HttpClientUtil;
import com.elco.eeds.agent.sdk.transfer.beans.http.request.AgentRegisterRequest;
import com.elco.eeds.agent.sdk.transfer.beans.http.request.AgentTokenRequest;
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
     * @param url
     * @param port
     * @param name
     * @param token
     * @return
     */
    public Agent register(String url, String port, String name, String token, String clientType) {
        // TODO
        AgentRegisterRequest agentRegisterRequest = new AgentRegisterRequest(name, url, port, token, clientType);
        String requestUrl = agent.getAgentBaseInfo().getServerUrl() + ConstantHttpApiPath.AGENT_REGISTER;
        try {
            String response = HttpClientUtil.sentHttpPostRequest(url, token, JSON.toJSONString(agentRegisterRequest));
            JSONObject result = JSON.parseObject(response, JSONObject.class);
            if ("000000".equals(result.get("code"))) {
                String data = result.get("data").toString();
                Agent agent = copyFieldToAgent(data);
                logger.info("rpc register interfaces,result:{}", JSON.toJSONString(agent));
                return agent;
            } else {
                logger.error("request rpc register error,msg:{}", JSON.toJSONString(result));
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
    public Agent copyFieldToAgent(String data) {
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

        // 全局配置，全量更新


        return agent;
    }

    /**
     * 更新客户端TOKEN生效时间
     * @param agentTokenRequest
     */
    public void updateAgentEffectTime(AgentTokenRequest agentTokenRequest) {
        String requestUrl = agent.getAgentBaseInfo().getServerUrl() + ConstantHttpApiPath.AGENT_TOKEN;
        try {
            HttpClientUtil.sentHttpPostRequest(requestUrl, agentTokenRequest.getCurrentToken(), JSON.toJSONString(agentTokenRequest));
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
    public static void main(String[] args) {
        String body = "{\"agentId\":\"1596699157005991936\",\"host\":\"192.168.0.109\",\"name\":\"王林测试0.109\",\"port\":\"8888\",\"token\":\"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJuYmYiOjE2Njc1MzQxMDcsImV4cCI6NDgyMzIwNzcwNywiaWF0IjoxNjY3NTM0MTA3LCJqdGkiOiIzNzE3OWE2My1hNjg0LTQwYTUtYmFiMC1lY2U3NDYxNzdjNjQifQ.Pw9nB3XkY1KeB20M65XFcjtUFf9crt1D7CBh37dayOs\",\"mqConfig\":{\"mqType\":\"nats\",\"urls\":[\"nats://192.168.60.62:4222\"],\"authType\":\"UserNamePassWord\",\"authInfo\":{\"token\":\"\",\"userName\":\"admin\",\"password\":\"Elco@2022\"},\"tlsInfo\":{\"rootCer\":\"-----BEGIN CERTIFICATE-----\\nMIIDXjCCAkYCCQCAK3YA7XXAwjANBgkqhkiG9w0BAQsFADBxMQswCQYDVQQGEwJ6\\naDELMAkGA1UECAwCdGoxCzAJBgNVBAcMAnRqMQ0wCwYDVQQKDARlbGNvMQ0wCwYD\\nVQQLDARlbGNvMQ0wCwYDVQQDDARlbGNvMRswGQYJKoZIhvcNAQkBFgxlbGNvQGVs\\nY28uY24wHhcNMjIwOTI5MDMxNDQwWhcNMzIwOTI2MDMxNDQwWjBxMQswCQYDVQQG\\nEwJ6aDELMAkGA1UECAwCdGoxCzAJBgNVBAcMAnRqMQ0wCwYDVQQKDARlbGNvMQ0w\\nCwYDVQQLDARlbGNvMQ0wCwYDVQQDDARlbGNvMRswGQYJKoZIhvcNAQkBFgxlbGNv\\nQGVsY28uY24wggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQDA3ollTTjX\\noav+K9K+reejhsKbteD04jksu5SRJfZCBEVdmF5/IFdeKnvONQHCMazsZ14Y/0Ua\\nzVpMf0TxiVKg2UJgqXoeIep8/Nl1EhG8m59sjFcH8Skvs9VfwkDaAuRp6CSfL2K8\\n8AmunkcUQs+N8RZ5CbR4yIn6/SCwIZiyjNojxjeOYQg8qGsohryG2gidkKjc7aeU\\noj1Z99J9b4ikc8WxdqEKgLRT6zvlYVlqaOzqkdU1d4PyjsYk5F+3vof99vb3qaDU\\n2twce+0QsNfiTjdhJ84+CB8dOoGnmY4jM7JcITn6Yrv3wocxKatatkwMl01b4nFS\\n8GNOtMT7Fv/xAgMBAAEwDQYJKoZIhvcNAQELBQADggEBAKnOx+UvFN7WqEvrVRCr\\n9b4LypDJOHZxLnP6pCgCTpamPHDHWqJGWEtj3q7A2LIS2bNPWoo5fAGBv5ctbjeb\\n25xlSdBhthQpcggd2mBMzrkaeujF2vME8iqgr5SeiB27KbkWpU77aUvDG/VVF8Y6\\nHbURn4QGKZQT2SGwH9yHFbMS2iNQDYD+1MXZz/YRbwlM/29iLe2wV82uSQV7UyQA\\nwAfAnVV0PVLLcZk6CGpzJ5ZYlllwmFid7FBUImj0FeGmkxG8kPi9+bIB9RM90WJ3\\nfIUf8QA+v0TJANqy88kz0w9xYWZpz00Uh1zHBq8SXUyP724WgwQ1pHgvNTt4Om8+\\nPXs=\\n-----END CERTIFICATE-----\\n\",\"keystore\":\"\",\"truststore\":\"\",\"storePassword\":\"\",\"keyPassword\":\"\",\"algorithm\":\"\"}},\"baseConfigCache\":\"{\\\"syncPeriod\\\":\\\"10000\\\",\\\"dataCacheCycle\\\":\\\"7\\\",\\\"dataCacheFileSize\\\":\\\"1\\\",\\\"type\\\":\\\"cache_config\\\"}\"}";

        Agent agent = new AgentRequestHttpService().copyFieldToAgent(body);

        System.out.println(agent);

    }

}
