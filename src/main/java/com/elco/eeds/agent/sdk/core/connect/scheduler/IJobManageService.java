package com.elco.eeds.agent.sdk.core.connect.scheduler;

import com.elco.eeds.agent.sdk.core.common.constant.ReadTypeEnums;
import com.elco.eeds.agent.sdk.core.connect.ThingsConnectionHandler;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;

/**
 * @Description 任务管理
 * @Author LCXU
 * @Date 2020/12/28 12:03
 * @Version 1.0
 */
public interface IJobManageService {

    /**
     * @return boolean
     * @Author LCXU
     * @Description //添加一个任务
     * @Date 12:05 2020/12/28
     * @Param [sysTask]
     */
    boolean addJob(SysTask sysTask) throws ClassNotFoundException, SchedulerException;


    boolean addJob(String cron, ThingsConnectionHandler handler, ReadTypeEnums enums) throws SchedulerException;

    /**
     * @return String
     * @Author LCXU
     * @Description //获取任务状态
     * @Date 12:06 2020/12/28
     * @Param [jobName, jobGroup]
     */
    String getJobState(String jobName, String jobGroup) throws SchedulerException;


    JobDetail getJobDetail(String jobName, String jobGroup) throws SchedulerException;

    /**
     * @return boolean
     * @Author LCXU
     * @Description //暂停所有任务
     * @Date 12:06 2020/12/28
     * @Param []
     */
    boolean pauseAllJob() throws SchedulerException;

    /**
     * @return boolean
     * @Author LCXU
     * @Description //暂停所有任务
     * @Date 12:06 2020/12/28
     * @Param []
     */
    boolean pauseJob(String jobName, String jobGroup) throws SchedulerException;

    /**
     * @return boolean
     * @Author LCXU
     * @Description //恢复所有任务
     * @Date 12:06 2020/12/28
     * @Param []
     */
    boolean resumeAllJob() throws SchedulerException;

    /**
     * @return boolean
     * @Author LCXU
     * @Description //恢复某个任务
     * @Date 12:07 2020/12/28
     * @Param [jobName, jobGroup]
     */
    boolean resumeJob(String jobName, String jobGroup) throws SchedulerException;

    /**
     * @return boolean
     * @Author LCXU
     * @Description //删除某个任务
     * @Date 12:07 2020/12/28
     * @Param [sysTask]
     */
    boolean deleteJob(SysTask sysTask) throws Exception;

    /**
     * @return boolean
     * @Author LCXU
     * @Description //修改任务
     * @Date 12:07 2020/12/28
     * @Param [sysTask]
     */
    boolean modifyJob(SysTask sysTask) throws Exception;

    /**
     * 添加下发指令超时任务
     *
     * @param msgSeqNo
     * @param thingsId
     * @param timeout
     * @return
     */
    boolean addCmdTimeOutJob(String msgSeqNo, String thingsId, Integer timeout);

    /**
     * 删除下发指令超时任务
     *
     * @param msgSeqNo
     * @return
     */
    boolean removeCmdTimeOutJob(String msgSeqNo);

}
