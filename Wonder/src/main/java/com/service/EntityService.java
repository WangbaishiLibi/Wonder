package com.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dao.EntityDao;
import com.util.Page;

@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class EntityService {

	/*
	 * 处理常规基础数据的服务
	 * 主要用于处理数据
	 */
	
	@Autowired
	private EntityDao entityDao;
	
	public List<Map> getColumnInfo(String tbname) {
		try{
			return entityDao.getColumnInfo(tbname);
		}catch(Exception e){
			e.printStackTrace();
			return new ArrayList();
		}
	}
	
	public Object queryByKey(Map map) {
		try{
			return entityDao.queryOne(translateMap(map));
		}catch(Exception e){
			e.printStackTrace();
			return new Object();
		}
	}
	
	public List<Map> queryList(String tbname) {
		try{
			return entityDao.queryList(tbname);
		}catch(Exception e){
			e.printStackTrace();
			return new ArrayList();
		}
	}
	
	public List<Object> queryList(String tbname, Page page) {
		try{
			return entityDao.queryList(tbname, page);
		}catch(Exception e){
			e.printStackTrace();
			return new ArrayList();
		}
	}
	
	
	public int insertEntity(Map map) {
		try{
			return entityDao.insertEntity(translateMap(map));
		}catch(Exception e){
			e.printStackTrace();
			return -1;
		}
	}
	
	public int updateEntity(Map map) {
		try{
			return entityDao.updateEntity(translateMap(map));
		}catch(Exception e){
			e.printStackTrace();
			return -1;
		}
	}
	
	public int deleteEntity(Map map) {
		try{
			return entityDao.deleteEntity(translateMap(map));
		}catch(Exception e){
			e.printStackTrace();
			return -1;
		}
	}
	
	/*
	 *  map转换
	 */
	private Map translateMap(Map map){	
		HashMap hashMap = new HashMap<String, String>();
		for(Object key : map.keySet().toArray()){
			if(map.get(key) instanceof String[]){
				String[] value = (String[]) map.get(key);
				if(value.length > 0)	hashMap.put((String)key, value[0]);
			}else if(map.get(key) instanceof String){
				hashMap.put((String)key, map.get(key));
			}
		}
		hashMap.remove("_method");
		return hashMap;
	}
	
}
