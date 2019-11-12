package com.gogirl.gogirl_user.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import springfox.documentation.annotations.ApiIgnore;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gogirl.gogirl_order.order_commons.dto.OrderManage;
import com.gogirl.gogirl_order.order_commons.dto.OrderRecord;
import com.gogirl.gogirl_order.order_commons.dto.OrderServe;
import com.gogirl.gogirl_order.order_commons.utils.JsonResult;
import com.gogirl.gogirl_order.order_manage.service.OrderManageService;
import com.gogirl.gogirl_order.order_manage.service.OrderRecordService;
import com.gogirl.gogirl_order.order_manage.service.OrderServeService;
import com.gogirl.gogirl_user.dao.CustomerDetailMapper;
import com.gogirl.gogirl_user.entity.Customer;
import com.gogirl.gogirl_user.entity.CustomerDepartmentRelevance;
import com.gogirl.gogirl_user.entity.CustomerDepartmentRelevanceKey;
import com.gogirl.gogirl_user.entity.CustomerDetail;
import com.gogirl.gogirl_user.service.CustomerDepartmentRelevanceService;
import com.gogirl.gogirl_user.service.CustomerDetailService;
import com.gogirl.gogirl_user.service.CustomerService;
import com.gogirl.gogirl_xcx.dao.GogirlConfigMapper;
import com.gogirl.gogirl_xcx.entity.GogirlConfig;
import com.gogirl.gogirl_xcx.service.WechatService;
import com.gogirl.gogirl_xcx.util.CheckUtil;
@Api(tags = { "1.小程和用户相关接口" },value = "小程序相关接口")
@Controller
public class CustomerCotroller {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Resource
	private CustomerService customerService;
	@Resource
	private CustomerDetailService customerDetailService;
	@Resource
	private CustomerDepartmentRelevanceService customerDepartmentRelevanceService;
	@Resource
	OrderManageService orderManageService;
	@Resource
	OrderRecordService orderRecordService;
	@Resource
	WechatService wechatService;
	@Resource
	GogirlConfigMapper gogirlConfigMapper;
	
	@ApiOperation(value = "查询用户可选年龄段", notes = "在gogirl_config中配置")
	@ResponseBody
	@RequestMapping(method={RequestMethod.POST},value="/getAgeGroups")
	public JsonResult getAgeGroups(){
		GogirlConfig gogirlConfig = gogirlConfigMapper.selectByPrimaryKey(3);
		if(gogirlConfig==null||gogirlConfig.getValue()==null||gogirlConfig.getValue().isEmpty()){
			return new JsonResult(true,"请联系管理员,在gogirl_config表中配置id=3的value,用英文逗号隔开多个选项");
		}
		String[] ageGroups = gogirlConfig.getValue().split(",");
//		Map<String, String> map = new HashMap<>();
//		for(int i=0;i<ageGroups.length;i++){
//			map.put(ageGroups[i], ageGroups[i]);
//		}
		return new JsonResult(true,JsonResult.APP_DEFINE_SUC,ageGroups);
	}
	@ApiOperation(value = "用户详情数据查看", notes = "customerId必传\r\norderId为可选参数，传了就查订单记录信息")
	@ResponseBody
	@RequestMapping(method={RequestMethod.POST},value="/queryCustomerDetail")
	public JsonResult queryCustomerDetail(String token,Integer customerId,Integer orderId){
		if(token==null){
			return new JsonResult(false,String.format(JsonResult.PARAM_NULL,"token"),null);
		}
		if(customerId==null){
			return new JsonResult(false,String.format(JsonResult.PARAM_NULL,"customerId"),null);
		}

		//查询用户信息
		Customer customer = customerService.selectByPrimaryKeyWithCard(customerId);
		OrderManage orderManage = null;
		if(orderId!=null){
			//查询订单信息
			orderManage = orderManageService.getOrderWithRecord(orderId);
		}
		Map<String, Object> map = new HashMap<>();
		map.put("customer", customer);
		map.put("orderManage", orderManage);
		return new JsonResult(true,JsonResult.APP_DEFINE_SUC,map);
	}
	
