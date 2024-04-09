package com.elco.eeds.agent.sdk.core.disruptor;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @ClassName DisruptorThreadFactory
 * @Description 自定义线程池
 * @Author OuYang
 * @Date 2024/4/7 10:53
 * @Version 1.0
 */
public class DisruptorThreadFactory implements ThreadFactory {

  public static final String DISRUPTOR_THREAD_GROUP = "disruptor";

  public static final String DISRUPTOR_THREAD_MAIN = "Disruptor Main-";

  public static final String DISRUPTOR_THREAD_PRODUCER = "Disruptor Producer-";

  public static final String DISRUPTOR_THREAD_CONSUMER = "Disruptor Consumer-";

  private static final AtomicLong THREAD_NUMBER = new AtomicLong(1);


  private static final ThreadGroup THREAD_GROUP = new ThreadGroup(DISRUPTOR_THREAD_GROUP);
  /**
   * 后台线程
   */
  private static volatile boolean daemon;
  /**
   * 线程名：前缀
   */
  private final String namePrefix;

  private DisruptorThreadFactory(final String namePrefix, final boolean daemon) {
    this.namePrefix = namePrefix;
    DisruptorThreadFactory.daemon = daemon;
  }

  @Override
  public Thread newThread(Runnable r) {
    Thread thread = new Thread(THREAD_GROUP, r,
        THREAD_GROUP.getName() + "-" + namePrefix + "-" + THREAD_NUMBER.getAndIncrement());
    thread.setDaemon(daemon);
    if (thread.getPriority() != Thread.NORM_PRIORITY) {
      thread.setPriority(Thread.NORM_PRIORITY);
    }
    return thread;
  }

  public static ThreadFactory create(final String namePrefix, final boolean daemon) {
    return new DisruptorThreadFactory(namePrefix, daemon);
  }
}
