package com.model.authority;

public class D_window_time_info {

	/******* primary key */
	private int section_id;	
	private int window_time;//以分钟计
	private String start_station;
	private String end_station;
	/**end** primary key */
	
	private int ruler_level;
	private String start_time;	
	private char window_type;
	//时间均以分钟计
	private String start_station_start_time;
	private String start_station_end_time;
	private String end_station_start_time;
	private String end_station_end_time;
	private String section_start_time;
	private String section_end_time;
	
	
	
	public int getRuler_level() {
		return ruler_level;
	}
	public void setRuler_level(int ruler_level) {
		this.ruler_level = ruler_level;
	}
	public int getSection_id() {
		return section_id;
	}
	public void setSection_id(int section_id) {
		this.section_id = section_id;
	}
	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getStart_station() {
		return start_station;
	}
	public void setStart_station(String start_station) {
		this.start_station = start_station;
	}
	public String getEnd_station() {
		return end_station;
	}
	public void setEnd_station(String end_station) {
		this.end_station = end_station;
	}
	public int getWindow_time() {
		return window_time;
	}
	public void setWindow_time(int window_time) {
		this.window_time = window_time;
	}
	public char getWindow_type() {
		return window_type;
	}
	public void setWindow_type(char window_type) {
		this.window_type = window_type;
	}
	public String getStart_station_start_time() {
		return start_station_start_time;
	}
	public void setStart_station_start_time(String start_station_start_time) {
		this.start_station_start_time = start_station_start_time;
	}
	public String getStart_station_end_time() {
		return start_station_end_time;
	}
	public void setStart_station_end_time(String start_station_end_time) {
		this.start_station_end_time = start_station_end_time;
	}
	public String getEnd_station_start_time() {
		return end_station_start_time;
	}
	public void setEnd_station_start_time(String end_station_start_time) {
		this.end_station_start_time = end_station_start_time;
	}
	public String getEnd_station_end_time() {
		return end_station_end_time;
	}
	public void setEnd_station_end_time(String end_station_end_time) {
		this.end_station_end_time = end_station_end_time;
	}
	public String getSection_start_time() {
		return section_start_time;
	}
	public void setSection_start_time(String section_start_time) {
		this.section_start_time = section_start_time;
	}
	public String getSection_end_time() {
		return section_end_time;
	}
	public void setSection_end_time(String section_end_time) {
		this.section_end_time = section_end_time;
	}
	
	
}
