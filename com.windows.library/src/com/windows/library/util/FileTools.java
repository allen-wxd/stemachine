package com.windows.library.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.channels.FileChannel;

import com.windows.library.service.Constants;

public class FileTools {
	 public static void fileChannelCopy(File s, File t) {

	        FileInputStream fi = null;

	        FileOutputStream fo = null;

	        FileChannel in = null;

	        FileChannel out = null;

	        try {

	            fi = new FileInputStream(s);

	            fo = new FileOutputStream(t);

	            in = fi.getChannel();//得到对应的文件通道

	            out = fo.getChannel();//得到对应的文件通道

	            in.transferTo(0, in.size(), out);//连接两个通道，并且从in通道读取，然后写入out通道

	        } catch (IOException e) {

	        	Util.writeLog(e, "FileTools.fileChannelCopy");

	        } finally {

	            try {

	                fi.close();

	                in.close();

	                fo.close();

	                out.close();

	            } catch (IOException e) {

	            	Util.writeLog(e, "FileTools.fileChannelCopy1");

	            }

	        }

	    }
	 
	//***************************************Util************************************************
		public static boolean delAllFile(String path) {
		       boolean flag = false;
		       File file = new File(path);
		       if (!file.exists()) {
		         return flag;
		       }
		       if (!file.isDirectory()) {
		         return flag;
		       }
		       String[] tempList = file.list();
		       File temp = null;
		       for (int i = 0; i < tempList.length; i++) {
		          if (path.endsWith(File.separator)) {
		             temp = new File(path + tempList[i]);
		          } else {
		              temp = new File(path + File.separator + tempList[i]);
		          }
		          if (temp.isFile()) {
		             temp.delete();
		          }
		          if (temp.isDirectory()) {
		             delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
		             delFolder(path + "/" + tempList[i]);//再删除空文件夹
		             flag = true;
		          }
		       }
		       return flag;
		     }
		
		public static void delFolder(String folderPath) {
		     try {
		        delAllFile(folderPath); //删除完里面所有内容
		        String filePath = folderPath;
		        filePath = filePath.toString();
		        java.io.File myFilePath = new java.io.File(filePath);
		        myFilePath.delete(); //删除空文件夹
		     } catch (Exception e) {
		    	 Util.writeLog(e, "FileTools.delFolder");
		     }
		}
		
		public static void copyDirectiory(String sourceDir, String targetDir) throws IOException {
	        // 新建目标目录
	        (new File(targetDir)).mkdirs();
	        // 获取源文件夹当前下的文件或目录
	        File[] file = (new File(sourceDir)).listFiles();
	        for (int i = 0; i < file.length; i++) {
	            if (file[i].isFile()) {
	                // 源文件
	                File sourceFile = file[i];
	                // 目标文件
	                File targetFile = new File(new File(targetDir).getAbsolutePath() + File.separator + file[i].getName());
	                fileChannelCopy(sourceFile, targetFile);
	            }
	            if (file[i].isDirectory()) {
	                // 准备复制的源文件夹
	                String dir1 = sourceDir + "/" + file[i].getName();
	                // 准备复制的目标文件夹
	                String dir2 = targetDir + "/" + file[i].getName();
	                copyDirectiory(dir1, dir2);
	            }
	        }
	    }
		
		public static void getWorkingpath() {
			Constants.WORKING_DIRECTORY=System.getProperty("user.dir")+"\\";
		}
}
