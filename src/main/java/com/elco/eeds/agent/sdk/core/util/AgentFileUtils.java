package com.elco.eeds.agent.sdk.core.util;

import com.alibaba.fastjson.JSONArray;
import com.elco.eeds.agent.sdk.core.bean.agent.Agent;
import com.elco.eeds.agent.sdk.core.bean.agent.AgentBaseInfo;
import com.elco.eeds.agent.sdk.core.bean.agent.AgentMqInfo;
import com.elco.eeds.agent.sdk.core.bean.agent.BaseConfigEntity;
import com.elco.eeds.agent.sdk.core.common.constant.ConstantFilePath;
import com.elco.eeds.agent.sdk.core.exception.SdkException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @title: AgentFileUtils
 * @Author wl
 * @Date: 2022/12/7 10:20
 * @Version 1.0
 */
public class AgentFileUtils {

    /**
     * 存储该客户端的AgentFile
     *
     * @param agentData
     * @throws IOException
     */
    public static void strogeLocalAgentFile(String agentData) throws IOException {
        String agentFilePath = getAgentFilePath();
        writeFile(agentFilePath, agentData);
    }

    /**
     * 获取该客户端的AgentFile
     *
     * @return
     * @throws IOException
     */
    public static String readLocalAgentFile() throws IOException {
        String agentFilePath = getAgentFilePath();
        return readFile(agentFilePath);
    }

    public static String getAgentFilePath() throws FileNotFoundException {
        String filePath = getFilePath(getAgentFolder());
        return filePath + ConstantFilePath.AGENT_FILE_PATH;
    }

    /**
     * 获取文件存储目录
     * @return
     * @throws FileNotFoundException
     */
    /**
     * @param pathName 子文件目录
     * @return
     */
    public static String getFilePath(String pathName) {
        //获取根目录
        File path = new File(AgentFileUtils.class.getClassLoader().getResource("").getPath());

        if (!path.exists()) {
            path = new File("");
        }
        File upload = new File(path.getAbsoluteFile(), pathName);
        if (!upload.exists()) {
            upload.mkdirs();
        }
        return upload.getPath();
    }

    /**
     * 读取文件信息
     *
     * @param path
     * @return
     * @throws IOException
     */
    public static String readFile(String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            return "";
        }
        String agentInfo = org.apache.commons.io.FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        return agentInfo;
    }

    /**
     * 存储agent注册信息
     *
     * @param path
     * @param data
     * @throws IOException
     */
    public static void writeFile(String path, String data) throws IOException {
        File file = new File(path);
        org.apache.commons.io.FileUtils.writeStringToFile(file, data, StandardCharsets.UTF_8, false);
    }

    public static String getAgentFolder() {
        return getBaseFolder() + ConstantFilePath.AGENT_FILE_FOLDER;
    }

    public static String getBaseFolder() {
        Agent agent = Agent.getInstance();
        return agent.getAgentBaseInfo().getBaseFolder().isEmpty() ? ConstantFilePath.BASE_FOLDER : agent.getAgentBaseInfo().getBaseFolder();

    }

}
