package com.service.cm.quartz;

import com.domain.cm.quartz.QrtzCronTriggersCondition;
import com.domain.cm.quartz.QrtzCronTriggersDto;
import com.entity.cm.quartz.QrtzCronTriggers;
import com.utils.exception.MsgException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;

import java.util.List;

/**
 * @author YangQing
 * @version 1.0.0
 */

public interface QrtzCronTriggersService {

    /**
     * 通过id删除定时任务配置信息
     *
     * @param id 主键id
     * @return
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 修改定时任务配置信息
     *
     * @param record 定时任务配置信息
     * @return 定时任务配置信息
     */
    int updateByPrimaryKeySelective(QrtzCronTriggers record);


    /**
     * 添加定时任务配置信息
     *
     * @param record 定时任务配置信息
     * @return 定时任务配置信息
     */
    QrtzCronTriggersDto insert(QrtzCronTriggersDto record);

    /**
     * 条件查询定时任务配置信息
     *
     * @param qrtzCronTriggersCondition 查询条件
     * @return 查询结果
     */
    List<QrtzCronTriggersDto> selectByCondition(QrtzCronTriggersCondition qrtzCronTriggersCondition);

    /**
     * 通过ID查询定时任务配置信息
     *
     * @param id id
     * @return 查询结果
     */
    QrtzCronTriggers selectByPrimaryKey(Long id);


    /**
     * 通过id启动定时任务
     *
     * @param id 主键id
     * @throws Exception
     */
    void startOneJob(Long id) throws MsgException, SchedulerException;

    /**
     * 通过id停止定时任务
     *
     * @param id 主键ID
     * @return
     */
    void shutdownOneJob(Long id) throws SchedulerException;

    /**
     * 停止所有定时任务
     *
     * @return
     */
    void shutdownJobs();

    /**
     * 将定时任务添加到Scheduler中
     *
     * @param scheduler
     * @param cls
     * @param qrtzCronTriggers 配置信息
     * @throws SchedulerException
     */
    void createStartJob2(Scheduler scheduler, Class cls, QrtzCronTriggersDto qrtzCronTriggers) throws SchedulerException;

    /**
     * 启动所有定时任务
     *
     * @return
     */
    void createJobs() throws SchedulerException;

     void staJobs() throws SchedulerException;
}
