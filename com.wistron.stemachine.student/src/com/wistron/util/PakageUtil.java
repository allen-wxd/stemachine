package com.wistron.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class PakageUtil {

	/**
	 * 此工具将字符串命令打包成.py 文件 并保存到服务器的某个指定目录
	 * 
	 */
//	final static String path = "D:\\w.py";
	public static File file;

	public static String pakageCommand(String command) {
		String path=System.getProperty("user.home");
	    path=path+"\\w.py";
		file = new File(path);
		if (!file.exists()) {
			file.getParentFile().mkdirs();
		}
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			file.createNewFile();
			fw = new FileWriter(file, true);
			bw = new BufferedWriter(fw);
			bw.write(command);
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				bw.close();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return path;
	}
}
