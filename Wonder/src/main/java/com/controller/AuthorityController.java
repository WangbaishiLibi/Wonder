package com.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.util.*;
import com.dao.*;
import com.model.authority.U_department;
import com.model.authority.U_privilege;
import com.model.authority.U_role;
import com.model.authority.U_user;


/**
 * 
 * @ClassName AuthorityController
 * @Description 权限相关表操作
 * @Author libi
 * @CreateDate 2015年6月17日
 */

@Controller
public class AuthorityController{
	
	
	@Autowired
	private EntityDao entityDao;
	
	@Autowired
	private UserDao userMapper;	
	
	@Autowired
	private RoleMapper roleMapper;	
	
	@Autowired
	private PrivilegeMapper privilegeMapper;	
	
	@Autowired
	private DepartmentMapper departmentMapper;
	

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("/getTreeData")
	public @ResponseBody List<Node> getTreeData(String type, HttpServletRequest req){
		List<Node> v_root = new ArrayList<Node>();
		List<Node> root = new ArrayList<Node>();
		
		String sql = "select * from u_department where superior_department_id is null";
		List<Map> header = entityDao.queryForList(sql);
			
		for(Map<String,Object> map : header){
			String headerId = String.valueOf(map.get("DEPARTMENT_ID"));
			Node node = new Node(headerId, String.valueOf(map.get("DEPARTMENT_NAME")), "UR",
					createTreeChildren(headerId, type));
		//	node.setIcon("images/depart.png");
			root.add(node);
		}					
		v_root.add(new Node("0", "部门结构图", "", root));
	
		
		return root;
	}
	
	//将参数节点的所有子节点加入到该节点中
	private List<Node> createTreeChildren(String nodeId, String type){
		List<Node> children = new ArrayList<Node>();
		for(U_department tmp: departmentMapper.getDepartmentBySuperiorId(nodeId)){
			Node node = new Node(tmp.getDepartment_id(), tmp.getDepartment_name(), "UD",
					createTreeChildren(tmp.getDepartment_id(), type));
		//	node.setIcon("images/depart.png");
			children.add(node);
		}
		return children;
	}
	
	
	@RequestMapping("/getChildDepartment")
	public @ResponseBody List<U_department> getChildDepartment(String department_id){
		
		List<U_department> departments = new ArrayList<U_department>();
		U_department head = departmentMapper.getDepartmentById(department_id);
		departments.add(head);	
		
		List<U_department> level = new ArrayList<U_department>();
		level.add(head);
		int cnt = 1000;
		while((--cnt)>0){
			List<U_department> tmp = new ArrayList<U_department>();		//保存当前层的节点
			for(U_department depart : level){
				List<U_department> ud = departmentMapper.getDepartmentBySuperiorId(depart.getDepartment_id());
				tmp.addAll(ud);
			}
			
			if(tmp.size() == 0) break;
			level.removeAll(level);
			level.addAll(tmp);
			
			departments.addAll(tmp);
		}
		for(U_department department: departments){
			department.setRoles(departmentMapper.getRoleByDepartmentId(department.getDepartment_id()));
			department.setSuperior_department_name(departmentMapper.getDepartmentNameById(department.getSuperior_department_id()));
		}
		
		return departments;
	}
	
	
	
	@RequestMapping("/getUserById")
	public @ResponseBody U_user getUserById(String user_id){
		return userMapper.getUserById(user_id);
	}
	
