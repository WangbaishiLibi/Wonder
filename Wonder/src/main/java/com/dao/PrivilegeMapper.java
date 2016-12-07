package com.dao;


import java.util.List;
import java.util.Map;

import com.model.authority.U_privilege;

public interface PrivilegeMapper {

	List<U_privilege> getAllPrivilege();
	int insertPrivilege(U_privilege privilege);
	List<U_privilege> getPrivilegeByUser(String user_id);
	List<Map<String,Object>> getRolePrivilegeByUser(String user_id);
	List<U_privilege> getPrivilegeByDepartment(String department_id);
}
	
