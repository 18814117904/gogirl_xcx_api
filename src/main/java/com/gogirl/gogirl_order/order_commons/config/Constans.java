package com.gogirl.gogirl_order.order_commons.config;

/**
 * 全局变量工具类
 */
public class Constans {

	// 定义登录后台用户在session中的键
	public final static String SESSION_SYSUSER = "SESSION_SYSUSER";

	// 定义登录客户端用户在session中的键
	public final static String SESSION_CLIENTUSER = "SESSION_CLIENTUSER";

	//预约状态
	public final static Integer SCHEDULE_STATUS_SERVICE = 1; //正在服务
	public final static Integer SCHEDULE_STATUS_ABIDING = 2; //守约
	public final static Integer SCHEDULE_STATUS_LOSSOFCONTRACT = 3; //失约
	public final static Integer SCHEDULE_STATUS_CANCEL = 4; //已取消
	public final static Integer SCHEDULE_STATUS_DELETE = 5; //已删除
	public final static Integer SCHEDULE_STATUS_SERVING = 6; //已接单，正在服务
	public final static Integer SCHEDULE_STATUS_WAITSERVE = 7; //自动开单，等待接单

	public final static Integer SCHEDULE_IS_READ = 1; //已读
	public final static Integer SCHEDULE_NO_READ = 2; //未读

	public final static Integer SCHEDULE_REMIND_NEW = 1; //用户端新增预约
	public final static Integer SCHEDULE_REMIND_TIME = 2; //预约时效提醒

	public final static Integer SCHEDULE_TYPE_USER = 1;  //用户端
	public final static Integer SCHEDULE_TYPE_PC = 2;  //店铺端

	public final static Integer SCHEDULE_RECORD_UPDATE = 0; //修改 预约记录
	public final static Integer SCHEDULE_RECORD_ADD = 1; //增加 预约记录
	public final static Integer SCHEDULE_RECORD_DELETE = 2; //删除 预约记录
	public final static Integer SCHEDULE_RECORD_OLD = 3; //原始数据 预约记录

	//订单
	public final static Integer ORDER_TYPE_SCHEDULE = 1;  //线上   预约订单(店铺端预约)
	public final static Integer ORDER_TYPE_STORE = 2;  //线下      新建订单(店铺端订单)
	public final static Integer ORDER_TYPE_OTHER = 3;  //大众点评
	public final static Integer ORDER_TYPE_RECHARGE = 4;  //充值
	public final static Integer ORDER_TYPE_USER = 5;  //线上订单(用户端预约)
	public final static Integer ORDER_TYPE_XCX = 6;  //小程序预约订单

	//订单状态
	public final static Integer ORDER_STATUS_SERVE = 1;  //正在服务
	public final static Integer ORDER_STATUS_PAMENT = 2;  //待付款
	public final static Integer ORDER_STATUS_EVALUATE = 3;  //未评价
	public final static Integer ORDER_STATUS_FINISH = 4;  //已完成
	public final static Integer ORDER_STATUS_SERVING = 5;  //接单后服务中
	public final static Integer ORDER_STATUS_WAIT = 6;  //预约后自动开单
	public final static Integer ORDER_STATUS_CANCEL = 7;  //用户已取消
	public final static Integer ORDER_STATUS_WAIT_SURE = 8;  //待美甲师端确认支付
	public final static Integer ORDER_STATUS_COUPON_WAIT_SURE = 9;  //待确认外部优惠券可用
	

	//支付方式
	public final static Integer ORDER_PAYMENT_WEI = 1; //微信
	public final static Integer ORDER_PAYMENT_MEMBER = 2;  //会员
	public final static Integer ORDER_PAYMENT_OTHER = 3;  //其他

	//订单评价状态
	public final static Integer ORDER_COMMENT_EVALUATED = 1; //未评价
	public final static Integer ORDER_COMMENT_FINISH = 2; //已评价

	//订单开始-结束状态
	public final static Integer ORDER_EXECUTION_START = 1; //开始
	public final static Integer ORDER_EXECUTION_END = 2;  //结束
	public final static Integer ORDER_EXECUTION_TIMESLOT = 3;  //待支付
	public final static Integer ORDER_EXECUTION_COMMEND = 4;  //订单服务待评价
	public final static Integer ORDER_EXECUTION_FINISH = 5;  //订单服务已评价

	//会员来源
	public final static Integer MEMBER_SCHEDULE = 1; //预约
	public final static Integer MEMBER_ORDER = 2;  //订单
	public final static Integer MEMBER_WEI = 3;  //微信

	//优惠券和订单关联表状态
	public final static Integer COUPON_ORDER_STATUS_CREATE = 1; //待确认
	public final static Integer COUPON_ORDER_STATUS_CANUSE = 2;  //已确认可用
	public final static Integer COUPON_ORDER_STATUS_NOTUSE = 3;  //已确认不可用
	public final static Integer COUPON_ORDER_STATUS_CUSDEL = 4;  //用户删除
	public final static Integer COUPON_ORDER_STATUS_TECDEL = 5;  //美甲师删除
	
	//优惠券状态
	public final static Integer COUPON_STATUS_YES = 1; //正常
	public final static Integer COUPON_STATUS_USED = 2;  //已使用
	public final static Integer COUPON_STATUS_TIMEOUT = 3;  //已过期
	
}
