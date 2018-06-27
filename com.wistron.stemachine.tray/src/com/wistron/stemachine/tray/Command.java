package com.wistron.stemachine.tray;

public class Command {  
	static String path;  
public static void main(String[] args) throws java.io.IOException, InterruptedException {  
         
  //"cmd /c start /wait " + 
       Process p = Runtime.getRuntime().exec(path);  
       p.waitFor();         
   }  
}  