package com.dao.cm.quartz;


import com.domain.cm.quartz.QrtzCronTriggersCondition;
import com.domain.cm.quartz.QrtzCronTriggersDto;

import java.util.List;

/**
 * 定时任务配置
 *
 * @author YangQing
 * @version 1.0.0
 */
public interface QrtzCronTriggersExtDao {
    /**
     * 条件查询定时任务配置信息
     *
     * @param qrtzCronTriggersCondition 查询条件
     * @return 查询结果
     */
    List<QrtzCronTriggersDto> selectByCodition(QrtzCronTriggersCondition qrtzCronTriggersCondition);

}