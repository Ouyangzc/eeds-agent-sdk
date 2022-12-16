package com.elco.eeds.agent.sdk.transfer.quartz;

import com.elco.eeds.agent.sdk.transfer.service.data.count.DataCountServiceImpl;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName CountJob
 * @Description 统计定时任务
 * @Author OUYANG
 * @Date 2022/12/9 16:20
 */
public class CountJob implements Job {
    public static final Logger logger = LoggerFactory.getLogger(CountJob.class);
    private DataCountServiceImpl dataCountService;

    public CountJob(DataCountServiceImpl dataCountService) {
        this.dataCountService = dataCountService;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        DataCountServiceImpl.setUpThingsDataCountMap();
        DataCountServiceImpl.sentCountData();
        //定时任务校验当前统计数据
        DataCountServiceImpl.scheduledCheckCurrentCountingData();
    }
}
