package com.gogirl.gogirl_service.dao.user;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.gogirl.gogirl_service.entity.Permission;

import java.util.List;
import java.util.Map;

/**
 * Created by yinyong on 2019/5/7.
 */
@Mapper
@Repository
public interface AuthorityMapper {

    List<Permission> getMenuByPermission(Map map);
}
