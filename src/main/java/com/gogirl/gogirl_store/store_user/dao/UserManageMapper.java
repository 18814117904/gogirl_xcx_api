package com.gogirl.gogirl_store.store_user.dao;

import com.gogirl.gogirl_store.store_commons.dto.UserManage;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by yinyong on 2018/9/17.
 */
@Mapper
@Repository
public interface UserManageMapper {

    List<UserManage> listUserManageForPage(UserManage userManage);

    List<UserManage> listUserManageForAll(UserManage userManage);

    List<UserManage> listUserForAllNotQuit(UserManage userManage);

    int updateUserManage(UserManage userManage);

    UserManage getUserManageForDetail(Integer id);

    UserManage checkLogin(UserManage userManage);

    int deleteUserManageById(Integer id);

    int insertUserManage(UserManage userManage);
	List<UserManage> getAllStoreUser();
}
