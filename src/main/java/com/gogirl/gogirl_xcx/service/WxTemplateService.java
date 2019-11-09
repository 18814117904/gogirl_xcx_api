package com.gogirl.gogirl_xcx.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.annotation.Resource;

import me.chanjar.weixin.common.error.WxErrorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.gogirl.gogirl_xcx.dao.TemplateMapper;
import com.gogirl.gogirl_xcx.entity.Template;



@Configuration          //证明这个类是一个配置文件
@EnableScheduling       //打开quartz定时器总开关

@Service
public class WxTemplateService {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	@Resource
	TemplateMapper templateDao;
	
	public List<Template> selectByTemplate(Template template){
		return templateDao.selectByTemplate(template);
	}
	public Template selectByPrimaryKey(Integer id){
		return templateDao.selectByPrimaryKey(id);
	}
	public int updateByPrimaryKeySelective(Template record){
		return templateDao.updateByPrimaryKeySelective(record);
	}
	
	
	
//	public String sendMessage(String appid,String openid,List<WxMpTemplateData> list){
//    	final WxMpService wxService = WxMpConfiguration.getMpServices().get(appid);
//    	WxMpTemplateMsgService templateMsgService = wxService.getTemplateMsgService();
//    	WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
//    	.url("http://test.begogirls.com")
//    	.templateId("2bYDkeQJ9ER0wgroFDkwbQLR8zTHHY0NxTexerHZHwk")
//    	.toUser(openid)
//    	.data(list)
//    	.build();
//    	// 设置消息的内容等信息
//    	try {
//    		
//			return templateMsgService.sendTemplateMsg(templateMessage);
//		} catch (WxErrorException e) {
//			logger.error(e.getMessage());
//			e.printStackTrace();
//			return null;
//		}
//
//	}
//	
//	public List<WxMpTemplate> getAllPrivateTemplate(String appid) throws WxErrorException{
//    	final WxMpService wxService = WxMpConfiguration.getMpServices().get(appid);
//    	WxMpTemplateMsgService templateMsgService = wxService.getTemplateMsgService();
//		List<WxMpTemplate> list= templateMsgService.getAllPrivateTemplate();
//		return list;
//	}
//	public String sendTemplateMsg(String appid,String openid,String url,String templateId,List<WxMpTemplateData> list) throws WxErrorException{
//    	final WxMpService wxService = WxMpConfiguration.getMpServices().get(appid);
//    	WxMpTemplateMsgService templateMsgService = wxService.getTemplateMsgService();
//    	WxMpTemplateMessageBuilder builder = WxMpTemplateMessage.builder()
//    	.url(url)
////    	.miniProgram(new MiniProgram(appid, pagePath, usePath))
//    	.templateId(templateId)
//    	.toUser(openid)
//    	.data(list);
//    	if(appid.equals("wx058b46db3cd0e979")){
//        	MiniProgram miniProgram = new MiniProgram();
//        	miniProgram.setAppid(appid);
//        	miniProgram.setPagePath("http://test.begogirls.com/members");
//        	miniProgram.setUsePath(false);
//        	builder.miniProgram(miniProgram);
//    	}
//    	WxMpTemplateMessage templateMessage = builder.build();
//
//    	// 设置消息的内容等信息
//		return templateMsgService.sendTemplateMsg(templateMessage);
//	}
//	public int sendTemplateMsgOpenidList(String appid,String[] openidList,String url,String templateId,List<WxMpTemplateData> list) throws WxErrorException{
//    	final WxMpService wxService = WxMpConfiguration.getMpServices().get(appid);
//    	WxMpTemplateMsgService templateMsgService = wxService.getTemplateMsgService();
//    	// 设置消息的内容等信息
//    	WxMpTemplateMessageBuilder builder = WxMpTemplateMessage.builder()
//    	.url(url)
//    	.templateId(templateId)
//    	.data(list);
//    	//便利发送所有的模板消息
//    	int size = openidList.length;
//    	
////    	//新建一条线程
////    	new Thread(new Runnable() {
////            @Override
////            public void run() {
//            	for(int i=0;i<size;i++){
//            		try {
//            			if(openidList[i]!=null&&!openidList[i].isEmpty()){
//                			WxMpTemplateMessage templateMessage = builder.toUser(openidList[i]).build();
//    						templateMsgService.sendTemplateMsg(templateMessage);
//            			}
//					} catch (WxErrorException e) {
//						logger.info("模板消息发送失败openid="+openidList[i]+".Exception:"+e.toString());
//					}
//            	}
////            }
////        }).start();
//    	return size;
//	}
}
