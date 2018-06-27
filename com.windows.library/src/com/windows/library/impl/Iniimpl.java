package com.windows.library.impl;

import java.io.File;
import java.util.List;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Monitor;

import com.windows.library.resource.IconCache;
import com.windows.library.resource.MonitorResource;
import com.windows.library.resource.ZLResource;
import com.windows.library.resource.ZLWindowsLibrary;
import com.windows.library.service.IUtil;
import com.windows.library.service.IiniOperator;
import com.windows.library.util.Util;

public class Iniimpl implements IiniOperator {

	@Override
	public void setIni(String Section, String option, String value) {
		// TODO Auto-generated method stub
		Util.setIni(Section, option, value);
	}

	@Override
	public void deleteOption(String filename, String Section, String option) {
		// TODO Auto-generated method stub
		Util.deleteOption(filename, Section, option);
	}

	@Override
	public List<String> getOptionList(String filename, String Section) {
		// TODO Auto-generated method stub
		return Util.getOptionList(filename, Section);
	}

	@Override
	public int getOptionnum(String Section) {
		// TODO Auto-generated method stub
		return Util.getOptionnum(Section);
	}

	@Override
	public void copyinioption(String Section, String option, File src, File dst) {
		// TODO Auto-generated method stub
		Util.copyinioption(Section, option, src, dst);
	}

	@Override
	public String getIni(String Section, String option) {
		// TODO Auto-generated method stub
		return Util.getIni(Section, option);
	}

 }
