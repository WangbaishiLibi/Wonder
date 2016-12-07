package com.dao;

import java.util.List;
import java.util.Map;


public interface IBaseDao {
	public int count(Map<String, ?> map);
	public List<Object> selectEntity(Map<String, ?> map);
	public int insertEntity(Map<String, ?> map);
	public int updateEntity(Map<String, ?> map);
	public int deleteEntity(Map<String, ?> map);
}
