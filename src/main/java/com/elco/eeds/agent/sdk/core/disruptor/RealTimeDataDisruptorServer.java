package com.elco.eeds.agent.sdk.core.disruptor;

import com.elco.eeds.agent.sdk.core.bean.properties.PropertiesValue;
import com.elco.eeds.agent.sdk.core.disruptor.entity.DisruptorConfig;
import com.elco.eeds.agent.sdk.core.disruptor.entity.EventHandlerResult;
import com.elco.eeds.agent.sdk.core.disruptor.event.DataEvent;
import com.elco.eeds.agent.sdk.core.disruptor.handler.ClearingEventHandler;
import com.elco.eeds.agent.sdk.core.disruptor.handler.DisruptorExceptionHandler;
import com.elco.eeds.agent.sdk.core.disruptor.handler.EventHandler;
import com.elco.eeds.agent.sdk.core.disruptor.handler.WorkerHandler;
import com.elco.eeds.agent.sdk.core.storage.RealTimeValueStorageServiceImpl;
import com.lmax.disruptor.BatchEventProcessor;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.util.DaemonThreadFactory;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @ClassName RealTimeDataDisruptorServer
 * @Description 试试数据DisruptorServer
 * @Author OuYang
 * @Date 2024/4/11 9:45
 * @Version 1.0
 */
public class RealTimeDataDisruptorServer extends AbstractDisruptorServer {

  private DisruptorProcessorService disruptorService;

  private static RealTimeDataDisruptorServer instance;
  static {
    DisruptorConfig config = new DisruptorConfig();
    RealTimeValueStorageServiceImpl disruptorService = new RealTimeValueStorageServiceImpl();
    instance = new RealTimeDataDisruptorServer(config, disruptorService);
  }
  private static RealTimeValueStorageServiceImpl storageService = new RealTimeValueStorageServiceImpl();

  public RealTimeDataDisruptorServer(DisruptorProcessorService disruptorService) {
    this(new DisruptorConfig(), disruptorService);
  }

  public RealTimeDataDisruptorServer(DisruptorConfig config,
      DisruptorProcessorService disruptorService) {
    super(config);
    this.disruptorService = disruptorService;
  }

  public static RealTimeDataDisruptorServer getInstance() {
    return getInstance(storageService);
  }

  public static RealTimeDataDisruptorServer getInstance(
      DisruptorProcessorService disruptorService) {
    DisruptorConfig config = new DisruptorConfig();
    return getInstance(config, disruptorService);
  }

  public static RealTimeDataDisruptorServer getInstance(DisruptorConfig config,
      DisruptorProcessorService disruptorService) {
    if (instance == null) {
      synchronized (RealTimeDataDisruptorServer.class) {
        if (instance == null) {
          instance = new RealTimeDataDisruptorServer(config, disruptorService);
        }
      }
    }
    return instance;
  }



  public static RealTimeDataDisruptorServer getInstance2() {
    return instance;
  }
  @Override
  protected DisruptorProducer createProducer(RingBuffer<DataEvent> ringBuffer,
      ExecutorService executor) {
    return new DisruptorProducer(ringBuffer, executor);
  }

  @Override
  protected void createConsumer() {
    int threads = config.getThreads();
    WorkerHandler[] disruptorConsumers = new WorkerHandler[threads];
    for (int i = 0; i < threads; i++) {
      disruptorConsumers[i] = new WorkerHandler(disruptorService);
    }
    //设置消费组
    disruptor.handleEventsWithWorkerPool(disruptorConsumers).then(new ClearingEventHandler());
    //设置异常处理
    disruptor.setDefaultExceptionHandler(new DisruptorExceptionHandler(disruptorService));
  }

  public void sendData(List<PropertiesValue> data) {
    checkAndStart();
    disruptorProducer.send(data);
  }

  /**
   * 动态添加消费者
   *
   * @param disruptorService
   */
  public EventHandlerResult addEventHandler(DisruptorProcessorService disruptorService) {
    checkAndStart();
    EventHandlerResult handlerResult = new EventHandlerResult();
    ExecutorService executor = Executors.newCachedThreadPool(DaemonThreadFactory.INSTANCE);
    EventHandler eventHandlerHandler = new EventHandler(disruptorService, consumerExecutor);
    BatchEventProcessor<DataEvent> processor =
        new BatchEventProcessor(ringBuffer, ringBuffer.newBarrier(), eventHandlerHandler);
    ringBuffer.addGatingSequences(processor.getSequence());
    executor.execute(processor);
    handlerResult.setConsumer(eventHandlerHandler);
    handlerResult.setEventProcessor(processor);
    handlerResult.setProcessorService(disruptorService);
    return handlerResult;
  }

  /**
   * 动态移除
   *
   * @param processor
   * @param eventHandlerHandler
   * @throws InterruptedException
   */
  public void removeEventHandler(BatchEventProcessor<DataEvent> processor,
      EventHandler eventHandlerHandler)
      throws InterruptedException {
    processor.halt();
    eventHandlerHandler.awaitShutdown();
    ringBuffer.removeGatingSequence(processor.getSequence());
  }


}
