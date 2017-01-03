package com.entity;

import java.util.Date;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(table="Ticket")
public class Ticket {

	@PrimaryKey  
    @Column(name="ticketid")
	@Persistent(valueStrategy=IdGeneratorStrategy.NATIVE)
    protected long id;
	

	@Column(name="film")
	private String film;
	
	
	@Column(name="date")
	private Date releaseTime;
	
	
	@Column(name="status")
	private String status;


	public Ticket() {
		// TODO Auto-generated constructor stub
	}
	
	public Ticket(String film, Date date, String status) {
		// TODO Auto-generated constructor stub
		this.film = film;
		this.releaseTime = date;
		this.status = status;
	}
	
	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getFilm() {
		return film;
	}


	public void setFilm(String film) {
		this.film = film;
	}


	public Date getReleaseTime() {
		return releaseTime;
	}


	public void setReleaseTime(Date releaseTime) {
		this.releaseTime = releaseTime;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
	
}
