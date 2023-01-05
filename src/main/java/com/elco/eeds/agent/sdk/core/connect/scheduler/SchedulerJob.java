package com.elco.eeds.agent.sdk.core.connect.scheduler;


import com.elco.eeds.agent.sdk.core.connect.ThingsConnectionHandler;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description 自定义任务实现，可以定义N个来执行不同的业务
 * @Author LCXU
 * @Date 2020/12/25 11:41
 * @Version 1.0
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class SchedulerJob implements Job {

    public static final Logger logger = LoggerFactory.getLogger(SchedulerJob.class);

    @Override
    public void execute(JobExecutionContext context) {

        JobKey jobKey = context.getJobDetail().getKey();
        logger.debug("当前执行的任务名：{}", jobKey.getName());
        JobDataMap data = context.getTrigger().getJobDataMap();
        //这里可以获取任务执行的相关参数
        ThingsConnectionHandler handler = (ThingsConnectionHandler) data.get("handler");
        handler.read(null);



    }



}
