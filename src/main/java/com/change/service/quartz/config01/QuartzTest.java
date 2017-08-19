package com.change.service.quartz.config01;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.text.ParseException;


public class QuartzTest {

	private static SchedulerFactory sf = new StdSchedulerFactory();
	private static String JOB_GROUP_NAME = "ddlib";
	private static String TRIGGER_GROUP_NAME = "ddlibTrigger";
	public static void main(String[] args) throws SchedulerException,
			ParseException {
//		startSchedule();
		new Thread(() -> {
			resumeJob();
			System.out.println("1");
		}).start();
		new Thread(() -> {
			resumeJob();
			System.out.println("2");
		}).start();

	}
	/**
	 * ��ʼһ��simpleSchedule()����
	 */
	public static void startSchedule() {
		try {
			// 1������һ��JobDetailʵ����ָ��Quartz
			JobDetail jobDetail = JobBuilder.newJob(MyJob.class)
					// ����ִ����
					.withIdentity("job1_2", "jGroup2")
					// ��������������
					.build();
			Trigger trigger = TriggerBuilder.newTrigger()
					.withIdentity("trigger1_2", "tGroup2").startNow()
					.withSchedule(CronScheduleBuilder.cronSchedule("0/4 * * * * ?")
					).build();
			// 3������Scheduler
			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
			scheduler.start();
			// 4������ִ��
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

	/**
	 * �����ݿ����ҵ��Ѿ����ڵ�job�������¿�������
	 */
	public static void resumeJob() {
		try {

			SchedulerFactory schedulerFactory = new StdSchedulerFactory();
			Scheduler scheduler = schedulerFactory.getScheduler();
//
//			// �ٻ�ȡ�����������еĴ�������
//			List<String> triggerGroups = scheduler.getTriggerGroupNames();
//			// �����»ָ���tgroup1���У���Ϊtrigger1_1������������
//			for (int i = 0; i < triggerGroups.size(); i++) {
//				List<String> triggers = scheduler.getTriggerGroupNames();
//				for (int j = 0; j < triggers.size(); j++) {
//					Trigger tg = scheduler.getTrigger(new TriggerKey(triggers
//							.get(j), triggerGroups.get(i)));
//					// ��-1:���������ж�
//					if (tg instanceof SimpleTrigger
//							&& tg.getDescription().equals("tgroup1.trigger1_122222")) {
//						// ��-1:�ָ�����
//						scheduler.resumeJob(new JobKey(triggers.get(j),
//								triggerGroups.get(i)));
//					}
//				}
//
//			}
			scheduler.start();
		} catch (Exception e) {
			e.printStackTrace();

		}
	}
}
