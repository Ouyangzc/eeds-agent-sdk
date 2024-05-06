package com.elco.eeds.agent.sdk.core.cache;

import java.time.Instant;

/**
 * @ClassName TimeStampCache
 * @Description 时间缓存类，放置并发获取时间戳
 * @Author OuYang
 * @Date 2024/4/29 14:43
 * @Version 1.0
 */
public class TimeStampCache extends BaseCache<Long> {

  public static final String TIMESTAMP_KEY = "timestamp";

  @Override
  protected Long getLoadDataIfNull(String key) {
    return Instant.now().toEpochMilli();
  }

  @Override
  protected Long getLoadData(String key) {
    return Instant.now().toEpochMilli();
  }
}
