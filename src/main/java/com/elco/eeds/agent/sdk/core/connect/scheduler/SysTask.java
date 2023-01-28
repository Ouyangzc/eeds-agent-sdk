package com.elco.eeds.agent.sdk.core.connect.scheduler;



import java.io.Serializable;
import java.util.Date;

/**
 * @author LCXU
 * @title: SysTask
 * @projectName springboot-quartz
 * @description: 任务实体，用来保存创建的任务，可以自定义
 * @date 2020/12/27 14:42
 */

public class SysTask implements Serializable {
    /** 主键 **/
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getClazzName() {
        return clazzName;
    }

    public void setClazzName(String clazzName) {
        this.clazzName = clazzName;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobDesc() {
        return jobDesc;
    }

    public void setJobDesc(String jobDesc) {
        this.jobDesc = jobDesc;
    }

    public String getTaskDesc() {
        return taskDesc;
    }

    public void setTaskDesc(String taskDesc) {
        this.taskDesc = taskDesc;
    }

    public Integer getJobType() {
        return jobType;
    }

    public void setJobType(Integer jobType) {
        this.jobType = jobType;
    }

    public String getParmas() {
        return parmas;
    }

    public void setParmas(String parmas) {
        this.parmas = parmas;
    }

    /** 定时器表达式 **/
    private String cron;
    /** 创建时间 **/
    private Date gmtCreate;
    /** 开始时间 **/

    private Date beginTime;
    /** 结束时间 **/

    private Date endTime;
    /** 任务类型 1-用药提醒 **/
    private String type;
    /** 状态0-正常，9-删除 **/
    private String status;
    /** 任务需要执行的类 **/
    private String clazzName;
    /** 任务组 **/
    private String jobGroup;
    /** 任务名称，不可重复 **/
    private String jobName;

    /** 任务描述，不可重复 **/
    private String jobDesc;

    /** 任务描述，不可重复 **/
    private String taskDesc;
    /** 任务描述，不可重复 **/
    private Integer jobType;
    /** 参数，多个参数已逗号隔开，此处可以用json格式**/
    private String parmas;


}
