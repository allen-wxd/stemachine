package com.wistron.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.SequenceInputStream;

public class CheckUtil {

	/**
	 * 此工具对指定的服务器目录中的.py进行语法检测
	 * 并返回检测的结果
	 */
    public static String checkCommand(String path){
    	 String line="";
         String[] command={"cmd","/c","pyflakes",path};
         StringBuilder sb = new StringBuilder();
         String result="";
         try {
       	  //获得系统运行环境，并执行命令行command
			Process pro =Runtime.getRuntime().exec(command);
			//获取输入流为获得cmd命令行执行的结果
			SequenceInputStream sis = new SequenceInputStream (pro.getInputStream (), pro.getErrorStream ());
            InputStreamReader isr = new InputStreamReader (sis, "gbk");
            BufferedReader br = new BufferedReader (isr);
			while ((line=br.readLine())!= null) {
				char ch='\\';
				int index=line.indexOf(ch)+1;
				//去除多余的字符，从第index+1的位置（索引值）开始输出
				line=line.substring(index);
				sb.append(line+"\n");
				result=sb.toString();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//运行语法检测工具若出现错误，在此处处理
		}
    	return result;
    }
}
