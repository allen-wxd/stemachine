package com.windows.library.util;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.internal.win32.SHELLEXECUTEINFO;
import org.eclipse.swt.internal.win32.TCHAR;
import org.eclipse.swt.widgets.Display;

//import com.wistron.util.IniEditor;
import com.windows.library.resource.MonitorResource;
import com.windows.library.service.Constants;

public class Util {
  
  public Util() {  }
  
  /**
   * Read a value from key and value name
   * @param hkey   HKEY_CURRENT_USER/HKEY_LOCAL_MACHINE
   * @param key
   * @param valueName
   * @return the value
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   * @throws InvocationTargetException
   */
  public static boolean PPTisRuning() 
  {
	 int Hwnd = OS.FindWindow(new TCHAR(0,"PPTFrameClass",true),null);
	 
	 if(Hwnd<=0)
	   Hwnd = OS.FindWindow(new TCHAR(0,"PP12FrameClass",true),null);
	 if(Hwnd<=0)
		 Hwnd = OS.FindWindow(new TCHAR(0,"PP11FrameClass",true),null);
	 if(Hwnd>0)
	 {
		 boolean re=OS.IsWindowVisible(Hwnd);
		 if(!re)
		 {
		    CloseProg("POWERPNT.EXE");
		    CloseProg("wpp.exe");
		    return false;
		 }
		 char[] title=new char[150];
//		 int length = OS.GetWindowTextLength(Hwnd);
		 OS.GetWindowTextW(Hwnd, title, 150);
		 String titles = new String(title);
//		 if(titles.contains(".ppt")||titles.contains(".pptx")||titles.contains(".pps")||titles.contains(".ppsx"))
		 {
			 return true;
		 }
	 }

	 return false;
  }
  
  public static void setIni(String Section,String option,String value)
  {
	    String path=System.getProperty("user.home");
	    path=path+"\\MakerConfig.ini";
	    File file = new File(path);
	    File srcfile = new File(Constants.WORKING_DIRECTORY+"MakerConfig.ini");
	    if(!file.exists())
	      FileTools.fileChannelCopy(srcfile, file);
	    else
	    {
	       if(file.lastModified()<srcfile.lastModified())
	    	   copyinioption("Section","screenmode",srcfile,file);
	    }
	    try {
		    IniEditor inieditor = new IniEditor();
		    inieditor.load(path);
		    {
		    	if(!inieditor.hasSection(Section))
			      inieditor.addSection(Section);
		    	inieditor.set(Section, option, value);
		    }
		    inieditor.save(path);
		} 
	    catch (IOException e) 
	    {
	    	Util.writeLog(e, "Util.setIni");
		}
  }
  
  public static ImageData reverseImage(ImageData srcData)  
  {  
      int bytesPerPixel = srcData.bytesPerLine / srcData.width;  
      int destBytesPerLine = srcData.width * bytesPerPixel;  
      byte[] newData = srcData.data;  
    
      for (int i = 0; i < newData.length; i ++)  
          newData[i] = (byte) (255 - newData[i]);  
      ImageData newImageData = new ImageData(srcData.width, srcData.height,   
              srcData.depth, srcData.palette, destBytesPerLine, newData);  
      newImageData.transparentPixel = srcData.transparentPixel;  
    
      return newImageData;  
  }  
  
  public static void deleteOption(String filename,String Section,String option)
  {
	    if(filename==null||Section==null||option==null)return;
	    String path=System.getProperty("user.home");
	    path=path+"\\MakerConfig.ini";
	    File file = new File(path);
	    File srcfile = new File(filename);
	    if(!file.exists())
	      FileTools.fileChannelCopy(srcfile, file);
	    else
	    {
	       if(file.lastModified()<srcfile.lastModified())
	    	   copyinioption("Section","screenmode",srcfile,file);
	    }
	    try {
		    IniEditor inieditor = new IniEditor();
		    inieditor.load(path);
		    {
		    	inieditor.remove(Section, option);
		    }
		    inieditor.save(path);
		} 
	    catch (IOException e) 
	    {
	    	Util.writeLog(e, "Util.deleteOption");
		}
  }
  
