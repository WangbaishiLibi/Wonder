package com.util;

public class Result {

	private boolean res;
	private String msg;
	private Object info;
	
	public Result() {
		// TODO Auto-generated constructor stub	
	}
	
	public Result(boolean res) {
		// TODO Auto-generated constructor stub
		this.res = res;
	}
	
	public Result(boolean res, String msg) {
		// TODO Auto-generated constructor stub
		this.res = res;
		this.msg = msg;
	}

	public boolean isRes() {
		return res;
	}

	public void setRes(boolean res) {
		this.res = res;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getInfo() {
		return info;
	}

	public void setInfo(Object info) {
		this.info = info;
	}
	
	
}
