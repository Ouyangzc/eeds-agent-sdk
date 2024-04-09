package com.elco.eeds.agent.sdk.core.disruptor;

import com.elco.eeds.agent.sdk.core.bean.properties.PropertiesValue;
import java.util.List;

/**
 * @ClassName DisruptorRealTimeValueService
 * @Description 异步服务
 * @Author OuYang
 * @Date 2024/4/7 10:52
 * @Version 1.0
 */
public interface DisruptorRealTimeValueService {

  /**
   * 异步处理服务
   * @param data 数据
   */
  void execute(List<PropertiesValue> data);
}
