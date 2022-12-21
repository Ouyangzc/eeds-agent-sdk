package com.elco.eeds.agent.sdk.core.common.constant;

import java.io.File;

/**
 * @title: ConstantFileOperator
 * @Author wl
 * @Date: 2022/12/7 10:36
 * @Version 1.0
 */
public class ConstantFilePath {

    public static final String BASE_FOLDER = "./elco/eeds";

    public static final String AGENT_FILE_FOLDER = "/agent";

    public static final String AGENT_FILE_PATH = File.separator + "agent.json";


    /**
     * 数据源文件
     */
    public static final String THINGS_FOLDER = "/things";

    public static final String THINGS_FILE = File.separator + "things.json";

    public static final String THINGS_SOURCE_FILE = File.separator + "source.json";

    /**
     * 统计文件目录
     */
    public static final String DATA_COUNT_FOLDER = "/count";
    /**
     * 统计文件
     */
    public static final String DATA_COUNT_UNDONE_PATH = File.separator + "/undone/count.txt";
    /**
     * 统计完成文件
     */
    public static final String DATA_COUNT_DONE_PATH = File.separator + "/done/count.txt";

    public static final String YML_NAME = "agent-sdk-config.yaml";

    /**
     * 变量数据文件
     */
    public static final String PROPERTIES_DATA_FOLDER = "/data/";


    /**
     * 文件后缀
     */
    public static final String FILE_FORMAT_JSON = ".json";

}
