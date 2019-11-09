package com.gogirl.gogirl_xcx.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class XmlUtil {
	public static Map<String, String> readStringXml(String xml) throws DocumentException {
        Document doc = DocumentHelper.parseText(xml);
        Element books = doc.getRootElement();
        System.out.println("rootNode:" + books.getName());
        Iterator Elements = books.elementIterator();
        Map<String, String> map = new HashMap<>();
        while (Elements.hasNext()) {
            Element user = (Element) Elements.next();
            System.out.println("node=" + user.getName() + "\ttext=" + user.getText());
            map.put(user.getName(), user.getText());
        }
        return map;
    }
//	public static void main(String [] arr) {//return_code=SUCCESS,,result_code=SUCCESS
//		String test = "<xml><appid><![CDATA[wxd5e3224465422206]]></appid>"+
//				"<attach><![CDATA[附属字段]]></attach>"+
//				"<bank_type><![CDATA[CFT]]></bank_type>"+
//				"<cash_fee><![CDATA[1]]></cash_fee>"+
//				"<device_info><![CDATA[暂时写死的设备号码]]></device_info>"+
//				"<fee_type><![CDATA[CNY]]></fee_type>"+
//				"<is_subscribe><![CDATA[N]]></is_subscribe>"+
//				"<mch_id><![CDATA[1516547781]]></mch_id>"+
//				"<nonce_str><![CDATA[1566272536463]]></nonce_str>"+
//				"<openid><![CDATA[o_79H49JPJ_y915zVgedcg6PqVIM]]></openid>"+
//				"<out_trade_no><![CDATA[14276551]]></out_trade_no>"+
//				"<result_code><![CDATA[SUCCESS]]></result_code>"+
//				"<return_code><![CDATA[SUCCESS]]></return_code>"+
//				"<sign><![CDATA[ACA2BF8B124B4E7998456FE80CE3A73B]]></sign>"+
//				"<time_end><![CDATA[20190820114136]]></time_end>"+
//				"<total_fee>1</total_fee>"+
//				"<trade_type><![CDATA[JSAPI]]></trade_type>"+
//				"<transaction_id><![CDATA[4200000341201908207746773792]]></transaction_id>"+
//				"</xml>";
//				try {
//			Map<String, String> map = XmlUtil.readStringXml(test);
//			System.out.println(map);
//		} catch (DocumentException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
}
