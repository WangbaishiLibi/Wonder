package com.service;

import java.util.Date;

import javax.jdo.Transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;

import com.dao.JPABaseDao;
import com.entity.Purchase;
import com.entity.Ticket;
import com.entity.User;
import com.util.Page;

@Service
@Configurable
public class TicketService {
	
	@Autowired
	private JPABaseDao jpa = new JPABaseDao();
	
	
	public Object getAllTicket(){
		return jpa.query(Ticket.class);
	}
	
	public Object getAllTicket(Page page){
		return jpa.queryByPage(Ticket.class, page);
	}
	
	
	public Object countTicket(){
		return jpa.count(Ticket.class , " where status=='待售'");
	}
	
	
	
	public synchronized boolean purchaseTicket(long uid, long ticketid){
		Transaction tx = jpa.begin();
		User user = jpa.getPM().getObjectById(User.class, uid);
		Ticket ticket = jpa.getPM().getObjectById(Ticket.class, ticketid);
		
		if(!"待售".equals(ticket.getStatus()))	
			return false;
		
		Purchase purchase = new Purchase(user, ticket, new Date());
		ticket.setStatus("已售");
		jpa.getPM().makePersistent(purchase);
		return jpa.commit(tx);
	}
	
	
	
}