	@RequestMapping("/deleteUserById")
	public @ResponseBody Result deleteUserById(String user_id){
		Result result  = new Result();
		if(user_id == null || user_id.contains("%")){
			result.setRes(false);
			result.setMsg("用户ID不对！");
			return result;
		}
		try{
			if(userMapper.deleteUserById(user_id)>0)	
				result.setRes(true);
		}catch(Exception e){
			result.setRes(false);
			result.setMsg("删除用户出现异常："+e.getMessage());
			return result;
		}
		
		return result;
	}
	
	
	@RequestMapping("/getUserDataByDepartment")
	public @ResponseBody List<U_user> getUserDataByDepartment(String department_id){
		List<U_user> userlist = userMapper.getUserByDepartmentId(department_id);
		for(U_user user: userlist){
			user.setRoles( userMapper.getRoleByUserId(user.getUser_id()));
			user.setDepartment_name(departmentMapper.getDepartmentNameById(user.getDepartment_id()));
		}
		return userlist;
	}
	
	
	@RequestMapping("/getUserPrivilege")
	public @ResponseBody List<List<String>> getUserPrivilege(String user_id){
		List<List<String>> data = new ArrayList<List<String>>();		
		
		List<U_privilege> privis = privilegeMapper.getPrivilegeByUser(user_id);
		for(U_privilege privi : privis){
			List<String> tmp = new ArrayList<String>();
			tmp.add(privi.getPrivilege_id());
			tmp.add(privi.getDisplay_name());
			tmp.add("");
			tmp.add("用户-权限");
			data.add(tmp);
		}
		
		String sql = "";

		try{
			for(Map<?, ?> map: privilegeMapper.getRolePrivilegeByUser(user_id)){
				List<String> tmp = new ArrayList<String>();
				tmp.add(String.valueOf(map.get("PRIVILEGE_ID")));
				tmp.add(String.valueOf(map.get("PRIVI_NAME")));
				tmp.add(String.valueOf(map.get("ROLE_NAME")));
				tmp.add("用户-角色-权限");
				data.add(tmp);
			}
		}catch(Exception e){
			e.printStackTrace();
			return data;
		}
		
		
		String departmentId = userMapper.getUserById(user_id).getDepartment_id();
		if(departmentId == null)	return data;
		
		List<U_privilege> pris = departmentMapper.getPriviByDepartmentId(departmentId);
		for(U_privilege privi : pris){
			List<String> tmp = new ArrayList<String>();
			tmp.add(privi.getPrivilege_id());
			tmp.add(privi.getDisplay_name());
			tmp.add("");
			tmp.add("所属部门-权限");
			data.add(tmp);
		}
		

		
		sql = "select U_PRIVILIGE.PRIVILEGE_ID, U_PRIVILIGE.DISPLAY_NAME PRIVI_NAME, U_ROLE.DISPLAY_NAME ROLE_NAME "+  
				"from U_ROLE, U_DEPARTMENT_ROLE, U_ROLE_PRIVILIGE, U_PRIVILIGE "+
				"where  U_DEPARTMENT_ROLE.ROLE_ID=U_ROLE_PRIVILIGE.ROLE_ID and "+
				"U_PRIVILIGE.PRIVILEGE_ID = U_ROLE_PRIVILIGE.PRIVILEGE_ID and "+
				"U_ROLE.ROLE_ID = U_DEPARTMENT_ROLE.ROLE_ID and "+
				"U_DEPARTMENT_ROLE.DEPARTMENT_ID = '"+ departmentId  +"' ";

		
		try{
			for(Map<?, ?> map: entityDao.queryForList(sql)){
				List<String> tmp = new ArrayList<String>();
				tmp.add(String.valueOf(map.get("PRIVILEGE_ID")));
				tmp.add(String.valueOf(map.get("PRIVI_NAME")));
				tmp.add(String.valueOf(map.get("ROLE_NAME")));
				tmp.add("所属部门-角色-权限");
				data.add(tmp);
			}
		}catch(Exception e){
			e.printStackTrace();
			return data;
		}
		
		
		
		return data;
	}
	
	
	@RequestMapping("/getUserBykey")
	public @ResponseBody List<U_user> getUserDataByKey(String key){
		if(StringUtil.isEmpty(key)) return null;
		List<U_user> userlist = userMapper.getUserByKey("%"+key+"%");

		for(U_user user: userlist){
			user.setRoles( userMapper.getRoleByUserId(user.getUser_id()));
			user.setDepartment_name(departmentMapper.getDepartmentNameById(user.getDepartment_id()));
		}
		return userlist;
	}
	
	
	@RequestMapping("/getUserData")
	public @ResponseBody List<U_user> getUserData(String nodeId){
		List<U_user> userlist = new ArrayList<U_user>();
		
		String []nodes = nodeId.split(",");
		for(String uid: nodes){
			U_user user = userMapper.getUserById(uid);
			user.setRoles( userMapper.getRoleByUserId(uid));
			userlist.add(user);
		}
		return userlist;
	}
	
