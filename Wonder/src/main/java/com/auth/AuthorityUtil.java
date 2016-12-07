package com.auth;


import java.util.Hashtable;
import java.util.Map;


public class AuthorityUtil {
/*
 * 存储用户的角色权限信息
 * 建议登录时存入，登出时删除
 */
	
	private static AuthorityUtil authorityUtil;
	
	//hashtable具有并发能力
	private Map<String, AuthToken> userTokens = new Hashtable<String, AuthToken>();
	
	private AuthorityUtil() {
		// TODO Auto-generated constructor stub
	}
	
	public static AuthorityUtil instance(){
		if(authorityUtil == null)	authorityUtil = new AuthorityUtil();
		return authorityUtil;
	}
	
	
	public void login(AuthToken token){
		this.userTokens.put(token.getTokenId(), token);
	}
	
	
	public void logout(AuthToken token){
		this.logout(token.getTokenId());
	}
	
	
	public void logout(String tokenId){
		this.userTokens.remove(tokenId);
	}
	
	
	public AuthToken getToken(String tokenId){
		return this.userTokens.get(tokenId);
	}
	
}
