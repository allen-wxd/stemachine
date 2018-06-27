package com.windows.library.control;

import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.wb.swt.SWTResourceManager;

import com.windows.library.util.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;

public abstract class SimpleListViewer extends Composite {
	
	public interface OnScrollListener {
		void OnScroll();
	}
	
	public interface OnClickListener {
		boolean onClick();
	}
	
	public interface LayoutViewListener {
		boolean onLayout(SimpleListItem view,Composite back);
	}
    private ArrayList<SimpleListItem> Items = new ArrayList();
    private SimpleListItem focusitem=null;
    private int count;
    private int rowheight;
    private int bottommargin=10;  
 	
 	private ScrollBar vBar;
 	
 	private OnScrollListener mOnScrollListener;
 	private OnClickListener mOnClickListener;
 	private LayoutViewListener mLayoutViewListener;
 	private Composite background;
 	private int ydisplacement;
 	private int contentheight;
    private Point movedownPoint=new Point(-1,-1);
    private boolean movemode=false;
    private boolean mousedown=false;
// 	private boolean longclick=false;
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public SimpleListViewer(Composite parent, int style) {
		super(parent, style|SWT.DOUBLE_BUFFERED);
		setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        this.setLayout(new FormLayout());
        
        background = new Composite(this,SWT.DOUBLE_BUFFERED);
        background.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        FormData formdata = new FormData();
        formdata.top = new FormAttachment(0);
	    formdata.bottom = new FormAttachment(100);
	    formdata.left = new FormAttachment(0);
	    formdata.right = new FormAttachment(100);
	    background.setLayoutData(formdata);
	    background.setLayout(new FormLayout());
	    
	    background.addPaintListener(new PaintListener(){
			@Override
			public void paintControl(PaintEvent e) {
				// TODO Auto-generated method stub
				GC gc = e.gc;
//				if(focusitem!=null)
//				{
//					focusitem.paint(gc);
//				}
//				else
				{
					for(SimpleListItem item:Items)
					{
						if(item.gety()+item.getHeight()>ydisplacement&&item.gety()<ydisplacement+contentheight)
						  item.paint(gc);
					}
				}
			}
	    });
	    background.addMouseListener(new MouseListener() {

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
//				if(focusitem!=null)
//				{
//				  focusitem.setSelected(false);
//				  focusitem=null;
//				}
				mousedown=true;
				clearSelected();
				for(SimpleListItem item:Items)
				{
					if(item.gety()+item.getHeight()>e.y&&item.gety()<e.y)
					{
					  focusitem=item;
					  focusitem.setSelected(true);
					  background.redraw();
					  item.mouseDown(e);
					  break;
					}
				}
			}

			@Override
			public void mouseUp(MouseEvent e) {
				// TODO Auto-generated method stub
				movedownPoint.x=-1;
				movedownPoint.y=-1;
				mousedown=false;
				if(movemode)
				{
					movemode=false;
					return;
				}
				if(focusitem!=null)
				  focusitem.mouseUp(e);
				else
				{
					if(mOnClickListener!=null)
					{
					  mOnClickListener.onClick();
					}
				}
				background.redraw();
			}
		});
	    
	    background.addMenuDetectListener(new MenuDetectListener()
		{
			@Override
			public void menuDetected(MenuDetectEvent e) {
				// TODO Auto-generated method stub
				if(focusitem!=null)
				  focusitem.addMenuDetectListener(e);
//				for(SimpleListItem item:Items)
//				{
//					if(item.gety()+item.getHeight()>e.y&&item.gety()<e.y)
//					{
//					  item.addMenuDetectListener(e);
//					  break;
//					}
//				}
		}});
	    
