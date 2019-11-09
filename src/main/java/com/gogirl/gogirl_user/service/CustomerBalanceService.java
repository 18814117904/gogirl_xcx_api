package com.gogirl.gogirl_user.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.gogirl.gogirl_order.order_commons.dto.OrderManage;
import com.gogirl.gogirl_order.order_commons.utils.JsonResult;
import com.gogirl.gogirl_technician.technician_commons.dto.TechnicianManage;
import com.gogirl.gogirl_technician.technician_commons.dto.UserTechnician;
import com.gogirl.gogirl_technician.technician_user.service.TechnicianManageService;
import com.gogirl.gogirl_technician.technician_user.service.UserTechnicianService;
import com.gogirl.gogirl_user.constant.GogirlProperties;
import com.gogirl.gogirl_user.controller.DiscountConfigController;
import com.gogirl.gogirl_user.dao.CustomerBalanceMapper;
import com.gogirl.gogirl_user.dao.CustomerBalanceRecordMapper;
import com.gogirl.gogirl_user.dao.CustomerIntegralMapper;
import com.gogirl.gogirl_user.dao.CustomerMapper;
import com.gogirl.gogirl_user.dao.DiscountConfigMapper;
import com.gogirl.gogirl_user.entity.CouponCustomerRelevance;
import com.gogirl.gogirl_user.entity.Customer;
import com.gogirl.gogirl_user.entity.CustomerBalance;
import com.gogirl.gogirl_user.entity.CustomerBalanceRecord;
import com.gogirl.gogirl_user.entity.DiscountConfig;
import com.gogirl.gogirl_user.service.balance.CalculateBalance;
import com.gogirl.gogirl_user.service.balance.CalculateBalanceXcx;
import com.gogirl.gogirl_user.service.balance.ChargeBalance;
import com.gogirl.gogirl_user.util.BalanceLock;
import com.gogirl.gogirl_user.util.BalanceOrderLock;
@Service
public class CustomerBalanceService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	CustomerBalanceMapper balanceDao;
	@Autowired
	CustomerBalanceRecordMapper balanceRecordDao;
	@Autowired
	DiscountConfigController discountConfigController;
	@Autowired
	CouponService couponService;
	@Autowired
	CustomerMapper customerMapper;
	@Resource
	UserTechnicianService userTechnicianService;
	
	BalanceOrderLock balanceOrderLock = BalanceOrderLock.getInsatance();
	BalanceLock balanceLock = BalanceLock.getInsatance();
	
	//小程序扣款固定逻辑余额变动
	@Transactional
	public JsonResult balanceChangeXcx(OrderManage order,Integer customer_id,Integer charge_balance,CalculateBalanceXcx calculateBalance,Integer source,Integer type) throws Exception {
		balanceOrderLock.lock(orderIdGetInt(order.getId().toString()));//为订单id上锁，防多次提交
		balanceLock.lock(customer_id);//为用户id上锁，防并发修改同一用户
		try {
			//判断订单是否可以支付
			JsonResult jr = checkCanPay(order.getStatus());
			if(jr!=null){
				balanceLock.unlock(customer_id);//为用户id解锁
				balanceOrderLock.unlock(orderIdGetInt(order.getId().toString()));//为订单id解锁
				return jr;
				}
			//查询会员卡
			CustomerBalance balance = balanceDao.selectByCustomerId(customer_id);//若数据库不存在该对象，则生成
			//建卡
			balance = calculateBalance.createCard(balance,customer_id,source);
			//扣卡
			JsonResult<CustomerBalance> balanceResult = calculateBalance.calculateBalance(balance,charge_balance,order.getId());//根据类型计算余额
			balanceLock.unlock(customer_id);//为用户id解锁
			balanceOrderLock.unlock(orderIdGetInt(order.getId().toString()));//为订单id解锁
			return balanceResult;
		} catch (Exception e) {
			logger.info(e==null?"e==null":e.getMessage());
			balanceLock.unlock(customer_id);//为用户id解锁
			balanceOrderLock.unlock(orderIdGetInt(order.getId().toString()));//为订单id解锁
			throw e;
		}
	}
	
	//余额变动
	@Transactional
	public int balanceChange(HttpServletRequest request,Integer customer_id,Integer charge_balance,CalculateBalance calculateBalance,Integer type,String orderId,Integer couponRelevanceId,Integer newOrderId) throws Exception {
		balanceOrderLock.lock(orderIdGetInt(orderId));//为订单id上锁，防多次提交
		balanceLock.lock(customer_id);//为用户id上锁，防并发修改同一用户
		int row = 0;
		try {
			CustomerBalanceRecord record = balanceRecordDao.selectByOrderId(orderId);
			if(record!=null){
				if(record.getOrderState()==2){
					throw new Exception("订单处理中");
				}else if(record.getOrderState()==3||record.getOrderState()==4){
					throw new Exception("订单已处理，请勿重复提交");
				}else if(record.getOrderState()==1||record.getOrderState()==4){
				}else{
					throw new Exception("订单状态异常");
				}
			}
			CustomerBalance balance = balanceDao.selectByCustomerId(customer_id);//若数据库不存在该对象，则生成

			if(balance==null&&calculateBalance.getType()!=null&&calculateBalance.getType()==-1&&calculateBalance.getSource()!=null&&calculateBalance.getSource()!=2){
				logger.info("没有会员卡,消费类型不为会员卡,直接返回成功");
				balanceLock.unlock(customer_id);//为用户id解锁
				balanceOrderLock.unlock(orderIdGetInt(orderId));//为订单id解锁
				return 1;
			}
			if(balance==null){
				balance = new CustomerBalance(customer_id, 0, new Date(), new Date(), 0, 0, 0,0.0,0,"普通会员");
			}
			calculateBalance.setBalance(balance);//设置当前余额
			
			CustomerBalance finalBalance = calculateBalance.calculateBalance(charge_balance);//根据类型计算余额
			int recordId = calculateBalance.saveRecord(calculateBalance.getOrderAmount(),orderId,newOrderId);//充值\消费记录保存到数据库
			calculateBalance.setRecordId(recordId);
			if(balance.getVersion()==1&&balance.getBalance()>0){//插入该用户余额
				row = balanceDao.insertSelective(finalBalance);
			}else if(balance.getVersion()==1&&balance.getBalance()==0){
				row = 1;
			}else{//更新该用户余额
				row = balanceDao.updateByPrimaryKeySelective(finalBalance);
			}
		} catch (Exception e) {
			logger.info(e==null?"e==null":e.getMessage());
			balanceLock.unlock(customer_id);//为用户id解锁
			balanceOrderLock.unlock(orderIdGetInt(orderId));//为订单id解锁
			throw e;
		}
		balanceLock.unlock(customer_id);//为用户id解锁
		balanceOrderLock.unlock(orderIdGetInt(orderId));//为订单id解锁
		return row;
	}
