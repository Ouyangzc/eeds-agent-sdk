package com.elco.eeds.agent.sdk.core.disruptor;

import com.elco.eeds.agent.sdk.core.bean.properties.PropertiesValue;
import com.elco.eeds.agent.sdk.core.disruptor.event.DataEvent;
import com.elco.eeds.agent.sdk.core.disruptor.event.DataEventFactory;
import com.elco.eeds.agent.sdk.core.disruptor.handler.ClearingEventHandler;
import com.elco.eeds.agent.sdk.core.disruptor.handler.DisruptorExceptionHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName DisruptorService
 * @Description Disruptor服务
 * @Author OuYang
 * @Date 2024/4/7 13:16
 * @Version 1.0
 */
public class DisruptorService {

  /**
   * RingBuffer 大小，必须是 2 的 N 次方；
   */
  private int ringBufferSize = 1024 * 1024;

  private Disruptor<DataEvent> disruptor;

  private boolean isMultiProducer;

  private WaitStrategy waitStrategy;


  private RingBuffer<DataEvent> ringBuffer;

  public volatile boolean isStart;

  private DisruptorRealTimeValueService disruptorService;

  private ExecutorService executor;

  private int threads;

  private DisruptorProducer disruptorProducer;

  private static DisruptorService instance;

  public static DisruptorService getInstance(DisruptorRealTimeValueService realTimeValueService) {
    if (instance == null) {
      synchronized (DisruptorService.class) {
        if (instance == null) {
          instance = new DisruptorService(realTimeValueService);
        }
      }
    }
    return instance;
  }

  private DisruptorService(DisruptorRealTimeValueService disruptorService) {
    threads = Runtime.getRuntime().availableProcessors();
    this.disruptorService = disruptorService;
    //单生产者
    isMultiProducer = true;
    ProducerType producerType = ProducerType.SINGLE;
    if (isMultiProducer) {
      //多生产者
      producerType = ProducerType.MULTI;
    }
    // 等待策略
    waitStrategy = new SleepingWaitStrategy();
    //初始化disruptor
    disruptor = new Disruptor<>(new DataEventFactory(), ringBufferSize, DisruptorThreadFactory
        .create(DisruptorThreadFactory.DISRUPTOR_THREAD_MAIN, false), producerType, waitStrategy);

    executor = new ThreadPoolExecutor(threads, threads, 0, TimeUnit.MILLISECONDS,
        new LinkedBlockingQueue<>(), DisruptorThreadFactory
        .create(DisruptorThreadFactory.DISRUPTOR_THREAD_PRODUCER, false),
        new ThreadPoolExecutor.AbortPolicy());
  }

  public void doStart() {
    //CPU可使用数量
    final Executor executor = new ThreadPoolExecutor(threads, threads, 0, TimeUnit.MILLISECONDS,
        new LinkedBlockingQueue<>(), DisruptorThreadFactory
        .create(DisruptorThreadFactory.DISRUPTOR_THREAD_CONSUMER, false),
        new ThreadPoolExecutor.AbortPolicy());
    DisruptorConsumer[] disruptorConsumers = new DisruptorConsumer[threads];
    for (int i = 0; i < threads; i++) {
      disruptorConsumers[i] = new DisruptorConsumer(disruptorService, executor);
    }
    //设置消费者Handler
    disruptor.handleEventsWithWorkerPool(disruptorConsumers).then(new ClearingEventHandler());
    //设置异常处理
    disruptor.setDefaultExceptionHandler(new DisruptorExceptionHandler(disruptorService));
    ringBuffer = disruptor.start();
    this.isStart = true;
    //创建生产者
    disruptorProducer = new DisruptorProducer(ringBuffer, this.executor);
  }

  /**
   * 销毁disruptor对象 方法会堵塞，直至所有的事件都得到处理；
   *
   * @throws Exception
   */
  public void destroy() throws Exception {
    if (isStart) {
      disruptor.shutdown();
    }
    executor.shutdown();
  }


  public void sendData(List<PropertiesValue> data) {
    if (!this.isStart) {
      doStart();
      this.isStart = true;
    }
    disruptorProducer.send(data);
  }

}
