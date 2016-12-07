package com.util;

import java.io.Serializable;
import java.util.List;

 @SuppressWarnings("serial")
public class Node implements Serializable{
	private String id;
	private String text;
	private String state;
	private String data;
	private String icon;
	private List<Node> children;
	
	public Node(String id, String text, String data, List<Node> children){
		this.id = id;
		this.text = text;
		this.children = children;
		this.data = data;
		this.state = "open";
	}

	
	public String getData() {
		return data;
	}


	public void setData(String data) {
		this.data = data;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getIcon() {
		return icon;
	}


	public void setIcon(String icon) {
		this.icon = icon;
	}


	public List<Node> getChildren() {
		return children;
	}

	public void setChildren(List<Node> children) {
		this.children = children;
	}
	
	
}
