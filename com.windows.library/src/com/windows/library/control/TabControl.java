package com.windows.library.control;

import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;

import java.awt.Toolkit;
import java.util.ArrayList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.gdip.Rect;

public abstract class TabControl extends Composite {
	public class TabItem{
		private int index;
		private String name;
		private Image img;
		private Image focusimg;
		private int tabbtnx,tabbtny;
		private int tabbtnwidth,tabbtnheight;
		private boolean focus;
		private boolean mouseentered;
		private int texttopmargin;
		private int textleftmargin;
		private int textwidth;
		private int textheight;
		
		private Rect imgrect;
		private Composite content;
		
		public TabItem(String name,Image img,Image focusimg)
		{
			this.name = name;
			this.img = img;
			this.focusimg = focusimg;
			this.index = -1;
			focus=false;
			mouseentered=false;
			content=new Composite(TabControl.this,SWT.NONE);
			content.setBackgroundMode(SWT.INHERIT_FORCE);
			content.setLayout(new FormLayout());
			content.setBackground(new Color(null,245,246,247));
			imgrect = new Rect();
			imgrect.X=0;
			imgrect.Y=0;
			imgrect.Height=0;
			imgrect.Width=0;
		}
	}
    private ArrayList<TabItem> Items = new ArrayList();
    private Font tabbtnfont;
    private Font tabbtnfocusfont;
    private int LEFTMARGIN=20;
    private int TOPMARGIN=5;
    private int TABHSPACE=20;
    private int HTEXTMARGIN=10;
    private int VTEXTMARGIN=5;
    
