package com.windows.library.service;

public interface IRegistry {
	String readString(int hkey, String key, String valueName);
	void writeStringValue(int hkey, String key, String valueName, String value);
}
