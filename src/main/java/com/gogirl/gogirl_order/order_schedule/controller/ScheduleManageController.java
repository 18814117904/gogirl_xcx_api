package com.gogirl.gogirl_order.order_schedule.controller;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gogirl.gogirl_order.order_commons.config.Constans;
import com.gogirl.gogirl_order.order_commons.dto.OrderManage;
import com.gogirl.gogirl_order.order_commons.dto.OrderServe;
import com.gogirl.gogirl_order.order_commons.dto.ScheduleManage;
import com.gogirl.gogirl_order.order_commons.dto.ScheduleServe;
import com.gogirl.gogirl_order.order_commons.utils.JsonResult;
import com.gogirl.gogirl_order.order_manage.service.OrderManageService;
import com.gogirl.gogirl_order.order_schedule.service.ScheduleManageService;
import com.gogirl.gogirl_order.order_schedule.service.ScheduleServeService;
import com.gogirl.gogirl_service.entity.Serve;
import com.gogirl.gogirl_service.service.service.serve.ServeService;
import com.gogirl.gogirl_store.store_classes.service.ClassesManageService;
import com.gogirl.gogirl_store.store_classes.service.ClassesTechnicianService;
import com.gogirl.gogirl_store.store_commons.dto.ClassesManage;
import com.gogirl.gogirl_store.store_commons.dto.ClassesTechnician;
import com.gogirl.gogirl_store.store_commons.dto.ShopManage;
import com.gogirl.gogirl_store.store_shop.service.ShopManageService;
import com.gogirl.gogirl_technician.technician_commons.dto.TechnicianManage;
import com.gogirl.gogirl_technician.technician_user.service.TechnicianManageService;
import com.gogirl.gogirl_user.entity.Customer;
import com.gogirl.gogirl_user.service.CustomerService;
import com.gogirl.gogirl_xcx.dao.XcxFormIdMapper;
import com.gogirl.gogirl_xcx.entity.GogirlToken;
import com.gogirl.gogirl_xcx.entity.XcxFormId;
import com.gogirl.gogirl_xcx.service.GogirlTokenService;
import com.gogirl.gogirl_xcx.service.SendTechXcxTemplateMsgService;
import com.gogirl.gogirl_xcx.service.SendXcxTemplateMsgService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import springfox.documentation.annotations.ApiIgnore;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.annotation.Resource;
import javax.print.DocFlavor.STRING;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

@Api(tags = { "6.预约" })
@RestController
@RequestMapping("schedule")
public class ScheduleManageController {
    private Logger logger = LoggerFactory.getLogger(ScheduleManageController.class);
    @Autowired
    private ScheduleManageService scheduleManageService;
	@Resource
	ServeService serveService;
    @Value("${store-url}")
    private String storeUrl;
    @Value("${technician-url}")
    private String technicianUrl;
    @Value("${start-time}")
    private String dayStartTime;
    @Value("${end-time}")
    private String dayEndTime;
    @Resource
    GogirlTokenService gogirlTokenService ;
    @Resource
    TechnicianManageService technicianManageService;
    @Resource
    private RestTemplate restTemplate;
    @Resource
    OrderManageService orderManageService;
    @Resource
    ClassesTechnicianService classesTechnicianService;
	@Resource
	XcxFormIdMapper xcxFormIdMapper;
	@Resource
	SendXcxTemplateMsgService sendXcxTemplateMsgService;
	@Resource
	SendTechXcxTemplateMsgService sendTechXcxTemplateMsgService;
	@Resource
	ShopManageService shopManageService;
	@Resource
	CustomerService customerService;
	@Resource
	ClassesManageService classesManageService;
	@Resource
	ScheduleServeService scheduleServeService;
	
