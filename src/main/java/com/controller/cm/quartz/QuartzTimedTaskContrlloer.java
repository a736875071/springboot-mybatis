package com.controller.cm.quartz;

import com.domain.cm.quartz.AutoDeductionCondition;
import com.domain.cm.quartz.QuartzTimedTaskDto;
import com.service.cm.quartz.QuartzTimedTaskService;
import com.service.cm.quartz.ScheduledJob;
import com.service.cm.quartz.config01.MyJob;
import com.service.cm.quartz.enums.QuartzEnum;
import com.service.cm.redis.IRedisService;
import com.utils.IpUtils;
import com.utils.Response.Response;
import com.utils.ValidateUtils;
import com.utils.exception.MsgException;
import com.utils.log.EpmLogMessage;
import com.utils.log.LogMessage;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

import static org.quartz.DateBuilder.evenMinuteDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;


/**
 * 定时任务
 *
 * @author YangQing
 * @version 1.0.0
 */
@RestController
public class QuartzTimedTaskContrlloer {
    private static final Logger logger = LoggerFactory.getLogger(QuartzTimedTaskContrlloer.class);
    //    @Autowired
//    private QrtzCronTriggersService qrtzCronTriggersService;
    @Autowired
    private QuartzTimedTaskService quartzTimedTaskService;
    @Autowired
    private IRedisService redisService;


    /**
     * 查询定时任务列表
     *
     * @param autoDeductionCondition 查询条件
     * @return 查询结果
     */
    @RequestMapping(value = "/epm/cm/quartz/getquartztimedtasks", method = RequestMethod.GET)
    public Response<?> getQuartzTimedTasks(AutoDeductionCondition autoDeductionCondition) throws SchedulerException {
        Logger log = LoggerFactory.getLogger(QuartzTimedTaskContrlloer.class);

        log.info("------- Initializing ----------------------");

        // First we must get a reference to a scheduler
        //从调度管理器中获取调度对象
        Scheduler sched = QuartzScheduleMgr.getInstanceScheduler();
        log.info("------- Initialization Complete -----------");

        // computer a time that is on the next round minute
        Date runTime = evenMinuteDate(new Date());

        log.info("------- Scheduling Job  -------------------");

        // define the job and tie it to our HelloJob class
        //创建相关的job信息
        JobDetail job = newJob(ScheduledJob.class)
                .withIdentity("job1", "group1")
                .build();

        // Trigger the job to run on the next round minute
        //创建一个触发器的名称
        Trigger trigger = newTrigger()
                .withIdentity("trigger1", "group1")
                .startAt(runTime)
                .build();

        // Tell quartz to schedule the job using our trigger
        //设置调度相关的Job
        sched.scheduleJob(job, trigger);
        log.info(job.getKey() + " will run at: " + runTime);

        // Start up the scheduler (nothing can actually run until the
        // scheduler has been started)
        //启动调度任务
        sched.start();

        log.info("------- Started Scheduler -----------------");

        try {
            Thread.sleep(25L * 1000L);
            // executing...
        } catch (Exception e) {
        }
        //暂时停止Job任务开始执行
        log.info("-------pauseJob.. -------------");
        sched.pauseJob(job.getKey());

        try {
            Thread.sleep(10L * 1000L);
        } catch (Exception e) {
        }
        log.info("------- resumeJob... -------------");
        //恢复Job任务开始执行
        sched.resumeJob(job.getKey());
        try {
            Thread.sleep(10L * 1000L);
            // executing...
        } catch (Exception e) {
        }


        // wait long enough so that the scheduler as an opportunity to
        // run the job!
        log.info("------- Waiting 65 seconds... -------------");
        try {
            // wait 65 seconds to show job
            Thread.sleep(65L * 1000L);
            // executing...
        } catch (Exception e) {
        }

        // shut down the scheduler
        log.info("------- Shutting Down ---------------------");
        sched.shutdown(true);
        log.info("------- Shutdown Complete -----------------");
//        logger.info(new LogMessage<>(UUID.randomUUID().toString(),
//                new EpmLogMessage("epm-cm-service",
//                        "quartz",
//                        "getQuartzTimedTasks",
//                        "查询定时任务列表")).toString());
//        List<QuartzTimedTaskDto> quartzTimedTaskDtos = quartzTimedTaskService.getQuartzTimedTasks(autoDeductionCondition);
//        int toatl = quartzTimedTaskService.getQuartzTimedTasksTotal(autoDeductionCondition);
//        PageResponse pageResponse = new PageResponse<>(toatl, quartzTimedTaskDtos);
//        return new Response<>().success(pageResponse);
        return null;
    }

