package com.windows.library.control;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;


public class PaintPanel extends Composite {

	private Image backimg;
	private GC imggc;
	int lastX = 0, lastY = 0;
	int lastX1 = 0, lastY1 = 0;
	int currentX = 0, currentY = 0;
	
	private ArrayList<Point> List = new ArrayList(); 
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public PaintPanel(Composite parent, int style) {
		super(parent, style|SWT.DOUBLE_BUFFERED);
		List.clear();
		Listener listener = new Listener() {
			   

			   public void handleEvent(Event event)
			   {
			    switch (event.type)
			    {
			     case SWT.MouseMove :
			      if ((event.stateMask & SWT.BUTTON1) == 0)
			       break; // 判断是否为鼠标左键，如果不是跳出
//			      System.out.println("event.x="+event.x+" event.y="+event.y);
//			      System.out.println("lastX="+lastX+" lastY="+lastY);
//			      GC gc = new GC(PaintPanel.this);
//			      gc.setLineWidth(3);
//			      gc.setAdvanced(true);
//			      gc.setAntialias(SWT.ON);
//			      gc.drawLine(lastX, lastY, event.x, event.y);
//			      gc.setAdvanced(false);
//			      gc.dispose();
			      currentX=event.x;
			      currentY=event.y;
//			      lastX1=lastX;
//			      lastY1=lastY;
			      List.add(new Point(event.x,event.y));
			      PaintPanel.this.redraw();
			      break;
			     case SWT.MouseDown :
//			      System.out.println("MouseDownMouseDown");
			    	  List.add(new Point(-1,-1));
				      lastX = 0;
				      lastY = 0;
			      break;
			    }
			   }
			  };
			  PaintPanel.this.addListener(SWT.MouseDown, listener);
			  PaintPanel.this.addListener(SWT.MouseMove, listener);
			  
			  addPaintListener(new PaintListener() {
					public void paintControl(final PaintEvent event) {
						if(backimg==null)
						{
							backimg=new Image(null,PaintPanel.this.getBounds().width,PaintPanel.this.getBounds().height);
						}
						if(imggc==null)
						  imggc=new GC(backimg);
						GC gc = event.gc;

						{
							if(lastX==0)
							{
								lastX=currentX;
								lastY=currentY;
							}
							imggc.setLineWidth(3);
							imggc.setAdvanced(true);
							imggc.setAntialias(SWT.ON);
						    imggc.drawLine(lastX, lastY, currentX, currentY);
						    imggc.setAdvanced(false);
						    gc.setAdvanced(true);
						    gc.setAntialias(SWT.ON);
						    gc.drawImage(backimg, 0, 0,backimg.getBounds().width,backimg.getBounds().height,0,0,PaintPanel.this.getBounds().width,PaintPanel.this.getBounds().height);
						    gc.setAdvanced(false);
							lastX=currentX;
							lastY=currentY;
						}

				    }
					
				});
	}
	
	public void setimg(Image img)
	{
		if(backimg!=null)
		{
			backimg.dispose();
			backimg=null;
		}
		this.backimg=new Image(null,img.getImageData());
	}
	
	public Image getimg()
	{
		return backimg;
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
