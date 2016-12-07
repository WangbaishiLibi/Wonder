package com.websocket;


import java.util.regex.Pattern;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class TWSHandler extends TextWebSocketHandler{

	private final String charset = "UTF-8";
	
	@Autowired
	private WSDispacher wsDispacher;
	

	@Override
	public void handleTextMessage(WebSocketSession session,
			TextMessage message) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("===========>handlerText");
		String msg = message.getPayload();
		Object result = null;
		

		Object stamp = null;	//时间戳，用来标识返回结果
		Pattern pattern = Pattern.compile("^\\{(\"\\w+\":\\S+,{0,1})+\\}$");
		if(pattern.matcher(msg).matches()){
			JSONObject json = JSONObject.fromObject(message.getPayload());
			stamp = json.get("stamp");
			result = wsDispacher.dispatch(json);
		}
		
		
		if(result == null)	result = "404";
		if(stamp != null){
			result = String.valueOf(result) + "?" + stamp;
		}
		session.sendMessage(new TextMessage(String.valueOf(result).getBytes(charset)));		

	}
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session)
			throws Exception {
		// TODO Auto-generated method stub
		super.afterConnectionEstablished(session);
	}
	

	
}
