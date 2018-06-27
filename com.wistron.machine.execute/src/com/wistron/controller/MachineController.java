package com.wistron.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
//import java.io.IOException;
//import java.io.PrintWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.servlet.ModelAndView;

import com.windows.library.util.Util;
import com.wistron.machine.execute.Activator;
import com.wistron.machine.execute.StreamHandler;

@Controller
@RequestMapping("/machine")
public class MachineController {
	@Autowired
//	private BookFormService service;
	SqlSession sqlSession;
	private ServletInputStream ris;
	private FileOutputStream output=null;
	private boolean recording=false;
	public SqlSession getSqlSession() {
		return sqlSession;
	}

	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	
	/***
	 * @param request
	 */
	@RequestMapping("/python.exec")
	public void excute(HttpServletRequest req, HttpServletResponse resp){
	    try 
		{
	    	System.out.println("come in");
	    	String targetIp = getIpAddr(req);
	    	System.out.println("targetIp="+targetIp);
			ServletInputStream ris = req.getInputStream();
		    byte[] b = new byte[1024];  
		    int lens = -1;  
		    File f=new File("/home/pi/example.py");
		    if(f.exists()) f.delete();
		    
		    FileOutputStream output = new FileOutputStream(new File("/home/pi/example.py"));
		    while ((lens = ris.read(b)) > 0) {  
		        output.write(b, 0, lens);
		        output.flush();
		    } 
		    ris.close();
		    output.close();
		    if(new File("/home/pi/example.py").exists())
		    {
		    	beginRecord();
			    PrintWriter out =resp.getWriter();
	
		    	System.out.println("python.exec:");
		    	String cmd = "python /home/pi/example.py";
		    	String[] cmdA = { "/bin/sh", "-c", cmd };
		    	try{
		            Runtime rt = Runtime.getRuntime();
		            
		            Process proc = rt.exec(cmdA);  
		            proc.waitFor(60, TimeUnit.SECONDS);
		            proc.destroy();
		            System.out.println("python.end:");
		            endRecord(targetIp);
					out.print("ok");
		            out.close();
		        }catch (Exception e){
		            e.printStackTrace();
		            out.print("err");
		            out.close();
		        }
		    }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//Util.writeLog(e, "");
			writeLog(e,"");
		}
	}
	
	@RequestMapping("/videoServer.exec")
	public void videoServer(HttpServletRequest req, HttpServletResponse resp){
	    try 
		{
	    	System.out.println("video coming");
	    	ris = req.getInputStream();
	    	byte[] b = new byte[1024];
	    	int lens = -1;  
	    	
	    	while ( (lens=ris.read(b)) > 0) {  
		    {
		    	  if(recording)
		    	  {
		    		  if(output==null)
			    	  {
			    		  File recordfile=new File("/home/pi/55.mp4");
			    		  if(recordfile.exists())recordfile.delete();
			    		  output = new FileOutputStream(recordfile);
			    	  }
			    	  
			    	  {
			            output.write(b, 0, lens);
			    	  }
		    	  }
		    	  
		    	}
		    } 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			writeLog(e, "");
		}
	}
	
	@RequestMapping("/StartRecordVideo.exec")
	public void StartRecordVideo(HttpServletRequest req, HttpServletResponse resp){
		beginRecord();
	}
	
	public void Saveto()
	{
		String path = System.getProperty("user.dir");
		path+="/ffmpeg/ffmpeg";
		if(new File(path).exists())
		{
			File recordfile=new File("/home/pi/551.mp4");
  		    if(recordfile.exists())recordfile.delete();
	    	String[] cmdA = new String[]{path,"-i", "/home/pi/55.mp4", "-y", "/home/pi/551.mp4"};
	    	try{
	            Runtime rt = Runtime.getRuntime();
//		        rt.exec("chmod a+x "+path);
	            Process proc = rt.exec(cmdA);  

//				proc.waitFor();
//	            StreamHandler errorStreamHandler = new StreamHandler(proc.getErrorStream(), ">"); 
//	            errorStreamHandler.start();
//	            StreamHandler outputStreamHandler = new StreamHandler(proc.getInputStream(), "STDOUT");
//	            outputStreamHandler.start();  
//	            proc.destroy();
	    	
	        }catch (Exception ex){
	        	Logger.getLogger(Activator.class.getName()).log(Level.SEVERE, null, ex);
	        }
		}

	}
	
	public void beginRecord()
	{
		if(recording)return;
    	recording=true;
	}
	
	public void endRecord(String ip)
	{
		try 
		{
	    	recording=false;
	    	Thread.sleep(1000);
		    output.close();
		    output=null;
		    String url = "http://"+ip+":8080/stemachine/getVideo.do";
		    HttpConnectionUtil.upload(url, new String[] { "/home/pi/55.mp4" });
//		    Saveto();
//		    HttpConnectionUtil.upload("http://10.43.148.158:8080/teacher/toQueryTeacher.taction", new String[] { "/home/pi/55.mp4" });
		} catch (Exception e) {
			// TODO Auto-generated catch block
			writeLog(e,"");
		}
	}
	public String getIpAddr(HttpServletRequest request) {  
	    String ip = request.getHeader("x-forwarded-for");  
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
	        ip = request.getHeader("Proxy-Client-IP");  
	    }  
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
	        ip = request.getHeader("WL-Proxy-Client-IP");  
	    }  
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
	        ip = request.getRemoteAddr();  
	    }  
	    return ip;  
	}  
	public void writeLog(Exception e,String logfile)
    {
        logfile="/home/pi/WiStemachine.log";
        StringWriter stackTrace = new StringWriter();
       e.printStackTrace(new PrintWriter(stackTrace));
       e.printStackTrace();
       try
       {
         BufferedWriter writer = new BufferedWriter(new FileWriter(new File(logfile),true));
           writer.write(stackTrace.toString()); 
         writer.close();
       }
       catch(Exception e1)
       {     
         
       }
    }

}
