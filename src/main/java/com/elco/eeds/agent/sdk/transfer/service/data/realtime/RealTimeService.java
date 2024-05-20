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
}
