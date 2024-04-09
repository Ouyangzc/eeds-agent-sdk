package com.elco.eeds.agent.sdk.core.util;

import com.elco.eeds.agent.sdk.core.bean.agent.Agent;
import com.elco.eeds.agent.sdk.core.common.constant.ConstantFilePath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @ClassName ThingsFileUtils
 * @Description 数据源文件工具类
 * @Author OUYANG
 * @Date 2022/12/16 14:38
 */
public class ThingsFileUtils {
    public static final Logger logger = LoggerFactory.getLogger(ThingsFileUtils.class);
    /**
     * 获取文件
     *
     * @return
     * @throws IOException
     */
    public static String getThingsFilePath() throws IOException {
        String baseFolder = Agent.getInstance().getAgentBaseInfo().getBaseFolder() + ConstantFilePath.THINGS_FOLDER;
        String filePath = AgentFileUtils.getFilePath(baseFolder);
        return filePath + ConstantFilePath.THINGS_FILE;
    }


    public static void saveThingsFileToLocal(String data) throws IOException {
        String path = ThingsFileUtils.getThingsFilePath();
        File file = new File(path);
        org.apache.commons.io.FileUtils.writeStringToFile(file, data, StandardCharsets.UTF_8, false);
    }

    public static String readLocalThingsFile() throws IOException {
        String path = ThingsFileUtils.getThingsFilePath();
        File file = new File(path);
        if (!file.exists()) {
            return "";
        }
        String content = org.apache.commons.io.FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        return content;
    }

}
