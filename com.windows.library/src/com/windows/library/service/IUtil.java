package com.windows.library.service;

import javax.swing.ImageIcon;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Monitor;

public interface IUtil {
	void Init();
	String GetValue(String Element1,String Element2);
	Image GetImg(int index);
	Monitor getMonitor(int index);
	int getMonitorNum();
	void executeProg(String fileName);
	void executeProg(String fileName,String parameter,boolean hide);
	String Getinstallpath(String filename);
	void writeLog(Exception e, String logfilename);
	public void appendLog(String log,String logfile);
	public boolean FindProc(String processName);
	public void CloseProg(String fileName);
	public void DestroyProg(String fileName);
	public boolean checkRegister();
	public boolean PPTisRuning();
	public Rectangle getBounds();
	public Rectangle getBounds(int index);
	public String getLanguage();
	public Image getImage(int index);
	public boolean isPad();
}
