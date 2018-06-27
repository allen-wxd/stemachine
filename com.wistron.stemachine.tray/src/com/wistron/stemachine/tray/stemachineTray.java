package com.wistron.stemachine.tray;

import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.internal.win32.TCHAR;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;

public class stemachineTray
{
	static Timer timer=null;
	static boolean batexcute=false;
	public static StartupManager myStartup = new StartupManager(8799);
	public static void run_cmd(String strcmd) {
		//
		        Runtime rt = Runtime.getRuntime(); //Runtime.getRuntime()返回当前应用程序的Runtime对象
		        Process ps = null;  //Process可以控制该子进程的执行或获取该子进程的信息。
		        try {
		            ps = rt.exec(strcmd);   //该对象的exec()方法指示Java虚拟机创建一个子进程执行指定的可执行程序，并返回与该子进程对应的Process对象实例。
		            ps.waitFor();  //等待子进程完成再往下执行。
		        } catch (IOException e1) {
		            e1.printStackTrace();
		        } catch (InterruptedException e) {
		            // TODO Auto-generated catch block
		            e.printStackTrace();
		        }

		        int i = ps.exitValue();  //接收执行完毕的返回值
		        if (i == 0) {
		            System.out.println("执行完成.");
		        } else {
		            System.out.println("执行失败.");
		        }

		        ps.destroy();  //销毁子进程
		        ps = null;   
		    }
	
   public static void setupmysql()
   {
	   batexcute=false;
	   String path=System.getProperty("user.dir")+"\\mysql";
	   path+="\\setup.bat";
//	   startMonitor("[setupmysql]");
	   run_cmd("cmd /c start \"\" /wait \""+path+"\"");//ConsoleWindowClass
   }
   
   public static void setuppyflakes()
   {
	   batexcute=false;
	   String path=System.getProperty("user.dir")+"\\pyflakes";
	   path+="\\setup.bat";
//	   startMonitor("[setupmysql]");
	   run_cmd("cmd /c start \"\" /wait \""+path+"\"");//ConsoleWindowClass
   }
	   
   public static void initmysql()
   {
	   batexcute=false;
	   String path=System.getProperty("user.dir")+"\\mysql";
	   path+="\\initialmysql.bat";
	   startMonitor("[initialmysql]");
	   run_cmd("cmd /c start /wait "+path);//ConsoleWindowClass
   }
   
   public static void importdatatomysql()
   {
	   String path=System.getProperty("user.dir")+"\\mysql";
	   path+="\\importdata.bat";
	   startMonitor("[importdata]");
	   run_cmd("cmd /c start /wait "+path);
   }
   
   public static void stopmysql()
   {
	   String path=System.getProperty("user.dir")+"\\mysql";
	   path+="\\stop.bat";
	   startMonitor("[stopmysql]");
	   run_cmd("cmd /c start /wait "+path);
   }
   
   public static void startmysql()
   {
	   String path=System.getProperty("user.dir")+"\\mysql";
	   path+="\\start.bat";
	   startMonitor("[startmysql]");
	   run_cmd("cmd /c start /wait "+path);
   }
   
   public static void KillPowerpnt() 
   {
	   int Hwnd = OS.FindWindow(null,new TCHAR(0,"cmd.exe",true));
	    if(Hwnd>0)
	    {
	       OS.SendMessage(Hwnd, OS.WM_CLOSE, null, null);
	    }
   }
   
   static char[] title;
   static String gcmdtitle;
   public static void startMonitor(String cmdtitle)
   {
	    gcmdtitle=cmdtitle;
		if(timer!=null)
		{
			timer=null;
			System.gc();
			System.gc();
			System.gc();
		}
   	   timer = new Timer();  
       timer.schedule(new TimerTask()
       {
			public void run() {				
//				int Hwnd = OS.FindWindow(new TCHAR(0,"ConsoleWindowClass",true),null);
			int Hwnd = OS.GetDesktopWindow();
				
       	    if(Hwnd>0)
       	    {
       	    	
       	    	int hwndChild = OS.GetWindow (Hwnd, OS.GW_CHILD);
       	    	int count=0;
       	    	while(hwndChild>0)
       	    	{
       	    		System.out.println("comindssssssssss=");
       	    		title=null;
           	    	title=new char[150];
       	    	    OS.GetWindowTextW(hwndChild, title, 150);
       	    	    String titles = new String(title);
       	    	    System.out.println("title333333333333333="+titles);
       	    	    if(titles.contains(gcmdtitle))
       	    	    {
       	    	    	System.out.println("title11111111111111111111111="+titles);
       	    	    	count++;
       	    	    }

       	    	    hwndChild = OS.GetWindow (hwndChild, OS.GW_HWNDNEXT);
       	    	}
       	    	if(count==1&&batexcute)
       	    	{
       	    		System.out.println("count=1111111111111111111111111111");
       	    		Hwnd = OS.FindWindow(new TCHAR(0,"ConsoleWindowClass",true),null);
       	    		if(Hwnd>0)
       	    		{
       	    		  OS.SendMessage(Hwnd, OS.WM_CLOSE, null, null);
       	    		  return;
       	    		}
       	    	}
       	    	else if(count==2)
       	    	{
       	    		System.out.println("count=2222222222222222222222222");
       	    		batexcute=true;
       	    	}
       	    	//OS.SendMessage(Hwnd, OS.WM_CLOSE, null, null);
       	    	startMonitor(gcmdtitle);
       	    }
       	    else
       	    	startMonitor(gcmdtitle);
				// TODO Auto-generated method stub
			}
       	
       }, 2000);  
   }
   
