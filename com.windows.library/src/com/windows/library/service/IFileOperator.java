package com.windows.library.service;

import java.io.File;

public interface IFileOperator {
	boolean delAllFile(String path);
	void copyDirectiory(String sourceDir, String targetDir);
	public void fileChannelCopy(File s, File t);
}
