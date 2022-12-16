package com.elco.eeds.agent.sdk.transfer.quartz;

import com.elco.eeds.agent.sdk.core.bean.agent.Agent;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

/**
 * @ClassName CountScheduler
 * @Description 统计定时任务
 * @Author OUYANG
 * @Date 2022/12/9 16:21
 */
public class CountScheduler {

    public void startCountScheduler() throws SchedulerException {
        // 获取任务调度的实例
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        // 定义任务调度实例, 并与TestJob绑定
        JobDetail job = JobBuilder.newJob(CountJob.class)
                .withIdentity("countJob", "countJobGroup")
                .build();

        //同步周期
        long syncPeriodL = Long.valueOf(Agent.getInstance().getAgentBaseInfo().getSyncPeriod()) / 1000L;
        Integer syncPeriod = Integer.valueOf(String.valueOf(syncPeriodL));

        // 定义触发器, 会马上执行一次, 接着5秒执行一次
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("testTrigger", "testTriggerGroup")
                .startNow()
                .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(syncPeriod))
                .build();
        // 使用触发器调度任务的执行
        scheduler.scheduleJob(job, trigger);
        // 开启任务
        scheduler.start();
    }
}
