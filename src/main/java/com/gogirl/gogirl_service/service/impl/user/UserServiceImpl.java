package com.gogirl.gogirl_service.service.impl.user;

import com.gogirl.gogirl_service.dao.user.UserMapper;
import com.gogirl.gogirl_service.entity.User;
import com.gogirl.gogirl_service.service.service.user.UserService;
import com.gogirl.gogirl_service.utils.RandomUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by yinyong on 2018/8/28.
 */
@Service(value = "sysUserService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> listUserForPage(User user) {
        return userMapper.listUserForPage(user);
    }

    @Override
    public User getUserForCheck(User user) {
        User reuser = userMapper.getUserForCheck(user);
        if(reuser != null){
            userMapper.updateLoginTime(reuser.getId());
        }
        return reuser;
    }

    @Override
    public User getUserForDetail(Integer id) {
        return userMapper.getUserForDetail(id);
    }

    @Override
    public int deleteUser(Integer id) {
        return userMapper.deleteUser(id);
    }

    @Override
    public int insertUser(User user) {
        user.setNo(createUserNO());
        return userMapper.insertUser(user);
    }

    @Override
    public int updateUser(User user) {
        return userMapper.updateUser(user);
    }

    public String createUserNO(){
        String sole = RandomUtils.random("ORD");
        sole = RandomUtils.random("ORD");
        return sole;
    }
}
