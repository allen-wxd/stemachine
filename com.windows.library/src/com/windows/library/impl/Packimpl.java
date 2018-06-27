package com.windows.library.impl;

import java.io.File;
import java.io.IOException;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Monitor;

import com.windows.library.resource.IconCache;
import com.windows.library.resource.MonitorResource;
import com.windows.library.resource.ZLResource;
import com.windows.library.resource.ZLWindowsLibrary;
import com.windows.library.service.IPacking;
import com.windows.library.service.IUtil;
import com.windows.library.util.Util;
import com.windows.library.util.ZipTools;

public class Packimpl implements IPacking {

	@Override
	public void CreateTar(String inputFile, String zipFileName) {
		// TODO Auto-generated method stub
		ZipTools.CreateTar(inputFile, zipFileName);
	}

	@Override
	public void unTar(File file, String outputDir) {
		// TODO Auto-generated method stub
		try {
			ZipTools.unTar(file, outputDir);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Util.writeLog(e, "Packimpl.unTar");
		}
	}

	@Override
	public void zip(String inputFile, String zipFileName) {
		// TODO Auto-generated method stub
		ZipTools.zip(inputFile, zipFileName);
	}

	@Override
	public void unZip(String zipFileName, String outputDirectory) {
		// TODO Auto-generated method stub
		ZipTools.unZip(zipFileName, outputDirectory);
	}
 }
