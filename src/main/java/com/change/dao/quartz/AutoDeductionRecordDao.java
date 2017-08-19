package com.change.dao.quartz;

import com.change.entity.quartz.AutoDeductionRecord;

public interface AutoDeductionRecordDao {
    int deleteByPrimaryKey(Long id);

    int insert(AutoDeductionRecord record);

    int insertSelective(AutoDeductionRecord record);

    AutoDeductionRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AutoDeductionRecord record);

    int updateByPrimaryKey(AutoDeductionRecord record);
}