package com.elco.eeds.agent.sdk.core.bean;

import com.elco.eeds.agent.sdk.core.bean.properties.PropertiesValue;
import java.io.Serializable;

/**
 * @ClassName RealTimeData
 * @Description 实时数据
 * @Author OuYang
 * @Date 2024/5/16 13:08
 * @Version 1.0
 */
public class RealTimeData implements Serializable {
  long timestamp;
  PropertiesValue propertiesValue;

  public RealTimeData(long timestamp, PropertiesValue propertiesValue) {
    this.timestamp = timestamp;
    this.propertiesValue = propertiesValue;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public PropertiesValue getPropertiesValue() {
    return propertiesValue;
  }
}
