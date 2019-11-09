package com.gogirl.gogirl_user.dao;

import com.gogirl.gogirl_user.entity.SysMemberAuthority;

public interface SysMemberAuthorityMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysMemberAuthority record);

    int insertSelective(SysMemberAuthority record);

    SysMemberAuthority selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysMemberAuthority record);

    int updateByPrimaryKey(SysMemberAuthority record);
}