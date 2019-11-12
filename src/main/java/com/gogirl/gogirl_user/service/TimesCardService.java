package com.gogirl.gogirl_user.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.gogirl.gogirl_order.order_commons.dto.OrderServe;
import com.gogirl.gogirl_user.dao.TimesCardCustomerRelevanceMapper;
import com.gogirl.gogirl_user.dao.TimesCardServeRelevanceMapper;
import com.gogirl.gogirl_user.dao.TimesCardServeTypeRelevanceMapper;
import com.gogirl.gogirl_user.dao.TimesCardTypeContentMapper;
import com.gogirl.gogirl_user.dao.TimesCardTypeMapper;
import com.gogirl.gogirl_user.dao.TimesCardUsedRecordMapper;
import com.gogirl.gogirl_user.entity.Customer;
import com.gogirl.gogirl_user.entity.TimesCardCustomerRelevance;
import com.gogirl.gogirl_user.entity.TimesCardType;
import com.gogirl.gogirl_user.entity.TimesCardUsedRecord;

@Service
public class TimesCardService {
	@Resource
	TimesCardTypeMapper timesCardTypeMapper;
	@Resource
	TimesCardCustomerRelevanceMapper timesCardCustomerRelevanceMapper;
	@Resource
	TimesCardTypeContentMapper timesCardTypeContentMapper;
	@Resource
	TimesCardServeRelevanceMapper timesCardServeRelevanceMapper;
	@Resource
	TimesCardServeTypeRelevanceMapper timesCardServeTypeRelevanceMapper;
	@Resource
	TimesCardUsedRecordMapper timesCardUsedRecordMapper;
	
	public List<TimesCardType> getTimesCardTypeList() {
		return timesCardTypeMapper.getTimesCardTypeList();
	}
	public TimesCardType getTimesCardTypeDetail(Integer id) {
		return timesCardTypeMapper.selectByPrimaryKey(id);
	}
	public int insertTimesCardRelevance(Customer customer,TimesCardType timesCardType) {
		//插入次卡
		TimesCardCustomerRelevance timesCardCustomerRelevance = new TimesCardCustomerRelevance();
		timesCardCustomerRelevance.setCardTypeId(timesCardType.getId());
		timesCardCustomerRelevance.setCreateTime(new Date());
		timesCardCustomerRelevance.setCustomerId(customer.getId());
//		timesCardCustomerRelevance.setRefereeId();//推荐人暂时不设置
		timesCardCustomerRelevance.setUsedTimes(0);
		timesCardCustomerRelevance.setStatus(1);
		timesCardCustomerRelevance.setValidStartTime(new Date());
		long validEndTime = new Date().getTime()+(long)86400000*timesCardType.getValidDays();
		timesCardCustomerRelevance.setValidEndTime(new Date(validEndTime));
		return timesCardCustomerRelevanceMapper.insertSelective(timesCardCustomerRelevance);
	}
	public List<TimesCardCustomerRelevance> getTimesCardList(TimesCardCustomerRelevance timesCardCustomerRelevance) {
		return timesCardCustomerRelevanceMapper.getTimesCardList(timesCardCustomerRelevance);
	}
	public TimesCardCustomerRelevance getTimesCardDetail(Integer id) {
		return timesCardCustomerRelevanceMapper.selectByPrimaryKey(id);
	}
	public List<TimesCardCustomerRelevance> getMyTimesCardByOrderId(Integer orderId) {
		return timesCardCustomerRelevanceMapper.getMyTimesCardByOrderId(orderId);
	}
	public int updateTimesCardCustomerRelevance(TimesCardCustomerRelevance record) {
		return timesCardCustomerRelevanceMapper.updateByPrimaryKeySelective(record);
	}
	public int insertTimesCardUsedRecord(TimesCardUsedRecord timesCardUsedRecord) {
		return timesCardUsedRecordMapper.insertSelective(timesCardUsedRecord);
	}
	public int updateTimesCardUsedRecord(TimesCardUsedRecord timesCardUsedRecord) {
		return timesCardUsedRecordMapper.updateByPrimaryKeySelective(timesCardUsedRecord);
	}
	public int cancelTimesCardByOrderId(Integer orderId) {
		return timesCardUsedRecordMapper.cancelTimesCardByOrderId(orderId);
	}
	public List<TimesCardType> listOrderCanUseCardByServeType(Integer orderId) {
		return timesCardTypeMapper.listOrderCanUseCardByServeType(orderId);
	}
	public List<TimesCardType> listOrderCanUseCardByServe(Integer orderId) {
		return timesCardTypeMapper.listOrderCanUseCardByServe(orderId);
	}
	
}
