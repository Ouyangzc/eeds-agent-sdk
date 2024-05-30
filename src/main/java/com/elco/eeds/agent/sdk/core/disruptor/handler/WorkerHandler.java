package com.elco.eeds.agent.sdk.core.disruptor.handler;

import com.elco.eeds.agent.sdk.core.bean.properties.PropertiesValue;
import com.elco.eeds.agent.sdk.core.disruptor.DisruptorProcessorService;
import com.elco.eeds.agent.sdk.core.disruptor.event.DataEvent;
import com.elco.eeds.agent.sdk.core.util.MapstructUtils;
import com.elco.eeds.core.json.JsonUtils;
import com.elco.storage.domain.PropertiesData;
import com.lmax.disruptor.WorkHandler;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName WorkerConsumer
 * @Description 消费者Handler
 * @Author OuYang
 * @Date 2024/4/7 10:55
 * @Version 1.0
 */
public class WorkerHandler implements WorkHandler<DataEvent> {

  public static final Logger logger = LoggerFactory.getLogger(WorkerHandler.class);

  private DisruptorProcessorService disruptorService;

//  private final Executor executor;


  public WorkerHandler(DisruptorProcessorService disruptorService) {
    this.disruptorService = disruptorService;
//    this.executor = executor;
  }

  @Override
  public void onEvent(DataEvent dataEvent) throws Exception {
    if (logger.isDebugEnabled()) {
      logger.debug("【Disruptor】消费：" + JsonUtils.toJsonString(dataEvent.getData()));
    }
    List<PropertiesValue> data = dataEvent.getData();
    List<PropertiesData> datas = MapstructUtils.valueToData(data);
    disruptorService.execute(datas);
    dataEvent.clear();
  }
}
