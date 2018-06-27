package com.library.core.util;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;

//import com.wistron.util.IniEditor;

public class Util {
  
  public Util() {  }
  
  public static void appendLog(String log,String logfile)
  {
	  String path=System.getProperty("user.home");
	    path=path+"\\WiClassMakerlog\\";
	    File file=new File(path);
	    if(!file.exists())file.mkdir();
	    logfile=path+logfile;
		FileWriter fw = null;
        try{
		  File f=new File(logfile);

		fw = new FileWriter(f,true);

		} catch (IOException e) {


		}

		PrintWriter pw = new PrintWriter(fw);

		pw.println(log);

		pw.flush();

		try{

		  fw.flush();

		  pw.close();

		  fw.close();

		  } 
		catch (IOException e) 
		{

		
		}
	}

  public static void writeLog(String fileName, String content) {                    
      try {                                                                        
          // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件      
           FileWriter writer = new FileWriter(fileName, true);                     
           writer.write(content);                                                  
           writer.close();                                                         
       } catch (IOException e) {                                                   
       }                                                                           
   }
  
  public static void addDir(String s) throws IOException {  
	    try {  
	        Field field = ClassLoader.class.getDeclaredField("usr_paths"); 
	        field.setAccessible(true);  
	        String[] paths = (String[])field.get(null);  
	        for (int i = 0; i < paths.length; i++) {  
	            if (s.equals(paths[i])) {  
	                return;  
	            }  
	        }  
	        String[] tmp = new String[paths.length+1];  
	        System.arraycopy(paths,0,tmp,0,paths.length);  
	        tmp[paths.length] = s;  
	        field.set(null,tmp);  
	    } catch (IllegalAccessException e) {  
	        throw new IOException("Failed to get permissions to set library path");  
	    } catch (NoSuchFieldException e) {  
	        throw new IOException("Failed to get field handle to set library path");  
	    }  
	}
  
//  WriteRegStr "HKLM" "SYSTEM\CurrentControlSet\Services\SharedAccess\Parameters\FirewallPolicy\FirewallRules" "zxmTFsynctcp" "v2.0|Action=Allow|Active=TRUE|Dir=In|Protocol=6|Profile=Public|LPort=4444|Name=zxmTFsynctcp|Edge=FALSE|"
}