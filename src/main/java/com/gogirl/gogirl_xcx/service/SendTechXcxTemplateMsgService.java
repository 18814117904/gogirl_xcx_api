package com.gogirl.gogirl_xcx.service;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import cn.binarywang.wx.miniapp.bean.WxMaTemplateData;
import cn.binarywang.wx.miniapp.bean.WxMaTemplateMessage;
import me.chanjar.weixin.common.error.WxErrorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import springfox.documentation.spring.web.json.Json;

import com.gogirl.gogirl_order.order_commons.utils.JsonResult;
import com.gogirl.gogirl_user.constant.GogirlProperties;
import com.gogirl.gogirl_xcx.config.WxMaConfiguration;
import com.gogirl.gogirl_xcx.dao.XcxFormIdMapper;
import com.gogirl.gogirl_xcx.entity.Template;
import com.gogirl.gogirl_xcx.entity.XcxFormId;
import com.gogirl.gogirl_xcx.service.WxTemplateService;

/**
 * @author Edward
 */
@Service
public class SendTechXcxTemplateMsgService {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	@Resource
	WxTemplateService wxTemplateService;
	@Resource
	GogirlProperties gogirlProperties;
	@Resource
	XcxFormIdMapper xcxFormIdMapper;
	
	/**pages/mine/mine-orders
	 * 被预约通知
	 */
	@ResponseBody
    @RequestMapping("/sendBeBookedMsg")
    public JsonResult<String> sendBeBookedMsg(String openid,String url,
			String time,String storeName,String technicianName,String serveName,String customerName,String customerPhone)  {
		if(openid==null)return new JsonResult<String>(false,JsonResult.APP_DEFINE_ERR,null);
		Date date = new Date(new Date().getTime()-518400000);
		XcxFormId formId = xcxFormIdMapper.selectFormIdByOpenid(openid, date);
		if(formId==null||formId.getFormId()==null||formId.getFormId().equals("the formId is a mock one")){
			logger.info("找不到可用的formId");
			return new JsonResult<>(false,"找不到可用的formId");
		}else{
			xcxFormIdMapper.deleteByPrimaryKey(formId.getId());
		}
		
		if(url==null||url.isEmpty()){url = "pages/order/orders";}
    	if(time==null){time="";}
    	if(storeName==null){storeName="gogirl美甲美睫沙龙";}
    	if(technicianName==null){technicianName="";}
    	if(serveName==null){serveName="";}
    	if(customerName==null){customerName="";}
    	if(customerPhone==null){customerPhone="";}
    	Template template = wxTemplateService.selectByPrimaryKey(14);//根据id拿到预约的模板
    	if(template==null){
    		return new JsonResult<String>(false,"找不到预约提醒模板",null);
    	}
    	List<WxMaTemplateData> list = new ArrayList<WxMaTemplateData>();
    	list.add(new WxMaTemplateData("first", template.getFirst(),"red"));
    	list.add(new WxMaTemplateData("remark", template.getRemark(),"#ff0043"));
    	list.add(getWxMaTemplateData("time",time,template,null));
    	list.add(getWxMaTemplateData("storeName",storeName,template,null));
    	list.add(getWxMaTemplateData("technicianName",technicianName,template,null));
    	list.add(getWxMaTemplateData("serveName",serveName,template,null));
    	list.add(getWxMaTemplateData("customerName",customerName,template,null));
    	list.add(getWxMaTemplateData("customerPhone",customerPhone,template,null));

    	WxMaTemplateMessage templateMessage = WxMaTemplateMessage.builder()
    			.formId(formId.getFormId())
    	.templateId(template.getTemplateId())
    	.toUser(openid)
    	.page(url)
    	.data(list)
    	.build();
    	try {
			WxMaConfiguration.getWxMaService().getMsgService().sendTemplateMsg(templateMessage);
		} catch (WxErrorException e) {
			logger.info("发送美甲师端模板消息报错e:"+e.getMessage());
			e.printStackTrace();
			return new JsonResult<String>(false,"发送美甲师端模板消息报错e:"+e.getMessage());
		}
    	return new JsonResult<String>(true,JsonResult.APP_DEFINE_SUC,"发送成功");
    }
	/**pages/mine/mine-orders
	 * 支付成功
	 */
	@ResponseBody
    @RequestMapping("/sendPaySuccessMsg")
    public JsonResult<String> sendPaySuccessMsg(String openid,String url,
			String orderNo,String serveName,String amount,String time,String payType,String customerName,String customerPhone)  {
		if(openid==null)return new JsonResult<String>(false,JsonResult.APP_DEFINE_ERR,null);
		Date date = new Date(new Date().getTime()-518400000);
		XcxFormId formId = xcxFormIdMapper.selectFormIdByOpenid(openid, date);
		if(formId==null||formId.getFormId()==null||formId.getFormId().equals("the formId is a mock one")){
			logger.info("找不到可用的formId");
			return new JsonResult<>(false,"找不到可用的formId");
		}else{
			xcxFormIdMapper.deleteByPrimaryKey(formId.getId());
		}
		
		if(url==null||url.isEmpty()){url = "pages/order/orders";}
    	if(orderNo==null){orderNo="";}
    	if(serveName==null){serveName="";}
    	if(amount==null){amount="";}
    	if(time==null){time="";}
    	if(payType==null){payType="";}
    	if(customerName==null){customerName="";}
    	if(customerPhone==null){customerPhone="";}
    	Template template = wxTemplateService.selectByPrimaryKey(15);//根据id拿到预约的模板
    	if(template==null){
    		return new JsonResult<String>(false,"找不到预约提醒模板",null);
    	}
    	List<WxMaTemplateData> list = new ArrayList<WxMaTemplateData>();
    	list.add(new WxMaTemplateData("first", template.getFirst(),"red"));
    	list.add(new WxMaTemplateData("remark", template.getRemark(),"#ff0043"));
    	list.add(getWxMaTemplateData("orderNo",orderNo,template,null));
    	list.add(getWxMaTemplateData("serveName",serveName,template,null));
    	list.add(getWxMaTemplateData("amount",amount,template,null));
    	list.add(getWxMaTemplateData("time",time,template,null));
    	list.add(getWxMaTemplateData("payType",payType,template,null));
    	list.add(getWxMaTemplateData("customerName",customerName,template,null));
    	list.add(getWxMaTemplateData("customerPhone",customerPhone,template,null));

    	WxMaTemplateMessage templateMessage = WxMaTemplateMessage.builder()
    			.formId(formId.getFormId())
    	.templateId(template.getTemplateId())
    	.toUser(openid)
    	.page(url)
    	.data(list)
    	.build();
    	try {
			WxMaConfiguration.getWxMaService().getMsgService().sendTemplateMsg(templateMessage);
		} catch (WxErrorException e) {
			logger.info("发送美甲师端模板消息报错e:"+e.getMessage());
			e.printStackTrace();
			return new JsonResult<String>(false,"发送美甲师端模板消息报错e:"+e.getMessage());
		}
    	return new JsonResult<String>(true,JsonResult.APP_DEFINE_SUC,"发送成功");
    }

