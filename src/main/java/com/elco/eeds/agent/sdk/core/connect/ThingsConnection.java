package com.elco.eeds.agent.sdk.core.connect;

import com.elco.eeds.agent.sdk.core.bean.properties.PropertiesEvent;
import com.elco.eeds.agent.sdk.core.start.AgentStartProperties;
import com.elco.eeds.agent.sdk.transfer.beans.things.ThingsDriverContext;

/**
 * @author ：ytl
 * @date ：Created in 2022/12/1 13:49
 * @description：数据源连接接口
 */
public interface ThingsConnection {

    /**
     *  协议名称
     * @return
     */
    default String protocolName(){
     return AgentStartProperties.getInstance().getAgentClientType();
    }

    /**
     * 连接数据源
     * @param info
     * @return
     */
    boolean connect(ThingsDriverContext info) ;



    /**
     * 断开数据源
     * @return
     */
    boolean disconnect();
    
    
    /**
     * 变量变动事件(增，删，改)
     * @param propertiesEvent
     */
    void propertiesEventNotify(PropertiesEvent propertiesEvent);






}
