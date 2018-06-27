package com.library.core.filesystem;

import java.util.*;
import java.io.*;

public final class ZLPhysicalFile extends ZLFile {
	private final File myFile;
	
	ZLPhysicalFile(String path) {
		this(new File(path));
	}

	public ZLPhysicalFile(File file) {
		myFile = file;
		init();
	}
	
	@Override
	public boolean exists() {
		return myFile.exists();
	}
	
	@Override
	public long size() {
		return myFile.length();
	}	
	
	@Override
	public boolean isDirectory() {
		return myFile.isDirectory();
	}
	
	public boolean delete() {
		return myFile.delete();
	}

	@Override
	public String getPath() {
		return myFile.getPath();
	}
	
	@Override
	public String getNameWithExtension() {
		return isDirectory() ? getPath() : myFile.getName();
	}

	@Override
	public ZLFile getParent() {
		return isDirectory() ? null : new ZLPhysicalFile(myFile.getParent());
	}

	@Override
	public ZLPhysicalFile getPhysicalFile() {
		return this;
	}
    
	@Override
	public InputStream getInputStream() throws IOException {
		return new FileInputStream(myFile);
	}

	protected List<ZLFile> directoryEntries() {
		File[] subFiles = myFile.listFiles();
		if ((subFiles == null) || (subFiles.length == 0)) {
			return Collections.emptyList();
		}

		ArrayList<ZLFile> entries  = new ArrayList<ZLFile>(subFiles.length);
		for (File f : subFiles) {
			if (!f.getName().startsWith(".")) {
				entries.add(new ZLPhysicalFile(f));
			}
		}
		return entries;
	}
}
