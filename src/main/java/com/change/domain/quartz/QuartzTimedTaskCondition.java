package com.change.domain.quartz;

import java.util.Date;

/**
 * @author YangQing
 * @version 1.0.0
 */

public class QuartzTimedTaskCondition {
    private Long id;

    private Long orgId;

    private String deduCycle;

    private Date beginTime;

    private Date endTime;

    private String portProcedure;

    private String taskStatus;

    private String cronExpression;

    private String timeZoneId;

    private String schedName;

    private String triggerName;

    private String triggerGroup;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getDeduCycle() {
        return deduCycle;
    }

    public void setDeduCycle(String deduCycle) {
        this.deduCycle = deduCycle;
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

    public String getPortProcedure() {
        return portProcedure;
    }

    public void setPortProcedure(String portProcedure) {
        this.portProcedure = portProcedure;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getTimeZoneId() {
        return timeZoneId;
    }

    public void setTimeZoneId(String timeZoneId) {
        this.timeZoneId = timeZoneId;
    }

    public String getSchedName() {
        return schedName;
    }

    public void setSchedName(String schedName) {
        this.schedName = schedName;
    }

    public String getTriggerName() {
        return triggerName;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    public String getTriggerGroup() {
        return triggerGroup;
    }

    public void setTriggerGroup(String triggerGroup) {
        this.triggerGroup = triggerGroup;
    }
}