    //判断对应的是哪个keyword,返回那个WxMpTemplateData
    public WxMaTemplateData getWxMaTemplateData(String key,String value,Template template,String color) {
    	if(template.getKeyword1 ()!=null&&template.getKeyword1 ().equals(key)){return new WxMaTemplateData("keyword1", value,color);}
    	if(template.getKeyword2 ()!=null&&template.getKeyword2 ().equals(key)){return new WxMaTemplateData("keyword2", value,color);}
    	if(template.getKeyword3 ()!=null&&template.getKeyword3 ().equals(key)){return new WxMaTemplateData("keyword3", value,color);}
    	if(template.getKeyword4 ()!=null&&template.getKeyword4 ().equals(key)){return new WxMaTemplateData("keyword4", value,color);}
    	if(template.getKeyword5 ()!=null&&template.getKeyword5 ().equals(key)){return new WxMaTemplateData("keyword5", value,color);}
    	if(template.getKeyword6 ()!=null&&template.getKeyword6 ().equals(key)){return new WxMaTemplateData("keyword6", value,color);}
    	if(template.getKeyword7 ()!=null&&template.getKeyword7 ().equals(key)){return new WxMaTemplateData("keyword7", value,color);}
    	if(template.getKeyword8 ()!=null&&template.getKeyword8 ().equals(key)){return new WxMaTemplateData("keyword8", value,color);}
    	if(template.getKeyword9 ()!=null&&template.getKeyword9 ().equals(key)){return new WxMaTemplateData("keyword9", value,color);}
    	if(template.getKeyword10()!=null&&template.getKeyword10().equals(key)){return new WxMaTemplateData("keyword10", value,color);}
    	if(template.getKeyword11()!=null&&template.getKeyword11().equals(key)){return new WxMaTemplateData("keyword11", value,color);}
    	if(template.getKeyword12()!=null&&template.getKeyword12().equals(key)){return new WxMaTemplateData("keyword12", value,color);}
    	if(template.getKeyword13()!=null&&template.getKeyword13().equals(key)){return new WxMaTemplateData("keyword13", value,color);}
    	if(template.getKeyword14()!=null&&template.getKeyword14().equals(key)){return new WxMaTemplateData("keyword14", value,color);}
    	if(template.getKeyword15()!=null&&template.getKeyword15().equals(key)){return new WxMaTemplateData("keyword15", value,color);}
    	return null;
	}
}
