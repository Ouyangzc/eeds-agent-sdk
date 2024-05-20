package com.elco.eeds.agent.sdk.transfer.service.data.count;

import com.elco.eeds.agent.sdk.core.bean.RealTimeData;
import com.elco.eeds.agent.sdk.core.bean.agent.Agent;
import com.elco.eeds.agent.sdk.transfer.beans.data.count.PostDataCount;
import com.elco.eeds.agent.sdk.transfer.beans.data.count.ThingsDataCount;
import com.google.common.collect.Range;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @ClassName RealTimeDataRange
 * @Description 实时数据统计范围
 * @Author OuYang
 * @Date 2024/5/16 15:11
 * @Version 1.0
 */
public class RealTimeDataStatistics {

  /**
   * 序列号
   */
  public static AtomicInteger countNum = new AtomicInteger(0);

  /**
   * 统计集合
   */
  Map<Range, PostDataCount> dataCountMap = new ConcurrentHashMap<>();



  private static RealTimeDataStatistics instance;

  public static RealTimeDataStatistics getInstance() {
    if (instance == null) {
      synchronized (RealTimeDataStatistics.class) {
        if (instance == null) {
          instance = new RealTimeDataStatistics();
        }
      }
    }
    return instance;
  }

  /**
   * 创建统计区间
   *
   * @param startTime
   * @param endTime
   */
  public void creatRange(Long startTime, Long endTime) {
    Range<Long> range = Range.closedOpen(startTime, endTime);
    //创建一个新的区间
    PostDataCount count = new PostDataCount();
    int num = countNum.incrementAndGet();
    String localAgentId = Agent.getInstance().getAgentBaseInfo().getAgentId();
    String countId = localAgentId + num + System.currentTimeMillis();
    count.setAgentId(Long.valueOf(localAgentId));
    count.setCountId(countId);
    count.setStartTime(startTime);
    count.setEndTime(endTime);
    ArrayList<ThingsDataCount> thingsDataCountList = new ArrayList<>();
    count.setThingsCountList(thingsDataCountList);
    dataCountMap.put(range, count);
  }


  public void scanRealTimeCache() {
    for (Range<Long> range : dataCountMap.keySet()) {
      Long lowerEndpoint = range.lowerEndpoint();
      Long upperEndpoint = range.upperEndpoint();
      List<RealTimeData> dataList = RealTimeDataStatisticsDeque.getInstance()
          .getCountInTimeRange(range.lowerEndpoint(), range.upperEndpoint());
      //根据数据源进行分组
      Map<String, List<RealTimeData>> thingsRealTimeDataMap = dataList.stream()
          .collect(Collectors.groupingBy(data -> data.getPropertiesValue().getThingsId()));

      PostDataCount postDataCount = dataCountMap.get(range);
      List<ThingsDataCount> thingsCountList = postDataCount.getThingsCountList();
      for (String thingsId : thingsRealTimeDataMap.keySet()) {

        List<RealTimeData> realTimeDataList = thingsRealTimeDataMap.get(thingsId);
        Optional<ThingsDataCount> dataOptional = thingsCountList.stream()
            .filter(count -> count.getThingsId().equals(thingsId)).findFirst();
        if (dataOptional.isPresent()) {
          //存在
          ThingsDataCount count = dataOptional.get();
          count.setSize(realTimeDataList.size());
        } else {
          //不存在则加入该队列
          ThingsDataCount thingsDataCount = new ThingsDataCount();
          thingsDataCount.setThingsId(thingsId);
          thingsDataCount.setSize(realTimeDataList.size());
          thingsDataCount.setStartTime(lowerEndpoint);
          thingsDataCount.setEndTime(upperEndpoint);
          thingsCountList.add(thingsDataCount);
        }

      }
    }
  }

}
