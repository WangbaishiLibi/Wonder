package com.entity;

import java.util.Date;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@PersistenceCapable(table="purchase")
public class Purchase {

	
	@PrimaryKey
    @Persistent(valueStrategy=IdGeneratorStrategy.NATIVE)
    protected long id;
	
	@OneToOne
	@JoinColumn(name="userid")
	private User user;
	
	
	@OneToOne
	@JoinColumn(name="ticketid")
	private Ticket ticket;
	
	
	@Column(name="purchaseTime")
	private Date purchaseTime;

	public Purchase() {
		// TODO Auto-generated constructor stub
	}
	
	public Purchase(User user, Ticket ticket, Date date) {
		// TODO Auto-generated constructor stub
		this.user = user;
		this.ticket = ticket;
		this.purchaseTime = date;
	}
	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}

	public Date getPurchaseTime() {
		return purchaseTime;
	}

	public void setPurchaseTime(Date purchaseTime) {
		this.purchaseTime = purchaseTime;
	}

	

	
	
	
	
	
}