    private int tabHeight;
    
    
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public TabControl(Composite parent, int style) {
		super(parent, style|SWT.DOUBLE_BUFFERED);
//		this.setBackgroundMode(SWT.INHERIT_FORCE);
		setBackground(new Color(null,255,255,255));
        this.setLayout(new FormLayout());
        this.addPaintListener(new PaintListener(){
			@Override
			public void paintControl(PaintEvent e) {
				// TODO Auto-generated method stub
				GC gc = e.gc;
				if(tabbtnfont!=null)
				  gc.setFont(tabbtnfont);
//				System.out.println(gc.getFont().getFontData()[0].toString());
				if(Items.size()==0)return;
				if(Items.get(0).index==-1)
				{
					Items.get(0).focus=true;
					//计算每个item的位置
					LayoutItems(e.gc);
				}
				
				TabItem focusitem=null;
				for(TabItem item:Items)
				{
					
					if(item.focus)
					{
						focusitem=item;
						gc.setBackground(new Color(null,245,246,247));
						gc.fillRectangle(0, item.tabbtny+item.tabbtnheight, ((Composite)e.widget).getBounds().width, ((Composite)e.widget).getBounds().height-item.tabbtnheight-TOPMARGIN);
						gc.fillRectangle(item.tabbtnx, item.tabbtny, item.tabbtnwidth, item.tabbtnheight);
						gc.setForeground(new Color(null,50,119,205));
						
						if(item.focusimg!=null)
						{
							gc.drawImage(item.focusimg, item.tabbtnx+item.imgrect.X, item.imgrect.Y+VTEXTMARGIN);
						}
					}
					else
					{
						gc.setBackground(new Color(null,255,255,255));

						if(item.mouseentered)
						  gc.setBackground(new Color(null,236,236,237));
						gc.fillRectangle(item.tabbtnx, item.tabbtny, item.tabbtnwidth, item.tabbtnheight);
						gc.setForeground(new Color(null,68,68,68));
						if(item.img!=null)
					      gc.drawImage(item.img, item.tabbtnx+item.imgrect.X, item.imgrect.Y+VTEXTMARGIN);
					}
					
					gc.drawText(item.name, item.tabbtnx+item.textleftmargin, item.tabbtny+item.texttopmargin, true);

				}
				if(focusitem!=null)
				{
					gc.setForeground(new Color(null,217,217,217));
					gc.drawLine(0, focusitem.tabbtny+focusitem.tabbtnheight-1, focusitem.tabbtnx, focusitem.tabbtny+focusitem.tabbtnheight-1);
					gc.drawLine(focusitem.tabbtnx, focusitem.tabbtny, focusitem.tabbtnx, focusitem.tabbtny+focusitem.tabbtnheight);
					gc.drawLine(focusitem.tabbtnx, focusitem.tabbtny, focusitem.tabbtnx+focusitem.tabbtnwidth, focusitem.tabbtny);
					gc.drawLine(focusitem.tabbtnx+focusitem.tabbtnwidth, focusitem.tabbtny, focusitem.tabbtnx+focusitem.tabbtnwidth, focusitem.tabbtny+focusitem.tabbtnheight);
					gc.drawLine(focusitem.tabbtnx+focusitem.tabbtnwidth, focusitem.tabbtny+focusitem.tabbtnheight-1, ((Composite)e.widget).getBounds().width, focusitem.tabbtny+focusitem.tabbtnheight-1);
				}
			}
	    });
        
        addMouseTrackListener(new MouseTrackListener(){
			public void mouseEnter(MouseEvent arg0) {

			 }
			public void mouseExit(MouseEvent arg0) {
				((TabControl)arg0.widget).setCursor(new Cursor(null, SWT.CURSOR_ARROW));
				for(TabItem item:Items)
				{
					item.mouseentered=false;
				}
				((TabControl)arg0.widget).redraw();
			 }
			public void mouseHover(MouseEvent arg0) {
			 }
			 });
        
		addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseDown(MouseEvent e) 
			{
				if(e.button==3)
				{
					return;
				}
				int focusindex=-1;
				for(TabItem item:Items)
				{
					if(new Rectangle(item.tabbtnx,item.tabbtny, item.tabbtnwidth, item.tabbtnheight).contains(e.x, e.y))
					{
					  item.focus=true;
					  ((TabControl)e.widget).setCursor(new Cursor(null, SWT.CURSOR_HAND));
					  focusindex=Items.indexOf(item);
					}
				}
				if(focusindex!=-1)
				{
					for(TabItem item:Items)
					{
                      if(Items.indexOf(item)!=focusindex)
                      {
                    	item.content.setVisible(false);
                        item.focus=false;
                      }
                      else item.content.setVisible(true);
					}
				}
				((TabControl)e.widget).redraw();
			}
			@Override
			public void mouseUp(MouseEvent e) 
			{
				if(e.button==3)
				{
					return;
				}
			}
		});
		
		addMouseMoveListener(new MouseMoveListener(){
			@Override
			public void mouseMove(MouseEvent arg0) {
				// TODO Auto-generated method stub
				((TabControl)arg0.widget).setCursor(new Cursor(null, SWT.CURSOR_ARROW));
				for(TabItem item:Items)
				{
					if(new Rectangle(item.tabbtnx,item.tabbtny, item.tabbtnwidth, item.tabbtnheight).contains(arg0.x, arg0.y))
					{
					  item.mouseentered=true;
					  ((TabControl)arg0.widget).setCursor(new Cursor(null, SWT.CURSOR_HAND));
					}
					else
					  item.mouseentered=false;
				}
				((TabControl)arg0.widget).redraw();
			}});
	}
	
	public void setFont(Font font)
	{
		tabbtnfont = font;
	}
	
	public int getTabHeight()
	{
		return tabHeight;
	}
	
	public int getStringWidth(GC gc,char[] string, int offset, int length) {
		int width=0;
		for (int i = 0; i < length; i++) {
			width += gc.getAdvanceWidth(string[offset+i]);
		}
		width=(int) (width+0.5f);
		return width;
	}

	protected int getStringHeightInternal(GC gc) {
		float dpi = Toolkit.getDefaultToolkit().getScreenResolution();
		float bili=(float)(dpi/96.0);
		return (int)((gc.getFontMetrics().getHeight() + 0.5f));
	}
	
	public void LayoutItems(GC gc)
	{
		int maxwidth=0;
		tabHeight=0;
		int height = getStringHeightInternal(gc);
		for(TabItem item:Items)
		{
			int index = Items.indexOf(item);
			item.index = index;
			
			int width = getStringWidth(gc,item.name.toCharArray(),0,item.name.length());
			if(item.img!=null)
			{
				item.imgrect.Height=item.img.getBounds().height;
				item.imgrect.Width=item.img.getBounds().width;
			}
				
			item.textheight = height;
			item.textwidth = width;
			item.tabbtny=TOPMARGIN;
			item.tabbtnheight=height+VTEXTMARGIN*2;
			width=width+HTEXTMARGIN*2+item.imgrect.Width;
			if(width>maxwidth)
			  maxwidth=width;
			if(tabHeight<item.tabbtnheight)
				tabHeight=item.tabbtnheight;
		}
		for(TabItem item:Items)
		{
			int index = Items.indexOf(item);
			if(index==0)
				item.tabbtnx=LEFTMARGIN;
			else if(index!=0)
			    item.tabbtnx=Items.get(index-1).tabbtnx+maxwidth+TABHSPACE;
			item.tabbtnwidth=maxwidth;
			if(item.img!=null)
			{
				item.imgrect.X=(maxwidth-item.textwidth-item.imgrect.Width)/2;
//				item.imgrect.Y=(item.tabbtnheight-item.textheight)/2;
				item.imgrect.Y=(item.tabbtnheight-item.imgrect.Height)/2;
			    System.out.println("item.tabbtnheight="+item.tabbtnheight);
			    System.out.println("item.imgrect.Height="+item.imgrect.Height);
				item.textleftmargin = item.imgrect.X+item.imgrect.Width;
			}
			else
			    item.textleftmargin = (maxwidth-item.textwidth)/2;
			item.texttopmargin = (item.tabbtnheight-item.textheight)/2;
			
			FormData fd_newcut = new FormData();
			item.content.setLayoutData(fd_newcut);
			fd_newcut.bottom = new FormAttachment(100);
			fd_newcut.right = new FormAttachment(100);
			fd_newcut.top = new FormAttachment(0,item.tabbtny+item.tabbtnheight);
			fd_newcut.left = new FormAttachment(0);
						
			AddItemControl(index,item.content);
		}
		this.layout();
	}
	
	public int gettabbtnWidth()
	{
		GC gc = new GC(this);
		if(tabbtnfont!=null)
		  gc.setFont(tabbtnfont);
		if(Items.size()>0&&Items.get(0).index==-1)
		{
			Items.get(0).focus=true;
			//计算每个item的位置
			LayoutItems(gc);
		}
		int width=0;
		if(Items.size()>0)
		  width=Items.get(Items.size()-1).tabbtnx+Items.get(Items.size()-1).tabbtnwidth;
        return width;
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
	protected void AddItemControl(int index,Composite parent) {
	}
	
	public void addTabItem(TabItem item)
	{
		Items.add(item);
	}
}
