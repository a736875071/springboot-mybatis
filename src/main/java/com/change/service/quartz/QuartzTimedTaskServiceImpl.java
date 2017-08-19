package com.change.service.quartz;

import com.change.domain.quartz.*;
import com.change.utils.DateUtil;
import com.change.utils.exception.MsgException;
import com.change.utils.uuidkey.UUIDUtil;
import org.quartz.SchedulerException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.List;

/**
 * 定时任务
 *
 * @author YangQing
 * @version 1.0.0
 */
@Service
public class QuartzTimedTaskServiceImpl implements QuartzTimedTaskService {

    @Autowired
    private AutoDeductionService autoDeductionService;

    /**
     * 查询定时任务列表
     *
     * @param autoDeductionCondition 查询条件
     * @return 查询结果
     */
    @Override
    public List<QuartzTimedTaskDto> getQuartzTimedTasks(AutoDeductionCondition autoDeductionCondition) {
        List<QuartzTimedTaskDto> autoDeductionDtos = autoDeductionService.getAutoDeductionByCondition(autoDeductionCondition);
        return autoDeductionDtos;
    }


    /**
     * 查询定时任务列表条数
     *
     * @param autoDeductionCondition 查询条件
     * @return 查询结果条数
     */
    @Override
    public int getQuartzTimedTasksTotal(AutoDeductionCondition autoDeductionCondition) {
        return autoDeductionService.getAutoDeductionTotalByCondition(autoDeductionCondition);
    }

    /**
     * 添加一条定时任务记录
     *
     * @param quartzTimedTaskDto 定时任务信息
     * @return 定时任务信息
     * @throws MsgException
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public QuartzTimedTaskDto addQuartzTimedTasks(QuartzTimedTaskDto quartzTimedTaskDto) throws MsgException {
        //检验是否存在相同定时任务
        QrtzCronTriggersCondition qrtzCronTriggersCondition = new QrtzCronTriggersCondition();
        qrtzCronTriggersCondition.setTriggerGroup(quartzTimedTaskDto.getTriggerGroup());
        qrtzCronTriggersCondition.setSchedName(quartzTimedTaskDto.getJobName());
        qrtzCronTriggersCondition.setTriggerName(quartzTimedTaskDto.getTriggerName());
        //添加记录信息
        Long id = UUIDUtil.genSequenceUUID();
        quartzTimedTaskDto.setId(id);
        //时间转cron表达式
        try {
            quartzTimedTaskDto.setCronExpression(
                    DateUtil.DateToCronExpression(quartzTimedTaskDto.getDeduCycle(),
                            DateUtil.dateToStr(quartzTimedTaskDto.getBeginTime())
                    )
            );
        } catch (ParseException e) {
            e.printStackTrace();
        }
        AutoDeductionDto autoDeductionDto = new AutoDeductionDto();
        BeanUtils.copyProperties(quartzTimedTaskDto, autoDeductionDto);
        autoDeductionService.postAutoDeduction(autoDeductionDto);
        QrtzCronTriggersDto record = new QrtzCronTriggersDto();
        BeanUtils.copyProperties(quartzTimedTaskDto, record);
        return quartzTimedTaskDto;
    }

    /**
     * 通过ID删除定时任务记录
     *
     * @param id 定时任务记录id
     * @return id
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Long deleteQuartzTimedTasks(Long id) throws SchedulerException {
        autoDeductionService.deleteAutoDeduction(id);
        return id;
    }
}
