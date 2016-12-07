package com.dao;


import java.util.List;

import com.entity.User;
import com.model.authority.U_privilege;
import com.model.authority.U_role;
import com.model.authority.U_user;


public interface UserDao {

	public List<User> findAllUser();
	public User findUserById(String user_id);
	
	U_user getUserByName(String userName);
	U_user existUserByEmpno(String empno);
	U_user getUserById(String user_id);
	int deleteUserById(String user_id);
	List<U_role> getRoleByUserId(String user_id);
	List<U_role> getUserRoleByUserId(String user_id);
	List<U_user> getUserByDepartmentId(String department_id);
	List<U_user> getUserByKey(String key);
	List<U_user> getAllUser();
	List<U_user> getSpecialUser();
	U_user getSpecialUserByRole(String role_name, String user_id);
	int updateUser(U_user user);
	int insertUser(U_user user);
	int countUser();	
	List<U_privilege> getPrivilegeByRoleID(String role_id);
//	List<Menu> getMenus(String roleName);	//获取角色具有的菜单资源
	List<U_privilege> getPrivilege(String userId);//获取用户的权限
//	List<Menu> getIndexMenu(int menu_id);
	void insertUsers(List<U_user> u_users);
	
	List<U_privilege> checkUserPrivilege(String user_id, String privi_name);
}
