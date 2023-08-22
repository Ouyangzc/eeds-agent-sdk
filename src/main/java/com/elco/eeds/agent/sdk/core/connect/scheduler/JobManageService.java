package com.elco.eeds.agent.sdk.core.connect.scheduler;


import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.elco.eeds.agent.sdk.core.common.constant.ReadTypeEnums;
import com.elco.eeds.agent.sdk.core.connect.ThingsConnectionHandler;
import com.elco.eeds.agent.sdk.transfer.quartz.CmdTimeoutJob;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * @Description 任务管理实现
 * @Author LCXU
 * @Date 2020/12/25 11:24
 * @Version 1.0
 */

public class JobManageService implements IJobManageService {
    public static final Logger logger = LoggerFactory.getLogger(JobManageService.class);

    public JobManageService(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    private Scheduler scheduler;

    /**
     * 添加任务
     *
     * @param sysTask
     * @return
     */
    @Override
    public boolean addJob(SysTask sysTask) throws ClassNotFoundException, SchedulerException {

        Date startDate = sysTask.getBeginTime();

        if (!CronExpression.isValidExpression(sysTask.getCron())) {
            throw new RuntimeException("表达式不正确");   //表达式格式不正确
        }
        JobDetail jobDetail = null;
        Class jobClass = Class.forName(sysTask.getClazzName());
        jobDetail = JobBuilder.newJob(SchedulerJob.class).withIdentity(sysTask.getJobName(), sysTask.getJobGroup()).build();
        //表达式调度构建器(即任务执行的时间,不立即执行)
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(sysTask.getCron()).withMisfireHandlingInstructionDoNothing();
        //按新的cronExpression表达式构建一个新的trigger
        CronTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(sysTask.getJobName(), sysTask.getJobGroup())
                .startAt(startDate)
                .endAt(ObjectUtil.isNotEmpty(sysTask.getEndTime()) ? sysTask.getEndTime() : null)
                .withSchedule(scheduleBuilder).build();

        //传递参数 这里可以传递参数，在任务执行的时候可以获取参数

//        if(sysTask.getTask()!=null){
//            trigger.getJobDataMap().put("task", sysTask.getTask());
//        }

        if (sysTask.getParmas() != null && !"".equals(sysTask.getParmas())) {
            trigger.getJobDataMap().put("invokeParam", sysTask.getParmas());
            trigger.getJobDataMap().put("desc", sysTask.getJobDesc());
            trigger.getJobDataMap().put("type", sysTask.getJobType());
            trigger.getJobDataMap().put("taskDesc", sysTask.getTaskDesc());
        }
        scheduler.scheduleJob(jobDetail, trigger);
        // pauseJob(sysTask.getJobName(),sysTask.getJobGroup());
        return true;
    }

    @Override
    public boolean addJob(String cron, ThingsConnectionHandler handler, ReadTypeEnums enums) throws SchedulerException {
        String jobName = "read_job_" + System.currentTimeMillis();
        String jobGroup = "read_job_group";
        Date startDate = new Date();


        JobDetail jobDetail = null;
        jobDetail = JobBuilder.newJob(SchedulerJob.class).withIdentity(jobName, jobGroup).build();
        Trigger trigger;
        if (enums.equals(ReadTypeEnums.PASSIVE_CORN)) {
            if (!CronExpression.isValidExpression(cron)) {
                throw new RuntimeException("表达式不正确");   //表达式格式不正确
            }
            //表达式调度构建器(即任务执行的时间,不立即执行)
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron).withMisfireHandlingInstructionDoNothing();
            trigger = TriggerBuilder.newTrigger()
                    .withIdentity(jobName, jobGroup)
                    .startAt(startDate)
                    .endAt(null)
                    .withSchedule(scheduleBuilder).build();
        } else {
            trigger = TriggerBuilder.newTrigger().withIdentity(jobName, jobGroup).startAt(startDate)
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMilliseconds(Integer.parseInt(cron))
                            .withRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY)).build();
        }
        //SimpleTrigger.REPEAT_INDEFINITELY
        //表达式调度构建器(即任务执行的时间,不立即执行)
//        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron).withMisfireHandlingInstructionDoNothing();
        //按新的cronExpression表达式构建一个新的trigger
//        CronTrigger trigger = TriggerBuilder.newTrigger()
//                .withIdentity(jobName,jobGroup)
//                .startAt(startDate)
//                .endAt(null)
//                .withSchedule(scheduleBuilder).build();

        trigger.getJobDataMap().put("handler", handler);
        //传递参数 这里可以传递参数，在任务执行的时候可以获取参数

