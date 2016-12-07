package com.controller;


import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.service.EntityService;
import com.util.Page;
import com.util.Result;

@Controller
@RequestMapping("/table")
public class EntityHandler {
    /**
     *  订单处理
     *  order 1 GET 获取订单号为1的订单
     *  order 1 DELETE 删除订单号为1的订单
     *  order 1 PUT 更新订单号为1的订单
     *  order   POST 新增订单
     * RestFul 利用Http协议中的多种请求方式实现状态转换
     * 通过判断提交的请求类型来选择执行不同的处理请求的方法
     * {路径，请求参数}只能表示一层路径，必须得有
     * 利用@PathVariable注解从url中获取值给参数赋值
     */
	
	@Autowired
	private EntityService entityService;

	
	
	@RequestMapping(value="/entityInfo/{tbname}")
	public @ResponseBody Object entityInfo(@PathVariable("tbname")String tbname) throws Exception{
		return entityService.getColumnInfo(tbname);
	}
	
	@RequestMapping(value="/entityList/{tbname}")
	public @ResponseBody Object entityList(@PathVariable("tbname")String tbname, Page page) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		if(page == null){
			map.put("data", entityService.queryList(tbname));
		}else{
			map.put("data", entityService.queryList(tbname, page));
			map.put("page", page);
		}
		return map;
	}
	
    @RequestMapping(value="/entity",method=RequestMethod.GET)
    public @ResponseBody Object getEntity(HttpServletRequest request) {
        return this.entityService.queryByKey(request.getParameterMap());
    }
    @RequestMapping(value="/entity",method=RequestMethod.POST)
    public @ResponseBody Result saveEntity(HttpServletRequest request) {
        if(this.entityService.insertEntity(request.getParameterMap()) > 0){
        	return new Result(true);
		} 
		return new Result(false);
    }
    @RequestMapping(value="/entity",method=RequestMethod.PUT)
    public @ResponseBody Result updateEntity(HttpServletRequest request) {
    	if(this.entityService.updateEntity(request.getParameterMap()) > 0){
        	return new Result(true);
		} 
		return new Result(false);
    }
    @RequestMapping(value="/entity/delete")
    public @ResponseBody Result deleteEntity(HttpServletRequest request) {
    	if(this.entityService.deleteEntity(request.getParameterMap()) > 0){
        	return new Result(true);
		} 
		return new Result(false);
    }
}
