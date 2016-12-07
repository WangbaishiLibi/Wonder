package com.controller;

import java.util.Arrays;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.auth.AuthToken;
import com.auth.AuthorityUtil;
import com.auth.Permission;
import com.auth.PermissionType;
import com.dao.UserDao;
import com.util.LogUtil;

@Controller
//@RequestMapping("/login")
public class LoginController {

	@Autowired
	private UserDao userDao;
	@Autowired
	private LogUtil logUtil;
	
	@RequestMapping("/")
	@Permission(authType=PermissionType.ANON)
	public String defaultPage(){
		return "login";
	}
	
	
	@RequestMapping(value="/login")
	@Permission(authType=PermissionType.ANON)
	public String login(String name) throws Exception{
		LogUtil.console("login", "login");
		System.out.println(userDao.findAllUser());
		
		//1.查询用户，验证登录，存入session，可用user_id作为tokenId
		
		
		//2.查询角色权限，构造令牌，session存入tokenId，或token对象
	//	AuthToken token = new AuthToken("001");
		AuthToken token = new AuthToken("001", Arrays.asList("admin"));
		AuthorityUtil.instance().login(token);

		return "redirect:html/index.html";
	}
	
	@RequestMapping("/logout")
	@Permission(authType=PermissionType.ANON)
	public String logout(HttpSession session){
		LogUtil.console("login", "logout");
		AuthorityUtil.instance().logout("001");
		session.invalidate();
		return "redirect:html/login.html";
	}
	
	
	@RequestMapping("/error.do")
	public @ResponseBody String error(Exception e){
		return e.getMessage();
	}
	
	
}
