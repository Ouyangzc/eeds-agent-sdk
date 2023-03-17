package com.elco.eeds.agent.sdk.transfer.quartz;

import com.elco.eeds.agent.sdk.core.bean.agent.Agent;
import com.elco.eeds.agent.sdk.core.bean.agent.AgentBaseInfo;
import com.elco.eeds.agent.sdk.core.util.RealTimeDataMessageFileUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName DataFileJob
 * @Description 过期文件删除任务
 * @Author ouyang
 * @Date 2023/2/21 14:56
 * @Version 1.0
 */
public class DataFileJob implements Job {
	public static Logger logger = LoggerFactory.getLogger(DataFileJob.class);
	
	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		logger.debug("执行定时任务:{}", DataFileJob.class.getName());
		// 缓存周期
		AgentBaseInfo agentBaseInfo = Agent.getInstance().getAgentBaseInfo();
		String agentBaseFileCycle = agentBaseInfo.getDataCacheCycle();
		RealTimeDataMessageFileUtils.removeDayFile(Integer.valueOf(agentBaseFileCycle));
//		RealTimeDataMessageFileUtils.removeMinuteFile(Integer.valueOf(agentBaseFileCycle));
		
	}
}
