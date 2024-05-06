package com.elco.eeds.agent.sdk.core.cache;


import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName BaseCache
 * @Description 二级缓存 guava cache简单封装
 * @Author OuYang
 * @Date 2024/4/29 10:24
 * @Version 1.0
 */
public abstract class BaseCache<V> {

  private static final Logger logger = LoggerFactory.getLogger(BaseCache.class);

  /**
   * 缓存对象
   */
  private LoadingCache<String, V> cache;
  /**
   * 异步刷新线程池
   */

  private ExecutorService executorService;

  /**
   * 缓存失效时长
   */
  protected long duration = 60L;

  /**
   * 时间单位
   */
  protected TimeUnit timeUnit = TimeUnit.SECONDS;

  /**
   * 返回Loading cache(单例模式的)
   *
   * @return
   */
  private LoadingCache<String, V> getCache() {
    if (cache == null) {
      synchronized (BaseCache.class) {
        if (cache == null) {
          CacheBuilder<Object, Object> tempCache = null;

          if (duration > 0 && timeUnit != null) {
            tempCache = CacheBuilder.newBuilder()
                .expireAfterWrite(duration, timeUnit);
          }

//          //设置最大缓存大小
//          if (maximumSize > 0) {
//            tempCache.maximumSize(maximumSize);
//          }
          tempCache.recordStats();
          tempCache.removalListener(new RemovalListener<String, V>() {
            @Override
            public void onRemoval(RemovalNotification<String, V> notification) {
              logger.info("缓存自动清除, key为：{}", notification.getKey());
            }
          });
          //加载缓存
          cache = tempCache.build(new CacheLoader<String, V>() {
            //缓存不存在或过期时调用
            @Override
            public V load(String key) throws Exception {
              //不允许返回null值
              V target = getLoadData(key) != null ? getLoadData(key) : getLoadDataIfNull(key);
              return target;
            }

            @Override
            public ListenableFuture<V> reload(String key, V oldValue) throws Exception {
              ListenableFutureTask<V> task = ListenableFutureTask.create(new Callable<V>() {
                public V call() {
                  return getLoadData(key);
                }
              });
              executorService.execute(task);
              return task;
            }
          });
        }

      }
    }

    return cache;
  }

  /**
   * 调用getLoadData返回null值时自定义加载到内存的值
   *
   * @param key
   * @return
   */
  protected abstract V getLoadDataIfNull(String key);

  /**
   * 返回加载到内存中的数据，一般从数据库中加载
   *
   * @param key
   * @return V
   */
  protected abstract V getLoadData(String key);

  /**
   * 清除缓存(可以批量清除，也可以清除全部)
   *
   * @param keys 需要清除缓存的key值
   */
  public void batchInvalidate(List<String> keys) {
    if (keys != null) {
      getCache().invalidateAll(keys);
      logger.info("批量清除缓存, keys为：{}", keys);
    } else {
      getCache().invalidateAll();
      logger.info("清除了所有缓存");
    }
  }

  /**
   * 清除某个key的缓存
   */
  public void invalidateOne(String key) {
    getCache().invalidate(key);
    logger.info("清除了guava cache中的缓存, key为：{}", key);
  }

  /**
   * 写入缓存
   *
   * @param key   键
   * @param value 键对应的值
   */
  public void putIntoCache(String key, V value) {
    getCache().put(key, value);
  }

  /**
   * 获取某个key对应的缓存
   *
   * @param key
   * @return V
   */
  public V getCacheValue(String key) {
    V cacheValue = null;
    try {
      cacheValue = getCache().get(key);
    } catch (ExecutionException e) {
      logger.error("获取guava cache中的缓存值出错, {}");
    }
    return cacheValue;
  }
}
