package com.gogirl.gogirl_user.service.balance;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.gogirl.gogirl_order.order_commons.utils.JsonResult;
import com.gogirl.gogirl_user.constant.Constant;
import com.gogirl.gogirl_user.entity.CustomerBalance;
import com.gogirl.gogirl_user.entity.CustomerBalanceRecord;
import com.gogirl.gogirl_user.entity.DiscountConfig;

@Component
public class ConsumeBalanceXcx  extends CalculateBalanceXcx {
	Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public CustomerBalance createCard(CustomerBalance balance,Integer customer_id,Integer paytype){
		if(balance==null){
			balance = new CustomerBalance(customer_id, 0, new Date(), new Date(), 0, 0, 0,0.0,0,"普通会员");
			customerBalanceMapper.insertSelective(balance);
			return balance;
		}else{
			return balance;
		}
	}
	@Override
	public JsonResult<CustomerBalance> calculateBalance(CustomerBalance balance,int amount,Integer orderId) throws Exception{
		if(amount!=0&&balance.getBalance()==0){
			return new JsonResult<>(false,"余额为0元,请充值后在使用会员卡支付.");
		}else if(amount>balance.getBalance()){//余额不足,扣完余额,返回true,0003
			amount = balance.getBalance();
			balance.setBalance(balance.getBalance()-amount);
			balance.setUpdateTime(new Date());
			balance.setTotalExpenditure(balance.getTotalExpenditure()+amount);
			balance.setVersion(balance.getVersion()+1);
			balanceDao.updateByPrimaryKeySelective(balance);
			saveRecord(balance,amount, orderId.toString());
			return new JsonResult<>(true,JsonResult.PART_PAYMENT_CODE,null);
		}else{//余额充足,正常扣款
			balance.setBalance(balance.getBalance()-amount);
			balance.setUpdateTime(new Date());
			balance.setTotalExpenditure(balance.getTotalExpenditure()+amount);
			balance.setVersion(balance.getVersion()+1);
			balanceDao.updateByPrimaryKeySelective(balance);
			this.balance = balance;
			saveRecord(balance,amount, orderId.toString());
			return new JsonResult<>(true,JsonResult.APP_DEFINE_SUC,null);
		}
	}

	@Override
	public JsonResult<CustomerBalanceRecord> saveRecord(CustomerBalance balance,int charge_balance,String orderId) {
		CustomerBalanceRecord customerBalanceRecord = new CustomerBalanceRecord(
				balance.getCustomerId(), Constant.CONSUME_BALANCE, -1, new Date(), 
				balance.getBalance(),charge_balance,orderId,3,
				balance.getCurrentDiscount(),0,remark);
		customerBalanceRecord.setDepartmentId(departmentId);
		balanceRecordDao.insertSelective(customerBalanceRecord);
		recordId = customerBalanceRecord.getId();
		return new JsonResult<>(true,JsonResult.APP_DEFINE_SUC,customerBalanceRecord);
	}

}