  public static List<String> getOptionList(String filename,String Section)
  {
	  String path=System.getProperty("user.home");
	    path=path+"\\MakerConfig.ini";
	    File file = new File(path);
	    File srcfile = new File(filename);
	    if(!file.exists())
	      FileTools.fileChannelCopy(srcfile, file);
	    else
	    {
	       if(file.lastModified()<srcfile.lastModified())
	    	   copyinioption("Section","screenmode",srcfile,file);
	    }
	    try {
		    IniEditor inieditor = new IniEditor();
		    inieditor.load(path);
		    if(!inieditor.hasSection(Section))
		    {
		      inieditor.addSection(Section);
		      inieditor.save(path);
		    }
		    return inieditor.optionNames(Section);
		} 
	    catch (IOException e) 
	    {
	    	Util.writeLog(e, "Util.getOptionList");
		    return null;
		}
  }
  
  public static int getOptionnum(String Section)
  {
	    String path=System.getProperty("user.home");
	    path=path+"\\MakerConfig.ini";
	    File file = new File(path);
	    File srcfile = new File(Constants.WORKING_DIRECTORY+"MakerConfig.ini");
	    if(!file.exists())
	      FileTools.fileChannelCopy(srcfile, file);
	    else
	    {
	       if(file.lastModified()<srcfile.lastModified())
	    	   copyinioption("Section","screenmode",srcfile,file);
	    }
	    try {
		    IniEditor inieditor = new IniEditor();
		    inieditor.load(path);
		    if(!inieditor.hasSection(Section))
		    {
		      inieditor.addSection(Section);
		      inieditor.save(path);
		    }
		    return inieditor.optionNames(Section).size();
		    
		} 
	    catch (IOException e) 
	    {
	    	Util.writeLog(e, "Util.getOptionnum");
		    return 0;
		}
  }
  
  public static void copyinioption(String Section,String option,File src,File dst)
  {
	  String value="";
	  try {
		    IniEditor inieditor = new IniEditor();
		    inieditor.load(src);
		    value = inieditor.get(Section, option);
		} catch (IOException e) {
			Util.writeLog(e, "Util.copyinioption");
		}
	  try {
		    IniEditor inieditor = new IniEditor();
		    inieditor.load(dst);
		    {
		    	inieditor.set(Section, option, value);
		    }
		    inieditor.save(dst);
		} 
	    catch (IOException e) 
	    {
	    	Util.writeLog(e, "Util.copyinioption");
		}
  }
  
  public static String getIni(String Section,String option)
  {
	    String value="";
	    String path=System.getProperty("user.home");
	    path=System.getProperty("user.dir")+"\\MakerConfig.ini";
//	    File file = new File(path);
//	    File srcfile = new File(Constants.WORKING_DIRECTORY+"MakerConfig.ini");
//	    if(!file.exists())
//	      FileTools.fileChannelCopy(srcfile, file);
//	    else
//	    {
//	       if(file.lastModified()<srcfile.lastModified())
//	         copyinioption("Section","screenmode",srcfile,file);
//	    }
		try {
		    IniEditor inieditor = new IniEditor();
		    inieditor.load(path);
		    String screenmode = inieditor.get(Section, option);
		    value = screenmode;
		} catch (IOException e) {
			Util.writeLog(e, "Util.getIni");
		}
		if(value==null)
		  value="";
		return value;
  }
  
