package com.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.util.LogUtil;
import com.util.Page;

@Repository
@SuppressWarnings({"unchecked", "rawtypes"})
public class BatisBaseDao{

	@Autowired
	protected SqlSessionFactory sessionFactory;
	
	
	public Object queryForOne(String sql){
		LogUtil.console("BaseDao(queryForOne)", sql);
		Map map = new HashMap<String, String>();
		map.put("sql", sql);
		return sessionFactory.openSession(true).selectOne("selectEntity", map);
	}

	
	public List<Map> queryForList(String sql){
		LogUtil.console("BaseDao(queryForList)", sql);
		Map map = new HashMap<String, String>();
		map.put("sql", sql);
		return sessionFactory.openSession(true).selectList("selectEntity", map);
	}
	
	/*
	 * 分页封装
	 * 在原有sql查询基础上，多了total(总记录数)和rn(记录序号)
	 * 
	 */
	public List<Object> queryForPage(String sql, Page page){
		LogUtil.console("BaseDao(queryForPage)", sql);
		int min = (page.getPage_index() - 1) * page.getPage_total();
		int max = page.getPage_index() * page.getPage_total();
		sql = "select * from ( select count(1) over(partition by 1) as total, tb.*, rownum as rn from (" + sql + ") tb) " +   
			" where rn>" + min + " and rn<=" + max;	
		Map map = new HashMap<String, String>();
		map.put("sql", sql);
		List<Object> result = sessionFactory.openSession(true).selectList("selectEntity", map);
		//更新总记录数
		if(result.size()>0){
			page.setTotal(Integer.valueOf(((Map)result.get(0)).get("TOTAL").toString()));
			if(page.getPage_total()>0){
				page.setPage_count((int)Math.ceil((float)page.getTotal()/page.getPage_total()));
			}
		}
		return result;
	}
	
	public synchronized int getMaxId(String code_name, String tbname){
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("tb_name", tbname);
		paramMap.put("code_name", code_name);
		Object obj = sessionFactory.openSession(true).selectOne("getMaxId", paramMap);
		if(obj == null)	return 1;
		else{
			return Integer.valueOf(String.valueOf(obj)) + 1;
		}
	}
	
	
	
	public synchronized int insert(String sql){
		LogUtil.console("BaseDao(insert)", sql);
		Map map = new HashMap<String, String>();
		map.put("sql", sql);
		return sessionFactory.openSession(true).insert("insertEntity", map);
	}
	
	public synchronized int update(String sql){
		LogUtil.console("BaseDao(update)", sql);
		Map map = new HashMap<String, String>();
		map.put("sql", sql);
		return sessionFactory.openSession(true).update("updateEntity", map);
	}
	
	public synchronized int delete(String sql){
		LogUtil.console("BaseDao(delete)", sql);
		Map map = new HashMap<String, String>();
		map.put("sql", sql);
		return sessionFactory.openSession(true).delete("deleteEntity", map);
	}
	
}
