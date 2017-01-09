package com.websocket.controller;


import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.socket.WebSocketSession;

import com.entity.User;
import com.service.TicketService;
import com.websocket.WSController;
import com.websocket.WSServer;

@SuppressWarnings("rawtypes")
@Component
@WSController()
@RequestMapping("/msg")
public class MessageController {

	
	@Autowired
	private TicketService ticketService;
	





	@RequestMapping("/countTicket")
	public Object countTicket(){
		System.out.println(ticketService);
		return ticketService.countTicket();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@RequestMapping("/hello")
	public String hello(Map map, WebSocketSession session){
		System.out.println(session.getId());
		System.out.println("msg-----hello---------" + map);
		return "from hello";
	}
	
	
	@RequestMapping("/login")
	public String login(Map map, WebSocketSession session){
		System.out.println("msg-----login---------" + map);
		WSServer.instance().connect(map.get("uid"), session);
		return "from login";
	}
	
	/*
	 * 广播给所有用户
	 */
	@RequestMapping("/broadcast")
	public void broadcast(){
		System.out.println("msg-----broadcast---------");
		try {
			WSServer.instance().broadcast(new User());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
