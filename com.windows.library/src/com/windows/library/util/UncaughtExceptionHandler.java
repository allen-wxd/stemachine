
package com.windows.library.util;

import java.io.*;

public class UncaughtExceptionHandler implements java.lang.Thread.UncaughtExceptionHandler {

	public UncaughtExceptionHandler() {
		
	}

	public void uncaughtException(Thread thread, Throwable exception) {
		System.out.println("3333333333333333333333333333333333333333");
       Util.writeLog(exception, "");
	}	
}