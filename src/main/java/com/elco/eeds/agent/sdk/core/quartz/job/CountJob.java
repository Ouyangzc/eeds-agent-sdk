package com.elco.eeds.agent.sdk.core.quartz.job;

import com.elco.eeds.agent.sdk.core.bean.agent.Agent;
import com.elco.eeds.agent.sdk.core.bean.agent.AgentBaseInfo;
import com.elco.eeds.agent.sdk.transfer.service.data.count.DataCountServiceImpl;
import com.elco.eeds.agent.sdk.transfer.service.data.count.RealTimeDataStatisticsDeque;
import com.elco.eeds.agent.sdk.transfer.service.data.count.RealTimeDataStatistics;
import com.elco.eeds.core.utils.ObjectUtil;
import java.util.concurrent.atomic.AtomicLong;
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

	private static AtomicLong countTime = new AtomicLong();
	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		logger.debug("执行定时任务:{}", CountJob.class.getName());
		DataCountServiceImpl.sentCountData();


		RealTimeDataStatistics instance = RealTimeDataStatistics.getInstance();
		long collectTime = RealTimeDataStatisticsDeque.collectionStartTime.get();
		if (0==collectTime){
			return;
		}
		AgentBaseInfo agentBaseInfo = Agent.getInstance().getAgentBaseInfo();
		if (ObjectUtil.isNotEmpty(agentBaseInfo)){
			Long period = Long.valueOf(agentBaseInfo.getSyncPeriod());
			long countTime = CountJob.countTime.get();
			long countStartTime;
			long countEndTime;
			if (0==countTime){
				countStartTime = collectTime ;
				countEndTime = collectTime+ period;
			}else {
				countStartTime = countTime ;
				countEndTime = countTime+period;
			}
			instance.creatRange(countStartTime,countEndTime);
			instance.scanRealTimeCache();
			CountJob.countTime.set(countEndTime);
		}
		//定时任务校验当前统计数据
		DataCountServiceImpl.scheduledCheckCurrentCountingData();
	}
}
