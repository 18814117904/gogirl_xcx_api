package com.gogirl.gogirl_xcx.util;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gogirl.gogirl_order.order_commons.dto.OrderManage;
import com.gogirl.gogirl_order.order_commons.dto.OrderRecord;
import com.gogirl.gogirl_order.order_commons.dto.OrderServe;
import com.gogirl.gogirl_user.entity.Customer;
import com.gogirl.gogirl_user.entity.CustomerDetail;

public class CheckUtil {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	/**
     * 利用正则表达式判断字符串是否是数字
     * @param str
     * @return
     */
    public static boolean isNumeric(String str){
           Pattern pattern = Pattern.compile("[0-9]*");
           Matcher isNum = pattern.matcher(str);
           if( !isNum.matches() ){
               return false;
           }
           return true;
    }
	public static boolean isPhone(String phone) {
		String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
		if (phone.length() != 11) {
			return false;
		} else {
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(phone);
			boolean isMatch = m.matches();
			return isMatch;
		}
	}
	public static boolean isInteger(String str) {  
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");  
		return pattern.matcher(str).matches();  
	}
	public static double countOrderDataRate(Customer c, OrderManage om) {
		double dataRate = 0;
		
		if(c!=null&&c.getCustomerDetail()!=null){
			CustomerDetail cd = c.getCustomerDetail();
			if(cd.getAge()!=null&&cd.getAge()!=0){
				dataRate+=10;
			}
			if(cd.getJob()!=null&&!cd.getJob().isEmpty()){
				dataRate+=10;
			}
			if(cd.getPreference()!=null&&!cd.getPreference().isEmpty()){
				dataRate+=10;
			}
			if(cd.getCharacter()!=null&&!cd.getCharacter().isEmpty()){
				dataRate+=10;
			}
		}
		if(om!=null){
			List<OrderServe> listos = om.getListOrderServer();
			
			if(listos!=null){
				String userFeedback = null;
				String technicianFeedback = null;
				String picturePath = null;
				Iterator<OrderServe> it =  listos.iterator();
				while(it.hasNext()){
					OrderServe os = it.next();
					if(os!=null){
						OrderRecord or = os.getOrderRecord();
						if(or!=null){
							if(or.getUserFeedback()!=null&&!or.getUserFeedback().isEmpty()){
								userFeedback = or.getUserFeedback();
							}
							if(or.getTechnicianFeedback()!=null&&!or.getTechnicianFeedback().isEmpty()){
								technicianFeedback = or.getTechnicianFeedback();
							}
							if(or.getPicturePath()!=null&&!or.getPicturePath().isEmpty()){
								picturePath = or.getPicturePath();
							}
						}
					}
				}
				if(userFeedback!=null){
					dataRate+=10;
				}
				if(technicianFeedback!=null){
					dataRate+=10;
				}
				if(picturePath!=null){
					dataRate+=40;
				}
			}
		}
		return dataRate;
	}
}
