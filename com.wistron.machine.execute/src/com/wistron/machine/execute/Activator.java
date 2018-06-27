package com.wistron.machine.execute;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;


public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		startTimer();
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}
	
	private Timer timer;	 //时钟控制
	private TimerTask task;
	
	public void stopTimer()
	{
		if(timer!=null)
		{
			timer.cancel();
			timer=null;
		}
		if(task!=null)
		{
			task.cancel();
			task=null;
		}
	}
	
	public void startTimer()
	{
		if(timer!=null)
		{
			timer.cancel();
			timer=null;
		}
		if(task!=null)
		{
			task.cancel();
			task=null;
		}
		timer = new Timer(true);
		task = new TimerTask() {   
			public void run() 
			{
				stopTimer();
				String path = System.getProperty("user.dir");
				path+="/ffmpeg/ffmpeg";
				if(new File(path).exists())
				{
			    	String[] cmdA = new String[]{path,"-f", "video4linux2", "-s", "320*240", "-r", "10", "-i", "/dev/video0", "-b:v", "800k", "-r", "30", "-f", "mpeg1video", "http://localhost:8081/machine/videoServer.exec"};
			    	try{
			            Runtime rt = Runtime.getRuntime();
//			            rt.exec(new String[]{"sudo", "chmod","a+x",path});
			            Process proc = rt.exec(cmdA);  
			            StreamHandler errorStreamHandler = new StreamHandler(proc.getErrorStream(), ">"); 
			            errorStreamHandler.start();
			            StreamHandler outputStreamHandler = new StreamHandler(proc.getInputStream(), "STDOUT");
			            outputStreamHandler.start();  
			            stopTimer();
			    	
			        }catch (Exception ex){
			        	System.out.println("Device open failed");
			        }
				}
			}
		};
		timer.schedule(task, 15000);
	}

}
