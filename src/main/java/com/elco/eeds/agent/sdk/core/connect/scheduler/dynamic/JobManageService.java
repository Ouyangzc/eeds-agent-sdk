package com.elco.eeds.agent.sdk.core.connect.scheduler.dynamic;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.elco.eeds.agent.sdk.core.common.constant.ReadTypeEnums;
import com.elco.eeds.agent.sdk.transfer.quartz.CmdTimeoutJob;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.quartz.TriggerBuilder.newTrigger;

/**
 * @ClassName JobManageService
 * @Description 任务管理实现
 * @Author OuYang
 * @Date 2023/7/12 16:25
 * @Version 1.0
 */
public class JobManageService implements IJobManageService {

    private static final Logger logger = LoggerFactory.getLogger(JobManageService.class);

    private Scheduler scheduler;

    public JobManageService(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public void addJob(SysJob sysJob) throws Exception {
        String jobName = sysJob.getJobName();
        String jobGroup = sysJob.getJobGroup().getValue();
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
        Trigger oldTrigger = scheduler.getTrigger(triggerKey);
        if (null != oldTrigger) {
            //执行修改逻辑
            modifyJob(sysJob);
        } else {
            logger.info("新增定时任务,任务信息:{}", sysJob);
            //执行新增逻辑
            ReadTypeEnums readTypeEnums = sysJob.getReadTypeEnums();

            //定义一个JobDetail
            JobDetail jobDetail = null;
            jobDetail = JobBuilder.newJob(SysSchedulerJob.class)
                    .withIdentity(jobName, jobGroup)
                    .build();

            DateTime startDate = DateUtil.date();

            String cron = sysJob.getCron();
            //构建触发器
            Trigger trigger;
            if (readTypeEnums.equals(ReadTypeEnums.PASSIVE_CORN)) {

                if (!CronExpression.isValidExpression(cron)) {
                    //表达式格式不正确
                    throw new RuntimeException("表达式不正确");
                }
                //corn表达式调度构建器
                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron).withMisfireHandlingInstructionDoNothing();
                trigger = newTrigger()
                        .withIdentity(jobName, jobGroup)
                        .startAt(startDate)
                        .endAt(null)
                        .withSchedule(scheduleBuilder).build();
            } else {
                //固定间隔表达式调度构建器(
                SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule().withIntervalInMilliseconds(Integer.parseInt(cron))
                        .withRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
                trigger = newTrigger().withIdentity(jobName, jobGroup).startAt(startDate)
                        .withSchedule(scheduleBuilder).build();
            }
            trigger.getJobDataMap().put("job", sysJob);
            scheduler.scheduleJob(jobDetail, trigger);
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
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (null != jobDetail) {
            scheduler.deleteJob(jobKey);
        }
    }

    @Override
    public void modifyJob(SysJob sysJob) throws Exception {
        logger.info("修改定时任务,任务信息:{}", sysJob);
        String jobName = sysJob.getJobName();
        String jobGroup = sysJob.getJobGroup().getValue();

        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
        Trigger oldTrigger = scheduler.getTrigger(triggerKey);
        if (null == oldTrigger) {
            logger.error("修改定时任务,未找到该任务的Trigger,任务信息:{}",sysJob);
            return;
        }
        //构建新的Trigger
        String cron = sysJob.getCron();
        DateTime startDate = DateUtil.date();
        ReadTypeEnums readTypeEnums = sysJob.getReadTypeEnums();
        TriggerBuilder<Trigger> triggerBuilder = newTrigger();
        if (readTypeEnums.equals(ReadTypeEnums.PASSIVE_CORN)) {
            if (!CronExpression.isValidExpression(cron)) {
                throw new Exception("Illegal cron expression");
            }
            //corn表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron).withMisfireHandlingInstructionDoNothing();
            triggerBuilder
                    .withIdentity(jobName, jobGroup)
                    .startAt(startDate)
                    .endAt(null)
                    .withSchedule(scheduleBuilder).build();
        } else {
            //固定间隔表达式调度构建器(
            SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule().withIntervalInMilliseconds(Integer.parseInt(cron))
                    .withRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
            triggerBuilder.withIdentity(jobName, jobGroup)
                    .startAt(startDate)
                    .withSchedule(scheduleBuilder)
                    .build();
        }
        Trigger newTrigger = triggerBuilder.build();
        newTrigger.getJobDataMap().put("job", sysJob);
        //按新的trigger重新设置job执行
        scheduler.rescheduleJob(triggerKey, newTrigger);
    }

    @Override
    public boolean addCmdTimeOutJob(String msgSeqNo, String thingsId, Integer timeout) {
        try {
            JobDataMap dataMap = new JobDataMap();
            dataMap.put("msgSeqNo", msgSeqNo);
            dataMap.put("thingsId", thingsId);
            JobDetail jobDetail = JobBuilder.newJob(CmdTimeoutJob.class)
                    .withIdentity(msgSeqNo, msgSeqNo)
                    .setJobData(dataMap)
                    .build();
            DateTime date = DateUtil.offsetSecond(DateUtil.date(), timeout);
            // 定义触发器, 会马上执行一次, 接着5秒执行一次
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(msgSeqNo, msgSeqNo)
                    .startAt(date)
                    .build();
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            logger.error("定时任务,添加任务失败,流水号:{},所属任务:{},错误信息:{}", msgSeqNo, "指令下发超时任务", e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean removeCmdTimeOutJob(String msgSeqNo) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(msgSeqNo, msgSeqNo);
            Trigger trigger = scheduler.getTrigger(triggerKey);
            if (trigger == null) {
                return false;
            }
            scheduler.pauseTrigger(triggerKey);
            scheduler.unscheduleJob(triggerKey);
            scheduler.deleteJob(JobKey.jobKey(msgSeqNo, msgSeqNo));
        } catch (SchedulerException e) {
            logger.error("定时任务,移除任务失败,流水号:{},所属任务:{},错误信息:{}", msgSeqNo, "指令下发超时任务", e.getMessage());
            return false;
        }
        logger.info("定时任务,移除任务成功,流水号:{}", msgSeqNo);
        return true;
    }
}