	@ApiOperation(value = "用户详情数据修改", notes = "picturePath为多张图片的地址，用逗号,分隔开\r\ntoken暂时没有用到，customerId，orderId，orderServeId必传")
	@ResponseBody
	@RequestMapping(method={RequestMethod.POST},value="/updateCustomerDetail")
	public JsonResult updateCustomerDetail(Integer birthdayMonth,Integer birthdayDay,String ageGroup,String token,Integer customerId,Integer orderId,Integer orderServeId,String storeRecordRealName,String sex,Integer age,String job,String preference,String character,Integer customerSource){
//		Integer orderId,String remark,String userFeedback,String picturePath
		if(token==null){
			return new JsonResult(false,String.format(JsonResult.PARAM_NULL,"token"),null);
		}
		if(customerId==null){
			return new JsonResult(false,String.format(JsonResult.PARAM_NULL,"customerId"),null);
		}
		if(orderId==null){
			return new JsonResult(false,String.format(JsonResult.PARAM_NULL,"orderId"),null);
		}
//		if(orderServeId==null){
//			return new JsonResult(false,String.format(JsonResult.PARAM_NULL,"orderServeId"),null);
//		}

		//用户基本信息
		Customer customer = new Customer();
		customer.setId(customerId);
		customer.setStoreRecordRealName(storeRecordRealName);
		customer.setCustomerSource(customerSource);
		customer.setSex(sex);
		//用户详情信息
		CustomerDetail customerDetail = new CustomerDetail();
		customerDetail.setCustomerId(customerId);
		customerDetail.setAge(age);
		customerDetail.setJob(job);
		customerDetail.setPreference(preference);
		customerDetail.setCharacter(character);
		customerDetail.setBirthdayMonth(birthdayMonth);
		customerDetail.setBirthdayDay(birthdayDay);
		customerDetail.setAgeGroup(ageGroup);
		//订单信息
//		OrderManage  orderManage = new OrderManage();
//		orderManage.setId(orderId);
//		orderManage.setRemark(remark);
		
		//修改用户信息
		customerService.updateByPrimaryKeySelective(customer);
		
		//修改用户详情信息
		CustomerDetail cd = customerDetailService.selectByPrimaryKey(customerDetail.getCustomerId());
		if(cd==null){
			customerDetailService.insertSelective(customerDetail);
		}else{
			customerDetailService.updateByPrimaryKeySelective(customerDetail);
		}
		
		//修改订单信息
//		orderManageService.updateOrderStatus(orderManage);
		
//		//订单录入信息
//		OrderManage detail = orderManageService.getOrderForDetail(orderId);
//		if(detail.getListOrderServer()!=null){
//			List<OrderServe> list = detail.getListOrderServer();
//			for(int i =0;i<list.size();i++){
//				OrderRecord queryOrderRecord = orderRecordService.getOrderRecord(list.get(i).getId());
//				
//				OrderRecord orderRecord = new OrderRecord();
//				orderRecord.setOrderServeId(list.get(i).getId());
//				orderRecord.setUserFeedback(userFeedback);
//				orderRecord.setTechnicianFeedback(remark);
//				orderRecord.setPicturePath(picturePath);
//				//修改订单信息
//				if(queryOrderRecord==null){
//					orderRecordService.insertOrderRecord(orderRecord);
//				}else{
//					orderRecordService.updateOrderRecord(orderRecord);
//				}
//			}
//		}
		//计算资料录入百分比
		OrderManage om = orderManageService.getOrderForDetail(orderId);
		Customer c = customerService.selectByPrimaryKeyWithDetail(customerId);
		c.setCustomerDetail(customerDetail);
		double dataRate = CheckUtil.countOrderDataRate(c,om);
		OrderManage updateDataint = new OrderManage();
		updateDataint.setId(om.getId());
		updateDataint.setDataIntegrity(dataRate);
		orderManageService.updateOrderDataIntegrity(updateDataint);
		
		
		return new JsonResult(true,JsonResult.APP_DEFINE_SUC,null);
	}

