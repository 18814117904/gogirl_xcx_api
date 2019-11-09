//package com.gogirl.gogirl_xcx.config;
//
//import java.io.IOException;
//import java.util.Date;
//
//import javax.annotation.Resource;
//import javax.servlet.Filter;
//import javax.servlet.FilterChain;
//import javax.servlet.FilterConfig;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.gogirl.gogirl_xcx.dao.XcxFormIdMapper;
//import com.gogirl.gogirl_xcx.entity.GogirlToken;
//import com.gogirl.gogirl_xcx.entity.XcxFormId;
//import com.gogirl.gogirl_xcx.service.GogirlTokenService;
//
//public class FormIdFilter implements Filter {
//	private Logger logger = LoggerFactory.getLogger(this.getClass());
//	@Resource
//	GogirlTokenService gogirlTokenService;
//	@Resource
//	XcxFormIdMapper xcxFormIdMapper;
//	
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        HttpServletRequest request = (HttpServletRequest) servletRequest;
//        HttpServletResponse response = (HttpServletResponse) servletResponse;
//        String token = request.getParameter("token");
//        String formId = request.getParameter("formId");
//        if(formId!=null&&!formId.isEmpty()&&!formId.equals("the formId is a mock one")&&token!=null&&!token.isEmpty()){
//        	GogirlToken gogirltoken = gogirlTokenService.getTokenByToken(token);
//        	if(gogirltoken.getCustomerId()!=null){
//            	XcxFormId xcxFormId = new XcxFormId();
//            	xcxFormId.setCustomerId(gogirltoken.getCustomerId());
//            	xcxFormId.setFormId(formId);
//            	xcxFormId.setTime(new Date());
//            	if(gogirltoken.getCustomer()!=null&&gogirltoken.getCustomer().getOpenid1()!=null){
//            		xcxFormId.setOpenid(gogirltoken.getCustomer().getOpenid1());
//            	}
//            	xcxFormIdMapper.insertSelective(xcxFormId);
//        	}
//        }
//        filterChain.doFilter(request, response);
//    }
//
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//
//    }
//
//    @Override
//    public void destroy() {
//
//    }
//}
