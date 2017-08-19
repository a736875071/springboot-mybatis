package com.change.service.quartz;

import com.change.domain.quartz.QrtzCronTriggersDto;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;

/**
 * @author YangQing
 * @version 1.0.0
 */

public interface QrtzCronTriggersService {

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
