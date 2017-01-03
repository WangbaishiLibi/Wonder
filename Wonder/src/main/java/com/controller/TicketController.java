package com.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.auth.Permission;
import com.auth.PermissionType;
import com.service.TicketService;
import com.util.Page;

@Controller
@RequestMapping("/ticket")
public class TicketController {

	
	@Autowired
	private TicketService ticketService;
	
	@Permission(authType=PermissionType.ANON)
	@RequestMapping("/allTickets")
	public @ResponseBody Object allTickets(Page page){
		Map<String, Object> map = new HashMap<String, Object>();
		if(page == null){
			map.put("data", ticketService.getAllTicket());
		}else{
			map.put("data", ticketService.getAllTicket(page));
			map.put("page", page);
		}
		return map;
	}
	
	
	@RequestMapping("/purchase")
	public @ResponseBody Object purchase(long ticketid){
		return ticketService.purchaseTicket(1, ticketid);
	}
	
	
}