    /*1.查询可预约的时间列表*/
    @ApiOperation(value = "new 1.查询可预约的时间列表", notes = "{\"departmentId\":19,\"formId\":\"the formId is a mock one\",\"token\":\"91c23f20e3\",\"scheduleDate\":\"2019-10-29\",\"listScheduleServer\":[{\"serveId\":124,\"mainServeId\":153,\"lengthTimeForEndTime\":30},{\"serveId\":153,\"lengthTimeForEndTime\":90}]}")
    @RequestMapping(method={RequestMethod.POST},value = "queryIdleTime")
    public JsonResult<?> queryIdleTime (@RequestBody ScheduleManage scheduleManage) {
    	logger.info("查询可预约的时间列表");
    	//入参判断ScheduleManage
    	if(scheduleManage==null){return new JsonResult<>(false,"入参为空");}
    	if(scheduleManage.getToken()==null){return new JsonResult<>(false,String.format(JsonResult.PARAM_NULL,"token"));}
    	if(scheduleManage.getDepartmentId()==null){return new JsonResult<>(false,String.format(JsonResult.PARAM_NULL,"departmentId"));}
    	if(scheduleManage.getScheduleDate()==null){return new JsonResult<>(false,"scheduleDate为空,格式为'2019-10-09'");}
    	if(scheduleManage.getListScheduleServer()==null||scheduleManage.getListScheduleServer().size()==0){return new JsonResult<>(false,"服务为空,请至少选择1个服务再预约");}
    	//查找用户
    	GogirlToken gogirltoken = gogirlTokenService.getTokenByToken(scheduleManage.getToken());
    	if(gogirltoken==null||gogirltoken.getCustomerId()==null){
    		return new JsonResult<>(false,JsonResult.TOKEN_NULL_CODE);
    	}
    	//保存formId
    	if(scheduleManage.getFormId()!=null&&!scheduleManage.getFormId().equals("the formId is a mock one")&&gogirltoken.getCustomer().getOpenid1()!=null){
    		XcxFormId xcxFormId = new XcxFormId(gogirltoken.getCustomerId(), gogirltoken.getCustomer().getOpenid1(), scheduleManage.getFormId(), new Date(), 1);
        	xcxFormIdMapper.insertSelective(xcxFormId);
    	}
    	//0.根据店铺查询店铺排班时间
    	List<ClassesManage> classesManageList = classesManageService.listClassesManage(scheduleManage.getDepartmentId());
    	SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
    	Time startWorkTime=null;//上班时间
    	Time endWorkTime=null;//下班时间
		try {
			startWorkTime = new Time(sdfTime.parse("23:59:59").getTime());
			endWorkTime = new Time(sdfTime.parse("00:00:00").getTime());
		} catch (ParseException e1) {
			e1.printStackTrace();
			return new JsonResult<>(false,"请联系管理员设置店铺上下班时间");
		}
    	Iterator<ClassesManage> itClassesManage = classesManageList.iterator();
    	while(itClassesManage.hasNext()){//遍历得到最早上班时间startWorkTime和最晚下班时间endWorkTime
    		ClassesManage item = itClassesManage.next();
    		if(item.getStartTime().before(startWorkTime)){
    			startWorkTime = item.getStartTime();
    		}
    		if(item.getEndTime().after(endWorkTime)){
    			endWorkTime = item.getEndTime();
    		}
    	}
    	//1.查询美甲师排班表
    	List<ClassesTechnician> listClassesTechnician = classesTechnicianService.listClassesTechnician(scheduleManage.getDepartmentId(), scheduleManage.getScheduleDate());
    	//3.查询美甲师补种时间,还没做,预留位置
    	int timeNode = 15;//15分钟为一个节点
    	HashMap<Integer,HashSet<String>> listTechIdleTime = listTechIdleTime(scheduleManage.getScheduleDate(), startWorkTime, endWorkTime, scheduleManage.getDepartmentId(),timeNode, listClassesTechnician);
    	
    	
    	
    	
    	
    	//5.0.查出所有时长,把从服务移除,时长合并到主服务
    	List<ScheduleServe> listScheduleServe = scheduleManage.getListScheduleServer();
    	listScheduleServe = addTimeToMainServe(listScheduleServe);//从服务的时间加到主服务,移除从服务
    	List<Map<String, Object>> timeList=new ArrayList<Map<String,Object>>();//结果列表
    	//5.遍历每个时间点,确定这些时间点是否能找到一种方案安排预约,找到1种方案后返回true
		Calendar currentTime = Calendar.getInstance();
		Calendar endTime = Calendar.getInstance();
		Calendar nowTime = Calendar.getInstance();
    	currentTime.setTime(startWorkTime);//从头开始遍历
    	endTime.setTime(endWorkTime);
    	//今天字符串
    	SimpleDateFormat todaySdf = new SimpleDateFormat("yyyy-MM-dd");
    	SimpleDateFormat sdfHm = new SimpleDateFormat("HH:mm");
    	try {
			nowTime.setTime(sdfHm.parse(sdfHm.format(new Date())));
		} catch (ParseException e) {}
    	String todayDayString = todaySdf.format(new Date());
    	while(currentTime.before(endTime)){//遍历每个时间点
    		//如果是已经过去的时间,直接false不能预约
    		if(todayDayString.equals(scheduleManage.getScheduleDate())&&currentTime.before(nowTime)){//过去的时间,直接设置不可预约
            	Map<String, Object> maptem  = new HashMap<String, Object>();
            	maptem.put("time", sdfHm.format(currentTime.getTime()));
            	maptem.put("status", false);
            	timeList.add(maptem);
            	currentTime.add(Calendar.MINUTE, timeNode);
            	continue;
    		}
    		//判断该时间点是否可预约
        	Map<Integer, Calendar> hasPicTech = new HashMap<Integer, Calendar>();//初始化所以技师可选
        	Map<Integer,Set<String>> listTechIdleTimeTem = cloneListTechIdleTime(listTechIdleTime);
    		Iterator<Integer> itKey = listTechIdleTimeTem.keySet().iterator();
    		while(itKey.hasNext()){
    			Integer key = itKey.next();
        		Calendar temCalendar = Calendar.getInstance();
        		temCalendar.setTime(currentTime.getTime());
        		hasPicTech.put(key, temCalendar);
    		}
        	List<ScheduleServe> listScheduleServerTem = clonelistScheduleServer(listScheduleServe);
        	logger.info("--"+sdfHm.format(currentTime.getTime()));
        	List<ScheduleServe> isPicServe = new ArrayList<ScheduleServe>();
        	Boolean canSche = checkCanSche(timeNode,listTechIdleTimeTem,hasPicTech,listScheduleServerTem,isPicServe);//找出一种安排结果,返回可预约,否则不可预约
        	//保存判断结果
        	Map<String, Object> maptem  = new HashMap<String, Object>();
        	maptem.put("time", sdfHm.format(currentTime.getTime()));
        	maptem.put("status", canSche);
        	timeList.add(maptem);
        	currentTime.add(Calendar.MINUTE, timeNode);
    	}
    	return new JsonResult<>(true,JsonResult.APP_DEFINE_SUC,timeList);
    }
    //1.0.查美甲师空闲时间点
    public HashMap<Integer,HashSet<String>> listTechIdleTime(String scheduleDate,Time startWorkTime,Time endWorkTime,Integer departmentId,int timeNode,List<ClassesTechnician> listClassesTechnician) {
    	logger.info("查美甲师空闲时间点");
    	//4.1遍历初始化个美甲师时间点表
    	Iterator<ClassesTechnician> itClassesTechnician = listClassesTechnician.iterator();
    	HashMap<Integer,HashSet<String>> listTechIdleTime = new HashMap<Integer,HashSet<String>>();
		Calendar currentTime = Calendar.getInstance();
		Calendar endTime = Calendar.getInstance();
    	SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
    	currentTime.setTime(startWorkTime);
    	endTime.setTime(endWorkTime);
    	SimpleDateFormat sdfHm = new SimpleDateFormat("HH:mm");
		while(itClassesTechnician.hasNext()){//遍历所有美甲师
			ClassesTechnician itemClassesTechnician = itClassesTechnician.next();
			if(itemClassesTechnician.getClasses()!=null&&!itemClassesTechnician.getClasses().equals(0)){//加入上班的美甲师
				currentTime.setTime(itemClassesTechnician.getClassesManage().getStartTime());//该美甲师具体上班时间
		    	endTime.setTime(itemClassesTechnician.getClassesManage().getEndTime());//该美甲师具体下班时间
		    	HashSet<String> itemset = new HashSet<>();
				while(currentTime.before(endTime)){
					itemset.add(sdfHm.format(currentTime.getTime()));
					currentTime.add(Calendar.MINUTE, timeNode);
				}
				listTechIdleTime.put(itemClassesTechnician.getTechnicianManage().getId(),itemset);
			}
		}
    	//2.查询今日预约
    	List<ScheduleServe> listScheduleManage = scheduleServeService.listScheduleManageByDate(scheduleDate,departmentId);
    	//4.2去掉已预约节点
    	Iterator<ScheduleServe> itScheduleManage = listScheduleManage.iterator();
    	SimpleDateFormat sdfymd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	Date dateStartWorkTime=null;
    	Date dateEndWorkTime=null;
		try {
			dateStartWorkTime = sdfymd.parse(scheduleDate+" "+sdfTime.format(startWorkTime));
			dateEndWorkTime = sdfymd.parse(scheduleDate+" "+sdfTime.format(endWorkTime));
		} catch (ParseException e) {e.printStackTrace();}
    	while(itScheduleManage.hasNext()){
    		ScheduleServe itemScheServe = itScheduleManage.next();
    		Date scheStartTime = new Date(itemScheServe.getStartTime().getTime()-timeNode*60000);//早一个时间节点计算
    		Date scheEndTime = itemScheServe.getEndTime();
    		//找到所有在预约中的时间点
    		Set<String> beScheSet = new HashSet<>();
        	currentTime.setTime(dateStartWorkTime);
        	endTime.setTime(dateEndWorkTime);
    		while(currentTime.before(endTime)){
    			if(currentTime.getTime().after(scheStartTime)&&currentTime.getTime().before(scheEndTime)){//判断该时间点是否被预约
    				beScheSet.add(sdfHm.format(currentTime.getTime()));
    			}
    			currentTime.add(Calendar.MINUTE, timeNode);
    		}
    		//清除对应美甲师对应的空闲时间点
			HashSet<String> item = listTechIdleTime.get(itemScheServe.getTechnicianId());
			item.removeAll(beScheSet);
			System.out.println(item);
    	}
    	return listTechIdleTime;
	}
    //1.1深度克隆listTechIdleTime
    private HashMap<Integer,Set<String>> cloneListTechIdleTime(HashMap<Integer,HashSet<String>> listTechIdleTime) {
    	HashMap<Integer,Set<String>> map = new HashMap<Integer,Set<String>>();
    	Iterator<Entry<Integer, HashSet<String>>> it = listTechIdleTime.entrySet().iterator();
    	while(it.hasNext()){
    		Entry<Integer, HashSet<String>> item = it.next();
    		map.put(item.getKey(),(Set<String>) item.getValue().clone());
    	}
		return map;
	}
    //1.2深度克隆listScheduleServer
    private ArrayList<ScheduleServe> clonelistScheduleServer(List<ScheduleServe>  listScheduleServer) {
    	ArrayList<ScheduleServe> list = new ArrayList<ScheduleServe>();
    	for(ScheduleServe item : listScheduleServer){
    		list.add(item.clone());
    	}
		return list;
	}
    //1.3判断某个时间是否可预约,找到1个可行方案就返回true,默认排班结果放在isPicServe
	private Boolean checkCanSche(int timeNode,Map<Integer, Set<String>> listTechIdleTimeTem,Map<Integer, Calendar> hasPicTech, List<ScheduleServe> noPicServe,List<ScheduleServe> isPicServe) {
		logger.info("1.安排服务");
		SimpleDateFormat sdfHm = new SimpleDateFormat("HH:mm");
    	Map<Integer, Calendar> newHasPicTech = new HashMap<Integer, Calendar>();//新一轮的可选技师
    	Iterator<ScheduleServe> itNoPicServe = noPicServe.iterator();
    	while(itNoPicServe.hasNext()){//遍历服务
    		ScheduleServe itemSche = itNoPicServe.next();
    		logger.info("::服务时长itemSche:"+itemSche.getLengthTimeForEndTime());
    		int lengthTime = itemSche.getLengthTimeForEndTime();
    		Set<String> setNeed = new HashSet<String>();//该服务需要的所有时间节点
    		int tempLengthTime = 0;
    		Iterator<Entry<Integer, Set<String>>> itTechIdleTime = listTechIdleTimeTem.entrySet().iterator();
    		while(itTechIdleTime.hasNext()){
    			Entry<Integer, Set<String>> entry = itTechIdleTime.next();
    			Integer key = entry.getKey();
    			Set<String> item = entry.getValue();
    			if(!hasPicTech.containsKey(key)){
    				continue;
    			}
    			logger.info(":::美甲师techId:"+key);
    			Calendar currentTime = Calendar.getInstance();
    			currentTime.setTime(hasPicTech.get(key).getTime());
        		while(tempLengthTime<lengthTime){//生成时间节点
        			String currTimeString = sdfHm.format(currentTime.getTime());
        			setNeed.add(currTimeString);//加入需要的时间节点set
        			currentTime.add(Calendar.MINUTE, timeNode);//加时间节点
        			tempLengthTime+=timeNode;//计算时长
        		}
    			Set<String> set = listTechIdleTimeTem.get(key);
    			if(set.containsAll(setNeed)){//安排得上
    				logger.info("::::安排");
    				set.removeAll(setNeed);
    				newHasPicTech.put(key, currentTime);
    				itemSche.setTechnicianId(key);//默认分配美甲师到服务
    				itemSche.setStartTime(hasPicTech.get(key).getTime());//设置开始时间
    				isPicServe.add(itemSche);
    				itNoPicServe.remove();
    				noPicServe.remove(itemSche);
    				break;
    			}
    		}
    	}
    	//重复遍历未安排的服务,遍历已选美甲师(服务为空跳出,美甲师为空跳出)
    	if(noPicServe.size()>0){
    		if(hasPicTech.size()>0){
    			return checkCanSche(timeNode, listTechIdleTimeTem, newHasPicTech, noPicServe,isPicServe);
    		}else{
    			isPicServe.clear();//该选项不成,清空已选分配
    			return false;
    		}
    	}else{
    		return true;
    	}
	}
	/*2.选时间后直接 新增预约,状态为已删除*/
    @ApiOperation(value = "new 2.选时间的时候就新建一个服务,状态(已删除),确定预约的时候才把状态改为已预约", notes = "{\"departmentId\":19,\"formId\":\"the formId is a mock one\",\"token\":\"91c23f20e3\",\"scheduleDate\":\"2019-10-29\",\"arriveTime\":\"2019-10-29 10:00:00\",\"listScheduleServer\":[{\"serveId\":124,\"produceId\":124,\"mainServeId\":153,\"lengthTimeForEndTime\":30},{\"serveId\":153,\"lengthTimeForEndTime\":90}]}")
    @RequestMapping(method={RequestMethod.POST},value = "addScheduleXcx")
    public JsonResult<?> addScheduleXcx (@RequestBody ScheduleManage scheduleManage) {
    	logger.info("选时间后直接 新增预约,状态为已删除");
    	//入参判断ScheduleManage
    	if(scheduleManage==null){return new JsonResult<>(false,"入参为空");}
    	if(scheduleManage.getToken()==null){return new JsonResult<>(false,String.format(JsonResult.PARAM_NULL,"token"));}
    	if(scheduleManage.getDepartmentId()==null){return new JsonResult<>(false,String.format(JsonResult.PARAM_NULL,"departmentId"));}
    	if(scheduleManage.getScheduleDate()==null){return new JsonResult<>(false,"scheduleDate为空,格式为'2019-10-09'");}
    	if(scheduleManage.getListScheduleServer()==null||scheduleManage.getListScheduleServer().size()==0){return new JsonResult<>(false,"服务为空,请至少选择1个服务再预约");}
//    	if(scheduleManage.getArriveTimeString()==null){return new JsonResult<>(false,"已选时间arriveTimeString为空,格式为'15:30'");}
    	if(scheduleManage.getArriveTime()==null){return new JsonResult<>(false,"已选时间arriveTime为空,格式为'2019-10-09 15:30:00'");}
    	//查找用户
    	GogirlToken gogirltoken = gogirlTokenService.getTokenByToken(scheduleManage.getToken());
    	if(gogirltoken==null||gogirltoken.getCustomerId()==null){
    		return new JsonResult<>(false,JsonResult.TOKEN_NULL_CODE);
    	}
    	//保存formId
    	if(scheduleManage.getFormId()!=null&&!scheduleManage.getFormId().equals("the formId is a mock one")&&gogirltoken.getCustomer().getOpenid1()!=null){
    		XcxFormId xcxFormId = new XcxFormId(gogirltoken.getCustomerId(), gogirltoken.getCustomer().getOpenid1(), scheduleManage.getFormId(), new Date(), 1);
        	xcxFormIdMapper.insertSelective(xcxFormId);
    	}
    	//0.根据店铺查询店铺排班时间
    	List<ClassesManage> classesManageList = classesManageService.listClassesManage(scheduleManage.getDepartmentId());
    	SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
    	Time startWorkTime=null;//上班时间
    	Time endWorkTime=null;//下班时间
		try {
			startWorkTime = new Time(sdfTime.parse("23:59:59").getTime());
			endWorkTime = new Time(sdfTime.parse("00:00:00").getTime());
		} catch (ParseException e1) {
			e1.printStackTrace();
			return new JsonResult<>(false,"请联系管理员设置店铺上下班时间");
		}
    	Iterator<ClassesManage> itClassesManage = classesManageList.iterator();
    	while(itClassesManage.hasNext()){//遍历得到最早上班时间startWorkTime和最晚下班时间endWorkTime
    		ClassesManage item = itClassesManage.next();
    		if(item.getStartTime().before(startWorkTime)){
    			startWorkTime = item.getStartTime();
    		}
    		if(item.getEndTime().after(endWorkTime)){
    			endWorkTime = item.getEndTime();
    		}
    	}
    	//1.查询美甲师排班表
    	List<ClassesTechnician> listClassesTechnician = classesTechnicianService.listClassesTechnician(scheduleManage.getDepartmentId(), scheduleManage.getScheduleDate());
    	//3.查询美甲师补种时间,还没做,预留位置
    	int timeNode = 15;//15分钟为一个节点
    	HashMap<Integer,HashSet<String>> listTechIdleTime = listTechIdleTime(scheduleManage.getScheduleDate(), startWorkTime, endWorkTime, scheduleManage.getDepartmentId(),timeNode, listClassesTechnician);
    	//初始化各个美甲师可以开始预约的时间
    	Map<Integer, Calendar> hasPicTech = new HashMap<Integer, Calendar>();//初始化所以技师可选
    	Map<Integer,Set<String>> listTechIdleTimeTem = cloneListTechIdleTime(listTechIdleTime);
		Iterator<Integer> itKey = listTechIdleTimeTem.keySet().iterator();
		while(itKey.hasNext()){
			Integer key = itKey.next();
    		Calendar temCalendar = Calendar.getInstance();
    		temCalendar.setTime(scheduleManage.getArriveTime());
    		hasPicTech.put(key, temCalendar);
		}
    	//查询一种排班,且排班
    	List<ScheduleServe> listScheduleServe = scheduleManage.getListScheduleServer();
    	List<ScheduleServe> listScheduleServeMainServe = addTimeToMainServe(listScheduleServe);//从服务的时间加到主服务,移除从服务

    	List<ScheduleServe> isPicServe = new ArrayList<ScheduleServe>();
    	Boolean canSche = checkCanSche(timeNode, listTechIdleTimeTem, hasPicTech, listScheduleServeMainServe, isPicServe);
    	//isPicServe为已经默认分配美甲师的服务
    	//安排从服务的美甲师为主服务的美甲师
    	setScheduleServeTech(listScheduleServe,isPicServe);
    	
    	
    	//插入预约
    	scheduleManage.setScheduledNo(scheduleManageService.createScheduleID(scheduleManage.getDepartmentId()));//设置订单号
    	scheduleManage.setScheduledUser(gogirltoken.getCustomer().getId());//设置用户id
    	scheduleManage.setTelephone(gogirltoken.getCustomer().getPhone());//设置用户手机
    	String username = null;
    	if(username==null&&gogirltoken.getCustomer().getStoreRecordRealName()!=null){username=gogirltoken.getCustomer().getStoreRecordRealName();}//店铺录入名字
    	if(username==null&&gogirltoken.getCustomer().getRealName()!=null){username=gogirltoken.getCustomer().getRealName();}//用户自己输入真实名字
    	if(username==null&&gogirltoken.getCustomer().getNickname()!=null){username=gogirltoken.getCustomer().getNickname();}//微信昵称
    	if(username==null&&gogirltoken.getCustomer().getPhone()!=null){username=gogirltoken.getCustomer().getPhone();}//手机号码
    	scheduleManage.setStoreScheduleUsername(username);
    	scheduleManage.setScheduledTime(new Date());//创建时间
    	scheduleManage.setLastUpdateTime(new Date());//创建时间
    	scheduleManage.setScheduledType(1);//用户端预约
    	scheduleManage.setStatus(5);//先设置为已删除状态
    	int schId = scheduleManageService.insertScheduleManage(scheduleManage);
    	//插入预约服务
    	for(ScheduleServe item : listScheduleServe){
    		item.setSchId(schId);
    	}
    	int row1 = scheduleServeService.insertListScheduleServe(listScheduleServe);
    	//返回预约id
    	return new JsonResult<>(true,JsonResult.APP_DEFINE_SUC,schId);
    }
    //2.1.从服务的时间加到主服务,移除从服务
    public List<ScheduleServe> addTimeToMainServe(List<ScheduleServe> listScheduleServe) {
    	//从服务的时间加到主服务
    	List<ScheduleServe> listScheduleServeMainServe = clonelistScheduleServer(listScheduleServe);
    	Iterator<ScheduleServe> itScheduleServe = listScheduleServeMainServe.iterator();
    	while(itScheduleServe.hasNext()){
    		ScheduleServe itemScheduleServe = itScheduleServe.next();
    		if(itemScheduleServe.getMainServeId()==null){//主服务
    			for(ScheduleServe itemAddTime : listScheduleServeMainServe){//遍历找出子服务
    				if(itemAddTime.getMainServeId()!=null&&itemAddTime.getMainServeId().equals(itemScheduleServe.getServeId())){
    					int mainServiceDuration = 0;
    					int serviceDuration = 0;
    					if(itemScheduleServe.getLengthTimeForEndTime()!=null){
    						mainServiceDuration = itemScheduleServe.getLengthTimeForEndTime();
    					}
    					if(itemAddTime.getLengthTimeForEndTime()!=null){
    						serviceDuration = itemAddTime.getLengthTimeForEndTime();
    					}else if(itemAddTime.getProduce()!=null&&itemAddTime.getProduce().getServiceDuration()!=null){
    						serviceDuration=itemAddTime.getProduce().getServiceDuration();
    					}else if(itemAddTime.getServe()!=null&&itemAddTime.getServe().getServiceDuration()!=null){
    						serviceDuration=itemAddTime.getServe().getServiceDuration();
    					}else{
    						
    					}
    					itemScheduleServe.setLengthTimeForEndTime(mainServiceDuration+serviceDuration);
    				}
    			}
    		}
    	}
    	//移除从服务
    	Iterator<ScheduleServe> itScheduleServe2 = listScheduleServeMainServe.iterator();
    	while(itScheduleServe2.hasNext()){
    		ScheduleServe itemScheduleServe = itScheduleServe2.next();
    		if(itemScheduleServe.getMainServeId()!=null){
    			itScheduleServe2.remove();
    		}
    	}
		return listScheduleServeMainServe;
	}
    //2.3 安排从服务的美甲师和服务时间
    public List<ScheduleServe> setScheduleServeTech(List<ScheduleServe> listScheduleServe,List<ScheduleServe> listScheduleServeMainServe) {
    	Iterator<ScheduleServe> it = listScheduleServe.iterator();
    	while(it.hasNext()){
    		ScheduleServe item = it.next();
			if(item.getMainServeId()==null){//主服务直接从安排好的列表中拿技师
	    		for(ScheduleServe itemMainServe : listScheduleServeMainServe){
	    			if(item.getServeId().equals(itemMainServe.getServeId())){
	    				item.setTechnicianId(itemMainServe.getTechnicianId());
	    				item.setStartTime(itemMainServe.getStartTime());
	    				item.setLengthTimeForEndTime(itemMainServe.getLengthTimeForEndTime());
	    				item.setEndTime(new Date(item.getStartTime().getTime()+(long)60000*itemMainServe.getLengthTimeForEndTime()));
	    				break;//找到了,跳出
	    			}
	    		}
    		}else{//从服务,从安排好的列表中拿主服务id的技师
        		for(ScheduleServe itemMainServe : listScheduleServeMainServe){
	    			if(item.getMainServeId().equals(itemMainServe.getServeId())){
	    				item.setTechnicianId(itemMainServe.getTechnicianId());
	    				item.setStartTime(itemMainServe.getStartTime());
	    				item.setEndTime(itemMainServe.getEndTime());
	    				break;//找到了,跳出
	    			}
        		}
    		}
    	}
    	return listScheduleServeMainServe;
	}
    /*3.修改且查询默认分配的美甲师,scheduleId预约id必传,orderServeId和technicianId修改的时候才传.返回可选美甲师,选择后为指定美甲师,再次选择为取消指定*/
    @ApiOperation(value = "new 3.修改默认分配的美甲师", notes = "")
    @RequestMapping(method={RequestMethod.POST},value = "modifyTech")
    public JsonResult<?> modifyTech (Integer scheduleId,Integer orderServeId,Integer technicianId){
    	logger.info("修改且查询默认分配的美甲师");
    	//入参检查
    	if(scheduleId==null){return new JsonResult<>(false,"scheduleId为空");}
    	//找到预约
    	ScheduleManage scheduleManage = scheduleManageService.getScheduleForDetail(scheduleId);
    	if(scheduleManage==null){return new JsonResult<>(false,"找不到预约id:"+scheduleId);}
    	List<ScheduleServe> listScheduleServe = scheduleManage.getListScheduleServer();
    	//0.根据店铺查询店铺排班时间
    	List<ClassesManage> classesManageList = classesManageService.listClassesManage(scheduleManage.getDepartmentId());
    	SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
    	Time startWorkTime=null;//上班时间
    	Time endWorkTime=null;//下班时间
		try {
			startWorkTime = new Time(sdfTime.parse("23:59:59").getTime());
			endWorkTime = new Time(sdfTime.parse("00:00:00").getTime());
		} catch (ParseException e1) {
			e1.printStackTrace();
			return new JsonResult<>(false,"请联系管理员设置店铺上下班时间");
		}
    	Iterator<ClassesManage> itClassesManage = classesManageList.iterator();
    	while(itClassesManage.hasNext()){//遍历得到最早上班时间startWorkTime和最晚下班时间endWorkTime
    		ClassesManage item = itClassesManage.next();
    		if(item.getStartTime().before(startWorkTime)){
    			startWorkTime = item.getStartTime();
    		}
    		if(item.getEndTime().after(endWorkTime)){
    			endWorkTime = item.getEndTime();
    		}
    	}
    	//1.查询美甲师排班表
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	
    	List<ClassesTechnician> listClassesTechnician = classesTechnicianService.listClassesTechnician(scheduleManage.getDepartmentId(),sdf.format(scheduleManage.getArriveTime()) );
    	//所有美甲师id对应美甲师
    	Map<Integer, TechnicianManage> techMap = new HashMap<Integer, TechnicianManage>();
    	Iterator<ClassesTechnician> ite = listClassesTechnician.iterator();
    	while(ite.hasNext()){
    		ClassesTechnician item = ite.next();
    		techMap.put(item.getTechnicianManage().getId(),item.getTechnicianManage());
    	}
    	//设置orderServeId和technicianId为客户指定
    	if(orderServeId!=null&&technicianId!=null){
        	for(ScheduleServe item : listScheduleServe){
        		if(orderServeId.equals(item.getId())){
        			if(item.getTechnicianId().equals(technicianId)){
        				boolean tem = item.getIsCustomerPick()==null?true:!item.getIsCustomerPick();
        				item.setIsCustomerPick(tem);
        			}else{
            			item.setTechnicianId(technicianId);
            			item.setIsCustomerPick(true);
        			}
        			scheduleServeService.updateScheduleServe(item);
        			item.setTechnicianManage(techMap.get(technicianId));
        		}
        	}
    	}
    	//拿到所有美甲师的空闲时间
    	int timeNode=15;
    	HashMap<Integer,HashSet<String>> listTechIdleTime = listTechIdleTime(sdf.format(scheduleManage.getArriveTime()), startWorkTime, endWorkTime, scheduleManage.getDepartmentId(),timeNode, listClassesTechnician);
    	//客户指定美甲师的服务,放置指定美甲师到可选美甲师List<ScheduleServe> 
    	Iterator<ScheduleServe> it1 = listScheduleServe.iterator();
    	for(ScheduleServe item2 : listScheduleServe){
    		item2.setMapTechnicianManage(new HashMap<Integer,TechnicianManage>());//放置可选美甲师
		}
    	while(it1.hasNext()){
    		ScheduleServe item = it1.next();
    		//已指定或正常预约已占用美甲师,加入已经选好的美甲师
    		if(item.getIsCustomerPick()!=null&&item.getIsCustomerPick()==true||scheduleManage.getStatus().equals(6)||scheduleManage.getStatus().equals(7)){
    			for(ScheduleServe item2 : listScheduleServe){
    				if(item.getServeId().equals(item2.getServeId())||(item2.getMainServeId()!=null&&item2.getMainServeId().equals(item.getServeId()))){
    					item2.getMapTechnicianManage().put(item.getTechnicianManage().getId(),item.getTechnicianManage());//只放置指定的美甲师
    				}
    			}
    		}
    	}
    	//去掉指定的美甲师和去掉指定的服务
    	List<ScheduleServe> clonelistScheduleServer = clonelistScheduleServer(listScheduleServe);
    	Iterator<ScheduleServe> it2 = clonelistScheduleServer.iterator();
    	Set<Integer> mainServeSet = new HashSet<>();
    	while(it2.hasNext()){
    		ScheduleServe item = it2.next();
    		if(item.getIsCustomerPick()!=null&&item.getIsCustomerPick()==true){
    			listTechIdleTime.remove(item.getTechnicianId());//去掉指定的美甲师
    			mainServeSet.add(item.getServeId());
    			it2.remove();//去掉指定的服务
    		}
    	}
    	//去掉从服务
    	Iterator<ScheduleServe> it3 = clonelistScheduleServer.iterator();
    	while(it3.hasNext()){
    		ScheduleServe item = it3.next();
    		if(mainServeSet.contains(item.getMainServeId())){
    			it3.remove();
    		}
    	}
    			
    			
    	List<ScheduleServe> listScheduleServeMainServe = addTimeToMainServe(clonelistScheduleServer);//从服务的时间加到主服务,移除从服务
    	//初始化可以开始预约的时间
    	Map<Integer, Calendar> hasPicTech = new HashMap<Integer, Calendar>();//初始化所以技师可选
    	Map<Integer,Set<String>> listTechIdleTimeTem = cloneListTechIdleTime(listTechIdleTime);
		Iterator<Integer> itKey = listTechIdleTimeTem.keySet().iterator();
		while(itKey.hasNext()){
			Integer key = itKey.next();
    		Calendar temCalendar = Calendar.getInstance();
    		temCalendar.setTime(scheduleManage.getArriveTime());
    		hasPicTech.put(key, temCalendar);
		}
    	//找出所有可能安排预约,遇到可以安排的情况就加入到方案库
		List<ScheduleServe> isPicServe = new ArrayList<ScheduleServe>();
    	int possibilities = findAllCanSche(0,timeNode, listTechIdleTime, hasPicTech, listScheduleServeMainServe,isPicServe,listScheduleServe,techMap);
    	logger.info("总计"+possibilities+"种可能性的安排");
    	//合并所有可能的情况到服务可选美甲师
    	//返回各个服务附带的可选美甲师列表
    	return new JsonResult<>(true,JsonResult.APP_DEFINE_SUC,listScheduleServe);
    }
    //3.0遍历所有情况,找出可预约的所有方案
	private int findAllCanSche(int test,int timeNode,HashMap<Integer, HashSet<String>> listTechIdleTime   ,Map<Integer, Calendar> hasPicTech, List<ScheduleServe> noPicServe,List<ScheduleServe> isPicServe,List<ScheduleServe> listScheduleServe,Map<Integer, TechnicianManage> techMap) {
    	//遍历未安排的服务,遍历已选美甲师(服务为空记录方案,美甲师为空继续遍历)
//		logger.info("第"+(++test)+"层,已安排服务"+isPicServe.size()+"个,未安排服务"+noPicServe.size()+"个");
		SimpleDateFormat sdfHm = new SimpleDateFormat("HH:mm");
    	Map<Integer, Calendar> newHasPicTech = clonelistHasPicTech(hasPicTech);//新一轮的可选技师
    	Iterator<ScheduleServe> itNoPicServe = noPicServe.iterator();
    	int possibilities=0;
    	int countServe = 0;
    	while(itNoPicServe.hasNext()){//遍历服务
    		ScheduleServe itemSche = itNoPicServe.next();
//    		logger.info("--第"+(--countServe)+"个服务id:"+itemSche.getId());
    		int lengthTime = itemSche.getServe().getServiceDuration();
    		Set<String> setNeed = new HashSet<String>();//该服务需要的所有时间节点
    		int tempLengthTime = 0;
    		Iterator<Entry<Integer, HashSet<String>>> it = listTechIdleTime.entrySet().iterator();
    		int i =0;
    		while (it.hasNext()) {
    			Entry<Integer, HashSet<String>> entry = it.next();
    			Integer key = entry.getKey();
    			HashSet<String> item = entry.getValue();
    			if(!hasPicTech.containsKey(key)){
    				continue;
    			}
//    			logger.info("....第"+(i++)+"个美甲师id"+key);
    			Calendar currentTime = Calendar.getInstance();
    			currentTime.setTime(hasPicTech.get(key).getTime());
        		while(tempLengthTime<lengthTime){//生成时间节点
        			String currTimeString = sdfHm.format(currentTime.getTime());
        			setNeed.add(currTimeString);//加入需要的时间节点set
        			currentTime.add(Calendar.MINUTE, timeNode);//加时间节点
        			tempLengthTime+=timeNode;//计算时长
        		}
    			Set<String> set = listTechIdleTime.get(key);
    			if(set.containsAll(setNeed)){//安排得上,继续安排下一个服务
//    				logger.info("::::可选");
    				set.removeAll(setNeed);
    				
    				newHasPicTech.put(key, currentTime);
    				itemSche.setTechnicianId(key);//默认分配美甲师到服务
    				itemSche.setStartTime(hasPicTech.get(key).getTime());//设置开始时间
    				isPicServe.add(itemSche);
//    				itNoPicServe.remove();
    		    	List<ScheduleServe> clonenoPicServe = clonelistScheduleServer(noPicServe);//克隆一个未安排的服务列表,不影响其他层级的判断
    		    	Iterator<ScheduleServe> it3 = clonenoPicServe.iterator();
    		    	while(it3.hasNext()){
    		    		ScheduleServe item2 = it3.next();
    		    		if(item2.getId().equals(itemSche.getId()))
    		    			it3.remove();
    		    	}
    				if(clonenoPicServe.size()==0){// 所有服务已安排,记录美甲师到可选美甲师
    					//isPicServe中的服务设置到listScheduleServe
    					Iterator<ScheduleServe> it4 = isPicServe.iterator();
    					while(it4.hasNext()){
    						ScheduleServe item4 = it4.next();
    						for(ScheduleServe item5 : listScheduleServe){
    							if(item4.getId().equals(item5.getId())){
    								item5.getMapTechnicianManage().put(key, techMap.get(key));
    							}
    							if(item5.getMainServeId()!=null&&item5.getMainServeId().equals(item4.getId())){
    								item5.getMapTechnicianManage().put(key, techMap.get(key));
    							}
    						}
    					}
    					possibilities++;
    				}else{
    					possibilities+=findAllCanSche(test,timeNode, listTechIdleTime, newHasPicTech, clonenoPicServe, isPicServe, listScheduleServe,techMap);
//    					logger.info("回到第"+(test)+"层");
    				}
    				isPicServe.remove(itemSche);//去掉刚刚安排的服务
    				set.addAll(setNeed);//补回刚刚移除的部分
    			}else{
    				continue;//安排不上,结束该层遍历,继续下个美甲师
    			}
			}
    	}
    	test--;
    	return possibilities;
	}
    private Map<Integer, Calendar> clonelistHasPicTech(Map<Integer, Calendar> hasPicTech) {
    	Map<Integer, Calendar> list = new HashMap<Integer, Calendar>();
    	Iterator<Entry<Integer, Calendar>> it = hasPicTech.entrySet().iterator();
    	while(it.hasNext()){
    		Entry<Integer, Calendar> item = it.next();
    		list.put(item.getKey(), (Calendar) item.getValue().clone());
    	}
		return list;
	}
	/*4.确定预约,建订单,把已删除状态改为已预约状态,发模板消息*/
    @ApiOperation(value = "new 4.确定预约,建订单,把已删除状态改为已预约状态,发模板消息", notes = "")
    @RequestMapping(method={RequestMethod.POST},value = "comfirmSchedule")
    public JsonResult<?> comfirmSchedule (String token,String formId,Integer oldOrderId,Integer scheduleId,Integer activityId) {
    	logger.info("确定预约,建订单,把已删除状态改为已预约状态,发模板消息");
    	//入参判断
    	if(scheduleId==null){
    		return new JsonResult<>(false,"scheduleId为null");
    	}
    	//查询token
        GogirlToken gogirltoken = gogirltoken = gogirlTokenService.getTokenByToken(token);
    	if(gogirltoken==null){
    		return new JsonResult<>(false,JsonResult.TOKEN_NULL_CODE);
    	}
    	//保存formId
        if(formId!=null&&!formId.isEmpty()&&token!=null&&!token.isEmpty()){
        	if(gogirltoken.getCustomerId()!=null){
            	XcxFormId xcxFormId = new XcxFormId();
            	xcxFormId.setCustomerId(gogirltoken.getCustomerId());
            	xcxFormId.setFormId(formId);
            	xcxFormId.setTime(new Date());
            	if(gogirltoken.getCustomer()!=null&&gogirltoken.getCustomer().getOpenid1()!=null){
            		xcxFormId.setOpenid(gogirltoken.getCustomer().getOpenid1());
            	}
            	xcxFormIdMapper.insertSelective(xcxFormId);
        	}
        }
        OrderManage orderDetail = null;
    	if(oldOrderId!=null){
        	orderDetail = orderManageService.getOrderForDetail(oldOrderId);//找到旧订单和旧预约id,准备设置为删除状态
        	if(orderDetail==null){
        		return new JsonResult<>(false,"找不到该订单",null);
        	}
        	if(orderDetail.getStatus().equals(5)){
        		return new JsonResult<>(false,"已修改成功,返回列表查看",null);
        	}
    	}

        
        //找到预约
        ScheduleManage scheduleManage = scheduleManageService.getScheduleForDetail(scheduleId);
        if(scheduleManage==null){
        	return new JsonResult<>(true,"找不到预约scheduleId:"+scheduleId);
        }
        //获得预约最早的时间
        Date scheduledTime = null;
        String serveName = "";
        String storeName = "";
        String storePhone = "";
        String storeAddress = "";
        List<TechnicianManage> listTech = new ArrayList<TechnicianManage>();

    	if(scheduleManage!=null&&scheduleManage.getListScheduleServer()!=null){
    		List<ScheduleServe> list = scheduleManage.getListScheduleServer();
    		Map<Integer, Serve> isExist = new HashMap<Integer, Serve>();
    		SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
    		String schDay = null;
    		SimpleDateFormat sdfYearMonthDay = new SimpleDateFormat("yyyy-MM-dd");
    		String schYearMonthDay = null;
    		for(int i=0;i<list.size();i++){
    			ScheduleServe item = list.get(i);
    			if(item.getServeId()==null){
    				return new JsonResult(false,"serveId为空，请选择服务",null);
    			}
    			//取得预约日期,判断是否所有预约都在同一天
    			if(schDay!=null&&item.getStartTime()!=null&&!schDay.equals(sdf.format(item.getStartTime()))){
    				return new JsonResult<>(false,"不能跨天预约"+schDay+"和"+sdf.format(item.getStartTime())+",如要预约多天,可分为多次预约哟,谢谢理解.");
    			}else{
    				schDay=sdf.format(item.getStartTime());
    				schYearMonthDay = sdfYearMonthDay.format(item.getStartTime());
    			}
    			
    			
    			Serve s  = serveService.getServeForDetail(item.getServeId());
    			//判断预约是否可以在同一天预约
    			if(s.getSchTypeId()!=null){
        			if(isExist.containsKey(s.getSchTypeId())){
        				Serve existServe =isExist.get(s.getSchTypeId());
        				return new JsonResult<>(false,"\""+existServe.getName()+"\"和\""+s.getName()+"\"属于同种类型的服务,一天内不能提交多个预约哟");
        			}else{
        				isExist.put(s.getSchTypeId(), s);
        			}
    			}
    			
    			item.setServe(s);
    			if(scheduledTime== null||scheduledTime.getTime()>item.getStartTime().getTime()){ //获得预约最早的时间
    				scheduledTime = item.getStartTime();
    			}
    			if(item.getTechnicianId()!=null){
    				TechnicianManage technicianManage = technicianManageService.getTechnicianManageForDetail(item.getTechnicianId());
    				if(technicianManage!=null){
    					technicianManage.setTime(item.getStartTime());
    					technicianManage.setServeName(s.getName());
    					listTech.add(technicianManage);
    				}
    			}
    			
    			serveName = s.getName();
    			if(storeName.isEmpty()){
    				ShopManage shop = shopManageService.getShopManageForDetail(scheduleManage.getDepartmentId());
    				storeName = shop.getName();
    				storePhone = shop.getContactTelphone();
    				storeAddress = shop.getAddress();
    			}
    		}
    		//查该用户当天已有的预约,根于以后预约
    		Customer c = customerService.selectByPhone(scheduleManage.getTelephone());
    		if(c!=null){
        		List<ScheduleServe> listExistSch =  scheduleManageService.listExistScheduleServe(schYearMonthDay,c.getId());
        		if(listExistSch!=null){
        			Iterator<ScheduleServe> iterator = listExistSch.iterator();
        			while(iterator.hasNext()){
        				ScheduleServe item = iterator.next();
        				if(orderDetail.getScheduledId()!=null&&!item.getSchId().equals(orderDetail.getScheduledId())){//过滤当前订单的预约
            				if(item.getServe()!=null&&item.getServe().getSchTypeId()!=null&&isExist.containsKey(item.getServe().getSchTypeId())){
            					Serve existServe =isExist.get(item.getServe().getSchTypeId());
            					return new JsonResult<>(false,"您已存在预约\""+existServe.getName()+"\",该服务与\""+item.getServe().getName()+"\"属于同种类型的服务,同一天只能预约一次,如需修改时间,可直接在我的订单中修改时间");
            				}
        				}
        			}
        		}
    		}
    	}
    	ScheduleManage scheduleManageUpdate = new ScheduleManage();
    	scheduleManageUpdate.setId(scheduleManage.getId());
    	scheduleManageUpdate.setStatus(Constans.SCHEDULE_STATUS_WAITSERVE);
    	scheduleManageUpdate.setActivityId(activityId);
    	if(orderDetail!=null&&orderDetail.getScheduledId()!=null){
            //把原预约设置为删除4240 2816
    		ScheduleManage scheduleManagedel = new ScheduleManage();
    		scheduleManagedel.setId(orderDetail.getScheduledId());
    		scheduleManagedel.setStatus(5);
    		scheduleManageService.updateSchedule(scheduleManagedel);
    	}
     	int row = scheduleManageService.updateSchedule(scheduleManageUpdate);
    	if(row>0){
    		//直接开单
    		JsonResult<?> orderResult = orderManageService.insertOrderManageXcx(scheduleManage.getId(), scheduleManage.getScheduledUser());
    		if(orderResult.getSuccess()){
    			//把原订单设为删除
    			OrderManage orderManage = new OrderManage();
    			orderManage.setDelRemark("用户修改预约后自动删除订单");
    			orderManage.setId(oldOrderId);
    			orderManage.setStatus(Constans.ORDER_STATUS_SERVING);
    			orderManageService.updateOrderStatus(orderManage);
    		}
            String technicianName = "";
			String url = "pages/order/orders?orderId="+orderResult.getData().toString();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH时mm分");
    		for(int i=0;i<listTech.size();i++){//客户预约成功,提醒美甲师
    			technicianName=technicianName+","+listTech.get(i).getName();
    			String scheduledTimetem = sdf.format(listTech.get(i).getTime());
    			sendTechXcxTemplateMsgService.sendBeBookedMsg(listTech.get(i).getOpenid(), url, scheduledTimetem, storeName, listTech.get(i).getName(), serveName, scheduleManage.getStoreScheduleUsername(), scheduleManage.getTelephone());
    		}
    		if(technicianName.length()>0){
    			technicianName = technicianName.substring(1);
    		}
    		if(orderResult.getSuccess()&&gogirltoken!=null&&gogirltoken.getCustomer()!=null&&gogirltoken.getCustomer().getOpenid1()!=null){
    			sendXcxTemplateMsgService.sendScheduledMsg(gogirltoken.getCustomer().getOpenid1(),Integer.valueOf(orderResult.getData().toString()),
    					scheduledTime==null?"":sdf.format(scheduledTime), technicianName, serveName, storeName, storePhone, storeAddress);
    		}
    		return orderResult;
    	}else{
    		return new JsonResult<>(false,"确定预约,修改预约失败2");
    	}
    }
    
    
    
    
    /*占时用于前端删数据*/
    @ApiOperation(value = "删用户数据", notes = "根据电话号码删数据")
    @RequestMapping(method={RequestMethod.POST},value = "deleteCustomerByPhone")
    public JsonResult<?> deleteCustomerByPhone (String phone) {
    	//删除用户数据
    	logger.info("deleteCustomerByPhone删除用户数据phone:"+phone);
    	int row = customerService.deleteByPhone(phone);
    	return new JsonResult<>(true,"已删",row);
    }
    /*修改预约接口:新增一个预约,把原预约设为删除*/
    @ApiOperation(value = "修改预约接口:新增一个预约,把原预约设为删除", notes = "{\"需要修改的预约id\":\"1234\",\"createUser\": \"东方宝泰店\",\"departmentId\": \"19\",\"listScheduleServer\": [{\"needRemoveOldServe\":true,\"lengthTimeForEndTime\": 120,\"produceName\":\"脚部单色\",\"recordType\": 1,\"serve\": {\"type\": \"美甲\", \"id\": 82},\"serveId\": 82,\"startTime\": \"2019-08-15 10:00:00\",\"startTimer\": \"10:00:00\",\"technicianId\": 108}],\"remark\": \"\",\"storeScheduleUsername\": \"aaa\",\"telephone\": \"18888888888\"}")
    @RequestMapping(method={RequestMethod.POST},value = "modifyScheduleXcx")
    public JsonResult<?> modifyScheduleXcx (@RequestBody ScheduleManage scheduleManage, HttpServletRequest request) {
    	//插入formId
        String token = scheduleManage.getToken();
        String formId = scheduleManage.getFormId();
        GogirlToken gogirltoken = null;
        if(formId!=null&&!formId.isEmpty()&&token!=null&&!token.isEmpty()){
        	gogirltoken = gogirlTokenService.getTokenByToken(token);
        	if(gogirltoken.getCustomerId()!=null){
            	XcxFormId xcxFormId = new XcxFormId();
            	xcxFormId.setCustomerId(gogirltoken.getCustomerId());
            	xcxFormId.setFormId(formId);
            	xcxFormId.setTime(new Date());
            	if(gogirltoken.getCustomer()!=null&&gogirltoken.getCustomer().getOpenid1()!=null){
            		xcxFormId.setOpenid(gogirltoken.getCustomer().getOpenid1());
            	}
            	xcxFormIdMapper.insertSelective(xcxFormId);
        	}
        }
        //处理昵称中的特殊字符
        if(scheduleManage!=null&&scheduleManage.getStoreScheduleUsername()!=null){
        	scheduleManage.setStoreScheduleUsername(filterEmoji(scheduleManage.getStoreScheduleUsername()));
        }
        
    	Integer originalId=scheduleManage.getId();
    	scheduleManage.setId(null);
    	if(originalId==null){
    		return new JsonResult<>(false,"需要修改的预约id为空",null);
    	}
    	
    	OrderManage orderDetail = orderManageService.getOrderForDetail(originalId);
    	if(orderDetail==null){
    		return new JsonResult<>(false,"找不到该订单",null);
    	}
    	if(orderDetail.getStatus().equals(5)){
    		return new JsonResult<>(false,"已修改成功,返回列表查看",null);
    	}
        
        //获得预约最早的时间
    	if(scheduleManage!=null&&scheduleManage.getListScheduleServer()!=null){
    		List<ScheduleServe> list = scheduleManage.getListScheduleServer();
    		Map<Integer, Serve> isExist = new HashMap<Integer, Serve>();
    		SimpleDateFormat sdfYearMonthDay = new SimpleDateFormat("yyyy-MM-dd");
    		String schYearMonthDay = null;
    		for(int i=0;i<list.size();i++){
    			ScheduleServe item = list.get(i);
    			if(item.getServeId()==null){
    				return new JsonResult(false,"serveId为空，请选择服务",null);
    			}
    			Serve s  = serveService.getServeForDetail(item.getServeId());
    			item.setServe(s);
    			//判断预约是否可以在同一天预约
    			if(s.getSchTypeId()!=null){
        			if(isExist.containsKey(s.getSchTypeId())){
        				Serve existServe =isExist.get(s.getSchTypeId());
        				return new JsonResult<>(false,"\""+existServe.getName()+"\"和\""+s.getName()+"\"属于同种类型的服务,一天内不能提交多个预约哟");
        			}else{
        				isExist.put(s.getSchTypeId(), s);
        				schYearMonthDay = sdfYearMonthDay.format(item.getStartTime());
        			}
    			}

    		}
    		//查该用户当天已有的预约,根于以后预约
    		Customer c = customerService.selectByPhone(scheduleManage.getTelephone());
    		if(c!=null){
        		List<ScheduleServe> listExistSch =  scheduleManageService.listExistScheduleServe(schYearMonthDay,c.getId());
        		if(listExistSch!=null&&listExistSch.size()>0){
        			Iterator<ScheduleServe> iterator = listExistSch.iterator();
        			while(iterator.hasNext()){
        				ScheduleServe item = iterator.next();
        				if(item.getServe()!=null&&item.getServe().getSchTypeId()!=null&&isExist.containsKey(item.getServe().getSchTypeId())){
        					Serve existServe =isExist.get(item.getServe().getSchTypeId());
        					if(!item.getSchId().equals(orderDetail.getScheduledId())){//判断这个预约不是正在修改的订单
            					return new JsonResult<>(false,"您已存在预约\""+existServe.getName()+"\",该服务与\""+item.getServe().getName()+"\"属于同种类型的服务,同一天只能预约一次,如需修改时间,可直接在我的订单中修改时间");
        					}
        				}
        			}
        		}
    		}
    	}
    	if(orderDetail!=null&&orderDetail.getScheduledId()!=null){
            //把原预约设置为删除4240 2816
    		ScheduleManage scheduleManagedel = new ScheduleManage();
    		scheduleManagedel.setId(orderDetail.getScheduledId());
    		scheduleManagedel.setStatus(5);
    		scheduleManageService.updateSchedule(scheduleManagedel);
    	}
    	JsonResult j =  scheduleManageService.insertScheduleWithServeByStore(scheduleManage);
    	
    	if(j.getSuccess()){
    		//直接开单
    		JsonResult<?> orderResult = orderManageService.insertOrderManageXcx((Integer)j.getData(), scheduleManage.getScheduledUser());
    		if(orderResult.getSuccess()){
    			//把原订单设为删除
    			OrderManage orderManage = new OrderManage();
    			orderManage.setDelRemark("用户修改预约后自动删除订单");
    			orderManage.setId(originalId);
    			orderManage.setStatus(5);
    			orderManageService.updateOrderStatus(orderManage);
    			
    		}
    		return orderResult;
    	}
    	return j;
    }
    /*新增预约,预约后自动生成'已预约待美甲师接单'的订单*/
    @ApiOperation(value = "新增预约,预约后自动生成'已预约待美甲师接单'的订单", notes = "{\"createUser\": \"东方宝泰店\",\"departmentId\": \"19\",\"listScheduleServer\": [{\"needRemoveOldServe\":true,\"lengthTimeForEndTime\": 120,\"produceName\":\"脚部单色\",\"recordType\": 1,\"serve\": {\"type\": \"美甲\", \"id\": 82},\"serveId\": 82,\"startTime\": \"2019-08-15 10:00:00\",\"startTimer\": \"10:00:00\",\"technicianId\": 108}],\"remark\": \"\",\"storeScheduleUsername\": \"aaa\",\"telephone\": \"18888888888\"}")
    @RequestMapping(method={RequestMethod.POST},value = "addSchedule")
    public JsonResult<?> addSchedule (@RequestBody ScheduleManage scheduleManage, HttpServletRequest request) {
    	//插入formId
        String token = scheduleManage.getToken();
        String formId = scheduleManage.getFormId();
        GogirlToken gogirltoken = null;
        if(formId!=null&&!formId.isEmpty()&&token!=null&&!token.isEmpty()){
        	gogirltoken = gogirlTokenService.getTokenByToken(token);
        	if(gogirltoken.getCustomerId()!=null){
            	XcxFormId xcxFormId = new XcxFormId();
            	xcxFormId.setCustomerId(gogirltoken.getCustomerId());
            	xcxFormId.setFormId(formId);
            	xcxFormId.setTime(new Date());
            	if(gogirltoken.getCustomer()!=null&&gogirltoken.getCustomer().getOpenid1()!=null){
            		xcxFormId.setOpenid(gogirltoken.getCustomer().getOpenid1());
            	}
            	xcxFormIdMapper.insertSelective(xcxFormId);
        	}
        }
        //处理昵称中的特殊字符
        if(scheduleManage!=null&&scheduleManage.getStoreScheduleUsername()!=null){
        	scheduleManage.setStoreScheduleUsername(filterEmoji(scheduleManage.getStoreScheduleUsername()));
        }
        
        
        
        //获得预约最早的时间
        Date scheduledTime = null;
        String serveName = "";
        String storeName = "";
        String storePhone = "";
        String storeAddress = "";
        List<TechnicianManage> listTech = new ArrayList<TechnicianManage>();

    	if(scheduleManage!=null&&scheduleManage.getListScheduleServer()!=null){
    		List<ScheduleServe> list = scheduleManage.getListScheduleServer();
    		Map<Integer, Serve> isExist = new HashMap<Integer, Serve>();
    		SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
    		String schDay = null;
    		SimpleDateFormat sdfYearMonthDay = new SimpleDateFormat("yyyy-MM-dd");
    		String schYearMonthDay = null;
    		for(int i=0;i<list.size();i++){
    			ScheduleServe item = list.get(i);
    			if(item.getServeId()==null){
    				return new JsonResult(false,"serveId为空，请选择服务",null);
    			}
    			//取得预约日期,判断是否所有预约都在同一天
    			if(schDay!=null&&item.getStartTime()!=null&&!schDay.equals(sdf.format(item.getStartTime()))){
    				return new JsonResult<>(false,"不能跨天预约"+schDay+"和"+sdf.format(item.getStartTime())+",如要预约多天,可分为多次预约哟,谢谢理解.");
    			}else{
    				schDay=sdf.format(item.getStartTime());
    				schYearMonthDay = sdfYearMonthDay.format(item.getStartTime());
    			}
    			
    			
    			Serve s  = serveService.getServeForDetail(item.getServeId());
    			//判断预约是否可以在同一天预约
    			if(s.getSchTypeId()!=null){
        			if(isExist.containsKey(s.getSchTypeId())){
        				Serve existServe =isExist.get(s.getSchTypeId());
        				return new JsonResult<>(false,"\""+existServe.getName()+"\"和\""+s.getName()+"\"属于同种类型的服务,一天内不能提交多个预约哟");
        			}else{
        				isExist.put(s.getSchTypeId(), s);
        			}
    			}
    			
    			item.setServe(s);
    			if(scheduledTime== null||scheduledTime.getTime()>item.getStartTime().getTime()){ //获得预约最早的时间
    				scheduledTime = item.getStartTime();
    			}
    			if(item.getTechnicianId()!=null){
    				TechnicianManage technicianManage = technicianManageService.getTechnicianManageForDetail(item.getTechnicianId());
    				if(technicianManage!=null){
    					technicianManage.setTime(item.getStartTime());
    					technicianManage.setServeName(s.getName());
    					listTech.add(technicianManage);
    				}
    			}
    			
    			serveName = s.getName();
    			if(storeName.isEmpty()){
    				ShopManage shop = shopManageService.getShopManageForDetail(scheduleManage.getDepartmentId());
    				storeName = shop.getName();
    				storePhone = shop.getContactTelphone();
    				storeAddress = shop.getAddress();
    			}
    		}
    		//查该用户当天已有的预约,根于以后预约
    		Customer c = customerService.selectByPhone(scheduleManage.getTelephone());
    		if(c!=null){
        		List<ScheduleServe> listExistSch =  scheduleManageService.listExistScheduleServe(schYearMonthDay,c.getId());
        		if(listExistSch!=null){
        			Iterator<ScheduleServe> iterator = listExistSch.iterator();
        			while(iterator.hasNext()){
        				ScheduleServe item = iterator.next();
        				if(item.getServe()!=null&&item.getServe().getSchTypeId()!=null&&isExist.containsKey(item.getServe().getSchTypeId())){
        					Serve existServe =isExist.get(item.getServe().getSchTypeId());
        					return new JsonResult<>(false,"您已存在预约\""+existServe.getName()+"\",该服务与\""+item.getServe().getName()+"\"属于同种类型的服务,同一天只能预约一次,如需修改时间,可直接在我的订单中修改时间");
        				}
        			}
        		}
    		}
    	}
    	JsonResult j =  scheduleManageService.insertScheduleWithServeByStore(scheduleManage);
    	if(j.getSuccess()){
    		//直接开单
    		JsonResult<?> orderResult = orderManageService.insertOrderManageXcx((Integer)j.getData(), scheduleManage.getScheduledUser());
            String technicianName = "";
			String url = "pages/order/orders?orderId="+orderResult.getData().toString();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH时mm分");
    		for(int i=0;i<listTech.size();i++){//客户预约成功,提醒美甲师
    			technicianName=technicianName+","+listTech.get(i).getName();
    			String scheduledTimetem = sdf.format(listTech.get(i).getTime());
    			sendTechXcxTemplateMsgService.sendBeBookedMsg(listTech.get(i).getOpenid(), url, scheduledTimetem, storeName, listTech.get(i).getName(), serveName, scheduleManage.getStoreScheduleUsername(), scheduleManage.getTelephone());
    		}
    		if(technicianName.length()>0){
    			technicianName = technicianName.substring(1);
    		}
    		if(orderResult.getSuccess()&&gogirltoken!=null&&gogirltoken.getCustomer()!=null&&gogirltoken.getCustomer().getOpenid1()!=null){
    			sendXcxTemplateMsgService.sendScheduledMsg(gogirltoken.getCustomer().getOpenid1(),Integer.valueOf(orderResult.getData().toString()),
    					scheduledTime==null?"":sdf.format(scheduledTime), technicianName, serveName, storeName, storePhone, storeAddress);
    		}
    		return orderResult;
    	}
    	return j;
    }
    /*美甲师所在店铺的预约列表*/
    @ApiOperation(value = "美甲师所在店铺的预约列表")
    @RequestMapping(method={RequestMethod.GET},value = "listScheduleManageForPage")
    public JsonResult listScheduleManageForPage(String token,Integer pageNum,Integer pageSize){
        JsonResult jsonResult = new JsonResult();
//        String pageNum = request.getParameter("pageNum");
//        String pageSize = request.getParameter("pageSize");
        if(pageNum==null||pageSize==null){
        	jsonResult.setMessage("pageNum或pageSize为空");
        	jsonResult.setSuccess(false);
        	return jsonResult;
        }
        GogirlToken gogirlToken = gogirlTokenService.getTokenByToken_t(token);
        if(gogirlToken==null){
        	return new JsonResult(false,JsonResult.TOKEN_NULL_CODE,"token无效");
        }
        if(gogirlToken.getUserTechnician()==null){
        	return new JsonResult(false,"找不到技师Technicianid:"+gogirlToken.getCustomerId(),null);
        }
        
//        TechnicianManage t= technicianManageService.getTechnicianManageForDetail(id);
        ScheduleManage scheduleManage = new ScheduleManage();
        scheduleManage.setDepartmentId(gogirlToken.getUserTechnician().getDepartmentId());
        PageHelper.startPage(pageNum,pageSize);
        List<ScheduleManage> list = scheduleManageService.listScheduleManageForPage(scheduleManage);
        PageInfo<ScheduleManage> pageInfo = new PageInfo<ScheduleManage>(list);
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(pageInfo);
        return jsonResult;
    }
    /*查询可预约时间*/
    @ApiOperation(value = "查询可预约时间")
    @RequestMapping(method={RequestMethod.GET},value = "queryReservableTime")
    public JsonResult<List<Map<String, Object>>> queryReservableTime(String startDate, Integer lengthTime, Integer departmentId, Integer orderId,Boolean needRemoveOldServe){
        if(startDate == null || lengthTime == null){
            logger.info("传递参数为空：" + "开始时间：" + startDate + "时长：" + lengthTime);
            return new JsonResult<List<Map<String, Object>>>(false, String.format(JsonResult.PARAM_NULL, "startDate或lengthTime"), null);
        }
        if(needRemoveOldServe!=null&&needRemoveOldServe){
        	lengthTime+=30;
        }
        logger.info("获取可选取日期时间列表开始...");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startTime = dayStartTime;
        String endDateTime = null;
        String startDateTime = startDate + " " + startTime;
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
        	int timeNode = 15;
            while(simpleDateFormat.parse(startDateTime).before(simpleDateFormat.parse(startDate+ " " + dayEndTime))) {
	            Map<String, Object> statusMap = new HashMap<String, Object>();
	            Date startDateTimeFormat = simpleDateFormat.parse(startDateTime);
	            startDate = new SimpleDateFormat("yyyy-MM-dd").format(startDateTimeFormat);
	            startTime = new SimpleDateFormat("HH:mm:ss").format(startDateTimeFormat);
	            Calendar cal = Calendar.getInstance();
	            if(simpleDateFormat.parse(startDateTime).before(new Date())){
	            	if(needRemoveOldServe==null){
		                statusMap.put("status", true);
		                statusMap.put("time", startTime);
		                list.add(statusMap);
		                cal.setTime(startDateTimeFormat);
		                cal.add(Calendar.MINUTE, timeNode);
		                startDateTime = simpleDateFormat.format(cal.getTime());
		                continue;
	            	}else{
		                statusMap.put("status", false);
		                statusMap.put("time", startTime);
		                list.add(statusMap);
		                cal.setTime(startDateTimeFormat);
		                cal.add(Calendar.MINUTE, timeNode);
		                startDateTime = simpleDateFormat.format(cal.getTime());
		                continue;
	            	}
	            }
	            cal.setTime(startDateTimeFormat);
	            cal.add(Calendar.MINUTE, lengthTime);
	            endDateTime = simpleDateFormat.format(cal.getTime());
	            //查询这个时间点上班的人数
	            List<ClassesTechnician> listClassesTechnician = classesTechnicianService.getReservableTime(startDate, startTime, departmentId);
	            //获取已经被预约的数量，不包括当前订单
                List<ScheduleServe> listScheduleServe = new ArrayList<ScheduleServe>();
                listScheduleServe = scheduleManageService.listScheduleServeNotContainOrderId(startDateTime, endDateTime, departmentId, orderId);
	            if (listClassesTechnician.size() > listScheduleServe.size()) {
	                statusMap.put("status",true);
	                statusMap.put("time",startTime);
	                list.add(statusMap);
	            }else{
	                statusMap.put("status", false);
	                statusMap.put("time", startTime);
	                list.add(statusMap);
	            }
                cal.setTime(startDateTimeFormat);
                cal.add(Calendar.MINUTE, timeNode);
                startDateTime = simpleDateFormat.format(cal.getTime());
            }
        } catch (ParseException e) {
            e.printStackTrace();
            logger.info("日期参数格式错误");
            return new JsonResult<List<Map<String, Object>>>(false,"日期传输错误",null);
        }
        logger.info("获取可选取日期时间列表成功：" + list);
        return new JsonResult<List<Map<String, Object>>>(JsonResult.CODE_SUCCESS,JsonResult.APP_DEFINE_SUC,list);
    }
    /*根据id查询预约详情*/
    @ApiOperation(value = "根据id查询预约详情")
    @RequestMapping(method={RequestMethod.GET},value = "queryScheduleForDetail")
    public JsonResult<ScheduleManage> queryScheduleForDetail(Integer id, HttpServletRequest request, HttpServletResponse response){
        JsonResult<ScheduleManage> jsonResult = new JsonResult<ScheduleManage>();
        ScheduleManage scheduleManage = scheduleManageService.getScheduleForDetail(id);
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(scheduleManage);
        return jsonResult;
    }
    /*查询可预约的美甲师*/
    @ApiOperation(value = "查询可预约的美甲师")
    @RequestMapping(method={RequestMethod.GET},value = "queryReservableTechnician")
    public JsonResult queryReservableTechnician(String startDateTime, String pcType,Integer orderId, Integer lengthTime, Integer departmentId, Integer scheduleServeId, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        logger.info("获取可选美甲师列表开始...");
        if(startDateTime == null || lengthTime == null){
            logger.info("传递参数为空：" + "开始时间：" + startDateTime + "时长：" + lengthTime);
            return jsonResult;
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, Object> technicianMap= new LinkedMultiValueMap<String, Object>();
        technicianMap.add("departmentId", departmentId.toString());
        HttpEntity<MultiValueMap<String, Object>> technicianEntity = new HttpEntity<MultiValueMap<String, Object>>(technicianMap, headers);
        ResponseEntity<JsonResult> technicianResponseEntity = restTemplate.postForEntity(technicianUrl + "technician/queryTechnicianManageForAll", technicianEntity, JsonResult.class);
        JsonResult technicianJsonResult = technicianResponseEntity.getBody();
        //获取所有美甲师列表
        List<LinkedHashMap> listTechnician = (List<LinkedHashMap>) technicianJsonResult.getData();
//        List<TechnicianManage> listTechnician = technicianManageService.listTechnicianManageForAll(departmentId);
        try {//pc端过去的时间都可选
            if(pcType != null && simpleDateFormat.parse(startDateTime).before(new Date())){
                for(LinkedHashMap technician : listTechnician){
                    technician.put("status",true);
                }
                jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(listTechnician);
                return jsonResult;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            Date startDateTimeFormat = simpleDateFormat.parse(startDateTime);
            Calendar cal = Calendar.getInstance();
            cal.setTime(startDateTimeFormat);
            cal.add(Calendar.MINUTE, lengthTime);
            String endDateTime = simpleDateFormat.format(cal.getTime());
            String startDate = new SimpleDateFormat("yyyy-MM-dd").format(startDateTimeFormat);
            String startTime = new SimpleDateFormat("HH:mm:ss").format(startDateTimeFormat);
//            MultiValueMap<String, Object> map= new LinkedMultiValueMap<String, Object>();
//            map.add("startDate", startDate);
//            map.add("startTime", startTime);
//            map.add("departmentId", departmentId.toString());
//            HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String, Object>>(map, headers);
//            ResponseEntity<JsonResult> responseEntity = restTemplate.postForEntity(storeUrl + "classestechnician/getReservableTime", entity, JsonResult.class);
//            JsonResult jsonResult1 = responseEntity.getBody();
            //获取当前日期排班美甲师班次时间列表
            List<ClassesTechnician> listClassesTechnician = classesTechnicianService.getReservableTime(startDate, startTime, departmentId);
//            listClassTimeByStartDate
//            List<LinkedHashMap> listClassesTechnician = (List<LinkedHashMap>) jsonResult1.getData();
            //获取包含当前预约时间的已预约服务记录  --  修改时不查询自身已预约美甲师
            List<ScheduleServe> listScheduleServe = new ArrayList<ScheduleServe>();//这个东西没把technician插回来
            if(scheduleServeId != null){
                listScheduleServe = scheduleManageService.listScheduleServeNotContainItself(startDateTime, endDateTime, departmentId, scheduleServeId);
            }else{
                listScheduleServe = scheduleManageService.listScheduleServe(startDateTime, endDateTime, departmentId);
            }

            if(listClassesTechnician.size() > listScheduleServe.size()){
            listTechnician = retureOptionalTechnician(listClassesTechnician, listTechnician, listScheduleServe);
                /*for(LinkedHashMap technicianManage : listTechnician){
                    technicianManage.put("status",false);
                    if(!listTechnicianId.contains(technicianManage.get("id"))){
                        technicianManage.put("status",true);
                    }
                }*/
            }else {
                for(LinkedHashMap technician : listTechnician){
                    technician.put("status",false);
                }
            }
            logger.info("获取可选取美甲师列表成功：" + listTechnician);
          //查询订单的技师,设置到可选技师列表
            OrderManage om = null;
            if(orderId!=null){
            	om = orderManageService.getOrderForDetail(orderId);
            }
            Map<Integer, Integer> orderTechMap = new HashMap<Integer, Integer>();
            if(om!=null){
                List<OrderServe> list = om.getListOrderServer();
                Iterator<OrderServe> it = list.iterator();
                while(it.hasNext()){
                	OrderServe os = it.next();
                	if(os.getTechnicianId()!=null){
                		String orderTech [] = os.getTechnicianId().split(",");
                		for(int i=0;i<orderTech.length;i++){
                			if(orderTech[i]!=null&&!orderTech[i].isEmpty()){
                				Integer intTechId = null;
                				try {
                					intTechId = Integer.parseInt(orderTech[i]);
    							} catch (Exception e) {
    								
    							}
                				if(intTechId!=null){
                					orderTechMap.put(intTechId,intTechId);
                				}
                			}
                		}
                	}
                }
            }
            

            
            Iterator<LinkedHashMap> ittech = listTechnician.iterator();
            while(ittech.hasNext()){
            	LinkedHashMap item = ittech.next();
            	Integer techid = Integer.parseInt(item.get("id").toString());
            	if(orderTechMap.containsKey(techid)){
            		item.put("status", true	);
            	}
            }
            jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(listTechnician);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return jsonResult;
    }
    @ApiIgnore
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "queryScheduleForPage")
    public JsonResult queryScheduleForPage(ScheduleManage scheduleManage, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        String pageNum = request.getParameter("pageNum");
        String pageSize = request.getParameter("pageSize");
        if(pageNum==null||pageSize==null){
        	jsonResult.setMessage("pageNum或pageSize为空");
        	jsonResult.setSuccess(false);
        	return jsonResult;
        }
        PageHelper.startPage(Integer.parseInt(pageNum),Integer.parseInt(pageSize));
        List<ScheduleManage> list = scheduleManageService.listScheduleManageForPage(scheduleManage);
        PageInfo<ScheduleManage> pageInfo = new PageInfo<ScheduleManage>(list);
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(pageInfo);
        return jsonResult;
    }

    @ApiIgnore
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "queryScheduleNumber")
    public JsonResult queryScheduleNumber(Integer userId, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        Integer resultNumber = scheduleManageService.getScheduleNumber(userId);
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(resultNumber);
        return jsonResult;
    }

    @ApiIgnore
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "queryTodaySummary")
    public JsonResult queryTodaySummary(Integer departmentId, HttpServletRequest request, HttpServletResponse response) {
        JsonResult jsonResult = new JsonResult();
        int summaryCount = scheduleManageService.getTodaySummary(departmentId);
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(summaryCount);
        return jsonResult;
    }
    @ApiIgnore
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "queryScheduleSummary")
    public JsonResult queryScheduleSummary(ScheduleManage scheduleManage, HttpServletRequest request, HttpServletResponse response) {
        JsonResult jsonResult = new JsonResult();
        int summaryCount = scheduleManageService.getScheduleSummary(scheduleManage);
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(summaryCount);
        return jsonResult;
    }

    @ApiIgnore
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "queryDetailAndRecord")
    public JsonResult queryDetailAndRecord(Integer id, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = scheduleManageService.getDetailAndRecord(id);
        return jsonResult;
    }

    @ApiIgnore
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "modifySchedule")
    public JsonResult modifySchedule(ScheduleManage scheduleManage, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        int result = scheduleManageService.updateSchedule(scheduleManage);
        logger.info("取消预约：" + scheduleManage);
        if(result > 0){
            jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC);
        }
        return jsonResult;
    }

    @ApiIgnore
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "queryTimeSlot")
    public JsonResult queryTimeSlot(String startDate, Integer lengthTime, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        String endTime = "";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        logger.info("开始时间结束时间计算开始...");
        try {
            Date date = simpleDateFormat1.parse(startDate);
            cal.setTime(date);
            cal.add(Calendar.MINUTE, lengthTime);
            endTime = simpleDateFormat.format(cal.getTime());
            String resultTime = simpleDateFormat.format(date) +" - " + endTime;
            jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(resultTime);
        } catch (ParseException e) {
            logger.info("开始时间结束时间计算错误：" + "开始时间：" + startDate + "时长：" + lengthTime);
        }
        return jsonResult;
    }

    @ApiIgnore
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "getReservableTime")
    public JsonResult getReservableTime(String startDate, String pcType, Integer serveId, Integer produceId, Integer lengthTime, Integer departmentId, Integer scheduleServeId, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        logger.info("获取可选取日期时间列表开始...");
        if(startDate == null || lengthTime == null){
            logger.info("传递参数为空：" + "开始时间：" + startDate + "时长：" + lengthTime);
            return jsonResult;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startTime = dayStartTime;
        String endDateTime = null;
        String startDateTime = startDate + " " + startTime;
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            while(simpleDateFormat.parse(startDateTime).before(simpleDateFormat.parse(startDate+ " " + dayEndTime))) {
            Map<String, Object> statusMap = new HashMap<String, Object>();
            Date startDateTimeFormat = simpleDateFormat.parse(startDateTime);
            startDate = new SimpleDateFormat("yyyy-MM-dd").format(startDateTimeFormat);
            startTime = new SimpleDateFormat("HH:mm:ss").format(startDateTimeFormat);
            Calendar cal = Calendar.getInstance();
            if(pcType == null && simpleDateFormat.parse(startDateTime).before(new Date())){
                statusMap.put("status", false);
                statusMap.put("time", startTime);
                list.add(statusMap);
                cal.setTime(startDateTimeFormat);
                cal.add(Calendar.MINUTE, 30);
                startDateTime = simpleDateFormat.format(cal.getTime());
                continue;
            }else if(pcType != null && simpleDateFormat.parse(startDateTime).before(new Date())) {
                statusMap.put("status", true);
                statusMap.put("time", startTime);
                list.add(statusMap);
                cal.setTime(startDateTimeFormat);
                cal.add(Calendar.MINUTE, 30);
                startDateTime = simpleDateFormat.format(cal.getTime());
                continue;
            }
            cal.setTime(startDateTimeFormat);
            cal.add(Calendar.MINUTE, lengthTime);
            endDateTime = simpleDateFormat.format(cal.getTime());
            MultiValueMap<String, Object> map= new LinkedMultiValueMap<String, Object>();
            map.add("startDate", startDate);
            map.add("startTime", startTime);
            map.add("departmentId", departmentId.toString());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String, Object>>(map, headers);
            ResponseEntity<JsonResult> responseEntity = restTemplate.postForEntity(storeUrl + "classestechnician/getReservableTime", entity, JsonResult.class);
            JsonResult jsonResult1 = responseEntity.getBody();
            //获取当前日期排班美甲师班次时间列表
            List<ClassesTechnician> listClassesTechnician = (List<ClassesTechnician>) jsonResult1.getData();
            //获取包含当前预约时间的已预约服务记录  --  修改时不查询自身已预约时间
                List<ScheduleServe> listScheduleServe = new ArrayList<ScheduleServe>();
                if(scheduleServeId != null){
                    listScheduleServe = scheduleManageService.listScheduleServeNotContainItself(startDateTime, endDateTime, departmentId, scheduleServeId);
                }else{
                    listScheduleServe = scheduleManageService.listScheduleServe(startDateTime, endDateTime, departmentId);
                }
            if (listClassesTechnician.size() > listScheduleServe.size()) {
                statusMap.put("status",true);
                statusMap.put("time",startTime);
                list.add(statusMap);
            }else{
                statusMap.put("status", false);
                statusMap.put("time", startTime);
                list.add(statusMap);
            }
                cal.setTime(startDateTimeFormat);
                cal.add(Calendar.MINUTE, 30);
                startDateTime = simpleDateFormat.format(cal.getTime());
            }
        } catch (ParseException e) {
            e.printStackTrace();
            logger.info("日期参数格式错误");
            jsonResult.setMessage("日期传输错误");
            return jsonResult;
        }
        logger.info("获取可选取日期时间列表成功：" + list);
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(list);
        return jsonResult;
    }

    @ApiIgnore
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "queryNowTime")
    public JsonResult queryNowTime(HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        Long nowTime = new Date().getTime();
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(nowTime);
        return jsonResult;
    }


    @ApiIgnore
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "addScheduleAndServe")
    public JsonResult addScheduleAndServe(String startDateTime, Integer lengthTime, Integer userId, Integer departmentId, Integer technicianId, Integer serveId, String produceName, String serveName, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = scheduleManageService.insertScheduleAndServe(startDateTime, lengthTime, userId, departmentId, technicianId, serveId, produceName, serveName);
        if(jsonResult.getSuccess()){
            jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC);
        }
        return jsonResult;
    }

    @ApiIgnore
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "deleteScheduleAndServe")
    public JsonResult deleteScheduleAndServe(Integer serveId, String delRemark, HttpServletRequest request, HttpServletResponse response) {
        return scheduleManageService.deleteScheduleAndServe(serveId, delRemark);
    }

    @ApiIgnore
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "addScheduleWithServeByStore")
    public JsonResult addScheduleWithServeByStore (@RequestBody ScheduleManage scheduleManage, HttpServletRequest request, HttpServletResponse response) {
        return scheduleManageService.insertScheduleWithServeByStore(scheduleManage);
    }

    public List<LinkedHashMap> retureOptionalTechnician( List<ClassesTechnician> listClassesTechnician, List<LinkedHashMap> listTechnician, List<ScheduleServe> listTechnicianServe){
        List<Integer> list = new ArrayList<Integer>();
        //获取排班但不能预约美甲师列表  为下一步获取可选美甲师做准备
        for(ClassesTechnician classesTechnician : listClassesTechnician){
            for(ScheduleServe technicianServe : listTechnicianServe){
                if(classesTechnician.getUserId().equals(technicianServe.getTechnicianId())){
                    list.add((Integer) classesTechnician.getUserId());
                }
            }
        }
        //获取所有美甲师和不能预约美甲师状态
        for(LinkedHashMap technician : listTechnician){
            technician.put("status",false);
            for(ClassesTechnician classesTechnician : listClassesTechnician){//判断是否在上班,判断是否被预约
                if(technician.get("id").equals(classesTechnician.getUserId()) && !(list.contains(technician.get("id")))){
                    technician.put("status",true);
                }
            }
        }
        return listTechnician;
    }
	/**将emoji表情替换成空串,过滤后的字符串*/
	 public static String filterEmoji(String source) {
	  if (source != null && source.length() > 0) {
	   return source.replaceAll("[\ud800\udc00-\udbff\udfff\ud800-\udfff]", "");
	  } else {
	   return source;
	  }
	 }
}
