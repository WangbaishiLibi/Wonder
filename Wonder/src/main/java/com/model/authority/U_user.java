package com.model.authority;

import java.util.List;

import com.model.authority.U_role;

/**
 * 
 * @ClassName U_user
 * @Description 用户基本信息表
 * @Author dell
 * @CreateDate 2015年5月25日
 */
public class U_user {
	String user_id;
	String department_id;
	String department_name;
	String password;
	String empno;
	String empname;
	char status;
	String rank;
	String email;
	String qq;
	String register_time;
	String last_login_time;
	String memo;
	List<U_role> roles;
	String ip;
	
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmpno() {
		return empno;
	}
	public void setEmpno(String empno) {
		this.empno = empno;
	}
	public String getEmpname() {
		return empname;
	}
	public void setEmpname(String empname) {
		this.empname = empname;
	}
	public char getStatus() {
		return status;
	}
	public void setStatus(char status) {
		this.status = status;
	}
	public String getRank() {
		return rank;
	}
	public void setRank(String rank) {
		this.rank = rank;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getRegister_time() {
		return register_time;
	}
	public void setRegister_time(String register_time) {
		this.register_time = register_time;
	}
	public String getLast_login_time() {
		return last_login_time;
	}
	public void setLast_login_time(String last_login_time) {
		this.last_login_time = last_login_time;
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
	@Override
	public String toString() {
		return "U_user [user_id=" + user_id + ", department_id="
				+ department_id + ", password=" + password + ", empno=" + empno
				+ ", empname=" + empname + ", status=" + status + ", rank="
				+ rank + ", email=" + email + ", qq=" + qq + ", register_time="
				+ register_time + ", last_login_time=" + last_login_time
				+ ", memo=" + memo + ", roles=" + roles + "]";
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	
}
