package com.windows.library.control;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DragDetectEvent;
import org.eclipse.swt.events.DragDetectListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.GestureEvent;
import org.eclipse.swt.events.GestureListener;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.TouchEvent;
import org.eclipse.swt.events.TouchListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.wb.swt.SWTResourceManager;

import com.windows.library.control.ListItem.OnItemLongClickListener;
import com.windows.library.control.SimpleListItem.OnItemClickListener;
import com.windows.library.util.Util;

public class SimpleGridItem {

	private int height;
	private int width;
	private int x,y;
	
	private boolean drawRect = false;
	private boolean selected = false;
	private boolean drawfocus = true;
	private int focuslinewidth=3;
	private int scrollbarwidth;
	private Map Contents = new HashMap();
	private Font font;
	private boolean longclick=false;

	public interface OnItemLongClickListener {
		boolean onItemLongClick(SimpleGridItem view, long position);
	}
	
	public interface OnItemClickListener {
		boolean onItemClick(SimpleGridItem view, long position);
	}
	
	public interface OnItemPaintListener {
		boolean onItemPaint(SimpleGridItem view,GC gc);
	}

	private OnItemLongClickListener mOnItemLongClickListener;
	private OnItemClickListener mOnClickListener;
	private OnItemPaintListener mOnItemPaint;
	private long Position;
	private Timer timer;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public SimpleGridItem(long index) {
		this.Position = index;
		Contents.clear();
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
	
	public void setOnItemClickListener(OnItemClickListener listener) {
        mOnClickListener = listener;
    }
	
	public void setOnItemPaintListener(OnItemPaintListener listener) {
		mOnItemPaint = listener;
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
							mOnItemLongClickListener.onItemLongClick(SimpleGridItem.this,Position);
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
					mOnItemLongClickListener.onItemLongClick(SimpleGridItem.this, Position);
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
			mOnClickListener.onItemClick(SimpleGridItem.this,Position);
		}
		longclick=false;
	}
	
	public String getString(GC gc, String str, int maxwidth) {
		int width = getStringWidth(gc, "...".toCharArray(), 0, "...".length());
		for (int i = 0; i < str.length(); i++) {
			String s = new String(str.substring(0, i));
			int width1 = getStringWidth(gc, s.toCharArray(), 0, s.length());
			if (width1 + width> maxwidth)
				return new String(str.substring(0, i - 1)) + "...";
		}
		return str;
	}
	
	public void paint(GC gc)
	{
		gc.setAdvanced(true);
		gc.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		gc.fillRectangle(x, y, width, height);
		if(selected)
		{
			gc.setLineWidth(focuslinewidth);
		    gc.setForeground(new Color(null,218,90,2));
		    gc.drawRectangle(x, y, width-focuslinewidth, height-focuslinewidth);
		    gc.setLineWidth(1);
		}
		if(mOnItemPaint!=null)
		{
			mOnItemPaint.onItemPaint(this,gc);
			return;
		}
	}
	
	public void setFont(Font f)
	{
		this.font=f;
	}
	
	public void addContent(String key,Object value)
	{
		Contents.put(key, value);
	}

	public void drawFocus(boolean focus) {
		this.drawfocus = focus;
	}

	public void setSelected(boolean selected) {
		drawRect = this.selected = selected;
	}

	public void setOnItemLongClickListener(OnItemLongClickListener listener) {
		mOnItemLongClickListener = listener;
	}

	public long getPosition() {
		return this.Position;
	}
	
	public long setPosition(long position)
	{
		return this.Position=position;
	}

	public void closeTimer() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}
	
	public void setHeight(int height) {
		this.height = height;
	}

	public int getHeight() {
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

	public void setdrawRect(boolean drawrect) {
		drawRect = this.selected = selected;
	}
}