  public static String getInivalue(String Section,String option)
  {
	    String value="";
	    String path=System.getProperty("user.home");
	    path=path+"\\MakerConfig.ini";
		try {
		    IniEditor inieditor = new IniEditor();
		    inieditor.load(path);
		    String screenmode = inieditor.get(Section, option);
		    value = screenmode;
		} catch (IOException e) {
			Util.writeLog(e, "Util.getIni");
		}
		if(value==null)
		  value="";
		return value;
  }

  
	public static int getStringWidth(GC gc,char[] string, int offset, int length) {
		int width=0;
		for (int i = 0; i < length; i++) {
			width += gc.getAdvanceWidth(string[offset+i]);
		}
		width=(int) (width+0.5f);
		return width;
	}
	
	public static int getStringHeightInternal(GC gc) {
		float dpi = Toolkit.getDefaultToolkit().getScreenResolution();
		float bili=(float)(dpi/96.0);
		return (int)((gc.getFontMetrics().getHeight() + 0.5f));
	}
    
  public static String Getinstallpath(String filename) {
	    String path="";
		try {
			path = WinRegistry.readString (
				    WinRegistry.HKEY_LOCAL_MACHINE, //HKEY
				   "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\App Paths\\"+filename, //Key
				   "");
		} catch (IllegalArgumentException e1) {
			// TODO Auto-generated catch block
			Util.writeLog(e1, "Util.Getinstallpath");
			path = "";
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			Util.writeLog(e1, "Util.Getinstallpath1");
			path = "";
		} catch (InvocationTargetException e1) {
			// TODO Auto-generated catch block
			Util.writeLog(e1, "Util.Getinstallpath2");
			path = "";
		}
		if(path==null) path="";
		return path;
  }

  public static int getScreenMode()
  {
	    String screenmode = getIni("Section", "ScreenMode");
	    if(screenmode.isEmpty())return 0;
	    return Integer.parseInt(screenmode);
  }
  
  public static void runApp(String app,ArrayList<String> p)
  {
		Thread t = new Thread(new Runnable() 
		{
		    public void run() 
		    {
		        //Do business action
		    	
		    	List<String> commands = new java.util.ArrayList<String>();
				commands.add(app);
				if(p!=null)
				for(String para:p)
				  commands.add(para);
				
				ProcessBuilder builder = new ProcessBuilder();
				builder.command(commands);
				try {
					builder.start();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Util.writeLog(e, "Util.runApp");
				}
		    }
	
		});
		t.start();
  }
  
  public static void KillPowerpnt() 
  {
	  String command = "taskkill /f /im POWERPNT.EXE";  
		try {
			if(command!=null)
			  Runtime.getRuntime().exec(command.trim());
		} catch (IOException e1) {
			Util.writeLog(e1, "Util.KillPowerpnt");
		}
  }
  
  public static String readTxtFile(String filePath){
      try {
              String encoding="utf-8";
              File file=new File(filePath);
              if(file.isFile() && file.exists()){ //判断文件是否存在
                  InputStreamReader read = new InputStreamReader(
                  new FileInputStream(file),encoding);//考虑到编码格式
                  BufferedReader bufferedReader = new BufferedReader(read);
                  String lineTxt = null;
                  while((lineTxt = bufferedReader.readLine()) != null){
                      return lineTxt;
                  }
                  read.close();
      }else{
          System.out.println("找不到指定的文件");
      }
      } catch (Exception e) {
          System.out.println("读取文件内容出错");
          Util.writeLog(e, "Util.readTxtFile");
      }
   return "";
  }
  
  public static int getPixelFromRGBA( int depth, byte[] data )
  {
          switch ( depth )
          {
              case 32 :
                  return ( ( data[0] & 0xFF ) << 24 )
                          + ( ( data[1] & 0xFF ) << 16 )
                          + ( ( data[2] & 0xFF ) << 8 )
                          + ( data[3] & 0xFF );
              case 24 :
                  return ( ( data[0] & 0xFF ) << 16 )
                          + ( ( data[1] & 0xFF ) << 8 )
                          + ( data[2] & 0xFF );
              case 16 :
                  return ( ( data[1] & 0xFF ) << 8 ) + ( data[0] & 0xFF );
              case 8 :
                  return data[0] & 0xFF;
          }
//          SWT.error( SWT.ERROR_UNSUPPORTED_DEPTH );
          return 0;
  }
  
