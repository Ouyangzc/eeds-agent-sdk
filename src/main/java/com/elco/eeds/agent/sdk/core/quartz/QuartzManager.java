package com.elco.eeds.agent.sdk.core.quartz;

import static org.quartz.TriggerBuilder.newTrigger;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.elco.eeds.agent.sdk.core.common.constant.ReadTypeEnums;
import com.elco.eeds.agent.sdk.core.quartz.bean.CmdTimeOutBean;
import com.elco.eeds.agent.sdk.core.quartz.bean.CmdTimeOutBean.CmdTimeOutBeanBaseBuilder;
import com.elco.eeds.agent.sdk.core.quartz.bean.DataCountBean;
import com.elco.eeds.agent.sdk.core.quartz.bean.ExpireDataFileBean;
import com.elco.eeds.agent.sdk.core.quartz.bean.RealTimeReadBean;
import com.elco.eeds.agent.sdk.core.quartz.bean.RealTimeReadBean.RealTimeReadBeanBuilder;
import com.elco.eeds.agent.sdk.core.quartz.job.CmdTimeoutJob;
import com.elco.eeds.agent.sdk.core.quartz.job.RealTimeReadJob;
import com.elco.eeds.agent.sdk.core.quartz.job.CountJob;
import com.elco.eeds.agent.sdk.core.quartz.job.DataFileJob;
import java.util.Map;
import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName QuartzManager
 * @Description quartz管理类
 * @Author OuYang
 * @Date 2024/4/24 11:33
 * @Version 1.0
 */
public class QuartzManager {

  public static final Logger logger = LoggerFactory.getLogger(QuartzManager.class);
  private static IJobService jobManage;

  public QuartzManager() {
  }

  private void init() throws SchedulerException {
    StdSchedulerFactory schedulerFactory = new StdSchedulerFactory();
    jobManage = new IJobServiceImpl(schedulerFactory.getScheduler());
  }


  public void start() throws SchedulerException {
    init();
    jobManage.start();
  }
  public void load()throws SchedulerException{
    addDataCountJob();
    addDelExpireDataFileJob();
  }



  /**
   * 实时数据读取Job
   *
   * @param thingsId
   * @param readTypeEnums
   * @param cron
   * @param extraMap
   * @throws SchedulerException
   */
  public static void addRealTimePropertiesJob(String thingsId, ReadTypeEnums readTypeEnums,
      String cron, Map<String, Object> extraMap)
      throws SchedulerException {
    RealTimeReadBean bean = RealTimeReadBeanBuilder.create().thingsId(thingsId)
        .readTypeEnums(readTypeEnums).cron(cron).extraMap(extraMap).build();
    String jobName = bean.getJobName();
    String jobGroup = bean.getJobGroup();
    String jobCorn = bean.getCron();

    //定义一个JobDetail
    JobDetail jobDetail = JobBuilder.newJob(RealTimeReadJob.class)
        .withIdentity(jobName, jobGroup)
        .build();

    //构建触发器
    Trigger trigger;
    if (readTypeEnums.equals(ReadTypeEnums.PASSIVE_CORN)) {
      if (!CronExpression.isValidExpression(cron)) {
        //表达式格式不正确
        throw new RuntimeException("表达式不正确");
      }
      //corn表达式调度构建器
      CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(jobCorn)
          .withMisfireHandlingInstructionDoNothing();
      trigger = newTrigger()
          .withIdentity(jobName, jobGroup)
          .startAt(DateUtil.date())
          .endAt(null)
          .withSchedule(scheduleBuilder).build();
    } else {
      //固定间隔表达式调度构建器(
      SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
          .withIntervalInMilliseconds(Integer.parseInt(jobCorn))
          .withRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
      trigger = newTrigger().withIdentity(jobName, jobGroup).startAt(DateUtil.date())
          .withSchedule(scheduleBuilder).build();
    }
    trigger.getJobDataMap().put("job", bean);
    jobManage.addJob(jobDetail, trigger);
  }

