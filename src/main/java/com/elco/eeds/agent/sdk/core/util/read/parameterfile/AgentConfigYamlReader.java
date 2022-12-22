package com.elco.eeds.agent.sdk.core.util.read.parameterfile;

import cn.hutool.core.io.resource.ClassPathResource;
import com.elco.eeds.agent.sdk.core.common.enums.ErrorEnum;
import com.elco.eeds.agent.sdk.core.exception.SdkException;
import com.elco.eeds.agent.sdk.core.start.AgentStartProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.net.URL;
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

    public boolean isAbsolutePath(String path) {
        if (path.startsWith("/") || path.indexOf(":") > 0) {
            return true;
        }
        return false;

    }

    /**
     *
     * @param location
     * @return
     * @throws SdkException
     */
    public AgentStartProperties parseYaml(String location, boolean isAbsolutePath) throws SdkException {
        logger.info("获取配置文件路径：{}",location);
        Map<String,Object> map;
        try{
            Yaml yaml = new Yaml();
            URL resource;
            if (isAbsolutePath) {
                resource = resourceLoader.getResourceByAbsolutePath(location);
                if (resource != null){
                    //读取yaml中的数据并且以map集合的形式存储
                    UrlResource urlResource = new UrlResource(resource);
                    map = yaml.load(urlResource.getInputStream());
                    // logger.debug("yml内容为：{}", map);
                }else {
                    throw new SdkException(ErrorEnum.RESOURCE_OBTAIN_ERROR.code());
                }
            }else {
                ClassPathResource classPathResource = resourceLoader.getResource(location);
                map = yaml.load(classPathResource.getStream());
            }

        }catch (Exception e){
            logger.error("{}路径读取配置文件失败，没有此文件", e);
            e.printStackTrace();
            return null;
        }
        Map agent = null;
        AgentStartProperties agentStartProperties = null;
        try {
            agent = (Map) map.get("agent");

            agentStartProperties = AgentStartProperties.getInstance();
            agentStartProperties.setServerUrl(agent.get("serverUrl").toString());
            agentStartProperties.setName(agent.get("name").toString());
            agentStartProperties.setPort(agent.get("port").toString());
            agentStartProperties.setToken(agent.get("token").toString());
            agentStartProperties.setBaseFolder(agent.get("baseFolder").toString());
            agentStartProperties.setAgentClientType(agent.get("clientType").toString());
            agentStartProperties.setProtocolPackage(agent.get("protocolPackage").toString());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("配置文件中字段错误，请检查");
            throw new SdkException(ErrorEnum.CONFIG_FILE_ERROR.code());
        }

//        logger.info("从配置文件取出配置成功：serverUrl={}, name={}, port={}, token={}, baseFolder={}",
//                agent.get("serverUrl"), agent.get("name"), agent.get("port"),
//                agent.get("token"), agent.get("baseFolder"));
        return agentStartProperties;
    }
}
