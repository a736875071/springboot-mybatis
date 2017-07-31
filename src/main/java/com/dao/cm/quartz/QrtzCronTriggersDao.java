package com.dao.cm.quartz;

import com.entity.cm.quartz.QrtzCronTriggers;

public interface QrtzCronTriggersDao {
    int deleteByPrimaryKey(Long id);

    int insert(QrtzCronTriggers record);

    int insertSelective(QrtzCronTriggers record);

    QrtzCronTriggers selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(QrtzCronTriggers record);

    int updateByPrimaryKey(QrtzCronTriggers record);
}