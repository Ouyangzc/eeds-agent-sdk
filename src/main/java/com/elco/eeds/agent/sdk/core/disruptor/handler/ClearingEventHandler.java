package com.elco.eeds.agent.sdk.core.disruptor.handler;

import com.elco.eeds.agent.sdk.core.disruptor.event.DataEvent;
import com.lmax.disruptor.EventHandler;

/**
 * @ClassName ClearingEventHandler
 * @Description 清理对象
 * @Author OuYang
 * @Date 2024/4/7 11:06
 * @Version 1.0
 */
public class ClearingEventHandler implements EventHandler<DataEvent> {

  @Override
  public void onEvent(DataEvent dataEvent, long l, boolean b) throws Exception {
    dataEvent.clear();
  }
}
