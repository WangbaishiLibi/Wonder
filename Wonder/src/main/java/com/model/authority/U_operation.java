package com.model.authority;

import java.io.Serializable;

@SuppressWarnings("serial")
public class U_operation implements Serializable{

	private String operation_id;
	private String operation_name;
	
	public String getOperation_id() {
		return operation_id;
	}
	public void setOperation_id(String operation_id) {
		this.operation_id = operation_id;
	}
	public String getOperation_name() {
		return operation_name;
	}
	public void setOperation_name(String operation_name) {
		this.operation_name = operation_name;
	}

	
	
}
