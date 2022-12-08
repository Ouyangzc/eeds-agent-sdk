package com.elco.eeds.agent.sdk.core.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @title: AgentFileExtendUtils
 * @Author wl
 * @Date: 2022/12/8 14:37
 * @Version 1.0
 * @Description: 从agent.json中读写token以及config信息
 */
public class AgentFileExtendUtils {

    public static final Logger logger = LoggerFactory.getLogger(AgentFileExtendUtils.class);

    /**
     * 从本地agent.json文件中获取token字段
     * @return
     */
    public static String getTokenFromLocalAgentFile() throws IOException {
        String content = AgentFileUtils.readLocalAgentFile();
        JSONObject jsonObject = JSON.parseObject(content, JSONObject.class);
        return jsonObject.get("token").toString();
    }

    /**
     * 从本地agent.json文件中获取config
     * @return
     * @throws IOException
     */
    public static String getConfigFromLocalAgentFile() throws IOException {
        String content = AgentFileUtils.readLocalAgentFile();
        JSONObject jsonObject = JSON.parseObject(content, JSONObject.class);
        return jsonObject.get("config").toString();
    }

    /**
     * 将token保存至agent.json中
     * @param token
     * @throws IOException
     */
    public static void setTokenToLocalAgentFile(String token) throws IOException {
        String content = AgentFileUtils.readLocalAgentFile();
        logger.debug("当前agent.json中内容为：{}", content);
        JSONObject jsonObject = JSON.parseObject(content, JSONObject.class);
        logger.debug("当前文件中token为：" + jsonObject.get("token").toString());
        jsonObject.put("token", token);
        // 修改后保存
        AgentFileUtils.strogeLocalAgentFile(JSON.toJSONString(jsonObject));
        logger.debug("token保存至json文件成功");
    }

    /**
     * 将config保存至agent.json中
     * @param config
     * @throws IOException
     */
    public static void setConfigToLocalAgentFile(JSONArray config) throws IOException {
        String content = AgentFileUtils.readLocalAgentFile();
        logger.debug("当前agent.json中内容为：{}", content);
        JSONObject jsonObject = JSON.parseObject(content, JSONObject.class);
        logger.debug("当前文件中config为：" + jsonObject.get("config").toString());
        jsonObject.put("config", config);
        // 修改后保存
        AgentFileUtils.strogeLocalAgentFile(JSON.toJSONString(jsonObject));
        logger.debug("config保存至json文件成功");
    }

}
