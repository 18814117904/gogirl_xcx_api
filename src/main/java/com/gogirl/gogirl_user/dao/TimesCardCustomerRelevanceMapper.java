package com.gogirl.gogirl_user.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.gogirl.gogirl_user.entity.TimesCardCustomerRelevance;
@Mapper
public interface TimesCardCustomerRelevanceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TimesCardCustomerRelevance record);

    int insertSelective(TimesCardCustomerRelevance record);
    
    TimesCardCustomerRelevance selectByPrimaryKey(Integer id);
    List<TimesCardCustomerRelevance> getTimesCardList(TimesCardCustomerRelevance record);

    int updateByPrimaryKeySelective(TimesCardCustomerRelevance record);

    int updateByPrimaryKey(TimesCardCustomerRelevance record);

	List<TimesCardCustomerRelevance> getMyTimesCardByOrderId(Integer orderId);
}