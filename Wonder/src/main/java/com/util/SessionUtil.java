package com.util;


import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.web.servlet.support.RequestContextUtils;

public class SessionUtil  implements HttpSessionListener{


	public static final String fg_Info = "INFO";
//	private static final String fg_Access = "ACCESS_CNT";
	private static final String fg_User = "USER_CNT";
	
	public SessionUtil() {
		// TODO Auto-generated constructor stub

	}
	
	public void sessionCreated(HttpSessionEvent evt) {
		// TODO Auto-generated method stub
	//	ServletContext application = evt.getSession().getServletContext();
		System.out.println("session create");
	}

	public void sessionDestroyed(HttpSessionEvent se) {
		// TODO 自动生成的方法存根
	//	ServletContext application = se.getSession().getServletContext();
		System.out.println("session destroy"); 
	}
	
	public static synchronized  void increaseUserCnt(HttpServletRequest request){		
		ServletContext application = RequestContextUtils.getWebApplicationContext(request).getServletContext();
		appHandle(application, fg_User, true, request);
	}
	
	public static synchronized  void decreaseUserCnt(HttpServletRequest request){
		ServletContext application = RequestContextUtils.getWebApplicationContext(request).getServletContext();
		appHandle(application, fg_User, false, request);
	}
	
	private static synchronized void appHandle(ServletContext application, String fg, boolean increase, HttpServletRequest request){
		if(application.getAttribute(fg_Info) == null){
		//	application.setAttribute(fg_Info, new SessInfo());
		}
	}
	


	
}
