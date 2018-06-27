package com.windows.library.service;

import java.io.File;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Monitor;

public interface IPacking {
	void CreateTar(String inputFile, String zipFileName);
	void unTar(File file,String outputDir);
	void zip(String inputFile, String zipFileName);
	void unZip(String zipFileName, String outputDirectory);
}
