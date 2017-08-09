package com.service.cm.quartz;

import com.domain.cm.quartz.AutoDeductionCondition;
import com.domain.cm.quartz.AutoDeductionDto;
import com.domain.cm.quartz.QrtzCronTriggersDto;
import com.domain.cm.quartz.QuartzTimedTaskDto;
import com.service.cm.quartz.enums.QuartzEnum;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.quartz.DateBuilder.evenMinuteDate;

/**
 * 定时任务配置信息
 *
 * @author YangQing
 * @version 1.0.0
 */

@Service
public class QrtzCronTriggersServiceImpl implements QrtzCronTriggersService {
    private static SchedulerFactory gSchedulerFactory = new StdSchedulerFactory();
    @Autowired
    private QuartzTimedTaskService quartzTimedTaskService;
    @Autowired
    private AutoDeductionService autoDeductionService;

    private SimpleDateFormat dateFormat() {
        return new SimpleDateFormat("HH:mm:ss");
    }


    /**
     * 关闭所有定时任务
     */
    @Override
    public void shutdownJobs() {
        try {
            Scheduler scheduler = gSchedulerFactory.getScheduler();
            scheduler.shutdown();
            //将全部定时任务状态改为未执行
            autoDeductionService.BatchAutoDeductionTaskStatus(QuartzEnum.TaskStatus.OUTAGE.value());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将定时任务添加到Scheduler中
     *
     * @param scheduler
     * @param cls
     * @param qrtzCronTriggers 配置信息
     * @throws SchedulerException
     */
    public void createStartJob2(Scheduler scheduler, Class cls, QrtzCronTriggersDto qrtzCronTriggers) throws SchedulerException {
        try {
//            Scheduler sched = new StdSchedulerFactory().getScheduler();
            System.out.println("------- Initialization Complete -----------" + dateFormat().format(new Date()));

            // computer a time that is on the next round minute
            Date runTime = evenMinuteDate(new Date());
            //创建相关的job信息
            JobDetail job = JobBuilder
                    .newJob(cls)
                    .withIdentity(qrtzCronTriggers.getSchedName(), qrtzCronTriggers.getTriggerGroup())
                    .build();

            // Trigger the job to run on the next round minute
            //创建一个触发器的名称
            Trigger trigger = TriggerBuilder
                    .newTrigger()
                    .withIdentity(qrtzCronTriggers.getTriggerName(), qrtzCronTriggers.getTriggerGroup())
                    .withSchedule(CronScheduleBuilder.cronSchedule(qrtzCronTriggers.getCronExpression()))
                    .build();

            // Tell quartz to schedule the job using our trigger
            //设置调度相关的Job

            System.out.println(scheduler.checkExists(job.getKey()));
            if (scheduler.checkExists(job.getKey())) {
                return;
            }
            scheduler.scheduleJob(job, trigger);
            System.out.println(job.getKey() + " will run at: " + runTime);

            // Start up the scheduler (nothing can actually run until the
            // scheduler has been started)
            //启动调度任务
            scheduler.start();


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 启动所有定时任务
     *
     * @return
     */
    @Override
    public void createJobs() throws SchedulerException {
        AutoDeductionCondition autoDeductionCondition = new AutoDeductionCondition();
        autoDeductionCondition.setTaskStatus(QuartzEnum.TaskStatus.OUTAGE.value());
        Scheduler scheduler = gSchedulerFactory.getScheduler();
        List<QuartzTimedTaskDto> quartzTimedTaskDtos = quartzTimedTaskService.getQuartzTimedTasks(autoDeductionCondition);
        for (QuartzTimedTaskDto dto : quartzTimedTaskDtos) {
            QrtzCronTriggersDto qrtzCronTriggers = new QrtzCronTriggersDto();
            BeanUtils.copyProperties(dto, qrtzCronTriggers);
            this.createStartJob2(scheduler, ScheduledJob.class, qrtzCronTriggers);
            //修改定时任务状态
            dto.setTaskStatus(QuartzEnum.TaskStatus.STARTUSING.value());
            AutoDeductionDto autoDeductionDto = new AutoDeductionDto();
            BeanUtils.copyProperties(dto, autoDeductionDto);
            autoDeductionService.patchAutoDeduction(autoDeductionDto);
        }
        scheduler.start();
    }

    @Override
    public void staJobs() throws SchedulerException {
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();
        scheduler.start();
    }

}