	@ApiIgnore
	@ResponseBody
	@RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value="/insertOrUpdateCustomerDetail")
	public JsonResult insertOrUpdateCustomerDetail(CustomerDetail record){
		if(record==null||record.getCustomerId()==null){
			return new JsonResult(false,String.format(JsonResult.PARAM_NULL,"customerId"),null);
		}
		CustomerDetail cd= customerDetailService.selectByPrimaryKey(record.getCustomerId());
		if(cd==null){
			return new JsonResult(true,JsonResult.APP_DEFINE_SUC,customerDetailService.insertSelective(record));
		}else{
			return new JsonResult(true,JsonResult.APP_DEFINE_SUC,customerDetailService.updateByPrimaryKeySelective(record));
		}
	}
	@ApiIgnore
	@ResponseBody
	@RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value="/insertCustomer")
	public JsonResult insertCustomer(Customer customer) {
		//入参检查
		customer.setRegisterTime(new Date());
		customer.setUpdateTime(new Date());
		int result = customerService.insert(customer);
		Map<String, Object> map = new HashMap<>();
		map.put("id", result);
		return new JsonResult(true, "", map);
	}
	@ApiIgnore
	@ResponseBody
	@RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value="/insertOrUpdateCustomer")//订单系统和预约系统调用
	public JsonResult insertOrUpdateCustomer(Customer customer,Integer oldId,String openOrderUser) {
		//入参处理
		if(customer!=null&&customer.getStoreRecordRealName()!=null&&customer.getStoreRecordRealName().isEmpty()){
			customer.setStoreRecordRealName(null);
		}
		//入参处理
		Customer queryCustomer = null;
		if(customer!=null&&customer.getId()!=null){
			queryCustomer = customerService.selectByPrimaryKey(customer.getId());
		}else if(customer!=null&&customer.getPhone()!=null){
			if(customer.getPhone().equals("无")){
				customer.setPhone(null);
			}else{
				queryCustomer = customerService.selectByPhone(customer.getPhone());
			}
		}
//		else if(customer!=null&&customer.getSource()!=null&&customer.getSource()==3){
//			return new JsonResult(false,"微信预约或订单未传入id或电话号码。",null);
//		}
//		else if((customer!=null&&customer.getSource()!=null&&customer.getSource()==1)||(customer!=null&&customer.getSource()==2)){
//			return new JsonResult(false,"店铺预约或订单未传入id或电话号码。",null);
//		}
		else{
			//其他来源用户，默认插入
		}
		if(queryCustomer!=null){
			if((oldId==null||!oldId.equals(queryCustomer.getId()))&&customer.getSource()!=null&&(customer.getSource()==1||customer.getSource()==3)){
				customer.setScheduledTimes(queryCustomer.getScheduledTimes()==null?0:queryCustomer.getScheduledTimes()+1);
			}
			if((oldId==null||!oldId.equals(queryCustomer.getId()))&&customer.getSource()!=null&&customer.getSource()==2){
				customer.setOrderTimes(queryCustomer.getOrderTimes()==null?0:queryCustomer.getOrderTimes()+1);
			}
			//插入店铺关联
			if(queryCustomer.getId()!=null&&customer.getRegisterDepartment()!=null){
				customerDepartmentRelevanceService.insertDepartmentRelevanceIfNotExist(queryCustomer.getId() ,customer.getRegisterDepartment(),customer.getSource(),new Date());
			}
			customer.setRegisterDepartment(null);//除了第一次插入，修改一律不修改注册店铺
			customer.setSource(null);//除了第一次插入，修改一律不修改source
			customer.setId(queryCustomer.getId());
			if((queryCustomer.getResponsiblePerson()==null||queryCustomer.getResponsiblePerson().isEmpty())&&openOrderUser!=null&&!openOrderUser.isEmpty()){//设置开单人
				customer.setResponsiblePerson(openOrderUser);
			}
			int result = customerService.updateByPrimaryKeySelective(customer);
			Map<String, Object> map = new HashMap<>();
			map.put("id", queryCustomer.getId());
			map.put("openid", queryCustomer.getOpenid());
			return new JsonResult(true, "", map);			
		}else{
			if(customer.getSource()!=null&&customer.getSource()==1){
				customer.setScheduledTimes(1);
			}
			if(customer.getSource()!=null&&customer.getSource()==2){
				customer.setOrderTimes(1);
			}
			if(customer==null||customer.getStoreRecordRealName()==null||customer.getStoreRecordRealName().equals("")){
				customer.setStoreRecordRealName("无");
			}
			if(openOrderUser!=null&&!openOrderUser.isEmpty()){//设置开单人
				customer.setResponsiblePerson(openOrderUser);
			}
			int result = customerService.insertSelective(customer);
			//插入店铺关联
			if(result!=0&&customer.getRegisterDepartment()!=null){
				customerDepartmentRelevanceService.insertDepartmentRelevanceIfNotExist(result ,customer.getRegisterDepartment(),customer.getSource(),new Date());
			}
			Map<String, Object> map = new HashMap<>();
			map.put("id", result);
			map.put("openid", null);
			return new JsonResult(true, "", map);
		}
	}
	//增加订单次数
	@ApiIgnore
	@ResponseBody
	@RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value="/addOrderTimes")
	public JsonResult addOrderTimes(Integer customerId,String phone,String openOrderUser) {
		Customer queryCustomer = null;
		if(customerId!=null){
			queryCustomer = customerService.selectByPrimaryKey(customerId);
		}else if(phone!=null){
			queryCustomer = customerService.selectByPhone(phone);
		}
		if(queryCustomer==null){
			return new JsonResult(false,"无该用户",null);
		}else{
			if((queryCustomer.getResponsiblePerson()==null||queryCustomer.getResponsiblePerson().isEmpty())&&openOrderUser!=null&&!openOrderUser.isEmpty()){//设置开单人
				queryCustomer.setResponsiblePerson(openOrderUser);
			}
			queryCustomer.setOrderTimes(queryCustomer.getOrderTimes()+1);
		}
		int row = customerService.updateByPrimaryKeySelective(queryCustomer);
		return new JsonResult(true,JsonResult.APP_DEFINE_SUC,row);
	}
	//
	@ApiIgnore
	@ResponseBody
	@RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value="/minusOrderTimes")
	public JsonResult minusOrderTimes(Integer customerId,String phone) {
		Customer queryCustomer = null;
		if(customerId!=null){
			queryCustomer = customerService.selectByPrimaryKey(customerId);
		}else if(phone!=null){
			queryCustomer = customerService.selectByPhone(phone);
		}
		int row  = 0;
		if(queryCustomer==null){
			return new JsonResult(false,"无该用户",null);
		}else if(queryCustomer.getOrderTimes()!=null&&queryCustomer.getOrderTimes()>0){
			queryCustomer.setOrderTimes(queryCustomer.getOrderTimes()-1);
			row= customerService.updateByPrimaryKeySelective(queryCustomer);
		}
		return new JsonResult(true,JsonResult.APP_DEFINE_SUC,row);
	}
	@ApiIgnore
	@ResponseBody
	@RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value="/minusScheduledTimes")
	public JsonResult minusScheduledTimes(Integer customerId,String phone) {
		Customer queryCustomer = null;
		if(customerId!=null){
			queryCustomer = customerService.selectByPrimaryKey(customerId);
		}else if(phone!=null){
			queryCustomer = customerService.selectByPhone(phone);
		}
		int row  = 0;
		if(queryCustomer==null){
			return new JsonResult(false,"无该用户",null);
		}else if(queryCustomer.getScheduledTimes()!=null&&queryCustomer.getScheduledTimes()>0){
			queryCustomer.setScheduledTimes(queryCustomer.getScheduledTimes()-1);
			row= customerService.updateByPrimaryKeySelective(queryCustomer);
		}
		return new JsonResult(true,JsonResult.APP_DEFINE_SUC,row);
	}
		

	//电话绑定处用到,现在想规范
	@ApiIgnore
	@ResponseBody
	@RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value="/mergeRemoveCustomer/{id}")
	public JsonResult mergeRemoveCustomer(@PathVariable(value = "id") String id) {
		//入参检查
		logger.info("合并删除用户id:"+id);
		if(!isInteger(id)){
			return new JsonResult(false, "id格式不正确", null);
		}
		Map<String, Object> map = new HashMap<>();
		map.put("row", customerService.deleteByPrimaryKey(Integer.parseInt(id)));
		return new JsonResult(true, "", map);
	}
	@ApiIgnore
	@ResponseBody
	@RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value="/mergeDeleteCustomerByPhone/{phone}")
	public JsonResult mergeDeleteCustomerByPhone(@PathVariable(value = "phone") String phone) {
		//入参检查
		logger.info("合并删除用户phone:"+phone);
		Map<String, Object> map = new HashMap<>();
		map.put("row", customerService.deleteByPhone(phone));
		return new JsonResult(true, "", map);
	}
	
