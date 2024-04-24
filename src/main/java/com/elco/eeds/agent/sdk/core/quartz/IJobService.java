package com.elco.eeds.agent.sdk.core.quartz;

import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

/**
 * @ClassName IJobService
 * @Description 任务接口
 * @Author OuYang
 * @Date 2024/4/24 11:21
 * @Version 1.0
 */
public interface IJobService {

  /**
   * 添加任务
   * @param jobDetail
   * @param jobTrigger
   * @throws SchedulerException
   */
  void addJob(JobDetail jobDetail,Trigger jobTrigger) throws SchedulerException;

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
   * quartz启动
   * @throws SchedulerException
   */
  void start()throws SchedulerException;
}
