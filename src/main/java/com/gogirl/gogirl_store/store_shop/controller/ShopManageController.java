package com.gogirl.gogirl_store.store_shop.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gogirl.gogirl_order.order_commons.utils.JsonResult;
import com.gogirl.gogirl_store.store_classes.service.ClassesManageService;
import com.gogirl.gogirl_store.store_commons.dto.ClassesManage;
import com.gogirl.gogirl_store.store_commons.dto.ShopManage;
import com.gogirl.gogirl_store.store_commons.utils.MapDistance;
import com.gogirl.gogirl_store.store_shop.service.ShopManageService;
import com.gogirl.gogirl_xcx.dao.XcxFormIdMapper;
import com.gogirl.gogirl_xcx.entity.GogirlToken;
import com.gogirl.gogirl_xcx.entity.XcxFormId;
import com.gogirl.gogirl_xcx.service.GogirlTokenService;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import springfox.documentation.annotations.ApiIgnore;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.*;

/**
 * Created by yinyong on 2018/9/17.
 */


@RestController
@Api(tags = { "9.店铺" },value = "店铺")
@RequestMapping("shop")
public class ShopManageController {

    private Logger logger = LoggerFactory.getLogger(ShopManageController.class);

    @Autowired
    private ShopManageService shopManageService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ClassesManageService classesManageService;
    @Resource
    XcxFormIdMapper xcxFormIdMapper;
    @Resource
    GogirlTokenService gogirlTokenService;
	@ApiOperation(value = "查询店铺列表，longitude和latitude经纬度和页码都是可选参数")
    @RequestMapping(method={RequestMethod.GET},value = "queryShopForPage")
    public JsonResult queryShopForPage(String longitude,String latitude,Integer pageNum,Integer pageSize){
        JsonResult jsonResult = new JsonResult();
        if(pageNum!=null&&pageSize!=null){
            PageHelper.startPage(pageNum,pageSize);
        }
        ShopManage shopManage = new ShopManage();
        shopManage.setLongitude(longitude);
        shopManage.setLatitude(latitude);
        List<ShopManage> lists = shopManageService.listShopManageForPage(shopManage);
        if(StringUtils.isNotBlank(shopManage.getLongitude()) && StringUtils.isNotBlank(shopManage.getLatitude())&&!shopManage.getLongitude().equals("undefined")&&!shopManage.getLatitude().equals("undefined")){
        	for(int i=0;i<lists.size();i++){
        		ShopManage s1 = lists.get(i);
        		Double arg0 = MapDistance.getDistance(shopManage.getLongitude(), shopManage.getLatitude(), s1.getLongitude(), s1.getLatitude());
        		s1.setDistance(arg0);
        	}
            Collections.sort(lists,new Comparator<ShopManage>(){
                public int compare(ShopManage arg0, ShopManage arg1){
                    return arg0.getDistance().compareTo(arg1.getDistance());
                }
            });
        }

//        if(StringUtils.isNotBlank(shopManage.getLongitude()) && StringUtils.isNotBlank(shopManage.getLatitude())){
//            Collections.sort(lists,new Comparator<ShopManage>(){
//                public int compare(ShopManage arg0, ShopManage arg1){
//                    return MapDistance.getDistance(shopManage.getLongitude(), shopManage.getLatitude(), arg0.getLongitude(), arg0.getLatitude()).compareTo(MapDistance.getDistance(shopManage.getLongitude(), shopManage.getLatitude(), arg1.getLongitude(), arg1.getLatitude()));
//                }
//            });
//        }
        PageInfo<ShopManage> pageInfo = new PageInfo<ShopManage>(lists);
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(pageInfo);
        return jsonResult;
    }
	@ApiOperation(value = "id查店铺详情")
    @RequestMapping(method={RequestMethod.GET},value = "queryShopForDetail")
    public JsonResult queryShopForDetail(String longitude,String latitude,Integer id){
        JsonResult jsonResult = new JsonResult();
        ShopManage qshop = shopManageService.getShopManageForDetail(id);
        if(latitude!=null&&longitude!=null&&!latitude.equals("undefined")&&!longitude.equals("undefined")){
            Double arg0 = MapDistance.getDistance(longitude, latitude, qshop.getLongitude(), qshop.getLatitude());
            qshop.setDistance(arg0);
        }
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(qshop);
        return jsonResult;
    }

