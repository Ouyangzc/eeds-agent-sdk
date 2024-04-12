package com.elco.eeds.agent.sdk.core.disruptor.entity;

import com.elco.eeds.agent.sdk.core.disruptor.DisruptorProcessorService;
import com.elco.eeds.agent.sdk.core.disruptor.handler.EventHandler;
import com.lmax.disruptor.BatchEventProcessor;
import java.io.Serializable;

/**
 * @ClassName EventHandlerResult
 * @Description 动态handler返回对象
 * @Author OuYang
 * @Date 2024/4/11 13:14
 * @Version 1.0
 */
public class EventHandlerResult implements Serializable {
  private DisruptorProcessorService processorService;

  private EventHandler consumer;

  private BatchEventProcessor eventProcessor;

  public DisruptorProcessorService getProcessorService() {
    return processorService;
  }

  public void setProcessorService(
      DisruptorProcessorService processorService) {
    this.processorService = processorService;
  }

  public EventHandler getConsumer() {
    return consumer;
  }

  public void setConsumer(EventHandler consumer) {
    this.consumer = consumer;
  }

  public BatchEventProcessor getEventProcessor() {
    return eventProcessor;
  }

  public void setEventProcessor(BatchEventProcessor eventProcessor) {
    this.eventProcessor = eventProcessor;
  }
}
