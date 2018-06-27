package com.windows.library.control;

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
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.wb.swt.SWTResourceManager;

import com.windows.library.control.ListItem.OnItemLongClickListener;
import com.windows.library.util.Util;

public class GridItem extends Composite {

	private int height;
	private boolean drawRect = false;
	private boolean selected = false;
	private boolean drawfocus = true;

	public interface OnItemLongClickListener {
		boolean onItemLongClick(Widget view, long position);
	}

	private OnItemLongClickListener mOnItemLongClickListener;
	private long Position;
	private Timer timer;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public GridItem(Composite parent, int style, long index) {
		super(parent, style | SWT.DOUBLE_BUFFERED);
		this.setLayout(new FormLayout());
		this.Position = index;
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
		addMouseListener(new MouseListener() {

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
				System.out.println("mouseDown");
//				Util.appendLog("mouseDown="+System.currentTimeMillis(), "mouseclick.txt");
				drawRect = true;
				GridItem.this.redraw();
				closeTimer();
				timer = new Timer();
				timer.schedule(new TimerTask() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						closeTimer();
						if (mOnItemLongClickListener != null) {
							Display.getDefault().syncExec(new Runnable() {
								@Override
								public void run() {
									mOnItemLongClickListener.onItemLongClick(e.widget, Position);
								}
							});

						}
					}
				}, 1000, 1000);
			}

			@Override
			public void mouseUp(MouseEvent e) {
				// TODO Auto-generated method stub
				System.out.println("mouseUp");
//				Util.appendLog("mouseUp="+System.currentTimeMillis(), "mouseclick.txt");
				if (selected)
					drawRect = true;
				else
					drawRect = false;
				GridItem.this.redraw();
				closeTimer();
			}

		});
		
		this.addPaintListener(new PaintListener() {

			@Override
			public void paintControl(PaintEvent e) {
				// TODO Auto-generated method stub
				System.out.println("paintControl");
				if (!drawfocus)
					return;
				if (drawRect) {
					System.out.println("paintControl11111111");
					e.gc.setLineWidth(10);
					e.gc.setForeground(new Color(null, 218, 90, 2));
					e.gc.drawRectangle(0, 0, ((GridItem) e.widget).getBounds().width,
							((GridItem) e.widget).getBounds().height);
					e.gc.setLineWidth(1);
					e.gc.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
				} else {
					Color color = GridItem.this.getBackground();
					e.gc.setLineWidth(10);
					e.gc.setForeground(color);
					e.gc.drawRectangle(0, 0, ((GridItem) e.widget).getBounds().width,
							((GridItem) e.widget).getBounds().height);
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

	public void drawFocus(boolean focus) {
		this.drawfocus = focus;
	}

	public void setSelected(boolean selected) {
		drawRect = this.selected = selected;
		this.redraw();
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

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getHeight() {
		return height;
	}

	public void setdrawRect(boolean drawrect) {
		drawRect = this.selected = selected;
		this.redraw();
	}
}
