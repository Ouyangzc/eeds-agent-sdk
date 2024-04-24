package com.elco.eeds.agent.sdk.core.quartz.job;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.elco.eeds.agent.sdk.core.bean.properties.PropertiesContext;
import com.elco.eeds.agent.sdk.core.common.enums.QuartzEnum;
import com.elco.eeds.agent.sdk.core.connect.ThingsConnectionHandler;
import com.elco.eeds.agent.sdk.core.connect.status.ConnectionStatus;
import com.elco.eeds.agent.sdk.core.quartz.bean.RealTimeReadBean;
import com.elco.eeds.agent.sdk.transfer.service.things.ThingsSyncNewServiceImpl;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName RealTimeReadJob
 * @Description sysjob的定时任务
 * @Author OuYang
 * @Date 2023/7/13 10:01
 * @Version 1.0
 */
public class RealTimeReadJob implements Job {

    public static final Logger logger = LoggerFactory.getLogger(RealTimeReadJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Trigger trigger = context.getTrigger();
        Object job = trigger.getJobDataMap().get("job");
        if (ObjectUtil.isNotEmpty(job)) {
            RealTimeReadBean jobBean = (RealTimeReadBean) job;
            //job业务类型为读取点位类型
            if (jobBean.getJobGroup().equals(QuartzEnum.REALTIME_PROPERTIES_READ_GROUP)) {
                handReadProperties(jobBean);
            }
        }
    }


    private void handReadProperties(RealTimeReadBean jobBean) {
        Object handler = jobBean.getExtraMap().get("handler");
        if (ObjectUtil.isNotEmpty(handler)) {
            ThingsConnectionHandler thingsConnectionHandler = (ThingsConnectionHandler) handler;
            String thingsId = thingsConnectionHandler.getThingsId();
            if (thingsConnectionHandler.getConnectionStatus().equals(ConnectionStatus.CONNECTED)) {
                List<PropertiesContext> propertiesContexts = ThingsSyncNewServiceImpl.getThingsPropertiesContextList(thingsId);
                if (ObjectUtil.isNotEmpty(propertiesContexts)) {
                    //排除虚拟变量
                    propertiesContexts = propertiesContexts.stream().filter(p -> StrUtil.isNotEmpty(p.getAddress())).collect(Collectors.toList());
                    thingsConnectionHandler.read(propertiesContexts);
                }
            }
        }
    }

}
