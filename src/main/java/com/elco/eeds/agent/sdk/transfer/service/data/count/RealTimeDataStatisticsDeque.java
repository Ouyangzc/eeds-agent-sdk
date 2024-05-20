package com.elco.eeds.agent.sdk.transfer.service.data.count;

import com.elco.eeds.agent.sdk.core.bean.RealTimeData;
import com.elco.eeds.agent.sdk.core.bean.properties.PropertiesValue;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName RealTimeDataStatistics
 * @Description 实时数据统计队列
 * @Author OuYang
 * @Date 2024/5/16 13:10
 * @Version 1.0
 */
public class RealTimeDataStatisticsDeque {
  private Deque<RealTimeData> dataDeque = new ConcurrentLinkedDeque<>();

  public static AtomicLong collectionStartTime = new AtomicLong(System.currentTimeMillis());

  private static RealTimeDataStatisticsDeque instance;

  public static RealTimeDataStatisticsDeque getInstance() {
    if (instance == null) {
      synchronized (RealTimeDataStatisticsDeque.class) {
        if (instance == null) {
          instance = new RealTimeDataStatisticsDeque();
        }
      }
    }
    return instance;
  }

  private final Lock lock = new ReentrantLock();

  public  void addRealTimeData(long timestamp, PropertiesValue pv) {
    lock.lock();
    try {
      RealTimeData newData = new RealTimeData(timestamp, pv);
      dataDeque.addLast(newData);
      if (collectionStartTime.get()==0){
        collectionStartTime.set(timestamp);
      }
    }finally {
      lock.unlock();
    }
  }

  public  List<RealTimeData> getCountInTimeRange(long startTime, long endTime) {
    List<RealTimeData> rangeDataList = new ArrayList<>();
    lock.lock();
    try {
      for (RealTimeData data:dataDeque){
        if (data.getTimestamp() >= startTime && data.getTimestamp() <= endTime){
          rangeDataList.add(data);
        }
      }
    }finally {
      lock.unlock();
    }
    return rangeDataList;
  }

  public void cleanOldData(long currentTimestamp) {
    while (!dataDeque.isEmpty() && dataDeque.peekFirst().getTimestamp() < currentTimestamp) {
      dataDeque.pollFirst();
    }
  }



}
