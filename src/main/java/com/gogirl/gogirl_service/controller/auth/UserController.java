package com.gogirl.gogirl_service.controller.auth;

import com.gogirl.gogirl_service.entity.GetByBLUserAcct;
import com.gogirl.gogirl_service.entity.UserGetByBLUserAcct;
import com.gogirl.gogirl_service.service.service.auth.UserService;
import com.gogirl.gogirl_service.service.service.auth.UserSyncService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by yinyong on 2018/8/20.
 */
@RestController
@RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "user")
public class UserController {

    @Autowired
    private UserSyncService userSyncService;

    @Autowired
    private UserService userService;

    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "getUserMsg")
    public GetByBLUserAcct getUserMsg(HttpServletRequest request, HttpServletResponse response){

        GetByBLUserAcct userByCookie = userSyncService.getUserByCookie(request);
        UserGetByBLUserAcct data = userByCookie.getData();

       // Integer integer = userService.saveUser(data);
        return userByCookie;
    }
}
