package com.elco.eeds.agent.sdk.core.connect.scheduler;


import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.PersistJobDataAfterExecution;

/**
 * @Description 自定义任务实现，可以定义N个来执行不同的业务
 * @Author LCXU
 * @Date 2020/12/25 11:41
 * @Version 1.0
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class SchedulerJob implements Job {



    @Override
    public void execute(JobExecutionContext context) {
//        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
//        DefaultMessagePusher messagePusher = SpringUtil.getBean(DefaultMessagePusher.class);
//
//        TaskWorkMapper taskWorkMapper = SpringUtil.getBean(TaskWorkMapper.class);
//
//        JobKey jobKey = context.getJobDetail().getKey();
//        log.debug("当前执行的任务名：{}", jobKey.getName());
//        JobDataMap data = context.getTrigger().getJobDataMap();
//        //这里可以获取任务执行的相关参数
//        BsTask task = (BsTask) data.get("task");
////        String invokeParam = (String) data.get("invokeParam");
////        String jobDesc = (String) data.get("desc");
////        String taskDesc = (String) data.get("taskDesc");
////        Integer jobType = (Integer) data.get("type");
//        log.debug(Thread.currentThread().getName() + ":" + JsonUtils.toJsonString(task));
//
//
//        // 生成任务中的工作日志
//        BsTaskWork taskWork = new BsTaskWork();
//        taskWork.setWorkId(IDUtils.snowflakeId());
//        taskWork.setDesc(task.getTitle());
//        taskWork.setTaskId(Long.valueOf(jobKey.getName()));
//        taskWork.setStatus(StaticVariable.TASK_WORK_WORDING);
//        taskWork.setCreateTime(new Date());
//        taskWork.setDeleted(DeletedEnum.DELETED_N.getCode());
//        taskWork.setType(task.getType());
//        taskWork.setTaskDesc(task.getContent());
//        taskWorkMapper.insert(taskWork);


//        this.sendMessage(task, taskWork.getWorkId().toString());


    }



}
