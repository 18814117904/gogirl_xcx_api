package com.gogirl.gogirl_service.controller.user;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gogirl.gogirl_order.order_commons.utils.JsonResult;
import com.gogirl.gogirl_service.entity.User;
import com.gogirl.gogirl_service.service.service.user.AuthorityService;
import com.gogirl.gogirl_service.service.service.user.UserService;
import com.gogirl.gogirl_technician.technician_commons.dto.UserManage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yinyong on 2018/8/28.
 */

@RestController(value = "sysUserController")
@RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "user")
public class UserController {

    @Resource(name = "sysUserService")
    private UserService userService;

    @Autowired
    private AuthorityService authorityService;

    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "queryUserForPage")
    public JsonResult queryUserForPage(User user, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        String pageNum = request.getParameter("pageNum");
        String pageSize = request.getParameter("pageSize");
        PageHelper.startPage(Integer.parseInt(pageNum),Integer.parseInt(pageSize));
        List<User> lists = userService.listUserForPage(user);
        PageInfo<User> pageInfo = new PageInfo<User>(lists);
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(pageInfo);
        return jsonResult;
    }

    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "queryUserForCheck")
    public JsonResult queryUserForCheck(User user, HttpServletRequest request, HttpServletResponse response){
    	if(user==null){
    		return new JsonResult<>(false ,"请输入用户名和密码");
    	}
    	if(user.getName()==null){
    		return new JsonResult<>(false ,"请输入用户名");
    	}
    	if(user.getPassword()==null){
    		return new JsonResult<>(false ,"请输入密码");
    	}
    	
        JsonResult jsonResult = new JsonResult();
        User user1 = userService.getUserForCheck(user);
        if(user1 != null){
            jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(user1);
        }else{
            jsonResult.setSuccess(JsonResult.CODE_ERROR).setMessage("用户名或密码错误！");
        }
        return jsonResult;
    }

    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "queryMenuByPermission")
    public JsonResult queryMenuByPermission(Integer id, HttpServletRequest request, HttpServletResponse response) {
        JsonResult jsonResult = new JsonResult();
        User user = userService.getUserForDetail(id);
        String [] permission = user.getPermissionList().split(",");
        Map map = new HashMap();
        map.put("list",Arrays.asList(permission));
        map.put("type", "1");
        map.put("pid", "0");
        List<Map> listMenu = authorityService.getMenuByPermission(map);
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(listMenu);
        return jsonResult;
    }

    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "queryMenuByPermissionForStore")
    public JsonResult queryMenuByPermissionForStore(Integer id, HttpServletRequest request, HttpServletResponse response) {
        JsonResult jsonResult = new JsonResult();
        User user = userService.getUserForDetail(id);
        String [] permission = user.getPermissionList().split(",");
        Map map = new HashMap();
        map.put("list",Arrays.asList(permission));
        map.put("type", "2");
        map.put("pid", "0");
        List<Map> listMenu = authorityService.getMenuByPermission(map);
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(listMenu);
        return jsonResult;
    }

    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "queryUserForDetail")
    public JsonResult queryUserForDetail(Integer id, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        User user = userService.getUserForDetail(id);
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(user);
        return jsonResult;
    }

    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "deleteUser")
    public JsonResult deleteUser(Integer id, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        int result = userService.deleteUser(id);
        if(result > 0){
            jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC);
        }
        return jsonResult;
    }

    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "addUser")
    public JsonResult addUser(User user, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        int result = userService.insertUser(user);
        if(result > 0){
            jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC);
        }
        return jsonResult;
    }

    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "modifyUser")
    public JsonResult modifyUser(User user, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        int result = userService.updateUser(user);
        if(result > 0){
            jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC);
        }
        return jsonResult;
    }
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "changePass")
    public JsonResult changePass(Integer id,String oldPass,String newPass, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        User qUser = userService.getUserForDetail(id);
        if(qUser!=null&&qUser.getPassword()!=null&&oldPass!=null&&oldPass.equals(qUser.getPassword())){
            User user = new User();
            user.setId(id);
            user.setPassword(newPass);
            int result = userService.updateUser(user);
            if(result > 0){
                jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC);
            }
        }else{
        	jsonResult.setSuccess(JsonResult.CODE_ERROR).setMessage("原密码不正确");
        }
        return jsonResult;
    }
}
