package com.gogirl.gogirl_user.dao;

import org.apache.ibatis.annotations.Mapper;

import com.gogirl.gogirl_user.entity.TimesCardUsedRecord;
@Mapper
public interface TimesCardUsedRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TimesCardUsedRecord record);

    int insertSelective(TimesCardUsedRecord record);

    TimesCardUsedRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TimesCardUsedRecord record);

    int updateByPrimaryKey(TimesCardUsedRecord record);

	int cancelTimesCardByOrderId(Integer orderId);
}