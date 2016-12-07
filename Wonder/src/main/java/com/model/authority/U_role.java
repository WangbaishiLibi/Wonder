package com.model.authority;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class U_role implements Serializable{

	private String role_id;
	private String role_name;
	private String display_name;
	private String memo;
	private List<U_privilege> privileges;
	
	
	public String getRole_id() {
		return role_id;
	}
	public void setRole_id(String role_id) {
		this.role_id = role_id;
	}
	public String getRole_name() {
		return role_name;
	}
	public void setRole_name(String role_name) {
		this.role_name = role_name;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public List<U_privilege> getPrivileges() {
		return privileges;
	}
	public void setPrivileges(List<U_privilege> privileges) {
		this.privileges = privileges;
	}
	public String getDisplay_name() {
		return display_name;
	}
	public void setDisplay_name(String display_name) {
		this.display_name = display_name;
	}
	
	
}
