package com.elco.eeds.agent.sdk.transfer.service.data.count;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.elco.eeds.agent.sdk.core.bean.agent.Agent;
import com.elco.eeds.agent.sdk.core.bean.agent.AgentBaseInfo;
import com.elco.eeds.agent.sdk.core.common.constant.ConstantCount;
import com.elco.eeds.agent.sdk.core.util.DateUtils;
import com.elco.eeds.agent.sdk.core.util.JSONUtils;
import com.elco.eeds.agent.sdk.core.util.MqPluginUtils;
import com.elco.eeds.agent.sdk.transfer.beans.data.count.PostDataCount;
import com.elco.eeds.agent.sdk.transfer.beans.data.count.ThingsDataCount;
import com.elco.eeds.agent.sdk.transfer.beans.message.data.count.post.DataCountMessage;
import com.google.common.collect.Range;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName DataCountServiceImpl
 * @Description 数据统计接口
 * @Author OUYANG
 * @Date 2022/12/9 10:24
 */
public class DataCountServiceImpl implements DataCountService {

  public static final Logger logger = LoggerFactory.getLogger(DataCountServiceImpl.class);

  public static final int RECURSION_NUM = 3;

  private static CountDataHolder countDataHolder = new CountDataHolder();

  public static Map<Long, PostDataCount> thingsDataCountMap = new ConcurrentHashMap<>();

  public static AtomicLong endTime = new AtomicLong(0L);

  //序列号
  public static AtomicInteger countNum = new AtomicInteger(0);

  public static CountDataHolder getCountFile() {
    try {
      List<PostDataCount> fileData = countDataHolder.getCountDataFormFile();
      if (fileData == null) {
        return null;
      }
      //根据状态划分
      Map<String, List<PostDataCount>> statusPostDataCounts = fileData.stream()
          .collect(Collectors.groupingBy(PostDataCount::getStatus));
      Set<String> keySet = statusPostDataCounts.keySet();
      for (String key : keySet) {
        List<PostDataCount> postDataCounts = statusPostDataCounts.get(key);
        if (ConstantCount.STATUS_UN_SENT.equals(key)) {
          //未发送数据
          countDataHolder.setWaitingCountDatas(postDataCounts);
        } else if (ConstantCount.STATUS_SENTING.equals(key)) {
          //已发送数据
          PostDataCount dataCount = postDataCounts.get(0);
          countDataHolder.setDoPostData(dataCount);
        }
      }
      return countDataHolder;
    } catch (IOException e) {
      logger.error("统计,读取统计文件错误，错误信息:", e);
    }
    return null;
  }


  public static void recRealTimeData(String agentId, Long collectTime,
      ThingsDataCount thingsDataCount, int createCount) {
    try {
      Set<Long> keySet = thingsDataCountMap.keySet();
      Boolean flag = true;
      for (Long key : keySet) {
        PostDataCount postDataCount = thingsDataCountMap.get(key);
        Long startTime = postDataCount.getStartTime();
        Long currentEndTime = postDataCount.getEndTime();
        if (startTime <= collectTime && collectTime < currentEndTime) {
          addThingsDataCountToPostDataCount(postDataCount, thingsDataCount);
          flag = false;
          break;
        }
      }
      if (flag) {
        // 添加计数器限制递归次数为3
        if (createCount > RECURSION_NUM) {
          logger.warn("数据统计: 未找到统计区间，递归次数超限,统计信息:{},统计区间集合:{}",
              JSONUtils.toJsonStr(thingsDataCount),
              JSONUtil.toJsonStr(thingsDataCountMap.keySet()));
          return;
        }
        try {
          //创建新统计区间
          createCountMapSection(collectTime);
          //重新插入统计区间
          recRealTimeData(null, collectTime, thingsDataCount, createCount + 1);
        } catch (Exception e) {
          logger.error("创建新统计区间发生异常，异常信息:{}", e.getMessage());
        }
      }
    } catch (Exception e) {
      logger.error("统计实时数据出现异常，统计数据源信息:{},统计区间信息:{},异常信息:{}",
          JSONUtil.toJsonStr(thingsDataCount), JSONUtil.toJsonStr(thingsDataCountMap.keySet()), e);
      e.printStackTrace();
    } catch (Error error) {
      logger.error("统计实时数据出现异常，统计数据源信息:{},统计区间信息:{},异常信息:{}",
          JSONUtil.toJsonStr(thingsDataCount), JSONUtil.toJsonStr(thingsDataCountMap.keySet()),
          error);
    }
  }