  public static boolean addCmdTimeOutJob(String msgSeqNo, String thingsId, Integer timeout) {
    try {
      JobDataMap dataMap = new JobDataMap();
      dataMap.put("msgSeqNo", msgSeqNo);
      dataMap.put("thingsId", thingsId);

      CmdTimeOutBean bean = CmdTimeOutBeanBaseBuilder.create().thingsId(thingsId)
          .msgSeqNo(msgSeqNo).timeout(timeout).build();

      String jobGroup = bean.getJobGroup();
      String jobName = bean.getJobName();

      JobDetail jobDetail = JobBuilder.newJob(CmdTimeoutJob.class)
          .withIdentity(jobName, jobGroup)
          .setJobData(dataMap)
          .build();
      DateTime date = DateUtil.offsetSecond(DateUtil.date(), timeout);
      // 定义触发器, 会马上执行一次, 接着5秒执行一次
      Trigger trigger = TriggerBuilder.newTrigger()
          .withIdentity(jobName, jobGroup)
          .startAt(date)
          .build();
      jobManage.addJob(jobDetail, trigger);
    } catch (SchedulerException e) {
      logger.error("定时任务,添加任务失败,流水号:{},所属任务:{},错误信息:{}", msgSeqNo,
          "指令下发超时任务", e.getMessage());
      return false;
    }
    return true;
  }

  public static boolean removeCmdTimeOutJob(String thingsId, String msgSeqNo) {
    try {
      CmdTimeOutBean bean = CmdTimeOutBeanBaseBuilder.create().thingsId(thingsId)
          .msgSeqNo(msgSeqNo).build();
      String jobName = bean.getJobName();
      String jobGroup = bean.getJobGroup();
      jobManage.deleteJob(jobName, jobGroup);
    } catch (Exception e) {
      logger.error("定时任务,移除任务失败,流水号:{},所属任务:{},错误信息:{}", msgSeqNo,
          "指令下发超时任务", e.getMessage());
      return false;
    }
    logger.info("定时任务,移除任务成功,流水号:{}", msgSeqNo);
    return true;
  }

  /**
   * 数据统计定时任务
   * @throws SchedulerException
   */
  public void addDataCountJob() throws SchedulerException {
    DataCountBean bean = new DataCountBean();
    String jobName = bean.getJobName();
    String jobGroup = bean.getJobGroup();
    // 定义任务调度实例, 并与TestJob绑定
    JobDetail jobDetail = JobBuilder.newJob(CountJob.class)
        .withIdentity(jobName, jobGroup)
        .build();

    //同步周期
    Integer syncPeriod = Integer.valueOf(String.valueOf(bean.getCountPeriod()));

    // 定义触发器, 会马上执行一次, 接着5秒执行一次
    Trigger trigger = TriggerBuilder.newTrigger()
        .withIdentity(jobName, jobGroup)
        .startNow()
        .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(syncPeriod))
        .build();
    // 使用触发器调度任务的执行

    jobManage.addJob(jobDetail, trigger);
  }

  /**
   * 删除文件定时任务
   * @throws SchedulerException
   */
  public void addDelExpireDataFileJob() throws SchedulerException {
    ExpireDataFileBean bean = new ExpireDataFileBean();
    String jobName = bean.getJobName();
    String jobGroup = bean.getJobGroup();

    // 定义任务调度实例, 并与DataFileJob绑定
    JobDetail jobDetail = JobBuilder.newJob(DataFileJob.class)
        .withIdentity(jobName, jobGroup)
        .build();
    // 定义触发器, 会马上执行一次, 接着5秒执行一次
    Trigger trigger = TriggerBuilder.newTrigger()
        .withIdentity(jobName, jobGroup)
        .startNow()
        .withSchedule(SimpleScheduleBuilder.repeatHourlyForever(bean.getPeriod()))
//                .withSchedule(SimpleScheduleBuilder.repeatMinutelyForever(10))
        .build();
    // 使用触发器调度任务的执行
    jobManage.addJob(jobDetail, trigger);
  }


}
