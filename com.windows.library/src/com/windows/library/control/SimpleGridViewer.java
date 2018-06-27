package com.windows.library.control;

import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.wb.swt.SWTResourceManager;

import com.windows.library.control.SimpleListViewer.OnClickListener;

import java.util.ArrayList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;

public abstract class SimpleGridViewer extends Composite {
	
	public interface OnScrollListener {
		void OnScroll();
	}
	
    private ArrayList<SimpleGridItem> Items = new ArrayList();
    private ArrayList<Integer> RowItems = new ArrayList();
    private int count;
    private int column;
    private SimpleGridItem focusitem=null;
    
    private int MarginLeft=40;//：表示当前组件距离父组件左边距的像素点个数。 
    private int MarginRight=40;//：表示当前组件距离父组件右边距的像素点个数。 
    private int MarginTop=60;//：表示当前组件距离父组件上边距的像素点个数。 
    private int MarginBottom=20;//：表示当前组件距离父组件下边距的像素点个数。 
    private int HorizontalSpacing=30;//：表示子组件的水平间距。 
    private int VerticalSpacing=30;//：表示子组件的垂直间距。 
    
    private int colheight,colwidth;
    
    private float ratio; //(width/heigh);
    
    private OnScrollListener mOnScrollListener;
    private OnClickListener mOnClickListener;
    private Composite background;
    private int ydisplacement;
    private int contentheight;
    private Point movedownPoint=new Point(-1,-1);
    private boolean movemode=false;
    private boolean mousedown=false;

    
//    private Composite Content;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public SimpleGridViewer(Composite parent, int style) {
		super(parent, style|SWT.DOUBLE_BUFFERED);
		this.setBackgroundMode(SWT.INHERIT_FORCE);
		setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        this.setLayout(new FormLayout());
        background = new Composite(this,SWT.DOUBLE_BUFFERED);
        this.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
//        background.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
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
				{
					for(SimpleGridItem item:Items)
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
				for(SimpleGridItem item:Items)
				{
					if(item.gety()+item.getHeight()>e.y&&item.gety()<e.y
							&&e.x>item.getx()&&e.x<item.getx()+item.getWidth())
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
					if(!background.isDisposed())
					  background.redraw();
				}
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
						System.out.println("newposition="+newposition);
					}
				}
			}});
        ratio=0;
	}
	
	public void seekto(int position)
	{
		ScrollBar vBar = getVerticalBar ();
		vBar.setSelection(position);
		vScroll();
	}
	
	public SimpleGridItem getFocusItem()
	{
		return focusitem;
	}
	
	public int gety()
	{
		return ydisplacement;
	}
	
	public void setOnClickListener(OnClickListener listener) {
        mOnClickListener = listener;
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
	
	public void repaint()
	{
		background.redraw();
	}
	
	public void setOnScrollListener(OnScrollListener listener) {
		mOnScrollListener = listener;
    }

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
	public void setColumn(int column)
	{
		this.column=column;
	}
	
	public int getColumn() {
		return column;
	}

	public void setCount(int count)
	{
		this.count=count;
	}
	
	public void setratio(float ratio)
	{
		this.ratio=ratio;
	}
	
	protected SimpleGridItem getView(SimpleGridItem item,long index){
		return null;
	}
	
	public void setContent()
	{
	  Init();
	}
	
	public void Init()
	{
		boolean newline=false;
		SimpleGridItem pitem=null;
		int right=0;
		colwidth = (background.getBounds().width-MarginLeft-MarginRight-(HorizontalSpacing*(column-1)))/column;
		if(ratio==0)
		  colheight = colwidth;
		else
		{
			float height = ((float)colwidth)/ratio+0.5f;
			colheight = (int) height;
		}
		RowItems.clear();
		Items.clear();
		for(int i=0;i<count;i++){
			SimpleGridItem item= this.getView(null,i);
			Items.add(item);
			if(i%column==0)
			{
			  RowItems.add(i);
			  newline=true;
			}
			  
			if(pitem==null)
			{
			  item.setx(MarginLeft);
			  item.sety(MarginTop);
			  item.setHeight(colheight);
			  item.setWidth(colwidth);
			  newline=false;
//			  bottom = MarginTop;
			  right = MarginLeft+colwidth+HorizontalSpacing;
			}
			else 
			{
			  
			  if(newline)
			  {
				  item.setx(MarginLeft);
				  item.sety(MarginTop+(RowItems.size()-1)*(colheight+VerticalSpacing));
				  item.setHeight(colheight);
				  item.setWidth(colwidth);
				  newline=false;
//				  bottom = MarginTop+RowItems.size()*(colheight+VerticalSpacing);
				  right = MarginLeft+colwidth+HorizontalSpacing;
			  }
			  else
			  {
				  item.setx(right);
				  item.sety(MarginTop+(RowItems.size()-1)*(colheight+VerticalSpacing));
				  item.setHeight(colheight);
				  item.setWidth(colwidth);
				  right = right+colwidth+HorizontalSpacing;
			  }
			}
			pitem=item;
        }  
		background.layout();
		contentheight=background.getBounds().height;
		ScrollBar vBar = this.getVerticalBar();
		ydisplacement = vBar.getSelection();
		int lines = count/column;
		if((count%column)!=0)lines++;
		background.redraw();
	    if(MarginTop+(colheight+VerticalSpacing)*(lines)+MarginBottom<this.getBounds().height)
	    	vBar.setVisible(false);
	    else vBar.setVisible(true);
	    if(!vBar.isVisible())
	    {
	    	vBar.setSelection(0);
	    	vScroll();
	    	return;
	    }
	    	
		vBar.setMaximum(MarginTop+(colheight+VerticalSpacing)*(lines)+MarginBottom);
		vBar.setIncrement(50);
		vBar.setThumb(this.getBounds().height);
		int maxselection = vBar.getMaximum()-vBar.getThumb();
		if(ydisplacement>maxselection)//删除最后一行后调整垂直位置，否则最后一行会留空白
		{
		  if(maxselection<0)
			maxselection=0;
		  vBar.setSelection(maxselection);
		  vScroll();
		}
		vBar.setPageIncrement(this.getBounds().height);
		if (vBar != null&&vBar.isVisible()) {
			vBar.addListener(SWT.Selection, new Listener () 
			{
				public void handleEvent (Event e) {
					vScroll();
				}
			});
		}
		this.setFocus();
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
	
	public SimpleGridItem getItem(int position)
	{
		if(Items.size()==0||position<0)return null;
		if(position<Items.size())
		  return Items.get(position);
        return null;
	}
}
