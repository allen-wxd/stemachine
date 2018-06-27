/*

 */

package com.windows.library.resource;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.swt.widgets.Display;

import com.windows.library.service.Constants;
import com.windows.library.service.Constants.ScreenOrientation;
import com.windows.library.util.LandScreen;
import com.windows.library.util.LandScreen_awt;

public class WindowManagement {

	public interface OnCloseListener {
		void Close();
		boolean isExit();
		void onConfigureChanged();
	}

	private static ArrayList<OnCloseListener> windows = new ArrayList();
	public static void addWindow(OnCloseListener window) {
		if(windows.isEmpty())
		{
			checkOrientation();
		}
		windows.add(window);
	}

	public static void clear() {
		windows.clear();
	}

	static OnCloseListener w;
	public static void closeTopWindow() {
		if (windows.isEmpty())
		{
			System.exit(1);
			return;
		}
		
		w = windows.get(windows.size() - 1);
		boolean noexist=false;
		try{
			noexist = !w.isExit()&&(!windows.isEmpty());
		}
		catch(Exception e)
		{
			noexist=true;
		}
		while(noexist)
		{
			windows.remove(w);
			w = windows.get(windows.size() - 1);
			try{
				noexist = !w.isExit()&&(!windows.isEmpty());
			}
			catch(Exception e)
			{
				noexist=true;
			}
		}
		if (w != null) {
			windows.remove(w);
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					try{
						w.Close();
						windows.remove(w);
					}
					catch(Exception e)
					{
						windows.remove(w);
					}
					
					
				}
			});

		}
	}
	
	public static void checkOrientation()
	{
		Timer timer = new Timer();
	    timer.schedule(new TimerTask(){

			@Override
			public void run() {
				// TODO Auto-generated method stub  
				Display.getDefault().asyncExec (new Runnable () {
				      public void run () {
							Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
							if(screenSize.width>screenSize.height)
							{
								LandScreen.Destroy();
								LandScreen_awt.Destroy();
								if(Constants.Orientation!=ScreenOrientation.LANDSCAPE)
								{
									Constants.Orientation=ScreenOrientation.LANDSCAPE;
									for(int i=windows.size()-1;i>=0;i--)
									{
										windows.get(i).onConfigureChanged();
										if(LandScreen.isShowing()||LandScreen_awt.Showing())
										  break;
									}
								}		
							}
							else
							{
								if(Constants.Orientation!=ScreenOrientation.PORTRAIT)
								{
									Constants.Orientation=ScreenOrientation.PORTRAIT;
									for(int i=windows.size()-1;i>=0;i--)
									{
										windows.get(i).onConfigureChanged();
										if(LandScreen.isShowing()||LandScreen_awt.Showing())
										  break;
									}
								}
							}
				      }
				   });
				
			}}, 1000, 1000); 
	}
}
