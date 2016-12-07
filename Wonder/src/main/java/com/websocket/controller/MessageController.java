package com.websocket.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;

import com.websocket.WSController;

@SuppressWarnings("rawtypes")
@WSController()
@RequestMapping("/msg")
public class MessageController {

	@RequestMapping("/hello")
	public String hello(Map map){
		System.out.println("msg-----hello---------" + map);
		return "from hello";
	}
	
}
