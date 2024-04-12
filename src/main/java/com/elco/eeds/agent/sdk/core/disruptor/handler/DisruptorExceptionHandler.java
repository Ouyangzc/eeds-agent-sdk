package com.elco.eeds.agent.sdk.core.disruptor.handler;

import com.elco.eeds.agent.sdk.core.disruptor.DisruptorProcessorService;
import com.elco.eeds.agent.sdk.core.disruptor.event.DataEvent;
import com.lmax.disruptor.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName DisruptorExceptionHandler
 * @Description Disruptor自定义异常处理
 * @Author OuYang
 * @Date 2024/4/7 11:09
 * @Version 1.0
 */
public class DisruptorExceptionHandler implements ExceptionHandler<DataEvent> {

  public static final Logger logger = LoggerFactory.getLogger(DisruptorExceptionHandler.class);

  private DisruptorProcessorService disruptorService;

  public DisruptorExceptionHandler(DisruptorProcessorService disruptorService) {
    this.disruptorService = disruptorService;
  }

  @Override
  public void handleEventException(Throwable throwable, long sequence, DataEvent dataEvent) {
    //事件处理异常里面进行补偿执行
    disruptorService.execute(dataEvent.getData());
    logger.error(">>> Disruptor事件处理异常，进行立即执行补偿操作..........");
    logger.error(">>> 异常信息如下：", throwable.getMessage());
  }

  @Override
  public void handleOnStartException(Throwable throwable) {
    logger.error(">>> Disruptor 启动异常：{}", throwable.getMessage());
  }

  @Override
  public void handleOnShutdownException(Throwable throwable) {
    logger.error(">>> Disruptro 关闭异常：{}", throwable.getMessage());
  }
}