    /**
     * 通过id查询定时任务信息
     *
     * @param id id
     * @return 查询结果
     */
    @RequestMapping(value = "/epm/cm/quartz/getquartztimedtasks/{id}", method = RequestMethod.GET)
    public Response<?> getQuartzTimedTasksById(@PathVariable Long id) {
        logger.info(new LogMessage<>(UUID.randomUUID().toString(),
                new EpmLogMessage("epm-cm-service",
                        "quartz",
                        "getQuartzTimedTasksById",
                        "通过id查询定时任务信息")).toString());
        QuartzTimedTaskDto quartzTimedTaskDtos = quartzTimedTaskService.getQuartzTimedTasksById(id);
        return new Response<>().success(quartzTimedTaskDtos);
    }

    /**
     * 添加一条定时任务记录
     *
     * @param quartzTimedTaskDto 定时任务记录信息
     * @return 定时任务记录信息
     */
    @RequestMapping(value = "/epm/cm/quartz/postqrtzcrontriggers", method = RequestMethod.POST)
    public Response<?> postQuartzTimedTasks(@Valid @RequestBody QuartzTimedTaskDto quartzTimedTaskDto, BindingResult result) {
        if (result.hasErrors()) {
            return new Response().failure(ValidateUtils.getValidateErrors(result.getAllErrors()));
        }

        try {
            quartzTimedTaskDto.setJobName(quartzTimedTaskDto.getOrgId().toString());
            quartzTimedTaskDto.setJobGroup(quartzTimedTaskDto.getOrgId().toString());
            //默认触发器名称与分组
            quartzTimedTaskDto.setTriggerName(quartzTimedTaskDto.getOrgId().toString());
            quartzTimedTaskDto.setTriggerGroup(quartzTimedTaskDto.getOrgId().toString());
            //默认为停用状态
            quartzTimedTaskDto.setTaskStatus(QuartzEnum.TaskStatus.OUTAGE.value());
//            QuartzTimedTaskDto quartzTimedTask = quartzTimedTaskService.addQuartzTimedTasks(quartzTimedTaskDto);
            try {
               String ip= IpUtils.getIp();
                redisService.set(quartzTimedTaskDto.getOrgId().toString(), ip);
                startSchedule(quartzTimedTaskDto);
            } catch (Exception e) {
                e.printStackTrace();
            }
            logger.info(new LogMessage<>(UUID.randomUUID().toString(),
                    new EpmLogMessage("epm-cm-service",
                            "quartz",
                            "postQuartzTimedTasks",
                            "添加一条定时任务记录,记录信息:" + quartzTimedTaskDto)).toString());
            return new Response<>().success("");
        } catch (MsgException e) {
            logger.error(new EpmLogMessage("epm-cm-service",
                    "quartz",
                    "postQuartzTimedTasks",
                    "添加一条定时任务记录:" + quartzTimedTaskDto + ",操作失败,原因:" + e.getMessage()).toString(), e);
            return new Response<>().failure(e.getMessage());
        }

    }

