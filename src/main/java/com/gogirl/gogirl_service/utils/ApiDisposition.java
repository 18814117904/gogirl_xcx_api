package com.gogirl.gogirl_service.utils;

/**
 * Created by yinyong on 2018/8/20.
 */
public class ApiDisposition {

//    private static final String WWW_OA_COM = "http://www.bailuntec.com/";
//    private static final String FINANSYS_OA_COM = "http://testcw.bailuntec.com/";
    private static final String WWW_OA_COM = "http://www.oa.com/";
    private static final String FINANSYS_OA_COM = "http://finansys.oa.com/";

    /**
     * OA 系统获取所有用户列表接口，method = get
     */
    public static final String BUYUSER_API = WWW_OA_COM+"Api/User/GetUser";

    public static final String LOGING_USER_API = WWW_OA_COM+"Login/GetUserByBLUserAcct";

    public static final String POST_COMPANYLIST_API = FINANSYS_OA_COM+"API/API/GetCompanyMainBody";

    public static final String POST_APPLY_API = FINANSYS_OA_COM+"API/API/Apply";

    public static final String POST_COMPANYLIST_FINANSYS_API = "http://cw.bailuntec.com/API/API/GetBankAccount";
}
