package com.change.service.quartz;

import com.change.domain.quartz.AutoDeductionCondition;
import com.change.domain.quartz.QuartzTimedTaskDto;
import com.change.utils.exception.MsgException;
import org.quartz.SchedulerException;

import java.util.List;

/**
 * 定时任务
 *
 * @author YangQing
 * @version 1.0.0
 */

public interface QuartzTimedTaskService {

    /**
     * 查询定时任务列表
     *
     * @param autoDeductionCondition 查询条件
     * @return 查询结果
     */
    List<QuartzTimedTaskDto> getQuartzTimedTasks(AutoDeductionCondition autoDeductionCondition);


    /**
     * 查询定时任务列表条数
     *
     * @param autoDeductionCondition 查询条件
     * @return 查询结果条数
     */
    int getQuartzTimedTasksTotal(AutoDeductionCondition autoDeductionCondition);

    /**
     * 添加一条定时任务记录
     *
     * @param quartzTimedTaskDto 定时任务信息
     * @return 定时任务信息
     * @throws MsgException
     */
    QuartzTimedTaskDto addQuartzTimedTasks(QuartzTimedTaskDto quartzTimedTaskDto) throws MsgException;

    /**
     * 通过ID删除定时任务记录
     *
     * @param id 定时任务记录id
     * @return id
     */
    Long deleteQuartzTimedTasks(Long id) throws SchedulerException;
}