	@ApiOperation(value = "查询店照，不传departmentId则查全部店铺的店照")
    @RequestMapping(method={RequestMethod.GET},value = "queryShopEnvironmentPhotos")
    public JsonResult queryShopEnvironmentPhotos(Integer departmentId,String token,String formId){
		//特俗任务，存formId
        if(formId!=null&&!formId.isEmpty()&&!formId.equals("the formId is a mock one")&&token!=null&&!token.isEmpty()){
        	GogirlToken gogirltoken = gogirlTokenService.getTokenByToken(token);
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
      //特俗任务，存formId
		
		
		
        JsonResult jsonResult = new JsonResult();
        ShopManage shopManage = new ShopManage();
        shopManage.setId(departmentId);
        List<ShopManage> lists = shopManageService.listShopManageForPage(shopManage);
        List<String> resultList = new ArrayList<String>();
        for(int i=0;i<lists.size();i++){
        	String item = lists.get(i).getShopEnvironmentPhotos();
        	if(item==null)continue;
        	String[] arr = item.trim().split(",");
        	for(int j=0;j<arr.length;j++){
        		if(arr[j]!=null&&!arr[j].isEmpty())resultList.add(arr[j]);
        	}
        }
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(resultList);
        return jsonResult;
    }

    @ApiIgnore
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "queryShopForAll")
    public JsonResult queryShopForAll(){
        JsonResult jsonResult = new JsonResult();
        List<ShopManage> lists = shopManageService.listShopManageForPage(new ShopManage());
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(lists);
        return jsonResult;
    }

    @ApiIgnore
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "queryShopByServeIdForPage")
    public JsonResult queryShopByServeIdForPage(Integer serveId, String longitude, String latitude, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        String pageNum = request.getParameter("pageNum");
        String pageSize = request.getParameter("pageSize");
        PageHelper.startPage(Integer.parseInt(pageNum),Integer.parseInt(pageSize));
        List<ShopManage> lists = shopManageService.listShopByServeIdForPage(serveId);
        if(StringUtils.isNotBlank(longitude) && StringUtils.isNotBlank(latitude)){
            Collections.sort(lists,new Comparator<ShopManage>(){
                public int compare(ShopManage arg0, ShopManage arg1){
                    return MapDistance.getDistance(longitude, latitude, arg0.getLongitude(), arg0.getLatitude()).compareTo(MapDistance.getDistance(longitude, latitude, arg1.getLongitude(), arg1.getLatitude()));
                }
            });
        }
        PageInfo<ShopManage> pageInfo = new PageInfo<ShopManage>(lists);
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(pageInfo);
        return jsonResult;
    }


    @ApiIgnore
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "modifyShopManageById")
    public JsonResult modifyShopManageById(ShopManage shopManage, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        int result = shopManageService.updateShopManage(shopManage);
        if(result > 0){
            jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC);
        }
        return jsonResult;
    }

    @ApiIgnore
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "deleteShopManageById")
    public JsonResult deleteShopManageById(Integer id, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        int result = shopManageService.deleteShopManageById(id);
        if(result > 0){
            jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC);
        }
        return jsonResult;
    }

    @ApiIgnore
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "addShopManage")
    public JsonResult addShopManage(ShopManage shopManage, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        int result = shopManageService.insertShopManage(shopManage);
        ClassesManage classesManage = new ClassesManage();
        classesManage.setDepartmentId(shopManage.getId());
        classesManageService.insertClassesManage(classesManage);
        if(result > 0){
            jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC);
        }
        return jsonResult;
    }

    @ApiIgnore
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "getAddress")
    public JsonResult getAddress(String address, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        MultiValueMap<String, Object> map1= new LinkedMultiValueMap<String, Object>();
        map1.add("address", address);
      //  map1.add("output", "json");
        map1.add("key", "FDTBZ-IATWJ-PPWF2-KJ5D4-RNZV6-GJBDO");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String, Object>>(map1, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("https://apis.map.qq.com/ws/geocoder/v1/", entity, String.class);
        String jsonResult1 = responseEntity.getBody();
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(jsonResult1);
        return jsonResult;
    }
}
