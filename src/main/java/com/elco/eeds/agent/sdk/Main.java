package com.elco.eeds.agent.sdk;

import com.elco.eeds.agent.sdk.core.start.AgentStarter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description
 * @Author ${USER}
 * @Date ${DATE} ${TIME}
 **/
public class Main {
    public static final Logger logger = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) throws Exception {
        logger.info("sdk start");
        // 给yml的相对路径
//         AgentStarter.init("./agent.yaml");
        // 默认位置读取配置文件
        AgentStarter.init("D:\\agent.yaml");
    }
}
