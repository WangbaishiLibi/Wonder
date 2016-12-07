package com.dao;


import java.util.List;
import com.model.authority.U_privilege;
import com.model.authority.U_role;

public interface RoleMapper {

	List<U_role> getAllRole();
	U_role getRoleById(String role_id);
	int deleteRoleById(String role_id);
	int insertRole(U_role role);
	int updateRole(U_role role);
	List<U_privilege> getAllAuthorityByRoleId(String role_id);
}
	
