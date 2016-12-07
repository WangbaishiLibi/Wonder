package com.model.authority;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class U_department implements Serializable{

	private String department_id;
	private String department_name;
	private String department_code;
	private String superior_department_id;
	private String superior_department_name;
	private String memo;
	private char category;
	private List<U_role> roles;
	
	
	public String getDepartment_code() {
		return department_code;
	}
	public void setDepartment_code(String department_code) {
		this.department_code = department_code;
	}
	public String getSuperior_department_id() {
		return superior_department_id;
	}
	public void setSuperior_department_id(String superior_department_id) {
		this.superior_department_id = superior_department_id;
	}
	
	public String getSuperior_department_name() {
		return superior_department_name;
	}
	public void setSuperior_department_name(String superior_department_name) {
		this.superior_department_name = superior_department_name;
	}
	public String getDepartment_id() {
		return department_id;
	}
	public void setDepartment_id(String department_id) {
		this.department_id = department_id;
	}
	public String getDepartment_name() {
		return department_name;
	}
	public void setDepartment_name(String department_name) {
		this.department_name = department_name;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public List<U_role> getRoles() {
		return roles;
	}
	public void setRoles(List<U_role> roles) {
		this.roles = roles;
	}
	public char getCategory() {
		return category;
	}
	public void setCategory(char category) {
		this.category = category;
	}
	
	
}
