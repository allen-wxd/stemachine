package org.eclipse.swt.widgets.MyButton;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.wb.swt.SWTResourceManager;

/*
 * This example builds on HelloWorld1 and demonstrates how to draw directly
 * on an SWT Control.
 */
public class ColorButton extends Canvas{
	public interface OnclickListener
	{
		abstract public void Onclick();
	}
	private Color normalimg;
	private Color disableimg;
	private Color hotimg;
	private Color pressimg;
	
	private Font normal;
	private Font disable;
	private Font hot;
	private Font press;
	
	private Color normalfc;
	private Color disablefc;
	private Color hotfc;
	private Color pressfc;
	
	private int status=0;
	private String title;
	private Font font;
	private int X,Y;
	private int titleh;
	private OnclickListener Onclick;
    public ColorButton(Composite parent, int style) {
		super(parent, style);
		titleh=0;
		Onclick=null;
		
		addMouseTrackListener(new MouseTrackListener(){
			 //������
			public void mouseEnter(MouseEvent arg0) {
				status=1;
				((Canvas)arg0.widget).redraw();
				((Canvas)arg0.widget).setCursor(new Cursor(null, SWT.CURSOR_HAND));
			 }
			 //����뿪
			public void mouseExit(MouseEvent arg0) {
				status=0;
				((Canvas)arg0.widget).redraw();
				((Canvas)arg0.widget).setCursor(new Cursor(null, SWT.CURSOR_ARROW));
			 }
			 //���ͣ��
			public void mouseHover(MouseEvent arg0) {
				status=1;
				((Canvas)arg0.widget).redraw();
			 }
			 });
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				
				status=2;
				((Canvas)e.widget).redraw();
			}
			@Override
			public void mouseUp(MouseEvent e) {
				status=1;
				((Canvas)e.widget).redraw();
				if(Onclick!=null)
					Onclick.Onclick();
			}
		});
		addPaintListener(new PaintListener () {
			public void paintControl(PaintEvent event){
				GC gc = event.gc;
				if(gc==null||normalfc==null) return;
				Canvas c = ((Canvas)event.widget);
				int height = getBounds().height;
				int width = getBounds().width;
				
				int titleh = 0;
				int tw = 0;
				if(status==0)
				{
					setBackground(normalimg);
					gc.setForeground(normalfc);
					gc.setFont(normal);
				}
				if(status==1)
				{
					setBackground(hotimg);
					gc.setForeground(hotfc);
					gc.setFont(hot);
				}
				if(status==2)
				{
					setBackground(pressimg);
					gc.setForeground(pressfc);
					gc.setFont(press);
				}
				if(!isEnabled())
				{
					setBackground(disableimg);
					gc.setForeground(disablefc);
					gc.setFont(disable);
				}
				if(title!=null)
				{
					titleh = getStringHeightInternal(gc);
					tw = getStringWidth(gc,title.toCharArray(),0,title.length());	
					int tx = (width-tw)/2;
					int ty = height/2-titleh/2;
					gc.drawString(title, tx, ty, true);
				}
			}
		});
		// TODO Auto-generated constructor stub
	}

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        this.redraw();
  	}
    
    public void setOnclick(OnclickListener Onclick) {
      this.Onclick = Onclick;		
	}
    
    public int getStringWidth(GC gc,char[] string, int offset, int length) {
		//return (int)(myTextPaint.measureText(string, offset, length) + 0.5f);
		int width=0;
		for (int i = 0; i < length; i++) {
			width += gc.getAdvanceWidth(string[offset+i]);
		}
		width=(int) (width+0.5f);
		return width;
	}
    
    protected int getStringHeightInternal(GC gc) {
		return (int)(gc.getFontMetrics().getHeight() + 0.5f);
	}
	//	private static ResourceBundle resHello = ResourceBundle.getBundle("examples_helloworld");
	
    public void setTitle(String title)
    {
    	this.title = title;
    }
    
    public void setFont(Font font)
    {
      this.font = font;
    }
    
	public void setposition(int x,int y)
	{
		X = x;
		Y = y;
	}
	
	public void Layout()
	{
//		GC gc = new GC(this);
//		int height = getBounds().height;
//		int width = getBounds().width;
//		
//		int titleh = 0;
//		int tw = 0;
//		if(font!=null)
//			gc.setFont(font);
//		if(title!=null)
//		{
//			titleh = getStringHeightInternal(gc);
//			tw = getStringWidth(gc,title.toCharArray(),0,title.length());					
//		}
////		if(getBounds().width!=width||getBounds().height!=height+titleh)
//		  setBounds(X, Y, width, height+titleh);
//		  gc.dispose();
////		super.layout();
	}
	
	public void setNormalimg(Color img)
	{
		normalimg = img;
	}
	
	public void sethotimg(Color img)
	{
		hotimg = img;
	}
	
	public void setdisimg(Color img)
	{
		disableimg = img;
	}
	
	public void setpressimg(Color img)
	{
		pressimg = img;
	}
	
	public void setNormalFont(Font f,Color c)
	{
		normal = f;
		normalfc = c;
		
	}
	
	public void sethotFont(Font f,Color c)
	{
		hot = f;
		hotfc = c;
	}
	
	public void setdisFont(Font f,Color c)
	{
		disable = f;
		disablefc = c;
	}
	
	public void setpressFont(Font f,Color c)
	{
		press = f;
		pressfc = c;
	}

}
