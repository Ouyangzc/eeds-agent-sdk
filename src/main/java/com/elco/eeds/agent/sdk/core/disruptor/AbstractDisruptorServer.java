package com.elco.eeds.agent.sdk.core.disruptor;

import com.elco.eeds.agent.sdk.core.disruptor.entity.DisruptorConfig;
import com.elco.eeds.agent.sdk.core.disruptor.event.DataEvent;
import com.elco.eeds.agent.sdk.core.disruptor.event.DataEventFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName AbstractDisruptorService
 * @Description disruptor得抽象
 * @Author OuYang
 * @Date 2024/4/11 9:26
 * @Version 1.0
 */
public abstract class AbstractDisruptorServer {

  protected DisruptorConfig config;

  protected Disruptor<DataEvent> disruptor;

  protected RingBuffer<DataEvent> ringBuffer;

  public volatile boolean isStart;

  protected DisruptorProducer disruptorProducer;



  private ExecutorService producerExecutor;

  protected ExecutorService consumerExecutor;

  public AbstractDisruptorServer(DisruptorConfig config) {
    this.config = config;
    //初始化disruptor
    disruptor = new Disruptor<>(new DataEventFactory(), config.getRingBufferSize(), DisruptorThreadFactory
        .create(DisruptorThreadFactory.DISRUPTOR_THREAD_MAIN, false), config.getProducerType(), config.getWaitStrategy());

    producerExecutor = new ThreadPoolExecutor(config.getThreads(), config.getThreads(), 0, TimeUnit.MILLISECONDS,
        new LinkedBlockingQueue<>(), DisruptorThreadFactory
        .create(DisruptorThreadFactory.DISRUPTOR_THREAD_PRODUCER, false),
        new ThreadPoolExecutor.AbortPolicy());

    consumerExecutor = new ThreadPoolExecutor(config.getThreads(), config.getThreads(), 0, TimeUnit.MILLISECONDS,
        new LinkedBlockingQueue<>(), DisruptorThreadFactory
        .create(DisruptorThreadFactory.DISRUPTOR_THREAD_CONSUMER, false),
        new ThreadPoolExecutor.AbortPolicy());
  }


  public void doStart() {
    //设置消费者Handler
    createConsumer();
    ringBuffer = disruptor.start();
    //创建生产者
    disruptorProducer =  createProducer(ringBuffer,this.producerExecutor);
    this.isStart = true;
  }

  /**
   * 创建生产者
   * @param ringBuffer
   * @param executor
   * @return
   */
  protected abstract DisruptorProducer createProducer(RingBuffer<DataEvent> ringBuffer, ExecutorService executor);

  /**
   * 创建消费者
   */
  protected abstract void createConsumer();


  /**
   * 销毁disruptor对象 方法会堵塞，直至所有的事件都得到处理；
   *
   * @throws Exception
   */
  public void destroy() throws Exception {
    if (isStart) {
      disruptor.shutdown();
    }
    producerExecutor.shutdown();
  }

  protected void checkAndStart(){
    if (!this.isStart) {
      doStart();
      this.isStart = true;
    }
  }
}
