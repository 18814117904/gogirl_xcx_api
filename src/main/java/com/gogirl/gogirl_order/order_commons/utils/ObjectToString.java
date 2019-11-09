package com.gogirl.gogirl_order.order_commons.utils;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by yinyong on 2018/9/20.
 */
public class ObjectToString {

    public static String parseURLPair(Object o) {
        try {
            Class<? extends Object> c = o.getClass();
            Field[] fields = c.getDeclaredFields();
            Map<String, Object> map = new TreeMap<String, Object>();
            for (Field field : fields) {
                field.setAccessible(true);
                String name = field.getName();
                Object value = field.get(o);
                if (value != null)
                    map.put(name, value);
            }
            Set<Map.Entry<String, Object>> set = map.entrySet();
            Iterator<Map.Entry<String, Object>> it = set.iterator();
            StringBuffer sb = new StringBuffer();
            while (it.hasNext()) {
                Map.Entry<String, Object> e = it.next();
                sb.append(e.getKey()).append("=").append(e.getValue()).append("&");
            }
            return sb.deleteCharAt(sb.length()-1).toString();
        }catch(Exception e){
            return "参数错误！";
        }

    }
}
