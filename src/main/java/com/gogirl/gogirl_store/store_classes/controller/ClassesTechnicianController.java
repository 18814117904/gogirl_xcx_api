package com.gogirl.gogirl_store.store_classes.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gogirl.gogirl_order.order_commons.utils.JsonResult;
import com.gogirl.gogirl_store.store_classes.service.ClassesManageService;
import com.gogirl.gogirl_store.store_classes.service.ClassesTechnicianService;
import com.gogirl.gogirl_store.store_classes.service.impl.ClassesTechnicianServiceImpl;
import com.gogirl.gogirl_store.store_commons.config.Constans;
import com.gogirl.gogirl_store.store_commons.dto.ClassesManage;
import com.gogirl.gogirl_store.store_commons.dto.ClassesTechnician;
import com.gogirl.gogirl_technician.technician_commons.dto.UserTechnician;
import com.gogirl.gogirl_technician.technician_user.service.TechnicianManageService;
import com.gogirl.gogirl_technician.technician_user.service.UserTechnicianService;
import com.gogirl.gogirl_xcx.entity.GogirlToken;
import com.gogirl.gogirl_xcx.service.GogirlTokenService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import springfox.documentation.annotations.ApiIgnore;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Api(tags={"11.美甲师排班"},value="美甲师排班")
@RestController
@RequestMapping("classestechnician")
public class ClassesTechnicianController {
    private final Logger logger = LoggerFactory.getLogger(ClassesTechnicianServiceImpl.class);
    @Autowired
    private ClassesTechnicianService classesTechnicianService;
    @Value("${start-time}")
    private String dayStartTime;
    @Value("${end-time}")
    private String dayEndTime;
    @Autowired
    private RestTemplate restTemplate;
    @Value("${technician-url}")
    private String technicianUrl;
	@Resource
	GogirlTokenService gogirlTokenService;
	@Resource
	ClassesManageService classesManageService;
	@Resource
	UserTechnicianService userTechnicianService;
    @ApiOperation(value = "美甲师端查询当天店铺排班")
    @RequestMapping(method={RequestMethod.GET},value = "queryDayTechnicianClasses")
    public JsonResult<List<UserTechnician>> queryDayTechnicianClasses(String token,String dayString){
    	GogirlToken gogirlToken = gogirlTokenService.getTokenByToken_t(token);
    	if(gogirlToken==null){
    		return new JsonResult<List<UserTechnician>>(false,JsonResult.TOKEN_NULL_CODE);
    	}
    	if(gogirlToken.getUserTechnician()==null){
    		return new JsonResult<>(false,"找不到美甲师");
    	}
    	Date day = null;
    	try{
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    		day = sdf.parse(dayString);
    	}catch(Exception e){
    		return new JsonResult<>(false,"day格式为yyyy-MM-dd");
    	}
    	Integer departmentId = gogirlToken.getUserTechnician().getDepartmentId();
    	//查询店铺下的美甲师
    	List<UserTechnician> list= userTechnicianService.queryDayTechnicianClasses(departmentId,day);
    	List<ClassesManage> classList = classesManageService.listClassesManage(departmentId);
    	Map<Integer, Integer> map = new HashMap<Integer, Integer>();//处理一个对应关系只返回012
    	map.put(0,0);
    	for(int i=0;i<classList.size();i++){
    		ClassesManage item = classList.get(i);
    		if(item.getName().equals("早班")){
    			map.put(item.getId(),1 );
    		}else if(item.getName().equals("晚班")){
    			map.put(item.getId(),2 );
    		}
//    		else if(item.getName().equals("休息")){
//    			map.put(item.getId(),0 );
//    		}
    	}
    	for(UserTechnician item : list){//根据map对应关系转换class
			item.setId(item.getTechnicianId());
    		if(item.getClassesTechnician()!=null){
    			item.getClassesTechnician().setClasses(map.get(item.getClassesTechnician().getClasses()));
    		}
    	}
    	return new JsonResult<>(true,JsonResult.APP_DEFINE_SUC,list);
    }
    @ApiOperation(value = "美甲师端提交当天店铺排班",notes="{\"dayString\":\"2019-08-28\",\"list\":[{\"id\":59,\"classesTechnician\":{\"classes\":1,\"id\":5678}},{\"id\":62,\"classesTechnician\":{\"classes\":2,\"id\":5709}}],\"token\":\"3a18ea5546\"}")
    @RequestMapping(method={RequestMethod.POST},value = "updateDayTechnicianClasses")
    public JsonResult<Object> updateDayTechnicianClasses(@RequestBody ParmTechnicianClasses ParmTechnicianClasses){
    	String token = ParmTechnicianClasses.getToken();
    	String dayString = ParmTechnicianClasses.getDayString();
    	List<UserTechnician> list = ParmTechnicianClasses.getList();
    	
    	
    	GogirlToken gogirlToken = gogirlTokenService.getTokenByToken_t(token);
    	if(gogirlToken==null){
    		return new JsonResult<>(false,JsonResult.TOKEN_NULL_CODE);
    	}
    	if(gogirlToken.getUserTechnician()==null){
    		return new JsonResult<>(false,"找不到美甲师");
    	}
    	Date day = null;
    	try{
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    		day = sdf.parse(dayString);
    	}catch(Exception e){
    		return new JsonResult<>(false,"day格式为yyyy-MM-dd");
    	}
    	Integer departmentId = gogirlToken.getUserTechnician().getDepartmentId();
    	//查询店铺下的早晚修排班
    	List<ClassesManage> classList = classesManageService.listClassesManage(departmentId);
    	Map<Integer, Integer> map = new HashMap<Integer, Integer>();//处理一个对应关系只返回012
    	map.put(0,0);
    	for(int i=0;i<classList.size();i++){
    		ClassesManage item = classList.get(i);
    		if(item.getName().equals("早班")){
    			map.put(1,item.getId() );
    		}else if(item.getName().equals("晚班")){
    			map.put(2,item.getId() );
    		}
//    		else if(item.getName().equals("休息")){
//    			map.put(0,item.getId() );
//    		}
    	}
    	
    	for(int i=0;i<list.size();i++){
    		UserTechnician item = list.get(i);
    		if(item.getId()==null){
    			return new JsonResult<>(false,"第"+(i+1)+"个美甲师id不能为空");
    		}
    		if(item.getClassesTechnician()==null){
    			return new JsonResult<>(false,"第"+(i+1)+"个美甲师排班classesTechnician不能为空");
    		}
    		//修改早晚班为对应的classid
    		if(item.getClassesTechnician().getClasses()!=null){
    			item.getClassesTechnician().setClasses(map.get(item.getClassesTechnician().getClasses()));
    		}
    		if(item.getClassesTechnician().getClasses()!=null&&item.getClassesTechnician().getId()!=null){
    			classesTechnicianService.updateClassesTechnician(item.getClassesTechnician());
    		}else if(item.getClassesTechnician().getClasses()!=null){
    			ClassesTechnician classesTechnician = new ClassesTechnician();
    			classesTechnician.setUserId(item.getId());
    			classesTechnician.setDays(day);
    			List<ClassesTechnician> hasClass=classesTechnicianService.listClassesTechnicianDetailByDays(item.getId(), dayString);
    			if(hasClass!=null&&hasClass.size()>0){
    				item.getClassesTechnician().setId(hasClass.get(0).getId());
    				classesTechnicianService.updateClassesTechnician(item.getClassesTechnician());
    			}else{
    				item.getClassesTechnician().setUserId(item.getId());
    				item.getClassesTechnician().setDays(day);
    				item.getClassesTechnician().setStatus(2);
    				classesTechnicianService.insertClassesTechnician(item.getClassesTechnician());
    			}
    		}else{
    			
    		}
    	}
    	
//    	Integer departmentId = gogirlToken.getTechnicianManage().getDepartmentId();
//    	//查询店铺下的美甲师
//    	List<TechnicianManage> list= technicianManageService.queryDayTechnicianClasses(departmentId,day);
    	return new JsonResult<>(true,JsonResult.APP_DEFINE_SUC,null);
    }

    
    @ApiOperation(value = "美甲师端查询当月排班排班")
    @RequestMapping(method={RequestMethod.GET},value = "queryMonthClasses")
	@ApiImplicitParams({
	@ApiImplicitParam(name = "month", value = "年月：2019-08", required = true, dataType = "String"),
	@ApiImplicitParam(name = "technicianId", value = "美甲师id:59", required = true, dataType = "int"),
	@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String")
	})
    public JsonResult<List<ClassesTechnician>> queryMonthClasses(String token,Integer technicianId,String month){
    	GogirlToken gogirlToken = gogirlTokenService.getTokenByToken_t(token);
    	if(gogirlToken==null){
    		return new JsonResult<>(false,JsonResult.TOKEN_NULL_CODE);
    	}
    	if(gogirlToken.getUserTechnician()==null){
    		return new JsonResult<>(false,"找不到美甲师");
    	}
    	Integer departmentId = gogirlToken.getUserTechnician().getDepartmentId();
    	//查询店铺下的美甲师
    	List<ClassesManage> classList = classesManageService.listClassesManage(departmentId);
    	Map<Integer, Integer> map = new HashMap<Integer, Integer>();//处理一个对应关系只返回012
    	map.put(0,0);
    	for(int i=0;i<classList.size();i++){
    		ClassesManage item = classList.get(i);
    		if(item.getName().equals("早班")){
    			map.put(item.getId(), 1);
    		}else if(item.getName().equals("晚班")){
    			map.put(item.getId(),2 );
    		}
//    		else if(item.getName().equals("休息")){
//    			map.put(item.getId(),0 );
//    		}
    	}
    	
        List<ClassesTechnician> lists = classesTechnicianService.listClassesTechnicianDetail(gogirlToken.getUserTechnician().getTechnicianId(), month);
    	//替换排班的class
    	for(ClassesTechnician item :lists){
    		item.setClasses(map.get(item.getClasses()));
    	}
        return new JsonResult<List<ClassesTechnician>>(true ,JsonResult.APP_DEFINE_SUC,lists);
    }
    
//    @ApiOperation(value = "设置月店铺默认排班")
//    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "queryClassesTechnicianForPage")
//    public JsonResult<PageInfo<?>> queryClassesTechnicianForPage(ClassesTechnician classesTechnician, HttpServletRequest request, HttpServletResponse response){
//        JsonResult<PageInfo<?>> jsonResult = new JsonResult<PageInfo<?>>();
//        String pageNum = request.getParameter("pageNum");
//        String pageSize = request.getParameter("pageSize");
//        PageHelper.startPage(Integer.parseInt(pageNum),Integer.parseInt(pageSize));
//        List<ClassesTechnician> lists = classesTechnicianService.listClassesTechnicianForPage(classesTechnician);
//        PageInfo<ClassesTechnician> pageInfo = new PageInfo<ClassesTechnician>(lists);
//        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(pageInfo);
//        return jsonResult;
//    }
    
    
    @ApiIgnore
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "queryClassesTechnicianForPage")
    public JsonResult<PageInfo<?>> queryClassesTechnicianForPage(ClassesTechnician classesTechnician, HttpServletRequest request, HttpServletResponse response){
        JsonResult<PageInfo<?>> jsonResult = new JsonResult<PageInfo<?>>();
        String pageNum = request.getParameter("pageNum");
        String pageSize = request.getParameter("pageSize");
        PageHelper.startPage(Integer.parseInt(pageNum),Integer.parseInt(pageSize));
        List<ClassesTechnician> lists = classesTechnicianService.listClassesTechnicianForPage(classesTechnician);
        PageInfo<ClassesTechnician> pageInfo = new PageInfo<ClassesTechnician>(lists);
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(pageInfo);
        return jsonResult;
    }

    @ApiIgnore
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "queryClassesTechnician")
    public JsonResult<List<?>> queryClassesTechnician(Integer departmentId, String days, HttpServletRequest request, HttpServletResponse response){
        JsonResult<List<?>> jsonResult = new JsonResult<List<?>>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            days = simpleDateFormat.format(simpleDateFormat.parse(days));
        } catch (ParseException e) {
            e.printStackTrace();
            jsonResult.setSuccess(JsonResult.CODE_ERROR).setMessage(JsonResult.APP_DEFINE_ERR);
            return jsonResult;
        }
        List<ClassesTechnician> list = classesTechnicianService.listClassesTechnician(departmentId, days);
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(list);
        return jsonResult;
    }

    @ApiIgnore
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "getReservableTime")
    public JsonResult<List<?>> getReservableTime(String startDate, String startTime, Integer departmentId, HttpServletRequest request, HttpServletResponse response){
        JsonResult<List<?>> jsonResult = new JsonResult<List<?>>();
        logger.info("获取当前日期排班美甲师列表开始... 开始日期：" + startDate + "开始时间：" + startTime + "部门："+ departmentId);
        List<ClassesTechnician> listClassesTechnician = classesTechnicianService.getReservableTime(startDate, startTime, departmentId);
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(listClassesTechnician);
        return jsonResult;
    }

    @ApiIgnore
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "modifyClassesTechnician")
    public JsonResult<?> modifyClassesManage(ClassesTechnician classesTechnician, HttpServletRequest request, HttpServletResponse response){
        JsonResult<?> jsonResult = new JsonResult<Object>();
        int result = classesTechnicianService.updateClassesTechnician(classesTechnician);
        if(result > 0){
            jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC);
        }
        return jsonResult;
    }

    @ApiIgnore
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "deleteClassesTechnicianById")
    public JsonResult<?> deleteClassesManageById(Integer id, HttpServletRequest request, HttpServletResponse response){
        JsonResult<?> jsonResult = new JsonResult<Object>();
        int result = classesTechnicianService.deleteClassesTechnicianById(id);
        if(result > 0){
            jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC);
        }
        return jsonResult;
    }

    @ApiIgnore
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "addClassesTechnician")
    public JsonResult<?> addClassesManage(ClassesTechnician classesTechnician, HttpServletRequest request, HttpServletResponse response){
        JsonResult<?> jsonResult = new JsonResult<Object>();
        int result = classesTechnicianService.insertClassesTechnician(classesTechnician);
        if(result > 0){
            jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC);
        }
        return jsonResult;
    }

    @ApiIgnore
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "queryTimeArray")
    public JsonResult<List<String>> queryTimeArray(HttpServletRequest request, HttpServletResponse response){
        JsonResult<List<String>> jsonResult = new JsonResult<List<String>>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        String startTime = dayStartTime;
        List<String> list = new ArrayList<String>();
        try{
            while(simpleDateFormat.parse(startTime).before(simpleDateFormat.parse(dayEndTime))){
                list.add(startTime);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(simpleDateFormat.parse(startTime));
                calendar.add(Calendar.MINUTE, 30);
                startTime = simpleDateFormat.format(calendar.getTime());
            }
        }catch (ParseException e) {
            e.printStackTrace();
            jsonResult.setMessage("日期传输错误");
            return jsonResult;
        }
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(list);
        return jsonResult;
    }

    @ApiIgnore
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "queryTechnicianClasses")
    public JsonResult<List<?>> queryTechnicianClasses (Integer departmentId, String dayTime, HttpServletRequest request, HttpServletResponse response) {
        JsonResult<List<?>> jsonResult = new JsonResult<List<?>>();
        Integer year = Integer.valueOf(dayTime.split("-")[0]);
        Integer month = Integer.valueOf(dayTime.split("-")[1]);

        //获取月的天数
        Integer numberOfDays = getDaysByYearMonth(year, month);

        //返回美甲师系统查询店铺所有美甲师
        MultiValueMap<String, Object> requestEntity = new LinkedMultiValueMap<>();
        requestEntity.add("departmentId", departmentId.toString());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String, Object>>(requestEntity, httpHeaders);
        ResponseEntity<JsonResult> responseEntity = restTemplate.postForEntity(technicianUrl+"technician/queryTechnicianManageForAll", entity, JsonResult.class);
        JsonResult<?> technicianJsonResult = responseEntity.getBody();
        //获取美甲师数据
        List<LinkedHashMap> listTechnician = (List<LinkedHashMap>) technicianJsonResult.getData();
        //获取美甲师具体排班情况
        List<Map<String, Object>> list = listtechnicianClasses(listTechnician, dayTime, numberOfDays);
        jsonResult.setSuccess(true).setMessage(JsonResult.APP_DEFINE_SUC).setData(list);
        return jsonResult;
    }

    @ApiIgnore
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "modifyTechnicianClasses")
    public JsonResult<?> modifyTechnicianClasses(@RequestBody List<Map<String, Object>> list, HttpServletRequest request, HttpServletResponse response){
        JsonResult<?> jsonResult = new JsonResult<Object>();
        int result = classesTechnicianService.updateClassesTechnicianByList(list);
        if(result > 0){
            jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC);
        }
        return jsonResult;
    }

    @ApiIgnore
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "addOrUpdateTechnicainClasses")
    public JsonResult<?> addTechnicainClasses(@RequestBody List<Map<String, Object>> list, HttpServletRequest request, HttpServletResponse response){
        JsonResult<?> jsonResult = new JsonResult<Object>();
        classesTechnicianService.insertClassesTechnicianByStore(list);
        jsonResult.setSuccess(true).setMessage(JsonResult.APP_DEFINE_SUC);
        return jsonResult;
    }

    public int getDaysByYearMonth(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1); //设置当月第一天
        a.roll(Calendar.DATE, -1); //当月内循环 当月第一天减一天为当月最后一天
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    public List<Map<String, Object>> listtechnicianClasses(List<LinkedHashMap> listTechnician, String dayTime, Integer numberOfDays){
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for(LinkedHashMap<?, ?> linkedHashMap : listTechnician){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("userId", linkedHashMap.get("id"));
            map.put("userName", linkedHashMap.get("name"));
            String [] month = dayTime.split("-");
            dayTime = month[0] + "-" + new DecimalFormat("00").format(Integer.parseInt(month[1]));
            List<ClassesTechnician> classesTechnicianList = classesTechnicianService.listClassesTechnicianDetail((Integer) linkedHashMap.get("id"), dayTime);
            if(classesTechnicianList != null && classesTechnicianList.size() > 0){
                map.put("modify", true);
                map.put("dateClasses", classesTechnicianList);
            }else{
            List<Map<String, Object>> listday = new ArrayList<Map<String, Object>>();
            for(int i = 1; i <= numberOfDays; i++){
                Map<String, Object> mapday = new HashMap<String, Object>();
                mapday.put("days", i);
                mapday.put("classes", Constans.CLASSES_REST);
                listday.add(mapday);
            }
            map.put("dateClasses", listday);
            }
            list.add(map);
        }
        return list;
    }
}
