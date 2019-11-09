package com.gogirl.gogirl_service.service.service.user;

import com.gogirl.gogirl_service.entity.User;

import java.util.List;

/**
 * Created by yinyong on 2018/8/28.
 */
public interface UserService {

    List<User> listUserForPage(User user);

    User getUserForCheck(User user);

    User getUserForDetail(Integer id);

    int deleteUser(Integer id);

    int insertUser(User user);

    int updateUser(User user);
}