//	撤回收款加钱
	public JsonResult withdrawBalance(String orderId) {
		balanceOrderLock.lock(orderIdGetInt(orderId));//为订单id上锁，防多次提交
		//找到消费记录，拿到金额，删除消费记录
		CustomerBalanceRecord customerBalanceRecord = new CustomerBalanceRecord();
		customerBalanceRecord.setOrderId(orderId);
		List<CustomerBalanceRecord> list = balanceRecordDao.getBalanceRecord(customerBalanceRecord);
		int row = 0;
		if(list==null||list.size()<1){
			balanceOrderLock.unlock(orderIdGetInt(orderId));//为订单id解锁
			return new JsonResult(true,"撤回收款成功，无该订单扣款记录，不需要改余额表",null);
		}
		CustomerBalanceRecord customerRecordForId = list.get(0);
		if(customerRecordForId==null){
			balanceOrderLock.unlock(orderIdGetInt(orderId));//为订单id解锁
			return new JsonResult(true,"撤回收款成功，无该订单扣款记录，不需要改余额表",null);
		}
		balanceLock.lock(customerRecordForId.getCustomerId());//为用户id上锁，防并发修改同一用户
		//查余额
		CustomerBalance cb = getCustomerBalance2(customerRecordForId.getCustomerId());
		int sumWithdrawAmount = 0;
		for(int i=0;i<list.size();i++){
			CustomerBalanceRecord item = list.get(i);
			if(item==null){
				continue;
			}
			//加上扣款金额到余额
			sumWithdrawAmount=+item.getOrderAmount();
			
			//删除消费记录
			balanceRecordDao.deleteByPrimaryKey(item.getId());
			row++;
		}
		cb.setBalance(cb.getBalance()+sumWithdrawAmount);
		balanceDao.updateByPrimaryKey(cb);

		balanceLock.unlock(customerRecordForId.getCustomerId());//为用户id解锁
		balanceOrderLock.unlock(orderIdGetInt(orderId));//为订单id解锁
		return new JsonResult(true,"撤回收款成功，无该订单扣款记录，不需要改余额表",row);
	}
