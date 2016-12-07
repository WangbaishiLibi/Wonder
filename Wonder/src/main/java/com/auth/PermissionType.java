package com.auth;

/*
 * 权限验证类型
 */
public enum PermissionType{
	ANON,			//忽略验证
	AUTHC,			//需要验证（默认）
	ROLE,			//通过角色验证
	PRIVILEGE		//通过权限验证
}
