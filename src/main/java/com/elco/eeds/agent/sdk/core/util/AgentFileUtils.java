package com.elco.eeds.agent.sdk.core.util;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import com.elco.eeds.agent.sdk.core.bean.agent.Agent;
import com.elco.eeds.agent.sdk.core.bean.agent.AgentBaseInfo;
import com.elco.eeds.agent.sdk.core.bean.agent.AgentMqInfo;
import com.elco.eeds.agent.sdk.core.common.constant.ConstantFilePath;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

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
     * @throws FileNotFoundException
     */
    public static String getFilePath(String pathName) throws FileNotFoundException {
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
        // TODO 测试用，需要删除
        AgentBaseInfo agentBaseInfo = new AgentBaseInfo();
        agentBaseInfo.setBaseFolder("./elco/eeds");
        agent.setAgentBaseInfo(agentBaseInfo);
        return agent.getAgentBaseInfo().getBaseFolder().isEmpty() ? ConstantFilePath.BASE_FOLDER : agent.getAgentBaseInfo().getBaseFolder();

    }

    public static void main(String[] args) throws IOException {
        // TODO 测试，后期删除
        JSONObject json = new JSONObject();
        json.put("token", "1234567890");

        AgentFileUtils.strogeLocalAgentFile(json.toString());

        // 测试单例
        Agent agent = Agent.getInstance();

        AgentBaseInfo agentBaseInfo = new AgentBaseInfo();
        agentBaseInfo.setBaseFolder("./elco/eeds");
        agent.setAgentBaseInfo(agentBaseInfo);

        AgentMqInfo agentMqInfo = new AgentMqInfo();
        agentMqInfo.setMqType("111");
        agent.setAgentMqInfo(agentMqInfo);

        System.out.println("==========>");
        System.out.println(agent.toString());

        agentBaseInfo.setAgentId("1111111111111111");
        agent.setAgentBaseInfo(agentBaseInfo);

        agentMqInfo.setMqType("2222222222");
        agent.setAgentMqInfo(agentMqInfo);

        System.out.println("==========>");
        System.out.println(agent.toString());
    }

}