	@RequestMapping("/insertUser")
	public @ResponseBody Result insertUser(U_user user, String[] roleIds, String[] privilegeIds){
		
		Result result = new Result();
		if(userMapper.existUserByEmpno(user.getEmpno()) != null){
			result.setMsg("用户工号已存在！");
			return result;
		}
		
		int uid  = entityDao.getMaxId("USER_ID", "U_USER");
		user.setUser_id(String.valueOf(uid));	
		user.setPassword(StringUtil.encryptPassword(user.getPassword()));	//MD5散列化密码

		userMapper.insertUser(user);
		
		String sql;
		if(roleIds != null && roleIds.length>0){
			for(String tmp: roleIds){
				if("".equals(tmp)) continue;
				sql = "insert into U_USER_ROLE values('"+ user.getUser_id() +"','"+ tmp +"')";
				entityDao.insert(sql);
			}
		}
		
		
		if(privilegeIds != null && privilegeIds.length>0){
			
			for(String tmp: privilegeIds){
				if("".equals(tmp)) continue;
				sql = "insert into U_USER_PRIVILIGE values('"+ user.getUser_id() +"','"+ tmp +"')";
				entityDao.insert(sql);
			}
		}
		result.setRes(true);
		return result;
	}
	
	@RequestMapping("/updateUser")
	public @ResponseBody Result updateUser(U_user user, String[] roleIds, String[] privilegeIds){

		Result result = new Result();
		
		
		userMapper.updateUser(user);
		
		
		if(roleIds != null && roleIds.length>0){
			List<String> paramRoles = new ArrayList<String>();
			Collections.addAll(paramRoles, roleIds);
			String sql;
			
			
			for(U_role role: userMapper.getRoleByUserId(user.getUser_id())){
				if(!paramRoles.contains(role.getRole_id())){
					sql = "delete from U_USER_ROLE where role_id='"+ role.getRole_id() +"' and user_id='"+ user.getUser_id() +"'";
					entityDao.delete(sql);
				}else{
					paramRoles.remove(role.getRole_id());
				}
			}
			
			
			for(String tmp: paramRoles){
				if("".equals(tmp)) continue;
				sql = "insert into U_USER_ROLE values('"+ user.getUser_id() +"','"+ tmp +"')";
				entityDao.insert(sql);
			}
		}
		
		
		if(privilegeIds != null && privilegeIds.length>0){
			List<String> paramPrivileges = new ArrayList<String>();
			Collections.addAll(paramPrivileges, privilegeIds);
			String sql;
			
			
			for(U_privilege privi: privilegeMapper.getPrivilegeByUser(user.getUser_id())){
				if(!paramPrivileges.contains(privi.getPrivilege_id())){
					sql = "delete from U_USER_PRIVILIGE where user_id='"+ user.getUser_id() +"' and privilege_id='"+ privi.getPrivilege_id() +"'";
					entityDao.delete(sql);
				}else{
					paramPrivileges.remove(privi.getPrivilege_id());
				}
			}
			
			
			for(String tmp: paramPrivileges){
				if("".equals(tmp)) continue;
				sql = "insert into U_USER_PRIVILIGE values('"+ user.getUser_id() +"','"+ tmp +"')";
				entityDao.insert(sql);
			}
		}
		result.setRes(true);
		return result;
	}
	
	
	