  @Override
  public PostDataCount getPostCountData() {
    return countDataHolder.getDoPostData();
  }


  public static void sentCountData() {
    PostDataCount doPostData = null;
    try {
      doPostData = countDataHolder.getDoPostData();
      if (null != doPostData) {
        doPostData.setStatus(ConstantCount.STATUS_SENTING);
        countDataHolder.overrideCountDataToFile(doPostData);
      }
    } catch (Exception e) {
      logger.error("统计记录--发送--状态变更存储文件异常，信息:{},统计数据：{}", e,doPostData);
    }
    if (null != doPostData) {
      String agentId = Agent.getInstance().getAgentBaseInfo().getAgentId();
      String topic = DataCountMessage.getTopic(agentId);
      String msg = DataCountMessage.getMsg(doPostData);
      MqPluginUtils.sendThingsDataCountMsg(topic, msg);
      logger.info("发送统计报文，主题:{}，消息内容:{}", topic, msg);
    }
  }

  /**
   * 保存统计数据
   *
   * @param doneData 统计记录
   */

  public static void saveDoneCountData(PostDataCount doneData) {
    //将完成统计移动到统计完成文件中
    countDataHolder.moveCountDataToDoneFile(doneData);
    //移除当前，并设置下一个等待统计为要发送状态
    PostDataCount lastPostData = countDataHolder.getLastPostData();
    countDataHolder.setDoPostData(lastPostData);
  }


  /**
   * 初始化
   */
  public static void setUp() {
    CountDataHolder countDataHolder = getCountFile();
    if (null != countDataHolder) {
      PostDataCount doPostData = countDataHolder.getDoPostData();
      if (null == doPostData) {
        //没有要发送的数据
        countDataHolder.setLastDataTODoPostData();
      }

    }
  }

  /**
   * 收到确认报文
   *
   * @param countId
   */
  public static void recConfirmMsg(String countId) {
    PostDataCount postData = countDataHolder.getDoPostData();
    if (null != postData && countId.equals(postData.getCountId())) {
      //该统计，更改状态
      postData.setStatus(ConstantCount.STATUS_DONE);
      //移动到完成文件中
      saveDoneCountData(postData);
      //执行下一个任务
      sentCountData();
    }
  }

  /**
   * 创建统计区间
   */
  public static void createCountMapSection(Long collectTime) {
    Long countStartTime = null;
    if (endTime.get() == 0L) {
      countStartTime = DateUtils.getTimestamp();
    } else {
      countStartTime = endTime.get();
    }
    AgentBaseInfo agentBaseInfo = Agent.getInstance().getAgentBaseInfo();
    String localAgentId = agentBaseInfo.getAgentId();
    Long period = Long.valueOf(agentBaseInfo.getSyncPeriod());
    //Calculate number of periods to create
    long countEndTime = countStartTime + period;
    int periodsNums;
    if (null == collectTime) {
      periodsNums = 1;
    } else {
      periodsNums = (int) Math.ceil((collectTime - countEndTime) / (double) period);
    }
    synchronized ("lock") {
      for (int i = 0; i < periodsNums; i++) {
        if (!thingsDataCountMap.containsKey(countEndTime)) {
          //创建一个新的区间
          PostDataCount count = new PostDataCount();
          int num = countNum.incrementAndGet();
          String countId = localAgentId + num + System.currentTimeMillis();
          count.setAgentId(Long.valueOf(localAgentId));
          count.setCountId(countId);
          count.setStartTime(countStartTime);
          count.setEndTime(countEndTime);
          count.setThingsCountList(null);
          thingsDataCountMap.put(countEndTime, count);
          endTime.set(countEndTime);
        }
        // Move to the next period
        countStartTime = countEndTime;
        countEndTime += period;
      }

    }

  }