//	@ResponseBody
//	@RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value="/deleteCustomerByPrimaryKey/{id}")
//	public JsonResult deleteCustomerByPrimaryKey(@PathVariable(value = "id") String id) {
//		//入参检查
//		logger.info("获取用户信息id:"+id);
//		Customer customer = new Customer();
//		customer.setId(Integer.parseInt(id));
//		customer.setState("2");
//		Map<String, Object> map = new HashMap<>();
//		map.put("row", customerService.updateByPrimaryKeySelective(customer));
//		return new JsonResult(true, "", map);
//	}
	

	@ApiIgnore
	@ResponseBody
	@RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value="/updateCustomerSelective")
	public JsonResult updateCustomerSelective(Customer customer) {
		logger.info("修改用户信息："+customer.toString());
		if(customer==null||customer.getId()==null||customer.getId().equals("0")){
			return new JsonResult(false,"id不能为空",null);
		}
		if(customer!=null&&customer.getPhone()!=null){
			Customer c = customerService.selectByPhone(customer.getPhone());
			if(c!=null&&c.getOpenid()!=null&&!c.getOpenid().isEmpty()&&!c.getId().equals(customer.getId())){
				logger.info("id是否相等:"+(c.getId().equals(customer.getId())));
				return new  JsonResult(false,JsonResult.APP_PHONE_BE_BIND_ERR,null);
			}
		}
		int result = customerService.updateByPrimaryKeySelective(customer);
		Map<String, Object> map = new HashMap<>();
		map.put("row", result);
		return new JsonResult(true, "修改成功!", map);
	}
	/*updateCustomer该方法已经被弃用,随时可能删除*/
	@ApiIgnore
	@ResponseBody
	@RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value="/updateCustomer")
	public JsonResult updateCustomer(Customer customer) {
		logger.info("修改用户信息："+customer.toString());
		if(customer==null||customer.getId()==null||customer.getId().equals("0")){
			return new JsonResult(true,"id不能为空",null);
		}
		if(customer!=null&&customer.getPhone()!=null){
			Customer c = customerService.selectByPhone(customer.getPhone());
			if(c!=null&&c.getOpenid()!=null&&!c.getOpenid().isEmpty()&&!c.getId().equals(customer.getId())){
				logger.info("id是否相等:"+(c.getId().equals(customer.getId())));
				return new  JsonResult(false,JsonResult.APP_PHONE_BE_BIND_ERR,null);
			}
		}
		//入参检查
		int result = customerService.updateByPrimaryKey(customer);
		Map<String, Object> map = new HashMap<>();
		map.put("row", result);
		return new JsonResult(true, "修改成功!", map);
	}

	@ApiIgnore
	@ResponseBody
	@RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value="/getCustomerByPrimaryKey/{id}")
	public JsonResult getCustomerByPrimaryKey(@PathVariable(value = "id") String id) {
		//入参检查
		logger.info("获取用户信息id:"+id);
		Map<String, Object> map = new HashMap<>();
		Customer customer = customerService.selectByPrimaryKey(Integer.parseInt(id));
		map.put("user", customer);
		return new JsonResult(true, "", map);
	}
	@ApiIgnore
	@ResponseBody
	@RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value="/getCustomerByPrimaryKey2")
	public JsonResult getCustomerByPrimaryKey2(String id) {
		//入参检查
		logger.info("获取用户信息id:"+id);
		Map<String, Object> map = new HashMap<>();
		Customer customer = customerService.selectByPrimaryKey(Integer.parseInt(id));
		map.put("user", customer);
		return new JsonResult(true, "", map);
	}
	//TODO 入参的方式实现获得用户
	
	@ApiIgnore
	@ResponseBody
	@RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value="/getCustomerByOpenid/{openid}")
	public JsonResult getCustomerByOpenid(@PathVariable(value = "openid") String openid) {
		//入参检查
		logger.info("获取用户信息openid:"+openid);
		Map<String, Object> map = new HashMap<>();
		Customer customer = customerService.selectByOpenid(openid);
		map.put("user",customer );
		return new JsonResult(true, "", map);
	}
	//TODO 入参的方式实现获得用户
	@ApiIgnore
	@ResponseBody
	@RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value="/getCustomerByPhone/{phone}")
	public JsonResult getCustomerByPhone(@PathVariable(value = "phone") String phone) {
		//入参检查
		logger.info("获取用户信息phone:"+phone);
		Map<String, Object> map = new HashMap<>();
		Customer customer = customerService.selectByPhone(phone);
		map.put("user", customer);
		return new JsonResult(true, "", map);
	}
	//TODO 入参的方式实现获得用户
	
	@ApiIgnore
	@ResponseBody
	@RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value="/getCustomer")
	public JsonResult getCustomer(Customer customer,HttpServletRequest request,HttpServletResponse response) {
        String origin = request.getHeader("Origin");
        response.setHeader("Access-Control-Allow-Origin", origin);
        response.setHeader("Access-Control-Allow-Credentials", "true");
		//入参检查
        if(customer!=null&&customer.getRealName()!=null&&customer.getRealName().equals("")){
        	customer.setRealName(null);
        }
        if(customer!=null&&customer.getPhone()!=null&&customer.getPhone().equals("")){
        	customer.setPhone(null);
        }
		logger.info("获取用户信息，查询条件:"+customer.toString());
		Map<String, Object> map = new HashMap<>();
		List<Customer> list = customerService.selectByCustomer(customer);
		map.put("users", list);
		return new JsonResult(true, "", map);
	}
	//查询会员信息，带店铺
	@ApiIgnore
	@ResponseBody
	@RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value="/selectByCustomerWithStore")
	public JsonResult selectByCustomerWithStore(Integer pageNum,Integer pageSize,Customer customer,Integer departmentId,HttpServletRequest request,HttpServletResponse response) {
        String origin = request.getHeader("Origin");
        response.setHeader("Access-Control-Allow-Origin", origin);
        response.setHeader("Access-Control-Allow-Credentials", "true");
		//入参检查
        if(customer!=null&&customer.getRealName()!=null&&customer.getRealName().equals("")){
        	customer.setRealName(null);
        }
        if(customer!=null&&customer.getPhone()!=null&&customer.getPhone().equals("")){
        	customer.setPhone(null);
        }
        PageHelper.startPage(pageNum,pageSize);
		logger.info("获取用户信息，查询条件:"+customer.toString());
//		Map<String, Object> map = new HashMap<>();
		List<Customer> list = customerService.selectByCustomerWithStore(customer,departmentId);
		PageInfo<Customer> pageInfo = new PageInfo<Customer>(list);
//		map.put("users", pageInfo);
		return new JsonResult(true, "", pageInfo);
	}
	//查询会员信息带店铺且带会员卡
	@ApiIgnore
	@ResponseBody
	@RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value="/selectByCustomerWithStoreAndCard")
	public JsonResult selectByCustomerWithStoreAndCard(String pageNum,String pageSize,Customer customer,Integer departmentId,HttpServletRequest request,HttpServletResponse response) {
        String origin = request.getHeader("Origin");
        response.setHeader("Access-Control-Allow-Origin", origin);
        response.setHeader("Access-Control-Allow-Credentials", "true");
		//入参检查
        if(customer!=null&&customer.getRealName()!=null&&customer.getRealName().equals("")){
        	customer.setRealName(null);
        }
        if(customer!=null&&customer.getPhone()!=null&&customer.getPhone().equals("")){
        	customer.setPhone(null);
        }
        if(pageNum!=null&&pageNum.contains("=")){
        	pageNum = pageNum.replace("=", "");
        }
        if(pageSize!=null&&pageSize.contains("=")){
        	pageSize = pageSize.replace("=", "");
        }
        PageHelper.startPage(Integer.parseInt(pageNum),Integer.parseInt(pageSize));
		logger.info("获取用户信息，查询条件:"+customer.toString());
//		Map<String, Object> map = new HashMap<>();
		List<Customer> list = customerService.selectByCustomerWithStoreAndCard(customer,departmentId);
		PageInfo<Customer> pageInfo = new PageInfo<Customer>(list);
//		map.put("users", pageInfo);
		return new JsonResult(true, "", pageInfo);
	}
	//查询会员信息带店铺且带会员卡
	@ApiIgnore
	@ResponseBody
	@RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value="/selectByCustomerCard")
	public JsonResult selectByCustomerCard(String pageNum,String pageSize,Customer customer,Integer departmentId,HttpServletRequest request,HttpServletResponse response) {
        String origin = request.getHeader("Origin");
        response.setHeader("Access-Control-Allow-Origin", origin);
        response.setHeader("Access-Control-Allow-Credentials", "true");
		//入参检查
        if(customer!=null&&customer.getRealName()!=null&&customer.getRealName().equals("")){
        	customer.setRealName(null);
        }
        if(customer!=null&&customer.getPhone()!=null&&customer.getPhone().equals("")){
        	customer.setPhone(null);
        }
        PageHelper.startPage(Integer.parseInt(pageNum),Integer.parseInt(pageSize));
		logger.info("获取用户信息，查询条件:"+customer.toString());
//		Map<String, Object> map = new HashMap<>();
		List<Customer> list = customerService.selectByCustomerCard(customer,departmentId);
		
		//查询美甲师名字,设置到id
		
		
		int listSize = list.size();
		for(int i=0;i<listSize;i++){
			Customer c = list.get(i);
			
		}
		PageInfo<Customer> pageInfo = new PageInfo<Customer>(list);
//		map.put("users", pageInfo);
		return new JsonResult(true, "", pageInfo);
	}
	//查询会员信息带店铺且带会员卡
	@ApiIgnore
	@ResponseBody
	@RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value="/selectByCustomerWithStoreAndCardAndDetail")
	public JsonResult selectByCustomerWithStoreAndCardAndDetail(String isNotRecord ,String pageNum,String pageSize,Customer customer,Integer departmentId,HttpServletRequest request,HttpServletResponse response) {
        String origin = request.getHeader("Origin");
        response.setHeader("Access-Control-Allow-Origin", origin);
        response.setHeader("Access-Control-Allow-Credentials", "true");
		//入参检查
        if(customer!=null&&customer.getRealName()!=null&&customer.getRealName().equals("")){
        	customer.setRealName(null);
        }
        if(customer!=null&&customer.getPhone()!=null&&customer.getPhone().equals("")){
        	customer.setPhone(null);
        }
        if(pageNum==null||pageSize==null){
        	new JsonResult(false, "页码为空,刷新后重试", null);
        }
        PageHelper.startPage(Integer.parseInt(pageNum),Integer.parseInt(pageSize));
		logger.info("获取用户信息，查询条件:"+customer.toString());
//		Map<String, Object> map = new HashMap<>();
		boolean isNotRecordB = false;
		if(isNotRecord!=null&&isNotRecord.equals("true")){
			isNotRecordB= true;
		}
		List<Customer> list = customerService.selectByCustomerWithStoreAndCardAndDetail(customer,departmentId,isNotRecordB);
		PageInfo<Customer> pageInfo = new PageInfo<Customer>(list);
//		map.put("users", pageInfo);
		return new JsonResult(true, "", pageInfo);
	}
	
	@ApiIgnore
	@ResponseBody
	@RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value="/getCustomerWithPhone")
	public JsonResult getCustomerWithPhone(Customer customer,HttpServletRequest request,HttpServletResponse response) {
        String origin = request.getHeader("Origin");
        response.setHeader("Access-Control-Allow-Origin", origin);
        response.setHeader("Access-Control-Allow-Credentials", "true");
		//入参检查
		Map<String, Object> map = new HashMap<>();
		String phone = null;
		if(customer!=null){
			logger.info("获取用户信息，查询条件:"+customer.toString());
			phone = customer.getPhone();
		}
		List<Customer> list = customerService.selectCustomerWithPhone(phone);
		map.put("users", list);
		return new JsonResult(true, "", map);
	}
	public boolean isInteger(String str) {
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");  
		return pattern.matcher(str).matches();  
	}
	@ApiIgnore
	@ResponseBody
	@RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value="/getCustomerDetailById")
	public JsonResult getCustomerDetailById(Integer customerId){
		if(customerId==null){
			return new JsonResult(false,String.format(JsonResult.PARAM_NULL,"customerId"),null);
		}
		return new JsonResult(true,JsonResult.APP_DEFINE_SUC,customerDetailService.selectByPrimaryKey(customerId));
	}