  public static void MinimiseTraywnd()
  {
	  int hWnd = OS.FindWindow(new TCHAR(0,"Shell_traywnd",true),null);
	  int bhWnd = OS.FindWindow(new TCHAR(0,"Button",true),null);
	  OS.PostMessage(hWnd,OS.WM_SYSCOMMAND, OS.SC_MINIMIZE,0);
	  OS.PostMessage(bhWnd,OS.WM_SYSCOMMAND, OS.SC_MINIMIZE,0);
	  
  }
  
  public static void RestoreTraywnd()
  {
	  int hWnd = OS.FindWindow(new TCHAR(0,"Shell_traywnd",true),null);
	  int bhWnd = OS.FindWindow(new TCHAR(0,"Button",true),null);
	  OS.PostMessage(hWnd,OS.WM_SYSCOMMAND, OS.SC_RESTORE,0);
	  OS.PostMessage(bhWnd,OS.WM_SYSCOMMAND, OS.SC_RESTORE,0);
	  
  }//
  
  public static void Hidetraywnd()
  {
		int hWnd = OS.FindWindow(new TCHAR(0,"Shell_traywnd",true),null);
		int bhWnd = OS.FindWindow(new TCHAR(0,"Button",true),null);
//		OS.SetWindowPos(hWnd,0,0,0,0,0,SWP_HIDEWINDOW);
        OS.ShowWindow(hWnd, 0);
        OS.ShowWindow(bhWnd, 0);
  }
  
  public static void Hidetraywnd1()
  {
		int hWnd = OS.FindWindow(new TCHAR(0,"Shell_SecondaryTrayWnd",true),null);
		int bhWnd = OS.FindWindow(new TCHAR(0,"Button",true),null);
//		OS.SetWindowPos(hWnd,0,0,0,0,0,SWP_HIDEWINDOW);
        OS.ShowWindow(hWnd, 0);
        OS.ShowWindow(bhWnd, 0);
  }
  
  public static void Showtraywnd()
  {
		int hWnd = OS.FindWindow(new TCHAR(0,"Shell_traywnd",true),null);
		int bhWnd = OS.FindWindow(new TCHAR(0,"Button",true),null);
//		OS.SetWindowPos(hWnd,0,0,0,0,0,SWP_HIDEWINDOW);
        OS.ShowWindow(hWnd, 1);
        OS.ShowWindow(bhWnd, 1);
  }
  
  public static void Showtraywnd1()
  {
		int hWnd = OS.FindWindow(new TCHAR(0,"Shell_SecondaryTrayWnd",true),null);
		int bhWnd = OS.FindWindow(new TCHAR(0,"Button",true),null);
//		OS.SetWindowPos(hWnd,0,0,0,0,0,SWP_HIDEWINDOW);
        OS.ShowWindow(hWnd, 1);
        OS.ShowWindow(bhWnd, 1);
  }
  
  public static boolean launch (String fileName, String Premeter) {
		if (fileName == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
		
		/* Use the character encoding for the default locale */
		int /*long*/ hHeap = OS.GetProcessHeap ();
		TCHAR buffer = new TCHAR (0, fileName, true);
		int byteCount = buffer.length () * TCHAR.sizeof;
		int /*long*/ lpFile = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, byteCount);
		OS.MoveMemory (lpFile, buffer, byteCount);
		
		int /*long*/ lpPremeter = 0;
		if (Premeter != null && OS.PathIsExe(lpFile)) {
		    TCHAR buffer1 = new TCHAR (0, Premeter, true);
		    byteCount = buffer1.length () * TCHAR.sizeof;
		    lpPremeter = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, byteCount);
		    OS.MoveMemory (lpPremeter, buffer1, byteCount);
		}
		
