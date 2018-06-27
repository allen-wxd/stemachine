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

import com.windows.library.control.GridViewer.OnScrollListener;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;

public abstract class ListViewer extends Composite {
	
	public interface OnScrollListener {
		void OnScroll();
	}
    private ArrayList<ListItem> Items = new ArrayList();
    private int count;
    private int rowheight;
// 	private Composite ContentView;
// 	private Composite ContentViewContainer;  
 	
 	private ScrollBar vBar;
 	
 	private OnScrollListener mOnScrollListener;
 	private Composite background;
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public ListViewer(Composite parent, int style) {
		super(parent, style);
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
	}
	
	public void setOnScrollListener(OnScrollListener listener) {
		mOnScrollListener = listener;
    }
	
	public void setRowheight(int height)
	{
		this.rowheight = height;
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
	public void setCount(int count)
	{
		this.count=count;
	}
	
	protected ListItem getView(ListItem item,long index,Composite parent){
		return null;
	}
	
	public void setContent()
	{
		Init();
//		ContentViewContainer.layout(true);
//		setMinSize(ContentViewContainer.computeSize(SWT.DEFAULT,SWT.DEFAULT));  
		
	}
	
	public void Init()
	{ 	
		for(int i=0;i<Items.size();i++)
		{
			Items.get(i).dispose();
		}
//		if(vBar!=null)
//			vBar.dispose();
		Items.clear();
		ListItem pitem=null;
		boolean enough=false;
		for(int i=0;i<count;i++)
		{
			ListItem item= this.getView(null,i,background);
			this.setRowheight(item.getHeight());
			item.setOnclick();
			FormData formdata = new FormData();
			if(pitem==null)
			{
			  formdata.top = new FormAttachment(0);
			  formdata.bottom = new FormAttachment(0,rowheight);
			}
			else 
			{
			  formdata.top = new FormAttachment(pitem,-1,SWT.BOTTOM);
			  formdata.bottom = new FormAttachment(pitem,rowheight-1,SWT.BOTTOM);
			}
			formdata.left = new FormAttachment(0);
			formdata.right = new FormAttachment(100);
			
			item.setLayoutData(formdata);	
			if(item.getBackgroundcolor()==null)
			  item.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			else
			  item.setBackground(item.getBackgroundcolor());
//			((FormData)item.getLayoutData()).left.offset=Integer.toUnsignedLong(i);
			
			pitem=item;
			Items.add(item);
			if(enough) break;
			if(i*rowheight+rowheight>=background.getBounds().height)
			  enough=true;
			
		}
		background.layout();
	    vBar = getVerticalBar ();
	    if(count*rowheight<this.getBounds().height)
	    	vBar.setVisible(false);
	    else vBar.setVisible(true);
	    if(!vBar.isVisible())return;
		vBar.setMaximum(count*rowheight);
		vBar.setIncrement(rowheight);
		vBar.setThumb(this.getBounds().height);
		
		vBar.setPageIncrement(rowheight*100);
		if (vBar != null&&vBar.isVisible()) {
			vBar.addListener(SWT.Selection, new Listener () {
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
		ScrollBar vBar = getVerticalBar ();
		long position = (long)vBar.getSelection();
		int ydisplacement=0;
		long index = (position/rowheight);
		
		ydisplacement = (int) (position-index*rowheight);
		((FormData)background.getLayoutData()).top.offset=0-(ydisplacement);
		((FormData)background.getLayoutData()).bottom.offset=0-(ydisplacement)+background.getBounds().height;
		System.out.println("position="+position+" index="+index);
		this.layout();
		for(ListItem item:Items)
		{
			item.removeListener();
			item.addListener();
			item.setSelected(false);
			Control [] controls = item.getChildren();
			for(Control c:controls)
			{
				c.dispose();
			}
//			if(index>=count) break;
////			int length = item.getChildren().length;
			if(index<count)
			{
				item.setVisible(true);
				item.setPosition(index);
				this.getView(item,index++,background);
				item.setOnclick();
				if(item.getBackgroundcolor()==null)
				  item.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
				else
				  item.setBackground(item.getBackgroundcolor());
			}
			  
			else item.setVisible(false);
			
			item.layout();
			item.redraw();
			background.layout();
		}
	}
	
	public void seekto(int position)
	{
		ScrollBar vBar = getVerticalBar ();
		vBar.setSelection(position);
		vScroll();
	}
	
//	public ArrayList<ListItem>  getItems(){
//		return Items;
//	}
	
	public ListItem getItem(int position)
	{
		if(Items.size()==0)return null;
		if(position<Items.size())
		  return Items.get(position);
		int realposition = position%(Items.size()-1);
		return Items.get(realposition);
//		return null;
	}
	
	public void clerSelected()
	{
		for(ListItem item:Items)
		{
			item.setSelected(false);
			item.redraw();
		}
	}
}
