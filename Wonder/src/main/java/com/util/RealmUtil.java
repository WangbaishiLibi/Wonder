package com.util;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import com.entity.User;




public class RealmUtil extends AuthorizingRealm {

	/*
	 * 注意：方法内部的name为用户实体的唯一索引
	 */
	
	public RealmUtil() {
		super();
	}
	
	
	/**
	 * 注入用户权限
	 * 用户的权限来源有三处 :
	 * 1:用户拥有的角色所对应的权限;
	 * 2:用户拥有的权限
	 * 3:用户所在部门所拥有的角色
	 * 4:用户所在部门拥有的权限
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
		//获取当前用户名
		String name = (String)principalCollection.fromRealm(this.getName()).iterator().next();

		//根据用户名查询用户权限
		
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
	
		//为用户加入角色和权限
//		info.addRole(role);
//		info.addStringPermission(permission);
		info.addRole("role");
		info.addStringPermission("admin");
		if("001".equals(name))
			return info;
		else 
			return null;
	}

	/*
	 * 注入用户实体
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken)authenticationToken;
		String name = token.getUsername();
		User user = new User();
		user.setId("001");
		user.setName("admin");
		user.setPassword("123");
		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user.getId(), user.getPassword(), this.getName());
		if("001".equals(name))
			return info;
		else 
			return null;
	}
	
	
	/*
	 shiro LoginController 样例（彻底弃坑了，ajax请求无法正常跳转）
	 	@RequestMapping(value="/login")
	public String login(String name){
		LogUtil.console("login", "login");
//		String[] strs =  {"sdf", "sdf1"};
//		System.out.println(logUtil.log(strs, " "));
		System.out.println(userDao.findAllUser());
		
		
		//System.out.println(baseDao.update("update Test set tname='489' where tid=111"));	

		//token的Username为用户的唯一索引
		UsernamePasswordToken token = new UsernamePasswordToken("001", "123");
		Subject subject = SecurityUtils.getSubject();

		try{
			subject.login(token);
		}catch(AuthenticationException e){
			e.printStackTrace();
			return "redirect:html/login.html";
		}
		
		if(subject.isAuthenticated()){
			return "redirect:html/index.html";
		}
		return "redirect:html/login.html";
	}
	
	@RequestMapping("/logout.do")
	public String logout(HttpSession session){
		System.out.println("logout");
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		session.invalidate();
		return "redirect:html/login.html";
	}
	 */

}
