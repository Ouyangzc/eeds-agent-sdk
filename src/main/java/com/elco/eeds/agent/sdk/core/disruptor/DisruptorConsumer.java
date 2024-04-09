package com.elco.eeds.agent.sdk.core.disruptor;

import com.elco.eeds.agent.sdk.core.disruptor.event.DataEvent;
import com.lmax.disruptor.WorkHandler;
import java.util.concurrent.Executor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName DisruptorConsumer
 * @Description 消费者Handler
 * @Author OuYang
 * @Date 2024/4/7 10:55
 * @Version 1.0
 */
public class DisruptorConsumer implements WorkHandler<DataEvent> {

  public static final Logger logger = LoggerFactory.getLogger(DisruptorConsumer.class);

  private DisruptorRealTimeValueService disruptorService;

  private final Executor executor;


  public DisruptorConsumer(DisruptorRealTimeValueService disruptorService, Executor executor) {
    this.disruptorService = disruptorService;
    this.executor = executor;
  }

  @Override
  public void onEvent(DataEvent dataEvent) throws Exception {
    if (logger.isDebugEnabled()) {
      logger.info("接受到数据更新请求  >>>" + dataEvent);
    }
    executor.execute(() -> {
      disruptorService.execute(dataEvent.getData());
    });
  }
}
