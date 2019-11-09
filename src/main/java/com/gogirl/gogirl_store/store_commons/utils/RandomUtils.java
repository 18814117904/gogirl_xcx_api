package com.gogirl.gogirl_store.store_commons.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by yinyong on 2018/9/28.
 */
public class RandomUtils {

    public static String random(String prefix){
        StringBuffer buffer = new StringBuffer();
        /*SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");*/
        Random random = new Random();
        for(int i = 0; i < 8; i++){
            buffer.append(random.nextInt(10));
        }
        return prefix + buffer.toString();
    }

    public static String randomMessage(){
        StringBuffer buffer = new StringBuffer();
        Random random = new Random();
        for(int i = 0; i < 6; i++){
            buffer.append(random.nextInt(10));
        }
        return buffer.toString();
    }
}
