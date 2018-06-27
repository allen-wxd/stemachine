package com.windows.library.impl;

import java.lang.reflect.InvocationTargetException;
import com.windows.library.service.IRegistry;
import com.windows.library.util.Util;
import com.windows.library.util.WinRegistry;

public class Registryimpl implements IRegistry {

	@Override
	public String readString(int hkey, String key, String valueName) {
		// TODO Auto-generated method stub
		String value="";
		try {
			value = WinRegistry.readString(hkey, key, valueName);
		} catch (IllegalArgumentException e) {
			Util.writeLog(e, "Registryimpl.readString");
			// TODO Auto-generated catch block
		} catch (IllegalAccessException e) {
			Util.writeLog(e, "Registryimpl.readString1");
			// TODO Auto-generated catch block
		} catch (InvocationTargetException e) {
			Util.writeLog(e, "Registryimpl.readString2");
			// TODO Auto-generated catch block
		}
		return value;
	}

	@Override
	public void writeStringValue(int hkey, String key, String valueName, String value) {
		// TODO Auto-generated method stub
		try {
			WinRegistry.writeStringValue(hkey, key, valueName, value);
		} catch (IllegalArgumentException e) {
			Util.writeLog(e, "Registryimpl.writeStringValue");
			// TODO Auto-generated catch block
		} catch (IllegalAccessException e) {
			Util.writeLog(e, "Registryimpl.writeStringValue1");
			// TODO Auto-generated catch block
		} catch (InvocationTargetException e) {
			Util.writeLog(e, "Registryimpl.writeStringValue2");
			// TODO Auto-generated catch block
		}
	}
 }
