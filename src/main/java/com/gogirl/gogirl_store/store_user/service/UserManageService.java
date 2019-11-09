package com.gogirl.gogirl_store.store_user.service;

import com.gogirl.gogirl_store.store_commons.dto.UserManage;

import java.util.List;

/**
 * Created by yinyong on 2018/9/17.
 */
public interface UserManageService {

    List<UserManage> listUserManageForPage(UserManage userManage);

    List<UserManage> listUserManageForAll(UserManage userManage);

    List<UserManage> listUserForAllNotQuit(UserManage userManage);

  int updateUserManage(UserManage userManage);

    UserManage getUserManageForDetail(Integer id);

    UserManage checkLogin(UserManage userManage);

    int deleteUserManageById(Integer id);

    int insertUserManage(UserManage userManage);

	List<UserManage> selectByopenid(String openid);
}
