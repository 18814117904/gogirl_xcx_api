package com.gogirl.gogirl_store.store_commons.config;

/**
 * 全局变量工具类
 */
public class Constans {

	// 定义登录后台用户在session中的键
	public final static String SESSION_SYSUSER = "SESSION_SYSUSER";

	// 定义登录客户端用户在session中的键
	public final static String SESSION_CLIENTUSER = "SESSION_CLIENTUSER";

	public final static Integer SCHEDULE_STATUS_SERVICE = 1; //正在服务
	public final static Integer SCHEDULE_STATUS_ABIDING = 2; //守约
	public final static Integer SCHEDULE_STATUS_LOSSOFCONTRACT = 3; //失约
	public final static Integer SCHEDULE_STATUS_CANCEL = 4; //已取消

	//订单
	public final static Integer ORDER_TYPE_SCHEDULE = 1;  //线上
	public final static Integer ORDER_TYPE_STORE = 2;  //线下
	public final static Integer ORDER_TYPE_OTHER = 3;  //其他
	public final static Integer ORDER_TYPE_RECHARGE = 4;  //充值

	public final static Integer ORDER_STATUS_SERVE = 1;  //正在服务
	public final static Integer ORDER_STATUS_PAMENT = 2;  //待付款
	public final static Integer ORDER_STATUS_EVALUATE = 3;  //未评价
	public final static Integer ORDER_STATUS_FINISH = 4;  //已完成

	public final static Integer CLASSES_REST = 0;  //休息

}