	    background.addMouseMoveListener(new MouseMoveListener(){

			@Override
			public void mouseMove(MouseEvent arg0) {
				// TODO Auto-generated method stub
				if(!mousedown)
				  return;
				ScrollBar vBar = getVerticalBar ();
				if(movedownPoint.x==-1)
				{
					movedownPoint.x=arg0.x;
					movedownPoint.y=arg0.y-vBar.getSelection();;
				}
				else
				{
					int distancey=movedownPoint.y-(arg0.y-vBar.getSelection());
					if(Math.abs(distancey)>5&&vBar.isVisible())
					{
						if(focusitem!=null)
						  focusitem.closeTimer();
						movemode=true;
						movedownPoint.x=arg0.x;
						movedownPoint.y=arg0.y-vBar.getSelection();
						int newposition = vBar.getSelection()+distancey;
						seekto(newposition);
					}
				}
			}});
	}
	
	public void clearSelected()
	{
		if(focusitem!=null)
		{
		  focusitem.setSelected(false);
		  focusitem=null;
		}
		background.redraw();
	}
	
	public void Repaint()
	{
		background.redraw();
	}
	
	public void setOnScrollListener(OnScrollListener listener) {
		mOnScrollListener = listener;
    }
	
	public void setLayoutviewListener(LayoutViewListener listener) {
		mLayoutViewListener = listener;
    }
	
	public void setRowheight(int height)
	{
		this.rowheight = height;
	}
	
	public void setOnClickListener(OnClickListener listener) {
        mOnClickListener = listener;
    }
	
	public int getScrollOffset() {
		vBar = getVerticalBar ();
		return vBar.getSelection();
    }
	
	public int getRowheight()
	{
		return this.rowheight;
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
	public void setCount(int count)
	{
		this.count=count;
	}
	
	protected SimpleListItem getView(SimpleListItem item,long index,Composite parent){
		return null;
	}
	
	public void setContent()
	{
		Init();
	}
	
	public void Init()
	{ 	
//		for(int i=0;i<Items.size();i++)
//		{
//			Items.get(i).dispose();
//		}
		vBar = getVerticalBar ();
        layout();
		Items.clear();
		SimpleListItem pitem=null;
		for(int i=0;i<count;i++)
		{
			SimpleListItem item= this.getView(null,i,background);
			item.setx(0);
			if(pitem==null)
				item.sety(1);
			else
				item.sety(pitem.gety()+pitem.getHeight());
			item.setPosition(i);
//			item.setOnclick();
//			FormData formdata = new FormData();
//			if(pitem==null)
//			{
//			  formdata.top = new FormAttachment(0);
//			  formdata.bottom = new FormAttachment(0,rowheight);
//			}
//			else 
//			{
//			  formdata.top = new FormAttachment(pitem,-1,SWT.BOTTOM);
//			  formdata.bottom = new FormAttachment(pitem,rowheight-1,SWT.BOTTOM);
//			}
//			formdata.left = new FormAttachment(0);
//			formdata.right = new FormAttachment(100);
//			
//			item.setLayoutData(formdata);	
//			if(item.getBackgroundcolor()==null)
//			  item.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
//			else
//			  item.setBackground(item.getBackgroundcolor());
//			((FormData)item.getLayoutData()).left.offset=Integer.toUnsignedLong(i);
			
			pitem=item;
			item.setWidth(background.getBounds().width+vBar.getThumbBounds().width);
			item.setscrollbarwidth(vBar.getThumbBounds().width);
			setRowheight(item.getHeight());
			if(mLayoutViewListener!=null)
			{
			  mLayoutViewListener.onLayout(item, background);
			}
			Items.add(item);
//			if(enough) break;
//			if(i*rowheight+rowheight>=background.getBounds().height)
//			  enough=true;
			
		}
		ydisplacement=vBar.getSelection();
		contentheight=background.getBounds().height;
		background.redraw();
		
	    if(count*rowheight+bottommargin<this.getBounds().height)
	    	vBar.setVisible(false);
	    else vBar.setVisible(true);
	    if(!vBar.isVisible())
	    {
	    	vBar.setSelection(0);
	    	vScroll();
	    	return;
	    }
		vBar.setMaximum(count*rowheight+bottommargin);
		vBar.setIncrement(rowheight);
		vBar.setThumb(this.getBounds().height);
		int maxselection = vBar.getMaximum()-vBar.getThumb();
		if(ydisplacement>maxselection)//删除最后一行后调整垂直位置，否则最后一行会留空白
		{
		  if(maxselection<0)
			maxselection=0;
		  vBar.setSelection(maxselection);
		  vScroll();
		}
		vBar.setPageIncrement(rowheight*50);
		if (vBar != null&&vBar.isVisible()) {
			vBar.addListener(SWT.Selection, new Listener () {
				public void handleEvent (Event e) {
					vScroll();
				}
			});
		}
	}
	
	public void vScroll()
	{
		ScrollBar vBar = getVerticalBar ();
		long position = (long)vBar.getSelection();
		ydisplacement=0;
		ydisplacement = (int) position;
		((FormData)background.getLayoutData()).top.offset=0-(ydisplacement);
		background.redraw();
		if(mOnScrollListener!=null)
		  mOnScrollListener.OnScroll();
		this.layout();
	}
	
	public void seekto(int position)
	{
		ScrollBar vBar = getVerticalBar ();
		vBar.setSelection(position);
		vScroll();
	}
}
