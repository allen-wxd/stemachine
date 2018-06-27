/*

 */

package com.windows.library.resource;

import java.io.*;

import com.library.core.library.ZLibrary;
import com.library.core.filesystem.ZLResourceFile;
import com.windows.library.service.Constants;
import com.windows.library.util.Util;


public final class ZLWindowsLibrary extends ZLibrary {
	public ZLWindowsLibrary() {
	}

	public void setActivity() {
	}

	public void rotateScreen() {

	}

	public void navigate() {

	}

	public boolean canNavigate() {
      return true;
	}

	public void finish() {
		//if ((myActivity != null) && !myActivity.isFinishing()) {
		//	myActivity.finish();
		//}
	}

	/*public View getWidget() 
	{
		if (myWidget == null) 
		{
			myWidget = myActivity.findViewById(R.id.main_view);
		}
		return myWidget;
	}*/

	public void openInBrowser(String reference) {
		//final Intent intent = new Intent(Intent.ACTION_VIEW);
		//boolean externalUrl = true;
/*		if (BookDownloader.acceptsUri(Uri.parse(reference))) {
			intent.setClass(myActivity, BookDownloader.class);
			intent.putExtra(BookDownloaderService.SHOW_NOTIFICATIONS_KEY, BookDownloaderService.Notifications.ALL);
			externalUrl = false;
		}*/
		// FIXME: initialize network library and use rewriteUrl!!!
		//reference = NetworkLibrary.Instance().rewriteUrl(reference, externalUrl);
		//intent.setData(Uri.parse(reference));
		//myActivity.startActivity(intent);
	}

	@Override
	public ZLResourceFile createResourceFile(String path) {
		return new WindowsResourceFile(path);
	}

	@Override
	public String getVersionName() {
		try {
			return "";
			//return myApplication.getPackageManager().getPackageInfo(myApplication.getPackageName(), 0).versionName;
		} catch (Exception e) {
			return "";
		}
	}

	private final class WindowsResourceFile extends ZLResourceFile 
	{
		private boolean myExists;
		private String fieldName;

		WindowsResourceFile(String path) 
		{
			super(path);
			fieldName = path.replace("/", "__").replace(".", "_").replace("-", "_").toLowerCase();
			fieldName = System.getProperty("user.dir")+"\\res\\raw\\"+fieldName;
			File file = new File(fieldName);
			file.getAbsolutePath();
			if(file!=null)
			  myExists = file.exists();
		}

		@Override
		public boolean exists() {
			return myExists;
		}

		@Override
		public long size() {
			//AssetFileDescriptor descriptor =
			//	myApplication.getResources().openRawResourceFd(myResourceId);
			//long length = descriptor.getLength();
			//descriptor.close();
			return new File(fieldName).length();
		}

		@Override
		public InputStream getInputStream() throws IOException 
		{
			if (!myExists) 
			{
				throw new IOException("File not found: " + getPath());
			}
			{
				return new FileInputStream(new File(fieldName));
			}
		}
	}
}
