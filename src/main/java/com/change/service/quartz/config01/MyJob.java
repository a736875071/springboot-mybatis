package com.change.service.quartz.config01;

import com.change.service.redis.IRedisService;
import com.change.utils.IpUtils;
import com.change.service.quartz.utils.SpringUtil;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyJob implements Job {
    private static final Logger logger = Logger.getLogger(MyJob.class);
    private static IRedisService redisService = SpringUtil.getBean(IRedisService.class);

    @Override
    public void execute(JobExecutionContext context)
            throws JobExecutionException {
        JobDetail jobDetail= context.getJobDetail();
        String name = redisService.get(jobDetail.getKey().getName());
        System.out.println(name);
        String ip = IpUtils.getIp();
        if (name != null) {
            if (!name.equals(ip)) {
                return;
            }
        }else {
            redisService.set(jobDetail.getKey().getName(), ip);
        }
        System.out.println("===================================");
        System.out.println("Hello quzrtz  " + context.getJobDetail().getKey().getName() + "时间:" +
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()));
        System.out.println("===========================================");

    }

}