		SHELLEXECUTEINFO info = new SHELLEXECUTEINFO ();
		info.cbSize = SHELLEXECUTEINFO.sizeof;
		info.lpFile = lpFile;
//		info.lpDirectory = lpDirectory;
		info.lpParameters = lpPremeter;
		info.nShow = OS.SW_SHOW;
		boolean result = OS.ShellExecuteEx (info);
		if (lpFile != 0) OS.HeapFree (hHeap, 0, lpFile);
		if (lpPremeter != 0) OS.HeapFree (hHeap, 0, lpPremeter);
		return result;
	}
  
  public static void executeProg(String fileName) throws Exception
	{
		int hHeap = OS.GetProcessHeap ();
		TCHAR buffer = new TCHAR (0, fileName, true);
		int byteCount = buffer.length () * TCHAR.sizeof;
		int lpFile = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, byteCount);
		OS.MoveMemory (lpFile, buffer, byteCount);
		String path="";
		if(!Constants.WORKING_DIRECTORY.isEmpty())
		  path="\""+Constants.WORKING_DIRECTORY+"\\\"";

		TCHAR buffer1 = new TCHAR(0,path,true);
		byteCount = buffer1.length () * TCHAR.sizeof;
		int lpPremeter = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, byteCount);
		OS.MoveMemory (lpPremeter, buffer1, byteCount);
		
		SHELLEXECUTEINFO info = new SHELLEXECUTEINFO ();
		info.cbSize = SHELLEXECUTEINFO.sizeof;
		info.lpFile = lpFile;
		info.lpParameters = lpPremeter;
		//隐藏启动
		info.nShow = OS.SW_HIDE;
		boolean result = OS.ShellExecuteEx (info);
		if (lpFile != 0) OS.HeapFree (hHeap, 0, lpFile);
		if(result==false){}
//		  throw new Exception("Fail!");
	}
  
  public static boolean isPad()
  {
	  Rectangle bound = MonitorResource.getBounds();
	  final String osname = System.getProperty("os.name");
	  if((bound.width==1280&&(bound.height==760||bound.height==752||bound.height==800)&&osname.equals("Windows 8"))||
	     (bound.width==800&&(bound.height==1240||bound.height==1232||bound.height==1280)&&osname.equals("Windows 8")))
	    return true;
	  return false;
  }
  
  public static void MouseMove(int x,int y)
  {
//	  Dimension dim; //存储屏幕尺寸
	  Robot robot = null;//主动化东西
//	  int width = 0,height=0;
//	  dim = Toolkit.getDefaultToolkit().getScreenSize();
      try{
          robot = new Robot();
      }catch(AWTException e){
    	  Util.writeLog(e, "Util.MouseMove");
      }
	     
//      System.out.println("enter Move()...");
//      Point mousepoint = MouseInfo.getPointerInfo().getLocation();
//        System.out.println("移动前坐标：" + mousepoint.x + " " + mousepoint.y);
//        width += mousepoint.x;
//        height += mousepoint.y;
      try{
          robot.delay(1000);
          robot.mouseMove(x,y);
      }catch(Exception e){
    	  Util.writeLog(e, "Util.MouseMove1");
      }
  }
  
  public static boolean isRunning(String exeName) {
      Process proc;
      int count=0;
      try {
          proc = Runtime.getRuntime().exec("tasklist");
          BufferedReader br = new BufferedReader(new InputStreamReader(proc
                  .getInputStream()));
          String info = br.readLine();
          while (info != null) {
              if (info.indexOf(exeName) >= 0) {
            	  count++;
            	  if(count==2)
                    return true;
              }
              info = br.readLine();
          }
      } catch (IOException e) {
    	  Util.writeLog(e, "Util.isRunning");
      } 
      return false;
  }
  
  public static boolean FindProc(String processName)
  {
	  try {
	        String[]cmmd = { "cmd" ,"/c", "FOR /F \"tokens=2,3*\"; %i  in ('tasklist /nh ^| find \"" + processName + "\"') do @echo %i" };       
	        String str=null;
	        Process process=Runtime.getRuntime().exec( cmmd );
	        BufferedReader br=new BufferedReader(new InputStreamReader(process.getInputStream()));
	        if((str=br.readLine())!=null)
	          return true;
	    } 
	    catch (IOException e) 
	    {
	    	Util.writeLog(e, "Util.FindProc");
	        return false;
	    }
	    return false;
  }
  
