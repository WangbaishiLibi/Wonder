package com.auth;


import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import com.dao.UserDao;
import com.util.LogUtil;

public class MainInterceptor implements HandlerInterceptor{
	/*
	 * 全体默认拦截
	 * 说明：只适用方法级别
	 *
	 */
	
	/*
	 * 资源请求失败后默认跳转路径
	 */
	private final String default_path = "/html/login.html";
	/*
	 * 资源型路径
	 */
	private final String[] unauth_path = {default_path, "/res", "/html/error.html"};
	
	@Autowired
	private UserDao userDao;
	
	
	
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		// TODO Auto-generated method stub
		LogUtil.console("interceptor", request.getRequestURI());
		LogUtil.console("interceptor handler", handler.toString());
		boolean result = true;
		String tokenId = "001";
		
		//过滤(全匹配，只要包含)
		for(String path : unauth_path){
			if(Pattern.matches(".*" + path + ".*", request.getRequestURI()))
				return true;
		}
		
		//ajax请求
		if(ReturnStatus.AjaxCode.equals(request.getParameter("ajaxStatus"))){
			if(AuthorityUtil.instance().getToken(tokenId) == null){
				response.setStatus(ReturnStatus.UnLogin);
				return false;
			}
		}
		
		
		//资源型请求
		if(handler instanceof ResourceHttpRequestHandler){
			//1.用户验证
			//session方式
			//token方式
			if(AuthorityUtil.instance().getToken(tokenId) == null){
				String projectName = request.getRequestURI().split("/")[1];
				response.sendRedirect("/" + projectName + default_path);
				return false;
			}
		}
		//接口请求
		else if(handler instanceof HandlerMethod){
		
			HandlerMethod method = (HandlerMethod)handler;
			Permission permission = method.getMethodAnnotation(Permission.class);
			
			if(permission != null && permission.authType() == PermissionType.ANON)
				return true;
			
			if(AuthorityUtil.instance().getToken(tokenId) == null){
				response.setStatus(ReturnStatus.UnLogin);
				return false;
			}
			
			if(permission != null){
				result = AuthorityUtil.instance().getToken(tokenId).
				checkByRoleAndPrivilege(permission.roles(), permission.privileges());
				if(!result)	response.setStatus(ReturnStatus.UnAuth);
				return result;
			}
			
		}

		LogUtil.console("interceptor", "result="+result);
		return result;
	}

	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
	}
	


}
