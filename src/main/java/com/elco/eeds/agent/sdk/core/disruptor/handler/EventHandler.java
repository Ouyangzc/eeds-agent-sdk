package com.elco.eeds.agent.sdk.core.disruptor.handler;

import com.elco.eeds.agent.sdk.core.bean.properties.PropertiesValue;
import com.elco.eeds.agent.sdk.core.disruptor.DisruptorProcessorService;
import com.elco.eeds.agent.sdk.core.disruptor.event.DataEvent;
import com.elco.eeds.agent.sdk.core.util.MapstructUtils;
import com.elco.storage.domain.PropertiesData;
import com.lmax.disruptor.LifecycleAware;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName EventConsumer
 * @Description event消费者
 * @Author OuYang
 * @Date 2024/4/11 9:18
 * @Version 1.0
 */
public class EventHandler implements com.lmax.disruptor.EventHandler<DataEvent>, LifecycleAware {
  public static final Logger logger = LoggerFactory.getLogger(EventHandler.class);

  private final CountDownLatch shutdownLatch = new CountDownLatch(1);

  private DisruptorProcessorService disruptorService;

  private final Executor executor;

  public EventHandler(DisruptorProcessorService disruptorService, Executor executor) {
    this.disruptorService = disruptorService;
    this.executor = executor;
  }

  @Override
  public void onEvent(DataEvent dataEvent, long l, boolean b) throws Exception {
    if (logger.isDebugEnabled()) {
      logger.info("接受到数据更新请求  >>>" + dataEvent);
    }
    List<PropertiesValue> propertiesValues = dataEvent.getData();
    List<PropertiesData> data = MapstructUtils.valueToData(propertiesValues);
    executor.execute(() -> {
      disruptorService.execute(data);
    });
  }

  @Override
  public void onStart() {

  }

  @Override
  public void onShutdown() {
    shutdownLatch.countDown();
  }

  public void awaitShutdown() throws InterruptedException {
    shutdownLatch.await();
  }
}
