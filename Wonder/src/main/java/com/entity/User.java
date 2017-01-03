package com.entity;

import java.io.Serializable;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;


@SuppressWarnings("serial")
@PersistenceCapable(table="u_user")
public class User implements Serializable{


	@PrimaryKey  
    @Column(name="userid")
	@Persistent(valueStrategy=IdGeneratorStrategy.NATIVE)
    protected long id;
	
	@Column(name="username")
	private String name;
	
	@Column(name="password")
	private String password;
	
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