//  static boolean filecheck=true;
  public static boolean checkRegister()
  {
	  String path=System.getProperty("user.home")+"\\Documents\\";
	  File file = new File(path+"1");
	  file.deleteOnExit();
	  
	  File filekey = new File(path+"20171212");
	  if(filekey.exists())return true;
	  try 
	  {
		  launch(Constants.WORKING_DIRECTORY+"WiRegCheck.exe".trim(),null);
		
	  } catch (Exception e) {
			// TODO Auto-generated catch block
		  Util.writeLog(e, "Util.checkRegister");
		  return false;
	  }
	  Util.writeLog("等待注册校验......");
	  while(!FindProc("WiRegCheck.exe"))
	  {
		  try {
			Thread.sleep(100);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  }
	  while(FindProc("WiRegCheck.exe"))
	  {
		  try {
			Thread.sleep(100);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  }
		  
	  if(file.exists())
	  {
		  Util.writeLog("注册校验成功");
//		String re=Readinfo(file);
		file.deleteOnExit();
//		if(re.equalsIgnoreCase("0"))
	      return true;
	  }
	  Util.writeLog("注册校验失败");
	  return false;
	  /*File file = new File(Constants.WORKING_DIRECTORY+"1");
	  file.deleteOnExit();
	  ProcessBuilder proc = new ProcessBuilder(Constants.WORKING_DIRECTORY+"VersionCheck.exe".trim(),"".trim());
	  try 
	  {
		Process p = proc.start();
		Thread.sleep(2000);
		
	  } catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
		  Util.writeLog(e, "Util.checkRegister");
			return false;
	  }
	  if(file.exists())
	  {
		file.delete();
	    return true;
	  }
	  return true;*/
  }
  
  public static String Readinfo(File file)
  {
		String str = null;
		FileReader reader = null;
		try 
		{
          // read file content from file
//          StringBuffer sb= new StringBuffer("");
         
			reader = new FileReader(file);
          BufferedReader br = new BufferedReader(reader);
          
         
          while((str = br.readLine()) != null) {
          	System.out.println(str);
          	break;
          }
		}
		catch(FileNotFoundException e) {
          e.printStackTrace();
          return "0";
      }
      catch(IOException e) {
          e.printStackTrace();
          return "0";
      }finally {
			try {
				reader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return str;
  }
    
  public static void executeProg(String fileName,String parameter,boolean hide) throws Exception
  {
		int hHeap = OS.GetProcessHeap ();
		TCHAR buffer = new TCHAR (0, fileName, true);
		int byteCount = buffer.length () * TCHAR.sizeof;
		int lpFile = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, byteCount);
		OS.MoveMemory (lpFile, buffer, byteCount);
		
		TCHAR buffer1 = new TCHAR(0,parameter,true);
		byteCount = buffer1.length () * TCHAR.sizeof;
		int lpPremeter = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, byteCount);
		OS.MoveMemory (lpPremeter, buffer1, byteCount);
		
		SHELLEXECUTEINFO info = new SHELLEXECUTEINFO ();
		info.cbSize = SHELLEXECUTEINFO.sizeof;
		info.lpFile = lpFile;
		info.lpParameters=lpPremeter;
		//隐藏启动
		if(hide)
		  info.nShow = OS.SW_HIDE;
		else
		  info.nShow = OS.SW_SHOW;
		boolean result = OS.ShellExecuteEx (info);
		if (lpFile != 0) OS.HeapFree (hHeap, 0, lpFile);
		if(result==false){}
//			throw new Exception("启动失败!");
  }
  
  public static void CloseProg(String fileName)
	{
	  String command = "taskkill /f /im "+fileName;  
		try {
			Runtime.getRuntime().exec(command.trim());
		} catch (IOException e1) {
			Util.writeLog(e1, "Util.CloseProg");
		}
	}
  
  public static void DestroyProg(String fileName)
	{
	  String command = "ntsd -c q -pn "+fileName;  
		try {
			Runtime.getRuntime().exec(command.trim());
		} catch (IOException e1) {
			Util.writeLog(e1, "Util.DestroyProg");
		}
	}
  
  public static void init()
  {
	  String path=System.getProperty("user.home");
	  Constants.CLASSMAKER_DIRECTORY = path+"\\temp";
	  Constants.CLASSMAKER_DIRECTORY_CONTENT = path+"\\temp\\content";
	  Constants.CLASSMAKER_DIRECTORY_THUM = path+"\\temp\\thum";
	  Constants.CLASSMAKER_DIRECTORY_QUIZ = path+"\\temp\\quiz";
	  Constants.CLASSMAKER_DIRECTORY_QUIZ_TEMP = path+"\\temp\\quiztemp";
	  Constants.CLASSMAKER_NOTES_DIRECTORY = path+"\\temp\\notes";
	  
	  File file = new File(Constants.CLASSMAKER_DIRECTORY);
	  if(!file.exists()) file.mkdirs();
	  
	  file = new File(Constants.CLASSMAKER_DIRECTORY_CONTENT);
	  if(!file.exists()) file.mkdirs();
	  
	  file = new File(Constants.CLASSMAKER_DIRECTORY_THUM);
	  if(!file.exists()) file.mkdirs();
	  
	  file = new File(Constants.CLASSMAKER_DIRECTORY_QUIZ);
	  if(!file.exists()) file.mkdirs();
	  
	  file = new File(Constants.CLASSMAKER_DIRECTORY_QUIZ_TEMP);
	  if(!file.exists()) file.mkdirs();
	  
	  file = new File(Constants.CLASSMAKER_NOTES_DIRECTORY);
	  if(!file.exists()) file.mkdirs();
	  
	  Constants.WORKING_DIRECTORY=System.getProperty("user.dir")+"\\";
  }
  
  private static boolean isChinese(char c) {
      Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
      if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
              || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
              || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
              || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
          return true;
      }
      return false;
  }

  public static boolean isChinese(String strName) {
      char[] ch = strName.toCharArray();
      for (int i = 0; i < ch.length; i++) {
          char c = ch[i];
          if (isChinese(c)) {
              return true;
          }
      }
      return false;
  }
  
  public static String getTime()
  {
	  SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
	  String datetime=df.format(new Date());
	  return datetime;
  }
  
  public static void Readerinit()
  {
	  String path=System.getProperty("user.home");
	  Constants.CLASSMAKER_DIRECTORY = path+"\\Rtemp";
	  Constants.CLASSMAKER_DIRECTORY_CONTENT = path+"\\Rtemp\\content";
	  Constants.CLASSMAKER_DIRECTORY_THUM = path+"\\Rtemp\\thum";
	  Constants.CLASSMAKER_DIRECTORY_QUIZ = path+"\\Rtemp\\quiz";
	  Constants.CLASSMAKER_DIRECTORY_QUIZ_TEMP = path+"\\Rtemp\\quiztemp";
	  Constants.CLASSMAKER_NOTES_DIRECTORY = path+"\\Rtemp\\notes";
	  
	  File file = new File(Constants.CLASSMAKER_DIRECTORY);
	  if(!file.exists()) file.mkdirs();
	  
	  file = new File(Constants.CLASSMAKER_DIRECTORY_CONTENT);
	  if(!file.exists()) file.mkdirs();
	  
	  file = new File(Constants.CLASSMAKER_DIRECTORY_THUM);
	  if(!file.exists()) file.mkdirs();
	  
	  file = new File(Constants.CLASSMAKER_DIRECTORY_QUIZ);
	  if(!file.exists()) file.mkdirs();
	  
	  file = new File(Constants.CLASSMAKER_DIRECTORY_QUIZ_TEMP);
	  if(!file.exists()) file.mkdirs();
	  
	  file = new File(Constants.CLASSMAKER_NOTES_DIRECTORY);
	  if(!file.exists()) file.mkdirs();
	  
	  Constants.WORKING_DIRECTORY=System.getProperty("user.dir")+"\\";
  }
  
  public static void writeLog(Exception e,String logfile)
  {
//	    String path=System.getProperty("user.home");
	    String path=System.getProperty("user.home")+"\\Documents";
	    path=path+"\\WiStemachine\\";
	    File file=new File(path);
	    if(!file.exists())file.mkdir();
	    logfile=path+"WiStemachine.log";
	    StringWriter stackTrace = new StringWriter();
		e.printStackTrace(new PrintWriter(stackTrace));
		e.printStackTrace();
		try
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(logfile),true));
			writer.write("************************"+getTime()+ "***************************"); 
			writer.newLine();
		    writer.write(stackTrace.toString()); 
			writer.close();
		}
		catch(Exception e1)
		{     
			
		}
  }
  
  public static void writeLog(Throwable e,String logfile)
  {
//	    String path=System.getProperty("user.home");
	    String path=System.getProperty("user.home")+"\\Documents";
	    path=path+"\\Wistemachinelog\\";
	    File file=new File(path);
	    if(!file.exists())file.mkdir();
	    logfile=path+"Wistemachine.log";
	    StringWriter stackTrace = new StringWriter();
		e.printStackTrace(new PrintWriter(stackTrace));
		e.printStackTrace();
		try
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(logfile),true));
			writer.write("************************"+getTime()+ "***************************"); 
			writer.newLine();
		    writer.write(stackTrace.toString()); 
			writer.close();
		}
		catch(Exception e1)
		{     
			
		}
  }
  
  public static void writeLog(String logstr)
  {
//	    String path=System.getProperty("user.home");
	    String path=System.getProperty("user.home")+"\\Documents";
	    path=path+"\\Msteachinglog\\";
	    File file=new File(path);
	    if(!file.exists())file.mkdirs();
	    String logfile=path+"Msteaching.log";
	    StringWriter stackTrace = new StringWriter();
//		e.printStackTrace(new PrintWriter(stackTrace));
//		e.printStackTrace();
		try
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(logfile),true));
			writer.newLine();
			writer.write("************************"+getTime()+ "***************************"); 
			writer.newLine();
		    writer.write(logstr); 
			writer.close();
		}
		catch(Exception e1)
		{     
			
		}
  }
  
	public static String getStackMsg(Exception e) {  
		  
	      StringBuffer sb = new StringBuffer();  
	      StackTraceElement[] stackArray = e.getStackTrace();  
	      for (int i = 0; i < stackArray.length; i++) {  
	          StackTraceElement element = stackArray[i];  
	          sb.append(element.toString() + "\n");  
	      }  
	      return sb.toString();  
	  }

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

			Util.writeLog(e, "Util.appendLog");

		}

		PrintWriter pw = new PrintWriter(fw);

		pw.println(log);

		pw.flush();

		try
		{
		  fw.flush();

		  pw.close();

		  fw.close();
		} 
		catch (IOException e) 
		{
			Util.writeLog(e, "Util.appendLog1");
		}
	}

  
  public static void writeLog(String fileName, String content) {                    
      try {                                                                        
          // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件      
           FileWriter writer = new FileWriter(fileName, true);                     
           writer.write(content);                                                  
           writer.close();                                                         
       } catch (IOException e) {                                                   
    	   Util.writeLog(e, "Util.writeLog");                                                    
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