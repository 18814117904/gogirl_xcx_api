package com.gogirl.gogirl_service.service.impl.user;

import com.gogirl.gogirl_service.dao.user.AuthorityMapper;
import com.gogirl.gogirl_service.entity.Permission;
import com.gogirl.gogirl_service.service.service.user.AuthorityService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yinyong on 2019/5/7.
 */
@Service
public class AuthorityServiceImpl implements AuthorityService {

    @Autowired
    private AuthorityMapper authorityMapper;

    @Override
    public List<Map> getMenuByPermission(Map map) {
        List<Map> menuList = new ArrayList<Map>();
        List<Permission> list = authorityMapper.getMenuByPermission(map);
        for(Permission permission : list){
            Map menus = new HashMap();
            menus.put("id", permission.getId());
            menus.put("title", permission.getTitle());
            menus.put("url", permission.getUrl());
            menus.put("keyword", permission.getKeyword());
            menus.put("imgSrc", permission.getImgSrc());
            menus.put("status", permission.getStatus());
            menus.put("type", permission.getType());
            //    menus.put("sequence", res.getSequence());
            map.put("pid", permission.getId());
            menus.put("children", getMenuByPermission(map ));
            menuList.add(menus);
        }
        return menuList;
    }
}
