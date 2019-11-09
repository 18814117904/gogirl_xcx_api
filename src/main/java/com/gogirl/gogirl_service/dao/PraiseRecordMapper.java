package com.gogirl.gogirl_service.dao;

import org.apache.ibatis.annotations.Mapper;

import com.gogirl.gogirl_service.entity.PraiseRecord;
@Mapper
public interface PraiseRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PraiseRecord record);

    int insertSelective(PraiseRecord record);
    
    PraiseRecord selectByPrimaryKey(Integer id);
    PraiseRecord selectByCustomerIdAndServ(PraiseRecord praiseRecord);

    int updateByPrimaryKeySelective(PraiseRecord record);

    int updateByPrimaryKey(PraiseRecord record);
}