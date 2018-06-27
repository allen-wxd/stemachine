/*

 */

package com.library.core.library;

import com.library.core.filesystem.ZLResourceFile;

public abstract class ZLibrary {
	public static ZLibrary Instance() {
		return ourImplementation;
	}
		
	private static ZLibrary ourImplementation;

	protected ZLibrary() {
		ourImplementation = this;
	}

	abstract public ZLResourceFile createResourceFile(String path);

	abstract public String getVersionName();
	abstract public void openInBrowser(String reference);
	abstract public void rotateScreen();
	abstract public void navigate();
	abstract public boolean canNavigate();
	abstract public void finish();
}
