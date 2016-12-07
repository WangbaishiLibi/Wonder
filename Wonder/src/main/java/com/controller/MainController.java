package com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dao.EntityDao;
import com.dao.HiberBaseDao;
import com.util.LogUtil;

@Controller
@RequestMapping("/main")
public class MainController{

	@Autowired
	protected HiberBaseDao hiberBaseDao;
	@Autowired
	private EntityDao entityDao;
	@Autowired
	private LogUtil logUtil;
	
	public MainController() {
		// TODO Auto-generated constructor stub
	
	}
	
	
	@RequestMapping("/index.do")
	public void index(String name)  throws Exception{
		System.out.println("index");
		if(32>0) System.out.println(name.toString());
	}
	

	@RequestMapping("/test.do")
	public String test(String name) {
		System.out.println("test.do==>");
	//	String sql = "insert into TEST(TID, TNAME) values('1', 'vwf')";
		String sql = "insert into TEST(TID, TNAME) values('1', 'vwf')";
		try{
			hiberBaseDao.excuteBySql(sql);
			sql = "select * from TEST";
			System.out.println(hiberBaseDao.query(sql));
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return "index";
	}
	
}
