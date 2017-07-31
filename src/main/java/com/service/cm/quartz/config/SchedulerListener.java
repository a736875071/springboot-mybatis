package com.service.cm.quartz.config;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.Properties;

/**
 * 定时任务自动服务监听
 *
 * @author YangQing
 * @version 1.0.0
 */
@Configuration
public class SchedulerListener implements ApplicationListener<ContextRefreshedEvent> {
    //    @Autowired
//    public MyScheduler myScheduler;
    @Value("${spring.quartz.url}")
    private String url;
    @Value("${spring.quartz.username}")
    private String username;
    @Value("${spring.quartz.password}")
    private String password;


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            Properties quartzProperties = QuartzDefaultName.quartz();
            quartzProperties.setProperty("org.quartz.dataSource.quartzDataSource.URL", url);
            quartzProperties.setProperty("org.quartz.dataSource.quartzDataSource.user", username);
            quartzProperties.setProperty("org.quartz.dataSource.quartzDataSource.password", password);
            StdSchedulerFactory stdSchedulerFactory = new StdSchedulerFactory();
            stdSchedulerFactory.initialize(quartzProperties);
            Scheduler scheduler = stdSchedulerFactory.getScheduler();
            scheduler.start();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}