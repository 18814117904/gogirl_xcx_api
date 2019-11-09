package com.gogirl.gogirl_order.dao;

import org.apache.ibatis.annotations.Mapper;

import com.gogirl.gogirl_order.entity.PurchaseSku;
@Mapper
public interface PurchaseSkuMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PurchaseSku record);

    int insertSelective(PurchaseSku record);

    PurchaseSku selectByPrimaryKey(Integer id);
    PurchaseSku selectBySkuName(String skuName);

    int updateByPrimaryKeySelective(PurchaseSku record);

    int updateByPrimaryKeyWithBLOBs(PurchaseSku record);

    int updateByPrimaryKey(PurchaseSku record);
}