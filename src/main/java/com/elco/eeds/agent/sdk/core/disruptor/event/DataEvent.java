package com.elco.eeds.agent.sdk.core.disruptor.event;

import com.elco.eeds.agent.sdk.core.bean.properties.PropertiesValue;
import java.io.Serializable;
import java.util.List;

/**
 * @ClassName DataEvent
 * @Description disruptor事件
 * @Author OuYang
 * @Date 2024/4/7 10:47
 * @Version 1.0
 */
public class DataEvent implements Serializable {

  private List<PropertiesValue> data;


  public List<PropertiesValue> getData() {
    return data;
  }

  public void setData(List<PropertiesValue> data) {
    this.data = data;
  }

  public void clear() {
    data = null;
  }
}
