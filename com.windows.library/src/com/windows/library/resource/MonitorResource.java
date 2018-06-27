/*

 */

package com.windows.library.resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Monitor;

public class MonitorResource 
{
	
	public static int getMonitorNum() 
	{
		return Display.getCurrent().getMonitors().length;
	}
	
	public static Monitor getMonitor(int index)
	{
		if(index>=getMonitorNum())
	      return Display.getCurrent().getMonitors()[0];
		return Display.getCurrent().getMonitors()[index];
	}
	
	public static Rectangle getBounds()
	{
		Monitor monitors[] = Display.getDefault().getMonitors();
		Rectangle finalRec=null;

		for(int i=0;i<monitors.length;i++)
		{
			if(finalRec==null)
			  finalRec=monitors[i].getBounds();
			else finalRec=finalRec.union(monitors[i].getBounds());
		}
		return finalRec;
	}
	
	@SuppressWarnings("unchecked")
	public static Rectangle getBounds(int index)
	{
		Monitor monitors[] = Display.getCurrent().getMonitors();
		Rectangle finalRec=null;
		ArrayList<Rectangle> List = new ArrayList<Rectangle>();
		List.clear();
		for(int i=0;i<monitors.length;i++)
		{
			List.add(monitors[i].getBounds());
		}
		Collections.sort(List, new Comparator(){

			@Override
			public int compare(Object arg0, Object arg1) {
				// TODO Auto-generated method stub
				Rectangle r0=(Rectangle)arg0;
				Rectangle r1=(Rectangle)arg1;
				return r0.x-r1.x;
			}});
		if(index<List.size())
		  finalRec=List.get(index);
		
		return finalRec;
	}
}
