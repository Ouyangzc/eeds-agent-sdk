package com.elco.eeds.agent.sdk.core.connect.scheduler;

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
     * @Author LCXU
     * @Description //添加一个任务
     * @Date 12:05 2020/12/28
     * @Param [sysTask]
     * @return boolean
     */
    boolean addJob(SysTask sysTask) throws ClassNotFoundException, SchedulerException;


    boolean addJob(String cron, ThingsConnectionHandler handler) throws SchedulerException;

    /**
     * @Author LCXU
     * @Description //获取任务状态
     * @Date 12:06 2020/12/28
     * @Param [jobName, jobGroup]
     * @return String
     */
    String getJobState(String jobName, String jobGroup) throws SchedulerException;



    JobDetail getJobDetail(String jobName , String jobGroup) throws SchedulerException;

    /**
     * @Author LCXU
     * @Description //暂停所有任务
     * @Date 12:06 2020/12/28
     * @Param []
     * @return boolean
     */
    boolean pauseAllJob() throws SchedulerException;

    /**
     * @Author LCXU
     * @Description //暂停所有任务
     * @Date 12:06 2020/12/28
     * @Param []
     * @return boolean
     */
    boolean pauseJob(String jobName, String jobGroup) throws SchedulerException;

    /**
     * @Author LCXU
     * @Description //恢复所有任务
     * @Date 12:06 2020/12/28
     * @Param []
     * @return boolean
     */
    boolean resumeAllJob() throws SchedulerException;

    /**
     * @Author LCXU
     * @Description //恢复某个任务
     * @Date 12:07 2020/12/28
     * @Param [jobName, jobGroup]
     * @return boolean
     */
    boolean resumeJob(String jobName, String jobGroup) throws SchedulerException;

    /**
     * @Author LCXU
     * @Description //删除某个任务
     * @Date 12:07 2020/12/28
     * @Param [sysTask]
     * @return boolean
     */
    boolean deleteJob(SysTask sysTask)  throws Exception;

    /**
     * @Author LCXU
     * @Description //修改任务
     * @Date 12:07 2020/12/28
     * @Param [sysTask]
     * @return boolean
     */
    boolean modifyJob(SysTask sysTask) throws Exception;

}
