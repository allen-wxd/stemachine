package com.windows.library.control;

import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.wb.swt.SWTResourceManager;

import com.windows.library.control.ListItem.OnItemLongClickListener;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.MouseListener;

public abstract class GridViewer extends Composite {
	
	public interface OnScrollListener {
		void OnScroll();
	}
	
    private ArrayList<GridItem> Items = new ArrayList();
    private ArrayList<Integer> RowItems = new ArrayList();
    private int count;
    private int column;
    
    private int MarginLeft=40;//：表示当前组件距离父组件左边距的像素点个数。 
    private int MarginRight=40;//：表示当前组件距离父组件右边距的像素点个数。 
    private int MarginTop=60;//：表示当前组件距离父组件上边距的像素点个数。 
    private int MarginBottom=20;//：表示当前组件距离父组件下边距的像素点个数。 
    private int HorizontalSpacing=30;//：表示子组件的水平间距。 
    private int VerticalSpacing=30;//：表示子组件的垂直间距。 
    
    private int colheight,colwidth;
    
    private float ratio; //(width/heigh);
    
    private OnScrollListener mOnScrollListener;
    private Composite background;

    
//    private Composite Content;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public GridViewer(Composite parent, int style) {
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
	    
        ratio=0;
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
	
	protected GridItem getView(GridItem item,long index,Composite parent){
		return null;
	}
	
	public void setContent()
	{
		Init();  
		
	}
	
	public void Init()
	{
		boolean enough=false;

		boolean newline=false;
		GridItem pitem=null;
		int bottom=0;
		colwidth = (background.getBounds().width-MarginLeft-MarginRight-(HorizontalSpacing*(column-1)))/column;
		if(ratio==0)
		  colheight = colwidth;
		else
		{
			float height = ((float)colwidth)/ratio+0.5f;
			colheight = (int) height;
		}
		for(int i=0;i<Items.size();i++)
		{
			Items.get(i).dispose();
		}
		RowItems.clear();
		Items.clear();
		for(int i=0;i<count;i++){
			GridItem item= this.getView(null,i,background);
			Items.add(item);
			if(i%column==0)
			{
			  RowItems.add(i);
			  newline=true;
			}
			  
			FormData formdata = new FormData();
			if(pitem==null)
			{
			  formdata.top = new FormAttachment(0,MarginTop);
			  formdata.bottom = new FormAttachment(0,colheight+MarginTop);
			  formdata.left = new FormAttachment(0,MarginLeft);
			  formdata.right = new FormAttachment(0,MarginLeft+colwidth);
			  newline=false;
			  bottom = MarginTop+RowItems.size()*(colheight+VerticalSpacing);
			}
			else 
			{
			  
			  if(newline)
			  {
				  formdata.left = new FormAttachment(0,MarginLeft);
				  formdata.right = new FormAttachment(0,MarginLeft+colwidth);
				  formdata.top = new FormAttachment(pitem,VerticalSpacing,SWT.BOTTOM);
				  formdata.bottom = new FormAttachment(pitem,VerticalSpacing+colheight,SWT.BOTTOM);
				  newline=false;
				  bottom = MarginTop+RowItems.size()*(colheight+VerticalSpacing);
			  }
			  else
			  {
				  formdata.left = new FormAttachment(pitem,HorizontalSpacing,SWT.RIGHT);
				  formdata.right = new FormAttachment(pitem,HorizontalSpacing+colwidth,SWT.RIGHT);
				  formdata.top = new FormAttachment(pitem,0,SWT.TOP);
				  formdata.bottom = new FormAttachment(pitem,0,SWT.BOTTOM);
			  }
			}
			
			item.setLayoutData(formdata);	
			pitem=item;
//			int bottom = item.getBounds().y+item.getBounds().height;
//
			if(enough&&(i+1)%column==0) break;
			if(bottom-colheight*2>background.getBounds().height)
			  enough=true;
        }  
		this.layout(true);
		background.layout();
		ScrollBar vBar = this.getVerticalBar();
		int lines = count/column;
		if((count%column)!=0)lines++;
		int contentheight = MarginTop+(colheight+VerticalSpacing)*(lines)+MarginBottom;
	    if(contentheight<this.getBounds().height)
	    	vBar.setVisible(false);
	    else vBar.setVisible(true);
	    if(!vBar.isVisible())return;
		vBar.setMaximum(contentheight);
		vBar.setIncrement(50);
		vBar.setThumb(this.getBounds().height);
		
		vBar.setPageIncrement(this.getBounds().height);
		if (vBar != null&&vBar.isVisible()) {
			vBar.addListener(SWT.Selection, new Listener () 
			{
				public void handleEvent (Event e) {
					vScroll();
					if(mOnScrollListener!=null)
						mOnScrollListener.OnScroll();
				}
			});
		}
	}
	
	public void vScroll()
	{
		ScrollBar vBar = this.getVerticalBar ();
		long position = (long)vBar.getSelection();
		int ydisplacement=0;

		int rowindex = (int) ((position-MarginTop)/(colheight+VerticalSpacing));

		ydisplacement = (int) (position-rowindex*(colheight+VerticalSpacing));
		((FormData)background.getLayoutData()).top.offset=0-(ydisplacement);
		((FormData)background.getLayoutData()).bottom.offset=0-(ydisplacement)+background.getBounds().height;
		
		this.layout(true);
		int itemindex = rowindex*column;
		for(GridItem item:Items)
		{
			item.removeListener();
			item.addListener();
			item.setSelected(false);
			Control [] controls = item.getChildren();
			for(Control c:controls)
			{
				c.dispose();
			}
			if(itemindex<count)
			{
				item.setVisible(true);
				item.setPosition(itemindex);
				this.getView(item,itemindex++,background);
			}
			  
			else item.setVisible(false);
			
			item.layout();
			item.redraw();
				
		}
	}
	
	public void clerSelected()
	{
		for(GridItem item:Items)
		{
			item.setSelected(false);
			item.redraw();
		}
	}
	
	public GridItem getItem(int position)
	{
		if(Items.size()==0)return null;
		if(position<Items.size())
		  return Items.get(position);
		int realposition = position%(Items.size()-1);
		return Items.get(realposition);
	}
}
