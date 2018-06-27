package com.windows.library.control;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.wb.swt.SWTResourceManager;


public class SimpleListItem {
	
	public interface OnItemLongClickListener {
		boolean onItemLongClick(SimpleListItem view, long position);
	}
	
	public interface OnItemClickListener {
		boolean onItemClick(SimpleListItem view, long position);
	}
	
	public interface OnItemPaintListener {
		boolean onItemPaint(SimpleListItem view,GC gc);
	}

	private int height;
	private int width;
	private int x,y;
	private boolean drawRect=false;
	private OnItemLongClickListener mOnItemLongClickListener;
	private OnItemClickListener mOnClickListener;
	private OnItemPaintListener mOnPaintListener;
	private long Position;
	private Timer timer;
	private boolean drawfocus=false;
	private boolean selected=false;
	private Color bcolor=null;//SWTResourceManager.getColor(SWT.COLOR_WHITE);
	private Map Contents = new HashMap();
	private Font font;
	private boolean longclick=false;
	private int scrollbarwidth;
	private int focuslinewidth=3;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public SimpleListItem(long position) {
		this.Position = position;
		Contents.clear();
	}
	
	public void setBackgroundcolor(Color color)
	{
		this.bcolor=color;
	}
	
	public void addContent(String key,Object value)
	{
		Contents.put(key, value);
	}
	
	public void setFont(Font f)
	{
		this.font=f;
	}
	
	public Color getBackgroundcolor()
	{
		return this.bcolor;
	}
	
	public void setSelected(boolean selected)
	{
		this.selected = selected;
	}
	
	public int getStringWidth(GC gc, char[] string, int offset, int length) {
		int width = 0;
		for (int i = 0; i < length; i++) {
			width += gc.getAdvanceWidth(string[offset + i]);
		}
		width = (int) (width + 0.5f);
		return width;
	}

	protected int getStringHeightInternal(GC gc) {
		return (int) (gc.getFontMetrics().getHeight() + 0.5f);
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
	
	public void mouseDown(MouseEvent e) {
		// TODO Auto-generated method stub
		closeTimer();
		timer = new Timer();
        timer.schedule(new TimerTask(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				closeTimer();
				if(mOnItemLongClickListener!=null)
				{
					longclick=true;
					Display.getDefault().syncExec(new Runnable() {
						@Override
						public void run() {
							mOnItemLongClickListener.onItemLongClick(SimpleListItem.this,Position);
						}
					});
				  
				}
			}}, 1000, 1000); 
	}
	
	public void addMenuDetectListener(MenuDetectEvent e)
	{
		if (mOnItemLongClickListener != null) {
			longclick=true;
			Display.getDefault().syncExec(new Runnable() {
				@Override
				public void run() {
					mOnItemLongClickListener.onItemLongClick(SimpleListItem.this, Position);
				}
			});

		}
	}

	public void mouseUp(MouseEvent e) {
		// TODO Auto-generated method stub
		closeTimer();
		if(mOnClickListener!=null&&!longclick)
		{
			longclick=false;
			mOnClickListener.onItemClick(SimpleListItem.this,Position);
		}
		longclick=false;
	}
	
	public void paint(GC gc)
	{
//		gc.setAdvanced(true);
		if(bcolor!=null)
			gc.setBackground(bcolor);
		else
		  gc.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		gc.fillRectangle(x, y, width, height);
		if(selected)
		{
			gc.setLineWidth(focuslinewidth);
		    gc.setForeground(new Color(null,218,90,2));
			gc.drawLine(x, y, x+width, y);
			gc.drawLine(x, y+height-focuslinewidth, x+width, y+height-focuslinewidth);
		    gc.setLineWidth(1);
		}
		else
		{
			gc.setLineWidth(1);
			gc.setForeground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
			gc.drawLine(x, y, x+width, y);
			gc.drawLine(x, y+height, x+width, y+height);
		}
		if(mOnPaintListener!=null)
		{
			mOnPaintListener.onItemPaint(this, gc);
			return;
		}
		int sx=x;
		gc.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		gc.setFont(font);
		Set set = Contents.entrySet();         
		Iterator i = set.iterator();         
		while(i.hasNext()){      
		     Map.Entry<String, Object> entry=(Map.Entry<String, Object>)i.next();  
		     Object obj=entry.getValue();
		     String key=entry.getKey();
		     int left=0;
		     int top=0;
		     if(obj instanceof String)
		     {
		    	 String[]list = key.split(":");
		    	 if(list[0].contains("%"))
		    	 {
		    		 if(list[0].equals("100%"))
		    		 {
		    			 left = getWidth()-scrollbarwidth-getStringWidth(gc,((String)obj).toCharArray(),0,((String)obj).toCharArray().length);
		    		 }
		    	 }
		    	 else left=Integer.parseInt(list[0]);
		    	 gc.drawString((String)obj, sx+left, y+Integer.parseInt(list[1]),true);
		     }
		}
	}//（1-）附件-副本
	
	private String String(Object obj) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setscrollbarwidth(int widht)
	{
		scrollbarwidth = widht;
	}
		
	public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        mOnItemLongClickListener = listener;
    }
	
	public void setOnItemPaintListener(OnItemPaintListener listener) {
        mOnPaintListener = listener;
    }
	
	public void setOnItemClickListener(OnItemClickListener listener) {
        mOnClickListener = listener;
    }
	
	public void setHeight(int height)
	{
		this.height = height;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public void setWidth(int width)
	{
		this.width = width;
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public void setx(int x)
	{
		this.x = x;
	}
	
	public int getx()
	{
		return x;
	}
	
	public void sety(int y)
	{
		this.y = y;
	}
	
	public int gety()
	{
		return y;
	}
}
