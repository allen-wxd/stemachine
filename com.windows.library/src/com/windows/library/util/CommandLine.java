package com.windows.library.util;

public class CommandLine {
	private native static String getcommandline();
	static {
		System.loadLibrary("getCommandLine");
	}
	
	public static String getline()
	{
		return getcommandline();
	}
}