	@RequestMapping("/getDepartmentBykey")
	public @ResponseBody List<U_department> getDepartmentDataByKey(String key){
		if(key == null) return null;
		try {
	    	 key = URLDecoder.decode(key,  "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<U_department> departList = departmentMapper.getDepartmentByKey("%"+key+"%");

		for(U_department depart: departList){
			depart.setRoles( departmentMapper.getRoleByDepartmentId(depart.getDepartment_id()));
			depart.setSuperior_department_id(departmentMapper.getDepartmentNameById(depart.getSuperior_department_id()));
		}
		return departList;
	}
	
	
	
	@RequestMapping("/getDepartmentData")
	public @ResponseBody List<U_department> getDepartmentData(){
		
		List<U_department> departments = departmentMapper.getAllDepartment();

		for(U_department department: departments){
			department.setRoles(departmentMapper.getRoleByDepartmentId(department.getDepartment_id()));
			department.setSuperior_department_id(departmentMapper.getDepartmentNameById(department.getSuperior_department_id()));
		}
		
		return departments;
	}
	
	
	@RequestMapping("/getDepartmentPageData")
	public @ResponseBody List<U_department> getDepartmentPageData(int page){
		List<U_department> departments;
		if(page < 1)
			departments = departmentMapper.getAllDepartment();
		else 
			departments = departmentMapper.getAllDepartmentByPage(page);
		for(U_department department: departments){
			department.setRoles(departmentMapper.getRoleByDepartmentId(department.getDepartment_id()));
			department.setSuperior_department_id(departmentMapper.getDepartmentNameById(department.getSuperior_department_id()));
		}
		
		return departments;
	}
	
	
	@RequestMapping("/getDepartmentById")
	public @ResponseBody U_department getDepartmentById(String department_id){	
		return departmentMapper.getDepartmentById(department_id);
	}
	
	
	@RequestMapping("/deleteDepartmentById")
	public @ResponseBody Result deleteDepartmentById(String department_id){	
		Result result = new Result();
		
		try{
			if(departmentMapper.deleteDepartmentById(department_id)>0){
				result.setRes(true);
			}else{ 
				result.setRes(false);
			}
				
		}catch(Exception e){
			result.setRes(false);
			result.setMsg("删除出现异常："+e.getMessage());
		}
		
		return result;
	}
	
	
	@RequestMapping("/insertDepartment")
	public @ResponseBody Result insertDepartment(U_department department, String rolelt, String privileges){
		
		Result result = new Result();
		if(departmentMapper.existDepartmentByCode(department.getDepartment_code()) != null){
			result.setMsg("部门编号已存在！");
			return result;
		}
		
		
		
		int did = 0;
		Object obj = entityDao.getMaxId("DEPARTMENT_ID", "U_DEPARTMENT");
		if(obj == null) did = 1;
		else did = Integer.valueOf(String.valueOf(obj)) + 1;	
		department.setDepartment_id(String.valueOf(did));

		
	/*
		try{
			if(departmentMapper.insertDepartment(department) > 0) result.addResult(true);
			else {
				result.addResult(false);
				result.setMsg("插入失败！");
				return result;
			}
			boolean bn = true;
			
			for(String tmp: rolelt.split(",")){
				if("".equals(tmp)) continue;
				String sql = "insert into U_DEPARTMENT_ROLE values('"+ did +"','"+ tmp +"')";
				paramMap = new HashMap<String, String>();
				paramMap.put("sql_text", sql);
				if(commonMapper.insertEntity(paramMap) != 1){
					bn = false;
				}
			}
			
			
			for(String tmp: privileges.split(",")){
				if("".equals(tmp)) continue;
				String sql = "insert into U_DEPARTMENT_PRIVILIGE values('"+ did +"','"+ tmp +"')";
				paramMap = new HashMap<String, String>();
				paramMap.put("sql_text", sql);
				if(commonMapper.insertEntity(paramMap) != 1){
					bn = false;
				}
			}
			result.addResult(bn);
		}catch(Exception e){
			DBUtil.saveException(e);
			e.printStackTrace();
			result.addResult(false);
			result.setMsg("插入出现异常："+e.getMessage());
			return result;
		}
	*/
		return result;
	}
	
	
	@RequestMapping("/updateDepartment")
	public @ResponseBody Result updateDepartment(U_department department, String rolelt, String privileges){
		
		List<String> paramRoles = new ArrayList<String>();
		List<String> paramPrivileges = new ArrayList<String>();
		for(String tmp:rolelt.split(","))	if(!"".equals(tmp)) paramRoles.add(tmp);
		for(String tmp:privileges.split(","))	if(!"".equals(tmp)) paramPrivileges.add(tmp);
		
		Result result = new Result();
		if(department == null || department.getDepartment_id() == null){
			result.setMsg("缺少参数");
			return result;
		}
	
		
	/*
		try{
			if(departmentMapper.updateDepartment(department) > 0) result.addResult(true);
			else{
				result.addResult(false);
				result.setMsg("插入失败！");
				return result;
			}
			boolean bn = true;
			
			Map<String, String> paramMap;
			String sql;
			
			
			for(U_role role: departmentMapper.getRoleByDepartmentId(department.getDepartment_id())){
				if(!paramRoles.contains(role.getRole_id())){
					sql = "delete from U_DEPARTMENT_ROLE where department_id='"+ department.getDepartment_id() +"' and role_id='"+ role.getRole_id() +"'";
					paramMap = new HashMap<String, String>();
					paramMap.put("sql_text", sql);
					if(commonMapper.deleteEntity(paramMap) != 1){
						bn = false;
					}
				}else{
					paramRoles.remove(role.getRole_id());
				}
			}
			
			for(String tmp: paramRoles){
				if("".equals(tmp)) continue;
				sql = "insert into U_DEPARTMENT_ROLE values('"+ department.getDepartment_id() +"','"+ tmp +"')";
				paramMap = new HashMap<String, String>();
				paramMap.put("sql_text", sql);
				if(commonMapper.insertEntity(paramMap) != 1){
					bn = false;
				}
			}
			
			
			for(U_privilege privi: privilegeMapper.getPrivilegeByDepartment(department.getDepartment_id())){
				if(!paramPrivileges.contains(privi.getPrivilege_id())){
					sql = "delete from U_DEPARTMENT_PRIVILIGE where department_id='"+ department.getDepartment_id() +"' and privilege_id='"+ privi.getPrivilege_id() +"'";
					paramMap = new HashMap<String, String>();
					paramMap.put("sql_text", sql);
					if(commonMapper.deleteEntity(paramMap) != 1){
						bn = false;
					}
				}else{
					paramPrivileges.remove(privi.getPrivilege_id());
				}
			}
			
			
			for(String tmp: paramPrivileges){
				if("".equals(tmp)) continue;
				sql = "insert into U_DEPARTMENT_PRIVILIGE values('"+ department.getDepartment_id() +"','"+ tmp +"')";
				paramMap = new HashMap<String, String>();
				paramMap.put("sql_text", sql);
				if(commonMapper.insertEntity(paramMap) != 1){
					bn = false;
				}
			}
			result.addResult(bn);
		}catch(Exception e){
			DBUtil.saveException(e);
			e.printStackTrace();
			result.addResult(false);
			result.setMsg("修改出现异常："+e.getMessage());
			return result;
		}
	*/
		return result;
	}
	
	@RequestMapping("/getRoleById")
	public @ResponseBody U_role getRoleById(String role_id){
		return roleMapper.getRoleById(role_id);
	}
	
	@RequestMapping("/deleteRoleById")
	public @ResponseBody Result deleteRoleById(String role_id){
		Result result = new Result();
		
		try{
			if(roleMapper.deleteRoleById(role_id)>0)
				result.setRes(true);
			else
				result.setRes(false);
		}catch(Exception e){
			result.setMsg("删除出现异常："+e.getMessage());
			return result;
		}
		
		return result;
	}
	
	
	
	@RequestMapping("/getRoleData")
	public @ResponseBody List<U_role> getRoleData(){
		
		List<U_role> roles = roleMapper.getAllRole();
		for(U_role role: roles){
			role.setPrivileges(roleMapper.getAllAuthorityByRoleId(role.getRole_id()));
		}
		
		return roles;
	}
	
	@RequestMapping("/getUserRoleData")
	public @ResponseBody List<List<U_role>> getUserRoleData(String user_id){
		List<List<U_role>> result = new ArrayList<List<U_role>>();
		List<U_role> userRole = userMapper.getUserRoleByUserId(user_id);
		List<U_role> unRole = new ArrayList<U_role>();
		for(U_role tmp: roleMapper.getAllRole()){
			boolean exist = false;
			for(U_role up: userRole){
				if(up.getRole_id().equals(tmp.getRole_id()))
					exist = true;
			}
			if(!exist){
				unRole.add(tmp);
			}
		}
		result.add(userRole);
		result.add(unRole);
		return result;
	}
	
	
	
	
	@RequestMapping("/getDepartmentRoleData")
	public @ResponseBody List<List<U_role>> getDepartmentRoleData(String department_id){
		List<List<U_role>> result = new ArrayList<List<U_role>>();
		List<U_role> departmentRole = departmentMapper.getRoleByDepartmentId(department_id);
		List<U_role> unRole = new ArrayList<U_role>();
		for(U_role tmp: roleMapper.getAllRole()){
			boolean exist = false;
			for(U_role up: departmentRole){
				if(up.getRole_id().equals(tmp.getRole_id()))
					exist = true;
			}
			if(!exist){
				unRole.add(tmp);
			}
		}
		result.add(departmentRole);
		result.add(unRole);
		return result;
	}
	
	
	@RequestMapping("/insertRole")
	public @ResponseBody Result insertRole(U_role role, String[] privilegeIds){
		
		Result result = new Result();
		
		int rid =  entityDao.getMaxId("ROLE_ID", "U_ROLE");
		role.setRole_id(String.valueOf(rid));
		roleMapper.insertRole(role);
		
		if(privilegeIds != null && privilegeIds.length>0){
			for(String tmp: privilegeIds){
				if("".equals(tmp)) continue;
				String sql = "insert into U_ROLE_PRIVILIGE values('"+ rid +"','"+ tmp +"')";
				entityDao.insert(sql);
			}
		}
		
		
		result.setRes(true);
		return result;
	}
	
	
	@RequestMapping("/updateRole")
	public @ResponseBody Result updateRole(U_role role, String[] privilegeIds) throws Exception{
	
		Result result = new Result();
		if(role == null || role.getRole_id() == null){
			result.setRes(false);
			result.setMsg("缺少参数");
			return result;
		}
	
		roleMapper.updateRole(role);
		
		if(privilegeIds != null && privilegeIds.length>0){
			List<String> paramPrivileges = new ArrayList<String>();
			Collections.addAll(paramPrivileges, privilegeIds);
			String sql;
			
			
			for(U_privilege privi: roleMapper.getAllAuthorityByRoleId(role.getRole_id())){
				if(!paramPrivileges.contains(privi.getPrivilege_id())){
					sql = "delete from U_ROLE_PRIVILIGE where role_id='"+ role.getRole_id() +"' and privilege_id='"+ privi.getPrivilege_id() +"'";
					entityDao.delete(sql);
				}else{
					paramPrivileges.remove(privi.getPrivilege_id());
				}
			}
			
			
			for(String tmp: paramPrivileges){
				if("".equals(tmp)) continue;
				sql = "insert into U_ROLE_PRIVILIGE values('"+ role.getRole_id() +"','"+ tmp +"')";
				entityDao.insert(sql);
			}
		}
		result.setRes(true);
		return result;
	}
	
	
	@RequestMapping("/getPrivilegeData")
	public @ResponseBody List<U_privilege> getPrivilegeData(){
		
		return privilegeMapper.getAllPrivilege();
	}
	
	
	@RequestMapping("/getUserPrivilegeData")
	public @ResponseBody List<List<U_privilege>> getUserPrivilegeData(String user_id){
		
		List<List<U_privilege>> result = new ArrayList<List<U_privilege>>();
		List<U_privilege> userPrivi = privilegeMapper.getPrivilegeByUser(user_id);
		List<U_privilege> unPrivi = new ArrayList<U_privilege>();
		for(U_privilege tmp: privilegeMapper.getAllPrivilege()){
			boolean exist = false;
			for(U_privilege up: userPrivi){
				if(up.getPrivilege_id().equals(tmp.getPrivilege_id()))
					exist = true;
			}
			if(!exist){
				unPrivi.add(tmp);
			}
		}
		result.add(userPrivi);
		result.add(unPrivi);
		return result;
	}
	
	
	@RequestMapping("/getDepartmentPrivilegeData")
	public @ResponseBody List<List<U_privilege>> getDepartmentPrivilegeData(String department_id){
		
		List<List<U_privilege>> result = new ArrayList<List<U_privilege>>();
		List<U_privilege> departmentPrivi = privilegeMapper.getPrivilegeByDepartment(department_id);
		List<U_privilege> unPrivi = new ArrayList<U_privilege>();
		for(U_privilege tmp: privilegeMapper.getAllPrivilege()){
			boolean exist = false;
			for(U_privilege up: departmentPrivi){
				if(up.getPrivilege_id().equals(tmp.getPrivilege_id()))
					exist = true;
			}
			if(!exist){
				unPrivi.add(tmp);
			}
		}
		result.add(departmentPrivi);
		result.add(unPrivi);
		return result;
	}
	
	@RequestMapping("/getRolePrivilegeData")
	public @ResponseBody List<List<U_privilege>> getRolePrivilegeData(String role_id){
		
		List<List<U_privilege>> result = new ArrayList<List<U_privilege>>();
		List<U_privilege> departmentPrivi = roleMapper.getAllAuthorityByRoleId(role_id);
		List<U_privilege> unPrivi = new ArrayList<U_privilege>();
		for(U_privilege tmp: privilegeMapper.getAllPrivilege()){
			boolean exist = false;
			for(U_privilege up: departmentPrivi){
				if(up.getPrivilege_id().equals(tmp.getPrivilege_id()))
					exist = true;
			}
			if(!exist){
				unPrivi.add(tmp);
			}
		}
		result.add(departmentPrivi);
		result.add(unPrivi);
		return result;
	}
	
	

	
}
