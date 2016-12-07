package com.model.authority;

/*
 * 通知消息
 */
public class D_notice {

	private int notice_id;
	private String notice_category;
	private String notify_obj_id;
	private String notify_obj_type;
	private String mark;
	private String message;
	private String update_time;
	private String period;
	private String isread;
	
	public D_notice() {
		// TODO Auto-generated constructor stub
	}

	public int getNotice_id() {
		return notice_id;
	}

	public void setNotice_id(int notice_id) {
		this.notice_id = notice_id;
	}

	public String getNotice_category() {
		return notice_category;
	}

	public void setNotice_category(String notice_category) {
		this.notice_category = notice_category;
	}

	public String getNotify_obj_id() {
		return notify_obj_id;
	}

	public void setNotify_obj_id(String notify_obj_id) {
		this.notify_obj_id = notify_obj_id;
	}

	public String getNotify_obj_type() {
		return notify_obj_type;
	}

	public void setNotify_obj_type(String notify_obj_type) {
		this.notify_obj_type = notify_obj_type;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getIsread() {
		return isread;
	}

	public void setIsread(String isread) {
		this.isread = isread;
	}
	
	
}
