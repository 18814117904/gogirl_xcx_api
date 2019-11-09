package com.gogirl.gogirl_service.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnalysisPicturePath {

    public static int appearNumber(String srcText, String findText) {
        int count = 0;
        Pattern p = Pattern.compile(findText);
        Matcher m = p.matcher(srcText);
        while (m.find()) {
            count++;
        }
        return count;
    }

    public static String analysis(String str){
        StringBuffer stringBuffer = new StringBuffer();
        int number = appearNumber(str,"src=\"");

        for(int i = 0 ; i < number; i++){
            int start = str.indexOf("src=");
            String picturePath = "";
            str = str.substring(start+5);
            picturePath = str.substring(0, str.indexOf("\""));
            if(i == number - 1){
                stringBuffer.append(picturePath);
            }else {
                stringBuffer.append(picturePath).append(",");
            }
        }
        return  stringBuffer.toString();
    }
}