    /**
     * 通过ID删除定时任务记录
     *
     * @param id 定时任务记录id
     * @return id
     */
    @RequestMapping(value = "/epm/cm/quartz/deletequartztimedtasks/{id}", method = RequestMethod.DELETE)
    public Response<?> deleteQuartzTimedTasks(@PathVariable Long id) {
        try {
            quartzTimedTaskService.deleteQuartzTimedTasks(id);
            logger.info(new LogMessage<>(UUID.randomUUID().toString(),
                    new EpmLogMessage("epm-cm-service",
                            "quartz",
                            "postQuartzTimedTasks",
                            "通过ID删除定时任务记录,id:" + id)).toString());
            return new Response<>().success(id);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return new Response<>().success(id);
    }

    /**
     * 启动所有定时任务
     *
     * @return
     */
    @RequestMapping(value = "/epm/cm/quartz/startJobs", method = RequestMethod.GET)
    public Response<?> startJobs() {
//        try {
//            qrtzCronTriggersService.createJobs();
//            logger.info(new LogMessage<>(UUID.randomUUID().toString(),
//                    new EpmLogMessage("epm-cm-service",
//                            "quartz",
//                            "startJobs",
//                            "启动所有定时任务")).toString());
//        } catch (SchedulerException e) {
//            e.printStackTrace();
//        }
        return new Response<>().success("成功");
    }

    /**
     * 通过id启动定时任务
     *
     * @param id id
     * @return
     */
    @RequestMapping(value = "/epm/cm/quartz/startonejobs/{id}", method = RequestMethod.GET)
    public Response<?> startOneJob(@PathVariable Long id) {
//        try {
////            qrtzCronTriggersService.startOneJob(id);
//            logger.info(new LogMessage<>(UUID.randomUUID().toString(),
//                    new EpmLogMessage("epm-cm-service",
//                            "quartz",
//                            "startOneJob",
//                            "通过id启动定时任务,id:" + id)).toString());
//        } catch (SchedulerException e) {
//            e.printStackTrace();
//        } catch (MsgException e) {
//            logger.error(new EpmLogMessage("epm-cm-service",
//                    "quartz",
//                    "startOneJob",
//                    "通过id启动定时任务,id:" + id + ",操作失败,原因:" + e.getMessage()).toString(), e);
//        }
        return new Response<>().success("成功");
    }

    /**
     * 停止所有定时任务
     *
     * @return
     */
    @RequestMapping(value = "/epm/cm/quartz/shutdownjobs", method = RequestMethod.GET)
    public Response<?> shutdownJobs() {
//        qrtzCronTriggersService.shutdownJobs();
        logger.info(new LogMessage<>(UUID.randomUUID().toString(),
                new EpmLogMessage("epm-cm-service",
                        "quartz",
                        "shutdownJobs",
                        "停止所有定时任务")).toString());
        return new Response<>().success("成功");
    }

    /**
     * 通过id停止定时任务
     *
     * @param id 主键ID
     * @return
     */
    @RequestMapping(value = "/epm/cm/quartz/shutdownonejobs/{id}", method = RequestMethod.GET)
    public Response<?> shutdownOneJob(@PathVariable Long id) {
//        try {
//            qrtzCronTriggersService.shutdownOneJob(id);
        logger.info(new LogMessage<>(UUID.randomUUID().toString(),
                new EpmLogMessage("epm-cm-service",
                        "quartz",
                        "shutdownJobs",
                        "通过id停止定时任务,id:" + id)).toString());
//        } catch (SchedulerException e) {
//            e.printStackTrace();
//        }
        return new Response<>().success("成功");
    }

    public static void startSchedule(QuartzTimedTaskDto quartzTimedTaskDto) throws ParseException {
        try {
            // 1、创建一个JobDetail实例，指定Quartz
            JobDetail jobDetail = JobBuilder.newJob(MyJob.class)
                    // 任务执行类
                    .withIdentity(quartzTimedTaskDto.getJobName(), quartzTimedTaskDto.getJobGroup())
                    // 任务名，任务组
                    .build();
//                 String cron=   DateUtil.DateToCronExpression(quartzTimedTaskDto.getDeduCycle(),
//                            DateUtil.dateToStr(quartzTimedTaskDto.getBeginTime())
            String cron = "0/5 * * * * ?";
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(quartzTimedTaskDto.getTriggerName(), quartzTimedTaskDto.getTriggerGroup()).startNow()
                    .withSchedule(CronScheduleBuilder.cronSchedule(cron)
                    ).build();
            // 3、创建Scheduler
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            // 4、调度执行
            scheduler.scheduleJob(jobDetail, trigger);
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
