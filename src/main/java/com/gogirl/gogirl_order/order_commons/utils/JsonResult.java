package com.gogirl.gogirl_order.order_commons.utils;

/**
 * Created by yinyong on 2018/9/17.
 */
public class JsonResult<T> {

    public static final boolean CODE_ERROR = false;// 请求返回码-失败
    public static final boolean CODE_SUCCESS = true;// 请求返回码-成功
    public static final String APP_DEFINE_ERR = "参数错误或者操作未成功";// 返回信息-失败
    public static final String APP_DEFINE_SUC = "操作成功";// 返回信息-成功
	public static final String TOKEN_NULL_CODE = "0001";// token失效码
	public static final String TOKEN_NULL_ERR = "token失效";// token失效
	public static final String TOKEN_NULL_AUTHORIZED_CODE = "0002";//用户未授权
	public static final String TOKEN_NULL_AUTHORIZED_MSG = "用户未授权";//
	public static final String PART_PAYMENT_CODE = "0003";//余额支付部分金额后,返回true和这个码
	public static final String NO_DEPARTMENT_CODE = "0004";//美甲师端操作的时候，token没有选店，则返回0004
	public static final String PARAM_NULL = "参数%s为空,请检查入参";// 失败
    
	
	
    public static final String APP_DEFINE_URL_ERR = "请求地址错误";// 返回信息-地址错误
    public static final String APP_DEFINE_SIGN_ERR = "参数签名验证未通过";// 返回信息-参数签名验证未通过
    public static final String APP_DEFINE_EXCEPTION = "操作未成功";// 返回信息-服务器发生异常
	public static final String APP_DEFINE_NICKNAME_ERR = "用户昵称转换出错";// 失败
	public static final String APP_OPENID_ERR = "无该openid的用户";// 失败
	public static final String APP_PHONE_BE_BIND_ERR = "该手机号码已被其他账号绑定";// 失败
	public static final String APP_CUSTOMER_HAVE_CARD = "该手机号码已有会员卡";// 失败
	public static final String APP_TOKEN_INVALID = "token无效";// 返回信息-服务器发生异常
	public static final String INVALID_CODE = "INVALID_CODE";
	public static final String TOKEN_NULL_MSG = "token过期";
	
	
	private boolean success;	// 返回码
    private String message;		// 返回信息
    private T data;		// 返回数据

    public JsonResult(){
       this.success = CODE_ERROR;
       this.message = APP_DEFINE_ERR;
           }

    public JsonResult(boolean success){
        this.success = success;
        if(success){
        	this.message = APP_DEFINE_SUC;
        }else{
        	this.message = APP_DEFINE_ERR;
        }
    }
    public JsonResult(boolean success,String message){
        this.success = success;
        this.message = message;
    }
    public JsonResult(boolean success,String message,T data){
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public boolean getSuccess() {
        return success;
    }

    public JsonResult<T> setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public JsonResult<T> setMessage(String message) {
        this.message = message;
        return this;
    }

	public T getData() {
		return data;
	}

	public JsonResult<T> setData(T data) {
		this.data = data;
		return this;
	}

	@Override
	public String toString() {
		return "JsonResult [success=" + success + ", message=" + message
				+ ", data=" + data + "]";
	}


}
