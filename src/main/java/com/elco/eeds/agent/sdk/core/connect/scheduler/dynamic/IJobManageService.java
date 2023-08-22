package com.elco.eeds.agent.sdk.core.connect.scheduler.dynamic;

import org.quartz.SchedulerException;

/**
 * @ClassName IJobManageService
 * @Description 任务管理
 * @Author OuYang
 * @Date 2023/7/12 16:15
 * @Version 1.0
 */
public interface IJobManageService {
    /**
     * 添加任务
     *
     * @param sysJob
     * @throws ClassNotFoundException
     * @throws SchedulerException
     */
    void addJob(SysJob sysJob) throws Exception;

    /**
     * 暂停任务
     *
     * @param jobName
     * @param jobGroup
     * @return
     * @throws SchedulerException
     */
    void pauseJob(String jobName, String jobGroup) throws SchedulerException;


    /**
     * 恢复任务
     * @param jobName
     * @param jobGroup
     * @throws SchedulerException
     */
    void resumeJob(String jobName, String jobGroup) throws SchedulerException;

    /**
     * 删除任务
     * @param jobName
     * @param jobGroup
     * @throws Exception
     */
    void deleteJob(String jobName, String jobGroup)  throws Exception;


    /**
     * 修改任务
     * @param sysJob
     * @throws Exception
     */
    void modifyJob(SysJob sysJob) throws Exception;

}
