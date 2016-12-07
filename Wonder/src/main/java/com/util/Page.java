package com.util;

public class Page {

	private int total;		//总记录数
	private int page_index;	//当前页码(从1开始)
	private int page_total;	//每页记录数
	private int page_count;	//页数  = [总记录数/每页记录数]	
	
	public Page() {
		// TODO Auto-generated constructor stub
		this.page_index = 1;
	}
	
	public Page(int total, int page_total) {
		// TODO Auto-generated constructor stub
		this.total = total;
		this.page_index = 1;
		this.page_total = page_total;
		this.page_count = (int)Math.ceil((float)total/page_total);
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getPage_index() {
		return page_index;
	}

	public void setPage_index(int page_index) {
		this.page_index = page_index;
	}

	public int getPage_total() {
		return page_total;
	}

	public void setPage_total(int page_total) {
		this.page_total = page_total;
	}

	public int getPage_count() {
		return page_count;
	}

	public void setPage_count(int page_count) {
		this.page_count = page_count;
	}
	
	
}
