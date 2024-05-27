package com.elco.eeds.agent.sdk.core.storage;

import com.elco.eeds.agent.sdk.core.disruptor.DisruptorProcessorService;
import com.elco.storage.domain.PropertiesData;
import com.elco.storage.service.StorageService;
import com.elco.storage.utils.SpringUtils;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName DisruptorRealTimeValueServiceImpl
 * @Description 试试数据存储消费逻辑
 * @Author OuYang
 * @Date 2024/4/8 11:23
 * @Version 1.0
 */

public class RealTimeValueStorageServiceImpl implements DisruptorProcessorService {

  public static Logger logger = LoggerFactory.getLogger(RealTimeValueStorageServiceImpl.class);

  @Override
  public void execute(List<PropertiesData> data) {
    try {
      StorageService storageService = SpringUtils.getBean(StorageService.class);
      if (null == storageService) {
        return;
      }
      storageService.store(data);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
