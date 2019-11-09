package com.gogirl.gogirl_service.dao.user;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.gogirl.gogirl_service.entity.User;

import java.util.List;

/**
 * Created by yinyong on 2018/8/28.
 */
@Mapper
@Repository
public interface UserMapper {

    List<User> listUserForPage(User user);

    User getUserForCheck(User user);

    User getUserForDetail(Integer id);

    int deleteUser(Integer id);

    int insertUser(User user);

    int updateUser(User user);

    int updateLoginTime(Integer id);
}
