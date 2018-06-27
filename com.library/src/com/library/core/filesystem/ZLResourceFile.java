package com.library.core.filesystem;

import com.library.core.library.ZLibrary;

public abstract class ZLResourceFile extends ZLFile {
	public static ZLResourceFile createResourceFile(String path) {
		return ZLibrary.Instance().createResourceFile(path);
	}

	private final String myPath;
	
	protected ZLResourceFile(String path) {
		myPath = path;
		init();
	}
	
	public boolean isDirectory() {
		return false;
	}
	
	public String getPath() {
		return myPath;
	}
	
	public String getNameWithExtension() {
		return myPath;
	}

	public ZLFile getParent() {
		return null;
	}

	public ZLPhysicalFile getPhysicalFile() {
		return null;
	}
}
