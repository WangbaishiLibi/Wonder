package com.model.authority;

public class D_ruler_info {

	/******* primary key */
	private String line_code;
	private String start_station_code;
	private String end_station_code;
	private int ruler_level;
	/**end** primary key */
		
	private int consuming_time;
	
	
	public String getLine_code() {
		return line_code;
	}
	public void setLine_code(String line_code) {
		this.line_code = line_code;
	}
	public String getStart_station_code() {
		return start_station_code;
	}
	public void setStart_station_code(String start_station_code) {
		this.start_station_code = start_station_code;
	}
	public String getEnd_station_code() {
		return end_station_code;
	}
	public void setEnd_station_code(String end_station_code) {
		this.end_station_code = end_station_code;
	}
	public int getRuler_level() {
		return ruler_level;
	}
	public void setRuler_level(int ruler_level) {
		this.ruler_level = ruler_level;
	}
	public int getConsuming_time() {
		return consuming_time;
	}
	public void setConsuming_time(int consuming_time) {
		this.consuming_time = consuming_time;
	}
	
	
}
