package com.elco.eeds.agent.sdk.transfer.beans.message.order;

import java.util.List;
import java.util.Map;

/**
 * @author ：ytl
 * @date ：Created in 2022/12/23 13:38
 * @description：下发指令确认报文
 */
public class SubOrderRequestMessage {
    List<ThingsEntity>command;
}


class ThingsEntity{
    private String msgSeqNo;
    private String name;
    private String thingsId;
    private String thingTypes;
    private String agentId;
    private String thingCode;
    List<PropertiesEntity>properties;
}


class PropertiesEntity{
    private String name;
    private String dataType;
    private String type;
    private String address;
    private String value;
    private Map<String,String> extendedMap;

}
