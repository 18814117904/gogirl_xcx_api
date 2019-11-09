package com.gogirl.gogirl_xcx.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.gogirl.gogirl_xcx.entity.XcxFormId;
@Mapper
public interface XcxFormIdMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(XcxFormId record);

    int insertSelective(XcxFormId record);
    Integer countNumByXcxFormId(XcxFormId xcxFormId);
    List<XcxFormId> selectByXcxFormId(XcxFormId xcxFormId);
    XcxFormId selectByPrimaryKey(Integer id);
    XcxFormId selectFormIdByOpenid(@Param(value = "openid")String openid,@Param(value = "date")Date date);

    int updateByPrimaryKeySelective(XcxFormId record);

    int updateByPrimaryKey(XcxFormId record);
}