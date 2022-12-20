package com.elco.eeds.agent.sdk.transfer.service.data;

import com.elco.eeds.agent.sdk.core.bean.properties.PropertiesValue;

import java.util.List;

/**
 * @ClassName RealTimePropertiesValueService
 * @Description 实时数据类
 * @Author OUYANG
 * @Date 2022/12/20 13:39
 */
public class RealTimePropertiesValueService {

    /**
     *
     * @param message 原始报文
     * @param thingsId 数据源ID
     * @param propertiesValueList 解析数据
     */
    public static void recRealTimePropertiesValue(String message, String thingsId, List<PropertiesValue> propertiesValueList) {
        //存储原始数据，并推送，调用统计接口

    }

}