  public static void scheduledCheckCurrentCountingData() {
    try {
      Long nowTimestamp = DateUtils.getTimestamp();
      Map<Range, PostDataCount> dataCountMap = RealTimeDataStatistics.getInstance().dataCountMap;
      if (dataCountMap != null) {
        Iterator<Entry<Range, PostDataCount>> iterator = dataCountMap.entrySet()
            .iterator();
        while (iterator.hasNext()) {
          Entry<Range, PostDataCount> dataCountEntry = iterator.next();
          PostDataCount postDataCount = dataCountEntry.getValue();
          Range<Long> range = dataCountEntry.getKey();
          List<ThingsDataCount> thingsCountList = postDataCount.getThingsCountList();
          if (!ObjectUtil.isEmpty(thingsCountList)) {
            Long countEndTime = postDataCount.getEndTime();
            Long period = Long.valueOf(Agent.getInstance().getAgentBaseInfo().getSyncPeriod()) * 2;
            if (nowTimestamp - countEndTime > period) {
              postDataCount.setStatus(ConstantCount.STATUS_UN_SENT);
              try {
                logger.info("统计记录--生成--追加写到文件，信息:{}", postDataCount);
                countDataHolder.countDataAppendToFile(postDataCount);
              } catch (IOException e) {
                logger.error("统计记录--未发送--追加写到文件,发生异常，信息:", e);
              }
              //删除该统计记录
              iterator.remove();
              RealTimeDataStatisticsDeque.getInstance().cleanOldData(range.upperEndpoint());
              //设置要发送记录
              countDataHolder.addPostCountDataToWait(postDataCount);
              PostDataCount doPostData = countDataHolder.getDoPostData();
              if (null == doPostData) {
                countDataHolder.setLastDataTODoPostData();
              }
            }
          }
        }
      }
    } catch (Exception e) {
      logger.error("定时任务结束统计发生异常，信息:", e);
      e.printStackTrace();
    }

  }

  /**
   * 定时任务检查当前统计队列:旧
   */
  public static void scheduledCheckCurrentCountingData2() {
    try {
      Long nowTimestamp = DateUtils.getTimestamp();
      if (thingsDataCountMap != null) {
        Iterator<Map.Entry<Long, PostDataCount>> iterator = thingsDataCountMap.entrySet()
            .iterator();
        while (iterator.hasNext()) {
          Map.Entry<Long, PostDataCount> dataCountEntry = iterator.next();
          PostDataCount postDataCount = dataCountEntry.getValue();
          List<ThingsDataCount> thingsCountList = postDataCount.getThingsCountList();
          if (!ObjectUtil.isEmpty(thingsCountList)) {
            Long countEndTime = postDataCount.getEndTime();
            Long period = Long.valueOf(Agent.getInstance().getAgentBaseInfo().getSyncPeriod()) * 2;
            if (nowTimestamp - countEndTime > period) {
              postDataCount.setStatus(ConstantCount.STATUS_UN_SENT);
              try {
                logger.info("统计记录--生成--追加写到文件，信息:{}", postDataCount);
                countDataHolder.countDataAppendToFile(postDataCount);
              } catch (IOException e) {
                logger.error("统计记录--未发送--追加写到文件,发生异常，信息:", e);
              }
              //删除该统计记录
              iterator.remove();
              //设置要发送记录
              countDataHolder.addPostCountDataToWait(postDataCount);
              PostDataCount doPostData = countDataHolder.getDoPostData();
              if (null == doPostData) {
                countDataHolder.setLastDataTODoPostData();
              }
            }
          }
        }
      }
    } catch (Exception e) {
      logger.error("定时任务结束统计发生异常，信息:", e);
      e.printStackTrace();
    }

  }

  public static void addThingsDataCountToPostDataCount(PostDataCount postDataCount,
      ThingsDataCount thingsDataCount) {
    synchronized ("lockNum") {
      List<ThingsDataCount> thingsCountList = postDataCount.getThingsCountList();
      Long startTime = postDataCount.getStartTime();
      Long currentEndTime = postDataCount.getEndTime();
      String thingsId = thingsDataCount.getThingsId();
      if (ObjectUtil.isEmpty(thingsCountList)) {
        thingsDataCount.setStartTime(startTime);
        thingsDataCount.setEndTime(currentEndTime);
        List<ThingsDataCount> dataCounts = new ArrayList<>();
        dataCounts.add(thingsDataCount);
        postDataCount.setThingsCountList(dataCounts);
      } else {
        Optional<ThingsDataCount> dataOptional = thingsCountList.stream()
            .filter(count -> count.getThingsId().equals(thingsId)).findFirst();
        if (dataOptional.isPresent()) {
          //存在
          ThingsDataCount count = dataOptional.get();
          Integer currentSize = count.getSize();
          Integer size = thingsDataCount.getSize();
          int sumSize = new AtomicInteger(currentSize).addAndGet(size);
          count.setSize(sumSize);
        } else {
          //不存在则加入该队列
          thingsDataCount.setStartTime(startTime);
          thingsDataCount.setEndTime(currentEndTime);
          thingsCountList.add(thingsDataCount);
        }
      }
    }
  }
}