//        if(sysTask.getTask()!=null){
//            trigger.getJobDataMap().put("task", sysTask.getTask());
//        }

//        if (sysTask.getParmas() != null && !"".equals(sysTask.getParmas())) {
//            trigger.getJobDataMap().put("invokeParam", sysTask.getParmas());
//            trigger.getJobDataMap().put("desc", sysTask.getJobDesc());
//            trigger.getJobDataMap().put("type", sysTask.getJobType());
//            trigger.getJobDataMap().put("taskDesc", sysTask.getTaskDesc());
//        }
        scheduler.scheduleJob(jobDetail, trigger);
        // pauseJob(sysTask.getJobName(),sysTask.getJobGroup());
        return true;
    }


    /**
     * 获取任务状态
     *
     * @param jobName
     * @param jobGroup
     * @return
     * @throws SchedulerException
     */
    @Override
    public String getJobState(String jobName, String jobGroup) throws SchedulerException {
        TriggerKey triggerKey = new TriggerKey(jobName, jobGroup);
        return scheduler.getTriggerState(triggerKey).name();
    }

    @Override
    public JobDetail getJobDetail(String jobName, String jobGroup) throws SchedulerException {
        JobKey jobKey = new JobKey(jobName, jobGroup);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        return jobDetail;
    }

    /**
     * 暂停所有任务
     *
     * @return
     * @throws SchedulerException
     */
    @Override
    public boolean pauseAllJob() throws SchedulerException {
        scheduler.pauseAll();
        return true;
    }


    /**
     * 暂停任务
     *
     * @param jobName
     * @param jobGroup
     * @return
     * @throws SchedulerException
     */
    @Override
    public boolean pauseJob(String jobName, String jobGroup) throws SchedulerException {
        JobKey jobKey = new JobKey(jobName, jobGroup);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null) {
            return false;
        } else {
            scheduler.pauseJob(jobKey);
            return true;
        }

    }

    /**
     * 回复所有任务
     *
     * @return
     * @throws SchedulerException
     */
    @Override
    public boolean resumeAllJob() throws SchedulerException {
        scheduler.resumeAll();
        return true;
    }

    /**
     * 回复任务
     *
     * @param jobName
     * @param jobGroup
     * @return
     * @throws SchedulerException
     */
    @Override
    public boolean resumeJob(String jobName, String jobGroup) throws SchedulerException {
        JobKey jobKey = new JobKey(jobName, jobGroup);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null) {
            return false;
        } else {
            scheduler.resumeJob(jobKey);
            return true;
        }
    }

    /**
     * 删除任务
     *
     * @param sysTask
     * @return
     * @throws SchedulerException
     */
    @Override
    public boolean deleteJob(SysTask sysTask) throws SchedulerException {
        JobKey jobKey = new JobKey(sysTask.getJobName(), sysTask.getJobGroup());
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null) {
//            log.error("jobDetail is null");
            return false;
        } else if (!scheduler.checkExists(jobKey)) {
//            log.error("jobKey is not exists");
            return false;
        } else {
            scheduler.deleteJob(jobKey);
            return true;
        }

    }

    /**
     * 修改任务
     *
     * @param sysTask
     * @return
     * @throws Exception
     */
    @Override
    public boolean modifyJob(SysTask sysTask) throws Exception {
        if (!CronExpression.isValidExpression(sysTask.getCron())) {
            throw new Exception("Illegal cron expression");
        }
        TriggerKey triggerKey = TriggerKey.triggerKey(sysTask.getJobName(), sysTask.getJobGroup());
        JobKey jobKey = new JobKey(sysTask.getJobName(), sysTask.getJobGroup());
        if (scheduler.checkExists(jobKey) && scheduler.checkExists(triggerKey)) {
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            //表达式调度构建器,不立即执行
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(sysTask.getCron()).withMisfireHandlingInstructionDoNothing();
            //按新的cronExpression表达式重新构建trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey)
                    .withSchedule(scheduleBuilder).build();
            //修改参数
            if (!trigger.getJobDataMap().get("invokeParam").equals(sysTask.getParmas())) {
                trigger.getJobDataMap().put("invokeParam", sysTask.getParmas());
            }
            //按新的trigger重新设置job执行
            scheduler.rescheduleJob(triggerKey, trigger);
            return true;
        } else {
//            log.error("job or trigger not exists");
            return false;
        }

    }

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
            System.out.println(date);
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
