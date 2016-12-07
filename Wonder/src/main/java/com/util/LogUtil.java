package com.util;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LogUtil {
	
	/*
	 * 一个日志文件的周期（单位毫秒）
	 */
	private final int file_duration = 7 * 24 * 3600 * 1000;

	@Value("${log_file}")
	private volatile String log_file;
	
	@Value("${log_dir}")
	private String log_dir = "E:/234";
	
	public LogUtil() {
		// TODO Auto-generated constructor stub

	}
	
	public static void console(String id, String msg){
		System.out.println(id + " ==> " + msg);
	}
	
	
	
	private File getLogFile(String dir) throws IOException, ParseException{
		File fdir = new File(dir);
		if(!fdir.exists()) fdir.mkdirs();
		
		File logFile = null;
		File[] logFiles = fdir.listFiles(new FileFilter(){
			public boolean accept(File pathname) {
				// TODO Auto-generated method stub
				return Pattern.matches("\\d{4}-\\d{2}-\\d{2}\\.log", pathname.getName());
			}
		});
		
		if(logFiles.length == 0){
			logFile = new File(fdir.getPath() + "/" + StringUtil.getCurrentTime("yyyy-MM-dd") + ".log");
			if(!logFile.exists())	logFile.createNewFile();
			return logFile;
		}
		logFile = logFiles[logFiles.length-1];
		Date latestDay = DateFormat.getDateInstance().parse(logFile.getName().substring(0, 10));
		if(new Date().getTime() - latestDay.getTime() >= file_duration){
			logFile = new File(fdir.getPath() + "/" + StringUtil.getCurrentTime("yyyy-MM-dd") + ".log");
			if(!logFile.exists())	logFile.createNewFile();
			return logFile;
		}
		
		return logFile;
	}
	
	
	
	public synchronized boolean log(String[] strs, String separator){	
		try{
			File logFile = getLogFile(log_dir);
			separator = StringUtil.isEmpty(separator)?" ":separator;
			if(!logFile.exists()){
				String dir = log_file.substring(0, log_file.lastIndexOf("/"));
				File fdir = new File(dir);
				if(!fdir.exists()) fdir.mkdirs();
				logFile.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(logFile, true);
			StringBuffer buffer = new StringBuffer(StringUtil.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
			buffer.append("===>");
			for(int i=0; i<strs.length; i++){
				if(i>0)	buffer.append(separator);
				buffer.append(strs[i]);
			}
			buffer.append("\r\n");
			fos.write(buffer.toString().getBytes());
			fos.flush();
			fos.close();
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public synchronized boolean log(Collection<String> array, String separator){	
		try{
			String[] strs = (String[]) array.toArray();
			return log(strs, separator);
		}catch(Exception e){
			return false;
		}
	}
	
	
	public static void main(String[] args) {
		
		String[] strs = {"sdf", "165"}; 
		new LogUtil().log(strs, " ");

	}
}
