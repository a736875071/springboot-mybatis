package com.service.cm.quartz;

import com.domain.cm.quartz.AutoDeductionCondition;
import com.domain.cm.quartz.QrtzCronTriggersDto;
import com.entity.cm.quartz.AutoDeductionRecord;
import com.service.cm.quartz.enums.QuartzEnum;
import com.domain.cm.quartz.AutoDeductionDto;
import com.domain.cm.quartz.QuartzTimedTaskDto;
import com.service.cm.quartz.utils.SpringUtil;
import com.utils.uuidkey.UUIDUtil;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 定时任务job
 *
 * @author YangQing
 * @version 1.0.0
 */

public class ScheduledJob implements Job {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private SimpleDateFormat dateFormat() {
        return new SimpleDateFormat("HH:mm:ss");
    }
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobKey jobKey = context.getJobDetail().getKey();
        //批量扣费
        System.out.println("定时任务:"+jobKey.getName()+"调用批量扣费" + dateFormat().format(new Date()));
        //日志输出级别info
        logger.debug("定时任务:"+jobKey.getName()+"调用批量扣费" + dateFormat().format(new Date()));
    }
}