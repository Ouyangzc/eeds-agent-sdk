package com.elco.eeds.agent.sdk.core.bean.agent;

import java.io.Serializable;

/**
 * @ClassName AgentClusterProperties
 * @Description 集群配置参数
 * @Author OuYang
 * @Date 2023/11/16 9:43
 * @Version 1.0
 */
public class AgentClusterProperties implements Serializable {

    private Boolean enable;

    private String nodeName;

    public AgentClusterProperties() {
        this.enable = false;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }
}
