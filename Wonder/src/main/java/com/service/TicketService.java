package com.service;

import java.util.Date;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dao.JPABaseDao;
import com.entity.Purchase;
import com.entity.Ticket;
import com.entity.User;
import com.util.Page;

@Service
public class TicketService {
	
	@Autowired
	private JPABaseDao jpa;
	
	
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
		PersistenceManager pm = jpa.getPM();
		User user = pm.getObjectById(User.class, uid);
		Ticket ticket = pm.getObjectById(Ticket.class, ticketid);
		
		if(!"待售".equals(ticket.getStatus())){
			jpa.commit(tx);
			return false;
		}
		ticket.setStatus("已售");	
		
		Purchase purchase = new Purchase(user, ticket, new Date());		
		pm.makePersistent(purchase);
		return jpa.commit(tx);
	}
	
	
	
}
