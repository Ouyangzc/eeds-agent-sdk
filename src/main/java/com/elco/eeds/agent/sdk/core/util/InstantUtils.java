package com.elco.eeds.agent.sdk.core.util;

import com.elco.eeds.agent.sdk.core.cache.TimeStampCache;
import java.time.Instant;

/**
 * @ClassName InstantUtils
 * @Description Instant 工具类
 * @Author OuYang
 * @Date 2024/4/29 9:36
 * @Version 1.0
 */
public class InstantUtils {



  private static TimeStampCache timeStampCache;
  static {
    timeStampCache = new TimeStampCache();
  }

  private InstantUtils() {
  }


  public static Long getUniqueTimestamp() {
    long currentTimestamp = Instant.now().toEpochMilli();
    Long cacheValue = timeStampCache.getCacheValue(TimeStampCache.TIMESTAMP_KEY);
    while (currentTimestamp == cacheValue) {
      currentTimestamp = Instant.now().toEpochMilli();
    }
    timeStampCache.putIntoCache(TimeStampCache.TIMESTAMP_KEY,currentTimestamp);
    return currentTimestamp;
  }

}
