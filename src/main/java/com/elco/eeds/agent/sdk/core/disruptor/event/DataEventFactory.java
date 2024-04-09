package com.elco.eeds.agent.sdk.core.disruptor.event;

import com.lmax.disruptor.EventFactory;

/**
 * @ClassName DataEventFactory
 * @Description 定义事件工厂
 * @Author OuYang
 * @Date 2024/4/7 11:07
 * @Version 1.0
 */
public class DataEventFactory implements EventFactory<DataEvent> {


  @Override
  public DataEvent newInstance() {
    return new DataEvent();
  }
}
