package com.elco.eeds.agent.sdk.core.storage;

import com.elco.eeds.agent.sdk.core.bean.properties.PropertiesValue;
import com.elco.eeds.agent.sdk.core.disruptor.DisruptorProcessorService;
import com.elco.eeds.agent.sdk.core.util.MapstructUtils;
import com.elco.storage.domain.PropertiesData;
import com.elco.storage.service.StorageService;
import com.elco.storage.utils.SpringUtils;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
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

  private static AtomicInteger nums = new AtomicInteger(0);

  @Override
  public void execute(List<PropertiesValue> data) {
    StorageService storageService = SpringUtils.getBean(StorageService.class);
    if (null == storageService) {
      return;
    }
    List<PropertiesData> datas = MapstructUtils.valueToData(data);
    storageService.store(datas);
    int addCount = nums.addAndGet(data.size());
    logger.info("数据存储,data size:{},入库数量:{}", data.size(),addCount);
  }
}
