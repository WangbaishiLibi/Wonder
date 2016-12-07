package com.util;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

//HandlerExceptionResolver
public class ExceptionUtil{

	@ExceptionHandler
	public String exception(HttpServletRequest request, Exception e){
		System.out.println("=======>"+e.getMessage());  
		return "error";
	}
	
	
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		// TODO Auto-generated method stub
		Map<String, Object> model = new HashMap<String, Object>();  
        model.put("ex", ex);  
        System.out.println("=======>"+ex.getMessage());  
        // 根据不同错误转向不同页面  
        return new ModelAndView("error.html", model);  
	}

	
}
