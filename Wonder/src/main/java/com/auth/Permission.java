package com.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Permission {

	
	
	/** 
     * 权限验证类型
     */
	PermissionType authType() default PermissionType.AUTHC;
	
	/** 
     * 角色
     */
	String[] roles() default {};
	
	/** 
     * 权限
     */
	String[] privileges() default {};
	
}

