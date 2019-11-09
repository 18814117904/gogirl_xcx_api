package com.gogirl.gogirl_user.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.gogirl.gogirl_store.store_commons.dto.UserManage;


@Mapper
public interface StoreUserMapper2 {
    int deleteByPrimaryKey(Integer id);

    int insert(UserManage record);

    int insertSelective(UserManage record);

    UserManage selectByPrimaryKey(Integer id);
    List<UserManage> getAllUserManage();

    int updateByPrimaryKeySelective(UserManage record);

    int updateByPrimaryKeyWithBLOBs(UserManage record);

    int updateByPrimaryKey(UserManage record);
}