//	查询用户余额
	public CustomerBalance getCustomerBalance(int customer_id) {
		CustomerBalance balance = balanceDao.selectByCustomerId(customer_id);
		if(balance==null){
			balance=new CustomerBalance(customer_id, 0, null, null, 0, 0, 0,new Double(1),0,"普通会员");
		}
		return balance;
	}
//	查询用户余额
	public CustomerBalance getCustomerBalance2(int customer_id) {
		CustomerBalance balance = balanceDao.selectByCustomerId(customer_id);
		return balance;
	}
	//字符串中所有字符相加得到一个int
	public int orderIdGetInt(String orderId) {
		StringBuffer sb = new StringBuffer(orderId);
		int sum = 0;
		int length = sb.length();
		for(int i=0;i<length;i++){
			sum+=sb.charAt(i);
		}
		return sum;
	}

	public List<CustomerBalanceRecord> getBalanceRecord(CustomerBalanceRecord customerBalanceRecord) {
		return balanceRecordDao.getBalanceRecord(customerBalanceRecord);
	}
	public List<CustomerBalanceRecord> getBalanceRecordCard(CustomerBalanceRecord customerBalanceRecord) {
		return balanceRecordDao.getBalanceRecordCard(customerBalanceRecord);
	}
	
public int  mergeCustomer(int fromCustomerId,int toCustomerId) {
	CustomerBalance c = balanceDao.selectByCustomerId(toCustomerId);
	if(c!=null){
		logger.error("两目标customerId已经存在.");
		return 0;
	}else{
		CustomerBalance cc = new CustomerBalance();
		cc.setCustomerId(fromCustomerId);
		return balanceDao.mergeCustomer(fromCustomerId,toCustomerId);
	}
}
public int deleteRecordByPrimaryKey(int id) {
	return balanceRecordDao.deleteByPrimaryKey(id);
}


	public int insertSelective(Integer customerId,Integer departmentId,String ip,Integer orderAmount,Integer orderId,String remark,Integer source){
		CustomerBalance customerBalance = null;
		if(customerId!=null){
			customerBalance = balanceDao.selectByCustomerId(customerId);
		}
		CustomerBalanceRecord record = new CustomerBalanceRecord();
		record.setBestowAmount(0);
		record.setCurrentBalance(customerBalance==null?0:customerBalance.getBalance());
		record.setCustomerId(customerId);
		record.setDepartmentId(departmentId);
		record.setDiscount(1.0);
		record.setIp(ip);
		record.setOrderAmount(orderAmount);
		record.setOrderId(String.valueOf(orderId));
		record.setNewOrderId(orderId);
		record.setOrderState(3);
		record.setRemark(remark);
		record.setSource(source);
		record.setTime(new Date());
		record.setType(-1);
		return balanceRecordDao.insertSelective(record);
	}
    @Scheduled(cron="0 50 23 * * *")//每天定时设置推荐人:当天服务的技师为推荐人
	public void setChargeReferees() {
		logger.info("每天晚上11:50定时设置推荐人:当天服务的技师为推荐人");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		setChargeReferees(sdf.format(new Date()));
	}
	public void setChargeReferees(String day) {
		logger.info("设置推荐人:当天服务的技师为推荐人");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Map<String, String> mapTechnicianManage = new HashMap<>();
		List<UserTechnician> listTechnicianManage = userTechnicianService.listTechnicianForPage(new UserTechnician());
		for(int i=0;i<listTechnicianManage.size();i++){//查出所有的技师,找到技师id和user_id
			UserTechnician item = listTechnicianManage.get(i);
			mapTechnicianManage.put(String.valueOf(item.getTechnicianId()), String.valueOf(item.getUserId()));
		}
		List<CustomerBalanceRecord> list = balanceRecordDao.getXcxChargeRecord(day==null?sdf.format(new Date()):day);//找到当天的订单的推荐人
		for(int i=0;i<list.size();i++) {//遍历设置推荐人
			Map<String, String> map = new HashMap();//推荐人列表
			String referee_ids = "";
			CustomerBalanceRecord item = list.get(i);
			CustomerBalanceRecord cbrq = balanceRecordDao.selectByPrimaryKey(item.getId());
//			if(cbrq!=null&&cbrq.getRefereeId()!=null&&!cbrq.getRefereeId().isEmpty()){
//				item.setRefereeId(cbrq.getRefereeId());
//			}else 
			if(item.getRefereeId()!=null) {
			//每次都修改推荐人,且没晚都会设置推荐人
				String[] arr = item.getRefereeId().split(",");
				for(int j=0;j<arr.length;j++) {
					if(!map.containsKey(arr[j])) {
						map.put(arr[j], arr[j]);
						referee_ids=referee_ids+","+mapTechnicianManage.get(arr[j]);
					}
				}
				if(referee_ids.startsWith(",")) {
					referee_ids = referee_ids.substring(1);
				}
				item.setRefereeId(referee_ids);
			}
			if(cbrq!=null&&cbrq.getDepartmentId()!=null){
				item.setDepartmentId(cbrq.getDepartmentId());
			}
			if(item.getId()!=null&&(item.getRefereeId()!=null||item.getDepartmentId()!=null)){
				balanceRecordDao.updateByPrimaryKeySelective(item);
			}
		}
	}
	public List<CustomerBalanceRecord> listPartPay(Integer orderId) {
		return balanceRecordDao.listPartPay(orderId);
	}
	
	/**
	 * 支付前检查订单状态status
	 */
	public JsonResult<?> checkCanPay(Integer status) {
		//1:正在服务 2:待付款#  3:未评价# 4:已完成#5: 已删除#6:已预约待接单 7用户取消8待美甲师确认9系统取消订单
		logger.info("支付前检查订单状态status:"+status);
		if(status.equals(1)){
			return new JsonResult<>(false,"服务中的订单暂时不能支付,请联系美甲师结束服务.");
		}else if(status.equals(2)||status.equals(8)){
			return null;
		}else if(status.equals(3)||status.equals(4)){
			return new JsonResult<>(false,"订单已支付,无需重复支付.");
		}else if(status.equals(5)){
			return new JsonResult<>(false,"订单已删除,不能再进行支付");
		}else if(status.equals(6)){
			return new JsonResult<>(false,"订单处于待接单,不能支付");
		}else if(status.equals(7)){
			return new JsonResult<>(false,"用户已取消订单,不能支付");
		}else if(status.equals(9)){
			return new JsonResult<>(false,"订单超时未接单,系统取消订单,不能支付");
		}else{
			return new JsonResult<>(false,"订单状态异常,请联系管理员status:"+status);
		}
	}
}
