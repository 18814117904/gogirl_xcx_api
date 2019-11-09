package com.gogirl.gogirl_order.order_commons.utils;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yinyong on 2019/3/5.
 */
public class CountUtils {

    public static Map<String, Map<String, Object>> scheduleMap = new ConcurrentHashMap<String, Map<String, Object>>();

    public static Map<String, Map<String, Object>> orderMap = new ConcurrentHashMap<String, Map<String, Object>>();


    public static void main(String[] args) {
        List list = new ArrayList();
        list.add("45");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", list);
        map.put("num", 0);
        scheduleMap.put("12", map);
        System.out.println(scheduleMap.get("12"));

        Map<String, Object> map1 = scheduleMap.get("12");
        ((List)map1.get("code")).add("789");
        int  i = (Integer) map1.get("num") + 1;
        map1.put("num", i);

        System.out.println(scheduleMap.get("12"));
    }
}
