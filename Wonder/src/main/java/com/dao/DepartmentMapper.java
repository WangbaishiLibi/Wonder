package com.dao;


import java.util.List;
import com.model.authority.U_department;
import com.model.authority.U_privilege;
import com.model.authority.U_role;

public interface DepartmentMapper {

	List<U_department> getDepartmentByKey(String key);
	String getDepartmentNameById(String department_id);
	List<U_department> getAllDepartment();
	List<U_department> getAllDepartmentByPage(int page);
	List<U_department> getDepartmentBySuperiorId(String superior_department_id);
	List<U_role> getRoleByDepartmentId(String department_id);
	List<U_privilege> getPrivilegeByDepartmentId(String department_id);
	List<U_privilege> getPriviByDepartmentId(String department_id);
	U_department getDepartmentById(String department_id);
	U_department existDepartmentByCode(String department_code);
	U_department checkDepartmentRole(String department_id, String role_name);
	List<U_department> getDepByRole(String role_name);
	int insertDepartment(U_department department);
	int updateDepartment(U_department department);
	int deleteDepartmentById(String department_id);
}
	
