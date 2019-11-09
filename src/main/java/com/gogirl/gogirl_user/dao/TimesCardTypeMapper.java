package com.gogirl.gogirl_user.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.gogirl.gogirl_order.order_commons.dto.OrderServe;
import com.gogirl.gogirl_user.entity.TimesCardServeRelevance;
import com.gogirl.gogirl_user.entity.TimesCardType;
@Mapper
public interface TimesCardTypeMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(TimesCardType record);

    TimesCardType selectByPrimaryKey(Integer id);
    
    int updateByPrimaryKeySelective(TimesCardType record);

	List<TimesCardType> getTimesCardTypeList();
    List<TimesCardType> listOrderCanUseCardByServeType(Integer orderId);
    List<TimesCardType> listOrderCanUseCardByServe(Integer orderId);

}