package com.elco.eeds.agent.sdk.core.quartz.job;

import com.elco.eeds.agent.sdk.transfer.service.cmd.CmdService;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName CmdTimeoutJob
 * @Description 指令下发超时任务
 * @Author OuYang
 * @Date 2023/6/12 9:36
 * @Version 1.0
 */
public class CmdTimeoutJob implements Job {
    public static final Logger logger = LoggerFactory.getLogger(CmdTimeoutJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //获取任务执行数据
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        String msgSeqNo = dataMap.get("msgSeqNo").toString();
        String thingsId = dataMap.get("thingsId").toString();
        //执行业务逻辑
        CmdService.sendTimeoutResult(thingsId, msgSeqNo, "指令下发超时,未收到数据源响应数据");
    }
}
