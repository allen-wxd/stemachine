package com.windows.library.util;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.swt.graphics.Image;

import com.windows.library.service.Constants;

public class CreateThumb {
	public interface GeneratedThumb {
//		abstract public void newcut();
		abstract public void GeneratedThumb(String filename ,String outfilename);
	}
	public static void processImg(final List<String> paths) 
	{
		Thread t = new Thread(new Runnable() 
		{
		    public void run() 
		    {
		        //Do business action
		    	
		    	try 
		    	{
		    		for(String path:paths)
		    		{
		    			File file = new File(path);
		    			if(!file.exists()) continue;
		    			String name = file.getName();
		    			name = name.substring(0, name.lastIndexOf("."))+ ".jpg";
			    		List<String> commands = new java.util.ArrayList<String>();
			    		commands.add("thumb.dll");
			    		commands.add("-i");
			    		commands.add(path);
			    		commands.add("-y");
			    		commands.add("-ss");
			    		commands.add("1");//这个参数是设置截取视频多少秒时的画面
			    		commands.add("-frames");
			    		commands.add("1");
			    		commands.add("-s");
			    		commands.add("352x240");
			    		path = path.substring(0, path.lastIndexOf("\\"));
			    		path=path+"\\.thumb";
			    		File thumbdir = new File(path);
			    		if(!thumbdir.exists()) thumbdir.mkdirs();
			    		String output = path+"\\"+name;
			    		commands.add(output);
			    		ProcessBuilder builder = new ProcessBuilder();
			    		builder.command(commands);
			    		builder.start();
		    		}
	//	    		System.out.println("截取成功");
		    	} 
		    	catch (Exception e) 
		    	{
		    		Util.writeLog(e, "CreateThumb.processImg");
		    	}
		    }
	
		});
		t.start();	
    }
	
	public static void processImg(final String path,GeneratedThumb generated) 
	{
		Thread t = new Thread(new Runnable() 
		{
		    public void run() 
		    {
		        //Do business action
		    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		    	try 
		    	{
		    			File file = new File(path);
		    			if(!file.exists()) return;
		    			String name = file.getName();
		    			long filesize = file.length();
		    			name = name.substring(0, name.lastIndexOf("."))+ "_"+filesize+".jpg";
			    		List<String> commands = new java.util.ArrayList<String>();
			    		commands.add(Constants.WORKING_DIRECTORY+"thumb.dll");
			    		commands.add("-i");
			    		commands.add(path);
			    		commands.add("-y");
			    		commands.add("-ss");
			    		commands.add("1");//这个参数是设置截取视频多少秒时的画面
			    		commands.add("-frames");
			    		commands.add("1");
			    		commands.add("-s");
			    		commands.add("352x240");
			    		String subpath = path.substring(0, path.lastIndexOf("\\"));
			    		subpath=subpath+"\\.thumb";
			    		File thumbdir = new File(subpath);
			    		if(!thumbdir.exists()) thumbdir.mkdirs();
			    		String output = subpath+"\\"+name;
			    		commands.add(output);
			    		ProcessBuilder builder = new ProcessBuilder();
			    		builder.command(commands);
			    		System.out.println("before"+df.format(new Date()));// new Date()为获取当前系统时间
			    		builder.start();
			    		System.out.println("after"+df.format(new Date()));// new Date()为获取当前系统时间
			    		if(generated!=null)
			    		{
			    			Thread.sleep(1000);
			    			generated.GeneratedThumb(path, output);
			    		}
		    		System.out.println("截取成功");
		    	} 
		    	catch (Exception e) 
		    	{
		    		Util.writeLog(e, "CreateThumb.processImg1");
		    	}
		    }
	
		});
		t.start();	
    }
	
	 public static void main(String[] args) 
	 { 
		 List<String> paths = new ArrayList<String>();
		 List<String> paths1 = new ArrayList<String>();
		 List<String> paths2 = new ArrayList<String>();
		 List<String> paths3 = new ArrayList<String>();
		 paths.clear();
		 paths1.clear();
		 paths2.clear();
		 paths3.clear();
		 paths.add("D:\\mediafile\\41.mp4");
		 paths1.add("D:\\mediafile\\4.mp4");
		 paths2.add("D:\\mediafile\\DSCN1111.mp4");
		 paths3.add("D:\\mediafile\\DSCN1116.mp4");
		 processImg(paths);
		 processImg(paths1);
		 processImg(paths2);
		 processImg(paths3);
	 }
}