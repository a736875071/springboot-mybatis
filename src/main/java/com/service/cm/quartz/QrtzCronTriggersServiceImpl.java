package com.service.cm.quartz;

import com.dao.cm.quartz.QrtzCronTriggersDao;
import com.dao.cm.quartz.QrtzCronTriggersExtDao;
import com.domain.cm.quartz.*;
import com.entity.cm.quartz.QrtzCronTriggers;
import com.service.cm.quartz.enums.QuartzEnum;
import com.utils.exception.MsgException;
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
    private QrtzCronTriggersDao qrtzCronTriggersDao;
    @Autowired
    private QrtzCronTriggersExtDao qrtzCronTriggersExtDao;
    @Autowired
    private QuartzTimedTaskService quartzTimedTaskService;
    @Autowired
    private AutoDeductionService autoDeductionService;

    private SimpleDateFormat dateFormat() {
        return new SimpleDateFormat("HH:mm:ss");
    }

    /**
     * 通过id删除定时任务配置信息
     *
     * @param id 主键id
     * @return
     */
    @Override
    public int deleteByPrimaryKey(Long id) {
        return qrtzCronTriggersDao.deleteByPrimaryKey(id);
    }

    /**
     * 添加定时任务配置信息
     *
     * @param record 定时任务配置信息
     * @return 定时任务配置信息
     */
    @Override
    public QrtzCronTriggersDto insert(QrtzCronTriggersDto record) {
        QrtzCronTriggers dto = new QrtzCronTriggers();
        BeanUtils.copyProperties(record, dto);
        qrtzCronTriggersDao.insert(dto);
        return record;
    }

    /**
     * 通过ID查询定时任务配置信息
     *
     * @param id id
     * @return 查询结果
     */
    @Override
    public QrtzCronTriggers selectByPrimaryKey(Long id) {
        QrtzCronTriggers qrtzCronTriggers = qrtzCronTriggersDao.selectByPrimaryKey(id);
        return qrtzCronTriggers;
    }

    /**
     * 修改定时任务配置信息
     *
     * @param record 定时任务配置信息
     * @return 定时任务配置信息
     */
    @Override
    public int updateByPrimaryKeySelective(QrtzCronTriggers record) {
        return qrtzCronTriggersDao.updateByPrimaryKeySelective(record);
    }


    /**
     * 条件查询定时任务配置信息
     *
     * @param qrtzCronTriggersCondition 查询条件
     * @return 查询结果
     */
    @Override
    public List<QrtzCronTriggersDto> selectByCondition(QrtzCronTriggersCondition qrtzCronTriggersCondition) {
        List<QrtzCronTriggersDto> qrtzCronTrigger = qrtzCronTriggersExtDao.selectByCodition(qrtzCronTriggersCondition);
        return qrtzCronTrigger;
    }


    /**
     * 通过id启动定时任务
     *
     * @param id 主键id
     * @throws Exception
     */
    @Override
    public void startOneJob(Long id) throws MsgException, SchedulerException {
        QuartzTimedTaskDto quartzTimedTaskDtos = quartzTimedTaskService.getQuartzTimedTasksById(id);
        if (quartzTimedTaskDtos == null) {
            throw new MsgException("未查询到相关定时任务信息");
        }
        if (quartzTimedTaskDtos.getTaskStatus().equals(QuartzEnum.TaskStatus.OUTAGE.value())) {
            TriggerKey triggerKey = TriggerKey.triggerKey(quartzTimedTaskDtos.getTriggerName(), quartzTimedTaskDtos.getTriggerGroup());
            Scheduler scheduler = gSchedulerFactory.getScheduler();
            if (!scheduler.checkExists(triggerKey)) {
                JobDetail jobDetail = JobBuilder.newJob(ScheduledJob.class).withIdentity(quartzTimedTaskDtos.getJobName(), quartzTimedTaskDtos.getJobGroup()).build();// 任务名，任务组，任务执行类
                // 触发器
                // 触发器名,触发器组
                CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey)
                        // 触发器时间设定
                        .withSchedule(CronScheduleBuilder.cronSchedule(quartzTimedTaskDtos.getCronExpression()))
                        .startNow().build();
                scheduler.scheduleJob(jobDetail, trigger);
                // 启动
                if (!scheduler.isShutdown()) {
                    scheduler.start();
                }
            }
            //修改定时任务状态
            quartzTimedTaskDtos.setTaskStatus(QuartzEnum.TaskStatus.STARTUSING.value());
            AutoDeductionDto autoDeductionDto = new AutoDeductionDto();
            BeanUtils.copyProperties(quartzTimedTaskDtos, autoDeductionDto);
            autoDeductionService.patchAutoDeduction(autoDeductionDto);
        } else {
            throw new MsgException("该定时任务已经启动");
        }
    }

    /**
     * 通过id停止定时任务
     *
     * @param id 主键ID
     * @return
     */
    @Override
    public void shutdownOneJob(Long id) throws SchedulerException {
        QuartzTimedTaskDto quartzTimedTaskDtos = quartzTimedTaskService.getQuartzTimedTasksById(id);
        if (quartzTimedTaskDtos == null) {
            throw new MsgException("停止失败,原因:未查询到相关定时任务");
        }
        Scheduler scheduler = gSchedulerFactory.getScheduler();
//        Scheduler scheduler = gSchedulerFactory.getScheduler();
        JobKey jobKey = new JobKey(quartzTimedTaskDtos.getJobName(), quartzTimedTaskDtos.getJobGroup());
//        TriggerKey triggerKey = TriggerKey.triggerKey(quartzTimedTaskDtos.getTriggerName(), quartzTimedTaskDtos.getTriggerGroup());
//        scheduler.pauseTrigger(triggerKey);// 停止触发器
//        scheduler.unscheduleJob(triggerKey);// 移除触发器
//        scheduler.deleteJob(new JobKey(quartzTimedTaskDtos.getSchedName(), quartzTimedTaskDtos.getTriggerGroup()));// 删除任务
        scheduler.pauseJob(jobKey);
        quartzTimedTaskDtos.setTaskStatus(QuartzEnum.TaskStatus.OUTAGE.value());
        AutoDeductionDto autoDeductionDto = new AutoDeductionDto();
        BeanUtils.copyProperties(quartzTimedTaskDtos, autoDeductionDto);
        autoDeductionService.patchAutoDeduction(autoDeductionDto);
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
            System.out.println("------- Initialization Complete -----------"+dateFormat().format(new Date()));

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
            if(scheduler.checkExists(job.getKey())){return;}
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
