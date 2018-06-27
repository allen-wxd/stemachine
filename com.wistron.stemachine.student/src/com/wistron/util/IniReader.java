package com.wistron.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

public class IniReader {  
    private String configpath;
    private static Properties properties =new Properties();   
    FileInputStream fis = null; // 读  
    OutputStream fos ;   
    /** 
     *  
     */  
    public IniReader(String path) {  
        try {  
        	this.configpath = path;
            fis = new FileInputStream(configpath);  
            properties.load(fis);  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
  
      
    public String getProperty(String key)  
    {  
        Object object = properties.get(key);  
        return object.toString();  
    }  
      
    public void setProperty(String key, String value)  
    {  
        try {  
            fos = new FileOutputStream(configpath);// 加载读取文件流  
            properties.setProperty(key, value);  
            properties.store(fos, null);  
            fos.close();  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
  
  
  
    /** 
     * @param args 
     */  
    public static void main(String[] args) { 
    	String path = System.getProperty("user.dir")+"\\com.wistron.stemachine.student\\WebContent\\config\\machineConfig.ini";
        IniReader ini = new IniReader(path);  
        String ip = ini.getProperty("ip");
		String port = ini.getProperty("port");
    }  
} 