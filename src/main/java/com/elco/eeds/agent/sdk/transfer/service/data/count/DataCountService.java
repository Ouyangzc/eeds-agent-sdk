package com.elco.eeds.agent.sdk.transfer.service.data.count;

import com.elco.eeds.agent.sdk.transfer.beans.data.count.PostDataCount;
import com.elco.eeds.agent.sdk.transfer.beans.data.count.ThingsDataCount;

/**
 * @ClassName DataCountService
 * @Description 数据统计接口
 * @Author OUYANG
 * @Date 2022/12/9 10:16
 */
public interface DataCountService {
    /**
     * 获取统计文件中的记录
     *
     * @return
     */
    //CountDataHolder getCountFile();


    /**
     * 接收实时数据
     * @param agentId 客户端ID
     * @param collectTime 数据采集时间戳
     * @param thingsDataCount 数据源统计信息
     */
    //void recRealTimeData(String agentId, Long collectTime, ThingsDataCount thingsDataCount);


    /**
     * 获取要发送的统计记录
     *
     * @return
     */
    PostDataCount getPostCountData();



}
