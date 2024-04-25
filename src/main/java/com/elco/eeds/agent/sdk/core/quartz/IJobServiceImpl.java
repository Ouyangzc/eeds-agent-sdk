package com.elco.eeds.agent.sdk.core.quartz;

import static org.quartz.TriggerBuilder.newTrigger;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.elco.eeds.agent.sdk.core.util.ObjectsUtils;
import com.elco.eeds.core.utils.ObjectUtil;
import org.jetbrains.annotations.NotNull;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName IJobServiceImlp
 * @Description 任务接口实现类
 * @Author OuYang
 * @Date 2024/4/24 11:22
 * @Version 1.0
 */
public class IJobServiceImpl implements IJobService {

  private static final Logger logger = LoggerFactory.getLogger(IJobServiceImpl.class);

  private Scheduler scheduler;

  public IJobServiceImpl(Scheduler scheduler) {
    this.scheduler = scheduler;
  }

  @Override
  public void addJob(JobDetail jobDetail, Trigger jobTrigger) throws SchedulerException {
    TriggerKey key = jobTrigger.getKey();
    TriggerKey triggerKey = TriggerKey.triggerKey(key.getName(),key.getGroup());
    Trigger oldTrigger = scheduler.getTrigger(triggerKey);
    if(ObjectsUtils.isNull(oldTrigger)){
      scheduler.scheduleJob(jobDetail, jobTrigger);
    }else if (ObjectsUtils.notEqual(oldTrigger,jobTrigger)){
      //执行修改逻辑
      modifyJob(jobDetail, jobTrigger);
    }
  }


  @Override
  public void pauseJob(String jobName, String jobGroup) throws SchedulerException {
    logger.info("暂停定时任务,任务类型:{},任务ID；{}", jobGroup, jobName);
    JobKey jobKey = new JobKey(jobName, jobGroup);
    JobDetail jobDetail = scheduler.getJobDetail(jobKey);
    if (null != jobDetail) {
      scheduler.pauseJob(jobKey);
    }
  }

  @Override
  public void resumeJob(String jobName, String jobGroup) throws SchedulerException {
    logger.info("恢复定时任务,任务类型:{},任务ID；{}", jobGroup, jobName);
    JobKey jobKey = new JobKey(jobName, jobGroup);
    JobDetail jobDetail = scheduler.getJobDetail(jobKey);
    if (null != jobDetail) {
      scheduler.resumeJob(jobKey);
    }
  }

  @Override
  public void deleteJob(String jobName, String jobGroup) throws Exception {
    logger.info("删除定时任务,任务类型:{},任务ID；{}", jobGroup, jobName);
    JobKey jobKey = new JobKey(jobName, jobGroup);
    TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
    Trigger trigger = scheduler.getTrigger(triggerKey);
    if (trigger == null) {
      return ;
    }
    scheduler.pauseTrigger(triggerKey);
    scheduler.unscheduleJob(triggerKey);
    scheduler.deleteJob(jobKey);
  }

  public void modifyJob(JobDetail jobDetail, Trigger jobTrigger) throws SchedulerException {
    logger.info("修改定时任务");
    JobKey jobKey = jobTrigger.getJobKey();

    TriggerKey triggerKey = TriggerKey.triggerKey(jobKey.getName(), jobKey.getGroup());
    Trigger oldTrigger = scheduler.getTrigger(triggerKey);
    if (null == oldTrigger) {
      logger.error("修改定时任务,未找到该任务的Trigger,jobKey:{}", jobKey);
      return;
    }
    //构建新的Trigger
    DateTime startDate = DateUtil.date();
    TriggerBuilder<Trigger> triggerBuilder = newTrigger();
    triggerBuilder.withIdentity(jobKey.getName(), jobKey.getGroup())
        .startAt(startDate)
        .withSchedule(jobTrigger.getScheduleBuilder())
        .build();
    Trigger newTrigger = triggerBuilder.build();
    newTrigger.getJobDataMap().put("job", jobTrigger.getJobDataMap());
    //按新的trigger重新设置job执行
    scheduler.rescheduleJob(triggerKey, newTrigger);
  }

  public void start() throws SchedulerException {
    scheduler.start();
  }
}
