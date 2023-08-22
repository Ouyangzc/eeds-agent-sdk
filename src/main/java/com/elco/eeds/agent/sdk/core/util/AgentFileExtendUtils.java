package com.elco.eeds.agent.sdk.core.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.elco.eeds.agent.sdk.core.common.enums.ErrorEnum;
import com.elco.eeds.agent.sdk.core.exception.SdkException;
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
    public static String getTokenFromLocalAgentFile() throws SdkException {
        try {
            String content = AgentFileUtils.readLocalAgentFile();
            JSONObject jsonObject = JSON.parseObject(content, JSONObject.class);
            return jsonObject.get("token").toString();
        } catch (Exception e) {
            logger.debug("从json文件读取token失败,失败信息:",e);
              throw new SdkException(ErrorEnum.READ_TOKEN_ERROR.code());
        }
    }

    /**
     * 从本地agent.json文件中获取config
     * @return
     * @throws IOException
     */
    public static String getConfigFromLocalAgentFile() throws SdkException {
        try {
            String content = AgentFileUtils.readLocalAgentFile();
            JSONObject jsonObject = JSON.parseObject(content, JSONObject.class);
            return jsonObject.get("config").toString();
        } catch (Exception e) {
            logger.error("从json文件读取config失败");
            e.printStackTrace();
            throw new SdkException(ErrorEnum.READ_CONFIG_ERROR.code());

        }
    }

    /**
     * 将token保存至agent.json中
     * @param token
     * @throws IOException
     */
    public static void setTokenToLocalAgentFile(String token) throws SdkException {
        try {
            JSONObject jsonObject = new JSONObject();
            String content = AgentFileUtils.readLocalAgentFile();
            logger.debug("当前agent.json中内容为：{}", content);
            if(!StrUtil.isEmpty(content)) {
                jsonObject = JSON.parseObject(content, JSONObject.class);
            }
            jsonObject.put("token", token);
            // 修改后保存
            AgentFileUtils.strogeLocalAgentFile(JSONUtil.toJsonStr(jsonObject));
            logger.debug("token保存至json文件成功");
        } catch (IOException ioException) {
            logger.debug("token保存agent.json失败", ioException);
            ioException.printStackTrace();
            throw new SdkException(ErrorEnum.SAVE_TOKEN_ERROR.code());
        }
    }

    /**
     * 将config保存至agent.json中
     * @param config
     * @throws IOException
     */
    public static void setConfigToLocalAgentFile(JSONArray config) throws SdkException {
        try {
            JSONObject jsonObject = new JSONObject();
            String content = AgentFileUtils.readLocalAgentFile();
            logger.debug("当前agent.json中内容为：{}", content);
            if(!StrUtil.isEmpty(content)) {
                jsonObject = JSON.parseObject(content, JSONObject.class);
            }
            jsonObject.put("config", config);
            // 修改后保存
            AgentFileUtils.strogeLocalAgentFile(JSONUtil.toJsonStr(jsonObject));
            logger.debug("config保存至json文件成功");
        } catch (IOException ioException) {
            logger.debug("config保存agent.json失败", ioException);
            ioException.printStackTrace();
            throw new SdkException(ErrorEnum.SAVE_CONFIG_ERROR.code());
        }
    }
}
