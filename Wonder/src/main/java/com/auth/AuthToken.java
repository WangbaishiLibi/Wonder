package com.auth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"rawtypes", "unchecked"})
public class AuthToken {
	/*
	 * 用户权限信息
	 */
	
	private String tokenId;		//令牌的唯一标识
	private List<String> roles = new ArrayList<String>();
	private List<String> privileges = new ArrayList<String>();
	
	public AuthToken(String tokenId) {
		// TODO Auto-generated constructor stub
		this.tokenId = tokenId;
	}
	
	
	public AuthToken(String tokenId, List roles) {
		// TODO Auto-generated constructor stub
		this(tokenId, roles, new ArrayList());
	}

	public AuthToken(String tokenId, List roles, List privileges) {
		// TODO Auto-generated constructor stub
		this.tokenId = tokenId;
		this.roles = roles;
		this.privileges = privileges;
	}
	
	public boolean checkByRoleAndPrivilege(String[] roles, String[] privileges){
		for(String role : roles){
			if(!checkByRole(role))	return false;
		}
		for(String privilege : privileges){
			if(!checkByPrivilege(privilege))	return false;
		}
		return true;
	}
	
	public boolean checkByRole(String role){
		if(Collections.binarySearch(roles, role) > -1)	return true;
		else return false;
	}
	
	public boolean checkByPrivilege(String privilege){
		if(Collections.binarySearch(privileges, privilege) > -1)	return true;
		else return false;
	}
	
	public String getTokenId() {
		return tokenId;
	}
	
	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public List<String> getPrivileges() {
		return privileges;
	}

	public void setPrivileges(List<String> privileges) {
		this.privileges = privileges;
	}
	
	
	
	
}
