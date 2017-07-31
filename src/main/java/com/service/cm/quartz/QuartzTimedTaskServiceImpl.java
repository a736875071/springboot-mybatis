package com.service.cm.quartz;

import com.domain.cm.quartz.*;
import com.entity.cm.quartz.QrtzCronTriggers;
import com.utils.DateUtil;
import com.utils.exception.MsgException;
import com.utils.uuidkey.UUIDUtil;
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
    @Autowired
    private QrtzCronTriggersService qrtzCronTriggersService;

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
     * 通过id查询定时任务信息
     *
     * @param id id
     * @return 查询结果
     */
    @Override
    public QuartzTimedTaskDto getQuartzTimedTasksById(Long id) {
        QrtzCronTriggers qrtzCronTriggers = qrtzCronTriggersService.selectByPrimaryKey(id);
        QuartzTimedTaskDto quartzTimedTaskDto = new QuartzTimedTaskDto();
        if (qrtzCronTriggers != null) {
            AutoDeductionDto autoDeductionDto = autoDeductionService.getAutoDeductionById(qrtzCronTriggers.getId());
            if (autoDeductionDto != null) {
                BeanUtils.copyProperties(qrtzCronTriggers, quartzTimedTaskDto);
                BeanUtils.copyProperties(autoDeductionDto, quartzTimedTaskDto);
            }
        }
        return quartzTimedTaskDto;
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
//        List<QrtzCronTriggersDto> qrtzCronTriggers = qrtzCronTriggersService.selectByCondition(qrtzCronTriggersCondition);
//        if (qrtzCronTriggers != null && qrtzCronTriggers.size() != 0) {
//            throw new MsgException("添加失败,原因:已存在相同的定时任务");
//        }
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
//        qrtzCronTriggersService.insert(record);
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
        //查询定时任务记录信息
//        QrtzCronTriggers qrtzCronTriggers = qrtzCronTriggersService.selectByPrimaryKey(id);
//        if (qrtzCronTriggers == null) {
//            throw new MsgException("删除失败,原因:未查询到相关定时任务信息");
//        }
//        //停止定时任务
//        qrtzCronTriggersService.shutdownOneJob(qrtzCronTriggers.getId());
//        //删除相关信息记录
//        qrtzCronTriggersService.deleteByPrimaryKey(id);
        autoDeductionService.deleteAutoDeduction(id);
        return id;
    }
}
