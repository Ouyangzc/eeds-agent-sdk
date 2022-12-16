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
    CountDataHolder getCountFile();


    /**
     * 接收实时数据
     * @param agentId 客户端ID
     * @param collectTime 数据采集时间戳
     * @param thingsDataCount 数据源统计信息
     */
    void recRealTimeData(String agentId, Long collectTime, ThingsDataCount thingsDataCount);


    /**
     * 获取要发送的统计记录
     *
     * @return
     */
    PostDataCount getPostCountData();




    /**
     * 保存统计数据
     *
     * @param dataCount 统计记录
     */
    void saveDoneCountData(PostDataCount dataCount);

    /**
     * 初始化
     */
    void setUp();


    /**
     * 收到确认报文
     *
     * @param countId 统计ID
     */
    void recConfirmMsg(String countId);
}
