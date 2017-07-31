//package com.controller.cm.quartz;
//
///**
// * @author YangQing
// * @version 1.0.0
// */
//
//import org.quartz.JobDetail;
//import org.quartz.Scheduler;
//import org.quartz.Trigger;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.Date;
//
//import static org.quartz.DateBuilder.evenMinuteDate;
//import static org.quartz.JobBuilder.newJob;
//import static org.quartz.TriggerBuilder.newTrigger;
//
///**
// * 一个简单的测试quartz任务管理器测试类
// * @author longgangbai
// *
// */
//public class QuartzScheduleMain {
//
//
//    /**
//     *
//     * @throws Exception
//     */
//    public void run() throws Exception {
//        Logger log = LoggerFactory.getLogger(QuartzScheduleMain.class);
//
//        log.info("------- Initializing ----------------------");
//
//        // First we must get a reference to a scheduler
//        //从调度管理器中获取调度对象
//        Scheduler sched = QuartzScheduleMgr.getInstanceScheduler();
//        log.info("------- Initialization Complete -----------");
//
//        // computer a time that is on the next round minute
//        Date runTime = evenMinuteDate(new Date());
//
//        log.info("------- Scheduling Job  -------------------");
//
//        // define the job and tie it to our HelloJob class
//        //创建相关的job信息
//        JobDetail job = newJob(HelloJob.class)
//                .withIdentity("job1", "group1")
//                .build();
//
//        // Trigger the job to run on the next round minute
//        //创建一个触发器的名称
//        Trigger trigger = newTrigger()
//                .withIdentity("trigger1", "group1")
//                .startAt(runTime)
//                .build();
//
//        // Tell quartz to schedule the job using our trigger
//        //设置调度相关的Job
//        sched.scheduleJob(job, trigger);
//        log.info(job.getKey() + " will run at: " + runTime);
//
//        // Start up the scheduler (nothing can actually run until the
//        // scheduler has been started)
//        //启动调度任务
//        sched.start();
//
//        log.info("------- Started Scheduler -----------------");
//
//        try {
//            Thread.sleep(25L * 1000L);
//            // executing...
//        } catch (Exception e) {
//        }
//        //暂时停止Job任务开始执行
//        log.info("-------pauseJob.. -------------");
//        sched.pauseJob(job.getKey());
//
//        try {
//            Thread.sleep(10L * 1000L);
//        } catch (Exception e) {
//        }
//        log.info("------- resumeJob... -------------");
//        //恢复Job任务开始执行
//        sched.resumeJob(job.getKey());
//        try {
//            Thread.sleep(10L * 1000L);
//            // executing...
//        } catch (Exception e) {
//        }
//
//
//        // wait long enough so that the scheduler as an opportunity to
//        // run the job!
//        log.info("------- Waiting 65 seconds... -------------");
//        try {
//            // wait 65 seconds to show job
//            Thread.sleep(65L * 1000L);
//            // executing...
//        } catch (Exception e) {
//        }
//
//        // shut down the scheduler
//        log.info("------- Shutting Down ---------------------");
//        sched.shutdown(true);
//        log.info("------- Shutdown Complete -----------------");
//    }
//
//    public static void main(String[] args) throws Exception {
//        System.out.println(evenMinuteDate(new Date()));
//        QuartzScheduleMain example = new QuartzScheduleMain();
//        example.run();
//
//    }
//
//}