//	@ResponseBody
//	@RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value="/updateCustomerDetail")
//	public JsonResult updateByPrimaryKeySelective(CustomerDetail record){
//		if(record==null||record.getCustomerId()==null){
//			return new JsonResult(false,String.format(JsonResult.PARAM_NULL,"customerId"),null);
//		}
//		return new JsonResult(true,JsonResult.APP_DEFINE_SUC,customerDetailService.updateByPrimaryKeySelective(record));
//	}
	@ApiIgnore
	@ResponseBody
	@RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value="/updateRemark")
	public JsonResult updateRemark(Integer customerId,String remark,String remark2,String remark3,String remark4){
		if(customerId==null){
			return new JsonResult(false,String.format(JsonResult.PARAM_NULL,"customerId"),null);
		}
		Customer customer = new Customer();
		customer.setId(customerId);
		customer.setRemark(remark);
		customer.setRemark2(remark2);
		customer.setRemark3(remark3);
		customer.setRemark4(remark4);
		return new JsonResult(true,JsonResult.APP_DEFINE_SUC,customerService.updateRemark(customer));
	}
	@ResponseBody
	@RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value="/setAllUnionid")
	public JsonResult setAllUnionid(String openid) {//oNzJP1YN_Tf6SP8tBWkIgztCpl6s
		Customer customer = new Customer();
		if(openid!=null){
			customer.setOpenid(openid);
		}
        List<Customer> list = customerService.selectByCustomer(customer);
        int row = 0;
        for(int i=0;i<list.size();i++){
        	logger.info("第"+i+"条数据");
        	if(list.get(i)==null||list.get(i).getOpenid()==null){
        		continue;
        	}
        	openid = list.get(i).getOpenid();
    		JsonResult userInfo = wechatService.getUserInfoByAccessToken2(openid,0);
    		logger.info("userInfo:"+userInfo);
    		if(userInfo!=null&&userInfo.getData()!=null){
    			JSONObject item = (JSONObject)userInfo.getData();
    			if(item.get("unionid")!=null){
    				Customer c = new Customer();
    				c.setId(list.get(i).getId());
    				c.setUnionid(item.get("unionid").toString());
    				customerService.updateByPrimaryKeySelective(c);
    			}
    		}
        }
		return new JsonResult(true, "", row);
	}

	
	
	
}
