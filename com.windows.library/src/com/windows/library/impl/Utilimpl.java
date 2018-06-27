package com.windows.library.impl;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Monitor;

import com.windows.library.resource.IconCache;
import com.windows.library.resource.MonitorResource;
import com.windows.library.resource.ZLResource;
import com.windows.library.resource.ZLWindowsLibrary;
import com.windows.library.service.IUtil;
import com.windows.library.util.Util;

public class Utilimpl implements IUtil {

	@Override
	public void Init() {
		// TODO Auto-generated method stub
		new ZLWindowsLibrary();
		Util.init();
		Util.Readerinit();
	}

	@Override
	public String GetValue(String Element1, String Element2) {
		// TODO Auto-generated method stub
		return ZLResource.resource(Element1).getResource(Element2).getValue();
	}

	@Override
	public Image GetImg(int index) {
		// TODO Auto-generated method stub
		return IconCache.Hinstance().stockImages[index];
	}

	@Override
	public Monitor getMonitor(int index) {
		// TODO Auto-generated method stub
		return MonitorResource.getMonitor(index);
	}

	@Override
	public void executeProg(String fileName) {
		// TODO Auto-generated method stub
		try {
			Util.executeProg(fileName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Util.writeLog(e, "Utilimpl.executeProg");
		}
	}

	@Override
	public String Getinstallpath(String filename) {
		// TODO Auto-generated method stub
		return Util.Getinstallpath(filename);
	}

	@Override
	public void writeLog(Exception e, String logfilename) {
		// TODO Auto-generated method stub
		Util.writeLog(e, logfilename);
	}

	@Override
	public void executeProg(String fileName, String parameter, boolean hide) {
		// TODO Auto-generated method stub
		try {
			Util.executeProg(fileName, parameter, hide);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Util.writeLog(e, "Utilimpl.executeProg1");
		}
	}

	@Override
	public int getMonitorNum() {
		// TODO Auto-generated method stub
		return MonitorResource.getMonitorNum();
	}

	@Override
	public boolean FindProc(String processName) {
		// TODO Auto-generated method stub
		return Util.FindProc(processName);
	}

	@Override
	public void CloseProg(String fileName) {
		// TODO Auto-generated method stub
		Util.CloseProg(fileName);
	}

	@Override
	public void DestroyProg(String fileName) {
		// TODO Auto-generated method stub
		Util.DestroyProg(fileName);
	}

	@Override
	public boolean checkRegister() {
		// TODO Auto-generated method stub
		return Util.checkRegister();
	}

	@Override
	public boolean PPTisRuning() {
		// TODO Auto-generated method stub
		return Util.PPTisRuning();
	}

	@Override
	public Rectangle getBounds() {
		// TODO Auto-generated method stub
		return MonitorResource.getBounds();
	}

	@Override
	public Rectangle getBounds(int index) {
		// TODO Auto-generated method stub
		return MonitorResource.getBounds(index);
	}

	@Override
	public String getLanguage() {
		// TODO Auto-generated method stub
		return ZLResource.getLanguage();
	}

	@Override
	public Image getImage(int index) {
		// TODO Auto-generated method stub
		return IconCache.Hinstance().stockImages[index];
	}

	@Override
	public void appendLog(String log, String logfile) {
		// TODO Auto-generated method stub
		Util.appendLog(log, logfile);
	}

	@Override
	public boolean isPad() {
		// TODO Auto-generated method stub
		return Util.isPad();
	} 

 }
