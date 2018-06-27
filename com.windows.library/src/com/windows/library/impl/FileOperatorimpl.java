package com.windows.library.impl;

import java.io.File;
import java.io.IOException;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Monitor;

import com.windows.library.resource.IconCache;
import com.windows.library.resource.MonitorResource;
import com.windows.library.resource.ZLResource;
import com.windows.library.resource.ZLWindowsLibrary;
import com.windows.library.service.IFileOperator;
import com.windows.library.service.IUtil;
import com.windows.library.util.FileTools;
import com.windows.library.util.Util;

public class FileOperatorimpl implements IFileOperator {

	@Override
	public boolean delAllFile(String path) {
		// TODO Auto-generated method stub
		return FileTools.delAllFile(path);
	}

	@Override
	public void copyDirectiory(String sourceDir, String targetDir) {
		// TODO Auto-generated method stub
		try {
			FileTools.copyDirectiory(sourceDir, targetDir);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Util.writeLog(e, "FileOperatorimpl.copyDirectiory");
		}
	}

	@Override
	public void fileChannelCopy(File s, File t) {
		// TODO Auto-generated method stub
		FileTools.fileChannelCopy(s, t);
	}

	

 }