   public static void main(String[] args)
   {
      // 判断是否支持系统托盘
	   if(myStartup==null)
		{
			myStartup = new StartupManager(8799);
		}
	   if(myStartup.isRunning("stemruning"))
		{
//			Shell shell = new Shell();
//			MessageBox dialog = new MessageBox(shell, SWT.ICON_INFORMATION | SWT.OK);
//			dialog.setMessage(ZLResource.resource("app").getResource("classmakerruning").getValue());
//			dialog.setText("MSTeaching");
//			shell.forceActive();
//			dialog.open();
			System.exit(0);
			return;
		}
	  setupmysql();
	  setuppyflakes();
      if (SystemTray.isSupported())
      {
         // 获取图片所在的URL
//         URL url = SystemTrayDemo.class.getResource("Server.ico");
         // 实例化图像对象
         ImageIcon icon = new ImageIcon("res\\drawable\\Server.png");
         // 获得Image对象
         Image image = icon.getImage();
         // 创建托盘图标
         TrayIcon trayIcon = new TrayIcon(image,"wistemachine");
         // 为托盘添加鼠标适配器
         trayIcon.addMouseListener(new MouseAdapter()
         {
            // 鼠标事件
            public void mouseClicked(MouseEvent e)
            {
               // 判断是否双击了鼠标
               if (e.getClickCount() == 2)
               {
//                  JOptionPane.showMessageDialog(null, "SystemTrayDemo");
               }
            }
         });
         
         // 添加工具提示文本
//         trayIcon.setToolTip("本地连接\r\n速度：100.0 Mbps\r\n状态：已连接上");
         
         // 创建弹出菜单
         PopupMenu popupMenu = new PopupMenu();
         MenuItem itemAbout = new MenuItem("关于");             
         MenuItem itemExit = new MenuItem("退出");  
         MenuItem itemstop = new MenuItem("stop");
         MenuItem itemstart = new MenuItem("start");
         itemExit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				System.exit(1);
				
			}}); 
         
         itemAbout.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				About a=new About(new Shell());
				a.open();
			}}); 
         
         itemstart.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				Bundle[] bundles = Activator.getContext().getBundles();
				for(Bundle b:bundles)
				{
					System.out.println("bundlename="+b.getSymbolicName());
//					if(b.getSymbolicName().contains("jetty"))
					{
//						try {
//							b.start();
//						} catch (BundleException e1) {
//							// TODO Auto-generated catch block
//							e1.printStackTrace();
//						}
					}
				}
			}}); 
         itemstop.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				Bundle[] bundles = Activator.getContext().getBundles();
				for(Bundle b:bundles)
				{
//					if(b.getSymbolicName().contains("jetty"))
					{
						try {
							b.stop();
						} catch (BundleException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
			}}); 
//         popupMenu.add(itemstart);
//         popupMenu.add(itemstop);
         popupMenu.add(itemExit);
         popupMenu.addSeparator();
         popupMenu.add(itemAbout);
         // 为托盘图标加弹出菜弹
         trayIcon.setPopupMenu(popupMenu);
         trayIcon.setImageAutoSize(true);
         // 获得系统托盘对象
         SystemTray systemTray = SystemTray.getSystemTray();
         try
         {
            // 为系统托盘加托盘图标
            systemTray.add(trayIcon);
         }
         catch (Exception e)
         {
            e.printStackTrace();
         }
      }
      else
      {
         JOptionPane.showMessageDialog(null, "not support");
      }
   }
}