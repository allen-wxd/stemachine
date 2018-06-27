package com.windows.library.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Monitor;

import com.windows.library.util.FileTools;
import com.windows.library.util.IniEditor;

public interface IiniOperator {
	  public void setIni(String Section,String option,String value);
	  public void deleteOption(String filename,String Section,String option);
	  public List<String> getOptionList(String filename,String Section);
	  public int getOptionnum(String Section);
	  public void copyinioption(String Section,String option,File src,File dst);
	  public String getIni(String Section,String option);
	  
}
