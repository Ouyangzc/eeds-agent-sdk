package com.elco.eeds.agent.sdk.core.connect.scheduler;


import cn.hutool.core.thread.ThreadFactoryBuilder;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.*;

/**
 * @Description 自定义任务实现，可以定义N个来执行不同的业务
 * @Author LCXU
 * @Date 2020/12/25 11:41
 * @Version 1.0
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class SchedulerJob implements Job {

    private static ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNamePrefix("realTimeData-pool-%d").build();

    private static int nThreads = (int) Math.ceil(Runtime.getRuntime().availableProcessors() * 2);

    private static ExecutorService threadUtils = new ThreadPoolExecutor(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(10000),
            namedThreadFactory,
            new ThreadPoolExecutor.AbortPolicy()

    );


    public static final Logger logger = LoggerFactory.getLogger(SchedulerJob.class);

    @Override
    public void execute(JobExecutionContext context) {
        JobKey jobKey = context.getJobDetail().getKey();
        logger.info("当前执行的任务名：{},当前时间{}", jobKey.getName()
                , getCurrentTime("yyyy-MM-dd HH:mm:ss.SSS"));


//        threadUtils.execute(new Runnable() {
//            @Override
//            public void run() {
//                logger.info("当前执行的任务名：{},当前时间{}", jobKey.getName(),System.currentTimeMillis());
//                JobDataMap data = context.getTrigger().getJobDataMap();
//            }
//        });

        //这里可以获取任务执行的相关参数
//        ThingsConnectionHandler handler = (ThingsConnectionHandler) data.get("handler");
//        handler.read(null);



    }

    /**
     * 获取当前时间
     *
     * @param format
     * @return
     */
    public static String getCurrentTime(String format) {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);

        LocalDateTime now = LocalDateTime.now();

        return now.format(dateTimeFormatter);

    }


}
