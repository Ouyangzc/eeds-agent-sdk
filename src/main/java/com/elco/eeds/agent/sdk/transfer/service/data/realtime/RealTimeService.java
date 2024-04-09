package com.elco.eeds.agent.sdk.transfer.service.data.realtime;

import com.elco.eeds.agent.sdk.core.bean.properties.PropertiesValue;
import java.util.List;

/**
 * @ClassName RealTimeService
 * @Description 实时数据接口
 * @Author OuYang
 * @Date 2024/4/7 16:29
 * @Version 1.0
 */
public interface RealTimeService {

  /**
   * 校验数据采集时间
   * @param collectTime
   * @return
   */
  boolean checkCollectTimeValid(Long collectTime);
  /**
   * 存储原始报文
   * @param thingsId
   * @param msg
   * @param collectTime
   * @throws Exception
   */
  void storageOriginalMsg(String thingsId, String msg, Long collectTime) throws Exception;

  /**
   * 统计数据
   * @param thingsId
   * @param valueList
   * @param collectTime
   * @throws Exception
   */
  void countRealTimeValueData(String thingsId, List<PropertiesValue> valueList, Long collectTime)
      throws Exception;
}
