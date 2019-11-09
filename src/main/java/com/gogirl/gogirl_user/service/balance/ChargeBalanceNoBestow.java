package com.gogirl.gogirl_user.service.balance;

import java.util.Date;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.gogirl.gogirl_user.dao.CustomerBalanceMapper;
import com.gogirl.gogirl_user.entity.CustomerBalance;
import com.gogirl.gogirl_user.entity.CustomerBalanceRecord;
import com.gogirl.gogirl_user.entity.DiscountConfig;
@Component
public class ChargeBalanceNoBestow extends CalculateBalance {
	Logger logger = LoggerFactory.getLogger(getClass());
	@Resource
	CustomerBalanceMapper balanceDao;
	@Override
	public CustomerBalance calculateBalance(int charge_balance) {//计算余额
		balance.setBalance(balance.getBalance()+charge_balance);//余额传了多少就设置多少
		if(orderAmount!=null){
			charge_balance=orderAmount;
		}
		orderAmount = charge_balance;
		balance.setUpdateTime(new Date());
		balance.setTotalCharge(balance.getTotalCharge()+charge_balance);
		balance.setVersion(balance.getVersion()+1);
		balance.setRefereeId(referee);
		DiscountConfig discountConfig = discountConfigDao.selectByCharge(charge_balance);
		//冲送，且记录打折信息
		if(discountConfig!=null){
//			balance.setBalance(balance.getBalance()+discountConfig.getBestowAmount());//该充值,不送,充多少存多少
			balance.setCurrentDiscount(discountConfig.getDiscount());
			if(bestowAmount!=null){
				balance.setTotalBestow(balance.getTotalBestow()+bestowAmount);
			}else{
				bestowAmount=0;
			}
			balance.setLevel(discountConfig.getLevel());
		}else{
			logger.info("充值金额"+charge_balance+"找不到折扣配置，转为不送且不打折");
		}
		/*设置折扣率*/
		balance.setDiscountRate(countDiscountRate(balance.getCustomerId(),orderAmount,bestowAmount));
		
		return balance;
	}

	@Override
	public int saveRecord(int charge_balance,String orderId,Integer newOrderId) {//保存记录到记录表
		if(source==null){
			source = 1;
		}
		CustomerBalanceRecord customerBalanceRecord = new CustomerBalanceRecord(
				balance.getCustomerId(), source, type, new Date(),
				balance.getBalance(),orderAmount,orderId,3,
				balance.getCurrentDiscount(),bestowAmount,remark);
		customerBalanceRecord.setRefereeId(referee);
		customerBalanceRecord.setDepartmentId(departmentId);
		balanceRecordDao.insertSelective(customerBalanceRecord);
		return customerBalanceRecord.getId();
	}



}
