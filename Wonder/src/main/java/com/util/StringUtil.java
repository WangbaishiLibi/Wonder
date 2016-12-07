package com.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringUtil {
	
	public static boolean isEmpty(String str){
		if(str == null || "".equals(str) || "null".equals(str))	return true;
		return false;
	}
	
	public static String getCurrentTime(){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		return formatter.format(new Date());
	}
	
	public static String getCurrentTime(String format){
		return new SimpleDateFormat(format).format(new Date());
	}
	
	
	/**
	 * 
	 * @description 此处采用的是md5加密算法 
	 * @param 要加密的字符串
	 * @return 返回经过加密的字符串
	 * @author zhaichang
	 * @date2015年5月24日
	 */
	public static String encryptPassword(String password) {
	    StringBuilder sb = new StringBuilder();
	    MessageDigest md5;
	    try {
	    	md5 = MessageDigest.getInstance("MD5");
	    	md5.update(password.getBytes());
	    	for (byte b : md5.digest()) {
	    		sb.append(String.format("%02X", b)); // 10进制转16进制，X 表示以十六进制形式输出，02 表示不足两位前面补0输出
	    	}
	    	byte tmp[] = sb.toString().getBytes();
			byte rs[] = new byte[16];
			for(int i=0; i<16; i++){
				rs[i] = tmp[i];
			}
			return new String(rs);
	    } catch (NoSuchAlgorithmException e) {
	    	e.printStackTrace();
	    }
	    return password;
	}
	
}
