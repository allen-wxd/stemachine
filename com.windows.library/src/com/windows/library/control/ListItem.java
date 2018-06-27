package com.windows.library.control;

import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.wb.swt.SWTResourceManager;

public class ListItem extends Composite {
	
	public interface OnItemLongClickListener {
		boolean onItemLongClick(Widget view, long position);
	}

	private int height;
	private boolean drawRect=false;
	private OnItemLongClickListener mOnItemLongClickListener;
	private long Position;
	private Timer timer;
	private boolean drawfocus=true;
	private boolean selected=false;
	private Color bcolor=null;//SWTResourceManager.getColor(SWT.COLOR_WHITE);

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public ListItem(Composite parent, int style,long position) {
		super(parent, style);
		this.setLayout(new FormLayout());
		this.Position = position;
		addListener();

	}
	public void removeListener()
	{
		Listener[] listner=getListeners(SWT.MouseDown);
		for(Listener li:listner)
		{
			removeListener(SWT.MouseDown, li);
		}
		Listener[] uplistner=getListeners(SWT.MouseUp);
		for(Listener li:uplistner)
		{
			removeListener(SWT.MouseUp,li);
		}
		Listener[] Paintlistner=getListeners(SWT.Paint);
		for(Listener li:Paintlistner)
		{
			removeListener(SWT.Paint,li);
		}
		Listener[] MenuDetectlistner=getListeners(SWT.MenuDetect);
		for(Listener li:MenuDetectlistner)
		{
			removeListener(SWT.MenuDetect,li);
		}
	}
	
	public void addListener()
	{
		addMouseListener(new MouseListener(){

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
//				if(selected)
				drawRect=true;
				ListItem.this.redraw();
				closeTimer();
				timer = new Timer();
		        timer.schedule(new TimerTask(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						closeTimer();
						if(mOnItemLongClickListener!=null)
						{
							Display.getDefault().syncExec(new Runnable() {
								@Override
								public void run() {
									mOnItemLongClickListener.onItemLongClick(e.widget,Position);
								}
							});
						  
						}
					}}, 1000, 1000); 
		        
			}

			@Override
			public void mouseUp(MouseEvent e) {
				// TODO Auto-generated method stub
				if(selected)
				  drawRect=true;
				else drawRect=false;
				ListItem.this.redraw();
				closeTimer();
			}
			
		});
	    this.addPaintListener(new PaintListener(){

			@Override
			public void paintControl(PaintEvent e) {
				// TODO Auto-generated method stub
				if(!drawfocus)return;
				if(drawRect)
				{
					e.gc.setLineWidth(5);
				    e.gc.setForeground(new Color(null,218,90,2));
				    e.gc.drawRectangle(0, 0, ((ListItem)e.widget).getBounds().width-5, ((ListItem)e.widget).getBounds().height-5);
				    e.gc.setLineWidth(1);
				    e.gc.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
				}
				else
				{
					e.gc.setLineWidth(5);
					if(bcolor!=null)
						e.gc.setForeground(bcolor);
					else
					  e.gc.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
					e.gc.drawRectangle(0, 0, ((ListItem)e.widget).getBounds().width-5, ((ListItem)e.widget).getBounds().height-5);
				    e.gc.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
				    e.gc.setLineWidth(1);
				}
			}
	    	
	    });
	    this.addMenuDetectListener(new MenuDetectListener()
		{
			@Override
			public void menuDetected(MenuDetectEvent e) {
				// TODO Auto-generated method stub
//				((GridItem)e.widget).setSelected(true);
				if (mOnItemLongClickListener != null) {
					Display.getDefault().syncExec(new Runnable() {
						@Override
						public void run() {
							mOnItemLongClickListener.onItemLongClick(e.widget, Position);
						}
					});

				}
		}});
	}
	
	public void setBackgroundcolor(Color color)
	{
		this.bcolor=color;
	}
	
	public Color getBackgroundcolor()
	{
		return this.bcolor;
	}
	
	public void setSelected(boolean selected)
	{
		drawRect = this.selected = selected;
		this.redraw();
	}
	
	public void drawFocus(boolean focus)
	{
		this.drawfocus=focus;
	}
	
	public long getPosition()
	{
		return this.Position;
	}
	
	public long setPosition(long position)
	{
		return this.Position=position;
	}
	
	public void closeTimer()
	{
		if(timer!=null)
		{
			timer.cancel();
			timer=null;
		}
	}
	
	public void setOnclick()
	{
		Control con[] = this.getChildren();
		for(Control control:con)
		{
			if(control instanceof Label)
			{
				control.addMouseListener(new MouseListener(){

					@Override
					public void mouseDoubleClick(MouseEvent e) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void mouseDown(MouseEvent e) {
						// TODO Auto-generated method stub
                      OS.SendMessage(ListItem.this.handle, OS.WM_LBUTTONDOWN, 0, 0);
					}

					@Override
					public void mouseUp(MouseEvent e) {
						// TODO Auto-generated method stub
						OS.SendMessage(ListItem.this.handle, OS.WM_LBUTTONUP, 0, 0);
					}
					
				});
			}
//			control.addListener(eventType, listener);
		}
	}
		
	public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        mOnItemLongClickListener = listener;
    }

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
	public void setHeight(int height)
	{
		this.height = height;
	}
	
	public int getHeight()
	{
		return height;
	}
	
}
