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
        AgentStarter.init("application-test.yaml");
    }
}
