package com.elco.eeds.agent.sdk.core.disruptor.entity;

import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.dsl.ProducerType;
import java.io.Serializable;

/**
 * @ClassName DisruptorConfig
 * @Description 配置类
 * @Author OuYang
 * @Date 2024/4/11 9:21
 * @Version 1.0
 */
public class DisruptorConfig implements Serializable {

  /**
   * RingBuffer 大小，必须是 2 的 N 次方；
   */
  private int ringBufferSize = 1024;
  /**
   * 生产者类型
   */
  private ProducerType producerType = ProducerType.MULTI;

  /**
   * 等待策略
   */
  private WaitStrategy waitStrategy = new SleepingWaitStrategy();

  /**
   * 线程数
   */
  private int threads = Runtime.getRuntime().availableProcessors() * 2;

  public int getRingBufferSize() {
    return ringBufferSize;
  }

  public void setRingBufferSize(int ringBufferSize) {
    this.ringBufferSize = ringBufferSize;
  }

  public ProducerType getProducerType() {
    return producerType;
  }

  public void setProducerType(ProducerType producerType) {
    this.producerType = producerType;
  }

  public WaitStrategy getWaitStrategy() {
    return waitStrategy;
  }

  public void setWaitStrategy(WaitStrategy waitStrategy) {
    this.waitStrategy = waitStrategy;
  }

  public int getThreads() {
    return threads;
  }


  public static final class DisruptorConfigBuilder implements Serializable {

    private int ringBufferSize;
    private ProducerType producerType;
    private WaitStrategy waitStrategy;
    private int threads;

    private DisruptorConfigBuilder() {
    }

    public static DisruptorConfigBuilder create() {
      return new DisruptorConfigBuilder();
    }

    public DisruptorConfigBuilder ringBufferSize(int ringBufferSize) {
      this.ringBufferSize = ringBufferSize;
      return this;
    }

    public DisruptorConfigBuilder isMultiProducer(boolean isMultiProducer) {
      this.producerType = isMultiProducer ? ProducerType.MULTI : ProducerType.SINGLE;
      return this;
    }

    public DisruptorConfigBuilder waitStrategy(WaitStrategy waitStrategy) {
      this.waitStrategy = waitStrategy;
      return this;
    }

    public DisruptorConfigBuilder threads(int threads) {
      this.threads = threads;
      return this;
    }

    public DisruptorConfig build() {
      DisruptorConfig disruptorConfig = new DisruptorConfig();
      disruptorConfig.setRingBufferSize(ringBufferSize);
      disruptorConfig.setProducerType(producerType);
      disruptorConfig.setWaitStrategy(waitStrategy);
      disruptorConfig.threads = this.threads;
      return disruptorConfig;
    }
  }
}
