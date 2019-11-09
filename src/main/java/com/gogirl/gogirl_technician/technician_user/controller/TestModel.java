package com.gogirl.gogirl_technician.technician_user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yinyong on 2019/5/20.
 */
@Controller
public class TestModel {

    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "firstModel")
    public String firstModel(Model model, HttpServletRequest request, HttpServletResponse response) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        for(int i = 0; i < 10; i++) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("id", "id:"+i);
            map.put("title", "title:"+i);
            map.put("summary", "summary:"+i);
            map.put("createTime", "createTime:"+i);
            list.add(map);
        }
        model.addAttribute("list", list);
        return "modelfile";
    }
}
