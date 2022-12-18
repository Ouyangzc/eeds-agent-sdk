package com.elco.eeds.agent.sdk.core.util.read.parameterfile;

import com.elco.eeds.agent.sdk.Main;
import com.elco.eeds.agent.sdk.core.common.enums.ErrorEnum;
import com.elco.eeds.agent.sdk.core.exception.SdkException;
import com.elco.eeds.agent.sdk.core.start.AgentStartProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @title: AgentConfigYamlReader
 * @Author wl
 * @Date: 2022/12/6 11:09
 * @Version 1.0
 */
public class AgentConfigYamlReader {

    public static final Logger logger = LoggerFactory.getLogger(AgentConfigYamlReader.class);

    private final ResourceLoader resourceLoader;

    public AgentConfigYamlReader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public AgentStartProperties parseYaml(String location) {
        Map<String,Object> map;
        try{
            Yaml yaml = new Yaml();
            //通过class.getResource来获取yaml的路径
            URL resource = resourceLoader.getResource(location);
            if (resource != null){
                //读取yaml中的数据并且以map集合的形式存储
                UrlResource urlResource = new UrlResource(resource);
                map = yaml.load(urlResource.getInputStream());
                logger.debug("yml内容为：{}", map);
            }else {
                throw new SdkException(ErrorEnum.RESOURCE_OBTAIN_ERROR.code());
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        Map agent = (Map) map.get("agent");

        AgentStartProperties agentStartProperties = AgentStartProperties.getInstance();
        agentStartProperties.setServerUrl(agent.get("serverUrl").toString());
        agentStartProperties.setName(agent.get("name").toString());
        agentStartProperties.setPort(agent.get("port").toString());
        agentStartProperties.setToken(agent.get("token").toString());
        agentStartProperties.setBaseFolder(agent.get("baseFolder").toString());
        logger.info("从配置文件取出配置成功：serverUrl={}, name={}, port={}, token={}, baseFolder={}",
                agent.get("serverUrl"), agent.get("name"), agent.get("port"),
                agent.get("token"), agent.get("baseFolder"));
        return agentStartProperties;
    }

    public static void main(String[] args) {
        String ymlPath = "./elco/yml/application-test.yaml";
//        String ymlPath = "D:\\elco\\agentSdk\\application-test.yaml";
        AgentConfigYamlReader agentConfigYamlReader = new AgentConfigYamlReader(new ResourceLoader());
        AgentStartProperties agentStartProperties = agentConfigYamlReader.parseYaml(ymlPath);
    }

}
