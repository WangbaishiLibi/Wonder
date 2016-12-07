package com.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.util.Page;
import com.util.StringUtil;

@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class EntityDao extends BatisBaseDao{
	/*
	 * 处理常规基础数据的CRUD
	 * 主要用于构造sql
	 */
	
	
	/*
	 * oracle 11g环境下
	 */
	public static final String tabCols = "user_tab_columns";	//表列信息
	public static final String tabCons = "user_cons_columns";	//表关键字列信息
	public static final String cons = "user_constraints";		//表关键字信息
	
	/*
	 * 表列信息（列名、数据类型、注释、键类型）
	 */
	public List<Map> getColumnInfo(String tbname) throws Exception {
//		String sql = "select utc.column_name, utc.data_type, ucc.comments from user_tab_columns utc, user_col_comments ucc " + 
//			" where utc.column_name = ucc.column_name and utc.table_name = '" + tbname + "'";
		String sql = "select a.column_name, a.data_type, a.comments, b.constraint_type from " +  
			" (select utc.column_name, utc.data_type, ucc.comments from " + tabCols + " utc, user_col_comments ucc " + 
			" where utc.column_name = ucc.column_name and utc.table_name = ucc.table_name and utc.table_name = '" + tbname + "') a left join " +   
			" (select ucc.column_name, uc.constraint_type from " + cons + " uc, " + tabCons + " ucc " + 
			     " WHERE (uc.constraint_type = 'R' or uc.constraint_type = 'P') " + 
			     " and uc.constraint_name = ucc.constraint_name  and  uc.table_name = '" + tbname + "') b " +
			" on a.column_name = b.column_name";
		return this.queryForList(sql);
	}
	
	/*
	 * 表关键列（主键和外键）信息（列名、键类型）
	 */
	public List<Map> getKeyColumnInfo(String tbname) throws Exception {
		String sql = "select ucc.column_name, uc.constraint_type from " + cons + " uc, " + tabCons + " ucc " +
			" WHERE (uc.constraint_type = 'R' or uc.constraint_type = 'P') " + 
			" and uc.constraint_name = ucc.constraint_name  and  uc.table_name = '" + tbname + "'";
		return this.queryForList(sql);
	}
	
	public Object queryOne(Map map) throws Exception {
		if(!checkConstraints(map))	return new Object();
		String sql = "select * from " + map.get("tbname")+ " where ";
		map.remove("tbname");
		List keyPair = new ArrayList();
		for(Object key : map.keySet().toArray()){
			keyPair.add(key + "='" + map.get(key)+"'");
		}
		
		sql += keyPair.toString().replaceAll("\\[|\\]", "").replaceAll(",", " and ");
		return this.queryForOne(sql);
	}
	
	public Object queryOne(String tbname, String where) throws Exception {
		String sql = "select * from " + tbname + " where " + where;
		return this.queryForOne(sql);
	}
	
	
	public List<Map> queryList(String tbname) throws Exception {
		String sql = "select * from " + tbname;
		return this.queryForList(sql);
	}
	
	
	public List<Object> queryList(String tbname, Page page) throws Exception {
		String sql = "select * from " + tbname;
		return this.queryForPage(sql, page);
	}
	
	

	public int insertEntity(Map map) throws Exception {
		if(!checkConstraints(map))	return -1;
		
		String tbname = (String) map.get("tbname");
		map.remove("tbname");
		String sql = "insert into " + tbname + "(";
		sql += map.keySet().toString().replaceAll("\\[|\\]", "") + ") values(";
		List values = new ArrayList();
		for(Object key : map.keySet().toArray()){
			values.add("'" + map.get(key) + "'");
		}
		sql += values.toString().replaceAll("\\[|\\]", "") + ")";
		return this.insert(sql);
	}
	
	
	public int updateEntity(Map map) throws Exception {
		if(!checkConstraints(map))	return -1;
		
		String tbname = (String) map.get("tbname");
		map.remove("tbname");
		
		List<Object> key_cols = new ArrayList();;
		for(Map obj : getKeyColumnInfo(tbname)){
			key_cols.add(obj.get("COLUMN_NAME"));
		}
		List keyPair = new ArrayList();
		List commonPair = new ArrayList();
		for(Object key : map.keySet().toArray()){
			String val = key + "='" + map.get(key) + "'";
			if(key_cols.contains(key)){
				keyPair.add(val);
			}else{
				commonPair.add(val);
			}
		}
		String sql = "update " + tbname + " set ";
		sql += commonPair.toString().replaceAll("\\[|\\]", "").replaceAll(",", " and ");
		sql += " where " + keyPair.toString().replaceAll("\\[|\\]", "").replaceAll(",", " and ");
		return this.update(sql);
	}
	
	
	public int deleteEntity(Map map) throws Exception {
		if(!checkConstraints(map))	return -1;
		
		String tbname = (String) map.get("tbname");
		map.remove("tbname");
		
		List keyPair = new ArrayList();
		for(Map obj : getKeyColumnInfo(tbname)){
			Object key = obj.get("COLUMN_NAME");
			keyPair.add(key + "='" + map.get(key) + "'");
		}
		
		String sql = "delete from " + tbname + " where ";
		sql += keyPair.toString().replaceAll("\\[|\\]", "").replaceAll(",", " and ");
		return this.delete(sql);
		
	}
	
	
	/*
	 *	验证是否包含关键字段 
	 */
	private boolean checkConstraints(Map map) throws Exception{		
		String tbname = (String) map.get("tbname");
		if(StringUtil.isEmpty(tbname))	return false;
		
		List<Map> key_cols = getKeyColumnInfo(tbname);
		for(Map obj : key_cols){
			if(map.get(obj.get("COLUMN_NAME")) == null)	return false;			
		}
		
		return true;
	}

}
