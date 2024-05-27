package com.elco.eeds.agent.sdk.core.disruptor;

import com.elco.eeds.agent.sdk.core.bean.properties.PropertiesValue;
import com.elco.eeds.agent.sdk.core.disruptor.event.DataEvent;
import com.elco.eeds.core.json.JsonUtils;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import java.util.List;
import java.util.concurrent.ExecutorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName DisruptorProducer
 * @Description Disruptor生产者
 * @Author OuYang
 * @Date 2024/4/7 11:12
 * @Version 1.0
 */
public class DisruptorProducer {

  public static Logger logger = LoggerFactory.getLogger(DisruptorProducer.class);
  private RingBuffer<DataEvent> ringBuffer;

  private ExecutorService executor;

  public DisruptorProducer(RingBuffer<DataEvent> ringBuffer, ExecutorService executor) {
    this.ringBuffer = ringBuffer;
    this.executor = executor;
  }

  public void send(List<PropertiesValue> data) {
    if (logger.isDebugEnabled()) {
      logger.debug("发送数据给消费者");
    }
    if (null != data) {
      translator(data);
    }
  }

  private static final EventTranslatorOneArg<DataEvent, List<PropertiesValue>> TRANSLATOR =new EventTranslatorOneArg<DataEvent, List<PropertiesValue>>() {
    @Override
    public void translateTo(DataEvent dataEvent, long sequence, List<PropertiesValue> propertiesValues) {
      dataEvent.setData(propertiesValues);
    }
  };


  private void translator(List<PropertiesValue> data) {
    if (logger.isDebugEnabled()) {
      logger.debug("【Disruptor】推送：" + JsonUtils.toJsonString(data));
    }
    ringBuffer.publishEvent(TRANSLATOR, data);
  }
}
