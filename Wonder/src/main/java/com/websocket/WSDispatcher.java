package com.websocket;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import net.sf.json.JSONObject;
import org.springframework.web.method.HandlerMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.socket.WebSocketSession;


@Component
public class WSDispatcher implements ApplicationListener<ContextRefreshedEvent>{

	@Autowired  
	private RequestMappingHandlerMapping requestMappingHandlerMapping;
	
	private static Map<Object, HandlerMethod> webSocketMapping = new HashMap<Object, HandlerMethod>();
	
	
	public HandlerMethod getHandlerMethod(String url){
		return webSocketMapping.get(url);
	}

	private void init(){
		webSocketMapping.clear();
		
		Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();
		for (Map.Entry<RequestMappingInfo, HandlerMethod> m : map.entrySet()) {
			RequestMappingInfo info = m.getKey();  
            HandlerMethod method = m.getValue();
            //无WSController注解的过滤掉
            if(method.getBeanType().getDeclaredAnnotationsByType(WSController.class).length == 0)	
            	continue;
            Set<String> patterns = info.getPatternsCondition().getPatterns();
            if(patterns.size()>0){
            	System.out.println(patterns.toArray()[0]);
            	webSocketMapping.put(patterns.toArray()[0], method);
            }
		}
	}

	/*
	 * 初始化WSController的映射路径
	 */
	public void onApplicationEvent(ContextRefreshedEvent event) {
		// TODO Auto-generated method stub
		if(event.getApplicationContext().getParent() == null){
			init();
		}
			
	}
	

	/*
	 * 接口分发
	 * 注意：参数只匹配第一项
	 */
	public Object dispatch(String url, Object parameter, WebSocketSession session){
		HandlerMethod method = getHandlerMethod(url);		
		if(method != null){
			try{
				Class<?> cls = method.getMethod().getDeclaringClass();
				
				Object[] args = new Object[method.getMethod().getParameterCount()];
				Class<?>[] argTypes = method.getMethod().getParameterTypes();
				for(int i=0; i<args.length; i++){
					if(argTypes[i].equals(Map.class))	args[i] = parameter;
					else if(argTypes[i].equals(WebSocketSession.class))	args[i] = session;
				}
				if(args.length == 0)
					return method.getMethod().invoke(cls.newInstance());
				else
					return method.getMethod().invoke(cls.newInstance(), args);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return null;
	}

	/*
	 * 接口分发
	 * 注意：json参数必须包含url和params
	 */
	public JSONObject dispatch(JSONObject json, WebSocketSession session){
		Object response = dispatch(String.valueOf(json.get("url")), json.get("params"), session);
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("data", response);
		return jsonObj;
	}
}
