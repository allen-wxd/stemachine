
package org.eclipse.swt.widgets.MyButton;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.wb.swt.SWTResourceManager;

public class ImgButton extends Canvas{
	public enum TAlignment {
		LEFT,
		RIGHT,
		TOP,
		BOTTOM,
		CENTER
	}
	public enum TPosition {
		LEFT,
		RIGHT,
		TOP,
		BOTTOM,
		CENTER
	}

	public interface OnclickListener
	{
		abstract public void Onclick();
	}
	
	public interface OnButtonclickListener
	{
		abstract public void Onclick(ImgButton widget);
	}
	
	public interface LayoutInterface
	{
		abstract public void Layout(int width,int height);
	}
	
	private Image normalimg,tempnormalimg;
	private Image disableimg;
	private Image hotimg;
	private Image selectedbkimg;
	private Image selectedimg;
	private Image pressimg,temppressimg;
	
	private int status=0;
	private String title;
	private Font font;
	private int X,Y;
	private int titleh;
	
	private int Width;
	private int Height;
	private TAlignment Talignment;
	private TPosition Tposition=TPosition.BOTTOM;
	private Color normal_bk;
	private Color disable_bk;
	private Color hot_bk;
	private Color press_bk;
	
	private Color selected_bk;
	
//	private Color FontColor;
	
	private Color NormalFontColor;
	private Color HotFontColor;
	private Color PressFontColor;
//	private int Margin=2;
	
	private int HMargin=2;
	private int VMargin=2;
	
	private boolean WrapContent=false;
	
	private boolean drawbk=false;
	private OnclickListener Onclick;
	private OnButtonclickListener Onbtclick;
	private LayoutInterface OnLayout;
	private boolean selected;
    public ImgButton(Composite parent, int style) {
		super(parent, style);
		titleh=0;
		Onclick=null;
		Onbtclick=null;
		
		normal_bk=null;
		disable_bk=null;
		hot_bk=null;
		press_bk=null;
		selected_bk=null;
		
		selectedimg=null;
		selectedbkimg=null;
		
		NormalFontColor = SWTResourceManager.getColor(0, 0, 0);
		HotFontColor = SWTResourceManager.getColor(0, 0, 0);
		PressFontColor = SWTResourceManager.getColor(0, 0, 0);
		Width = 0;
		Height = 0;
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
		addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseDown(MouseEvent e) 
			{
				if(e.button==3)
				{
					return;
				}
				status=2;
				((Canvas)e.widget).redraw();
			}
			@Override
			public void mouseUp(MouseEvent e) 
			{
				if(e.button==3)
				{
					return;
				}
				status=1;
				((Canvas)e.widget).redraw();
				if(Onclick!=null)
					Onclick.Onclick();
				if(Onbtclick!=null)
					Onbtclick.Onclick(ImgButton.this);
			}
		});
		addPaintListener(new PaintListener () {
			public void paintControl(PaintEvent event){
				if(normalimg==null)return;
				GC gc = event.gc;	
				Canvas c = ((Canvas)event.widget);
				
				int titleh = 0;
				int tw = 0;
				if(font!=null)
					gc.setFont(font);
				if(title!=null)
				{
					titleh = getStringHeightInternal(gc);
					tw = getStringWidth(gc,title.toCharArray(),0,title.length());					
				}
				
//				if(Width>0&&Height>0)//如果指定了大小
				{
					int imgh = normalimg.getBounds().height;
					int imgw = normalimg.getBounds().width;
					
					int imgx=(Width-imgw)/2;
					int imgy=(Height-imgh-titleh)/2;
					
					int tx = (Width-tw)/2;
					int ty = imgh+imgy;
//					System.out.print("draw");
					if(Tposition==TPosition.LEFT)
					{
						if(WrapContent&&(imgw+HMargin*2+tw!=Width||imgh+VMargin*2!=Height))
						  ImgButton.this.Layout();
						imgx=(Width-imgw-HMargin);
						imgy=(VMargin);
						tx = (imgx-tw);
						ty = Height/2-titleh/2;
						if(Talignment==TAlignment.TOP)
							ty = VMargin;
						else if(Talignment==TAlignment.BOTTOM)
							ty = Height-VMargin-titleh;
					}
					else if(Tposition==TPosition.BOTTOM)
					{
						if(WrapContent&&((imgw>tw?imgw:tw)+HMargin*2!=Width||imgh+VMargin*2+titleh!=Height))
						  ImgButton.this.Layout();
						imgx=(Width-imgw)/2;
						imgy=(Height-imgh-titleh)/2;
						
						tx = (Width-tw)/2;
						ty = imgh+imgy;
						if(Talignment==TAlignment.LEFT)
						  tx = HMargin;
						else if(Talignment==TAlignment.RIGHT)
						  tx = Width-HMargin-tw;
					}
					else if(Tposition==TPosition.RIGHT)
					{
						if(WrapContent&&(imgw+HMargin*2+tw!=Width||imgh+VMargin*2!=Height))
						  ImgButton.this.Layout();
						imgx=(HMargin);
						imgy=(VMargin);
						tx = (imgx+imgw);
						ty = Height/2-titleh/2;
						if(Talignment==TAlignment.TOP)
							ty = VMargin;
						else if(Talignment==TAlignment.BOTTOM)
							ty = Height-VMargin-titleh;						
					}
					else if(Tposition==TPosition.TOP)
					{
						if(WrapContent&&((imgw>tw?imgw:tw)+HMargin*2!=Width||imgh+VMargin*2+titleh!=Height))
						  ImgButton.this.Layout();
						imgx=(Width-imgw)/2;
						imgy=(Height-VMargin-imgh);
						
						tx = (Width-tw)/2;
						ty = imgy-titleh;
						if(Talignment==TAlignment.LEFT)
						  tx = HMargin;
						else if(Talignment==TAlignment.RIGHT)
						  tx = Width-HMargin-tw;
					}
					else if(Tposition==TPosition.CENTER)
					{
						if(WrapContent&&((imgw>tw?imgw:tw)+HMargin*2!=Width||imgh+VMargin*2!=Height))
						  ImgButton.this.Layout();
						imgx=(Width-imgw)/2;
						imgy=(Height-imgh)/2;
						tx = (Width-tw)/2;
						ty = Height/2-titleh/2;
					}
					
					if(!isEnabled())
					{
						if(disable_bk!=null)
						  c.setBackground(disable_bk);
						if(disableimg!=null)
						  gc.drawImage(disableimg, imgx, imgy);
						else gc.drawImage(normalimg, imgx, imgy);
						if(title!=null)
						{
							gc.setForeground(SWTResourceManager.getColor(150, 150, 150));
							gc.drawString(title, tx, ty, true);
						}
						return;
					}
					if(status==0)
					{
						if(selected)//选中状态要设置底色
						{
						  if(selected_bk!=null)
						  {
						    gc.setBackground(selected_bk);
						    gc.fillRectangle(0, 0, c.getBounds().width, c.getBounds().height);
						  }
						  if(selectedbkimg!=null)
						  {
							  gc.drawImage(selectedbkimg, 0, 0,selectedbkimg.getBounds().width,selectedbkimg.getBounds().height,0,0,c.getBounds().width, c.getBounds().height);
						  }
						}
						else if(normal_bk!=null)
						{
							gc.setBackground(normal_bk);
							gc.fillRectangle(0, 0, c.getBounds().width, c.getBounds().height);
						}
						if(normalimg!=null)
						  gc.drawImage(normalimg, imgx, imgy);
						if(selectedimg!=null&&selected)
					    {
						  gc.drawImage(selectedimg, imgx, imgy);
						}
						gc.setForeground(NormalFontColor);
					}
					if(status==1)
					{
						if(selected)//选中状态要设置底色
						{
						  if(selected_bk!=null)
						  {
						    gc.setBackground(selected_bk);
						    gc.fillRectangle(0, 0, c.getBounds().width, c.getBounds().height);
						  }
						  if(selectedbkimg!=null)
						  {
							  gc.drawImage(selectedbkimg, 0, 0,selectedbkimg.getBounds().width,selectedbkimg.getBounds().height,0,0,c.getBounds().width, c.getBounds().height);
						  }
						}
						else
						{
							if(hot_bk!=null)
							{
							  gc.setBackground(hot_bk);
							  gc.fillRectangle(0, 0, c.getBounds().width, c.getBounds().height);
							}

						}
						if(hotimg!=null)
						  gc.drawImage(hotimg, imgx, imgy);
						else 
						  gc.drawImage(normalimg, imgx, imgy);
						if(selectedimg!=null&&selected)
					    {
						  gc.drawImage(selectedimg, imgx, imgy);
						}
						

						gc.setForeground(HotFontColor);
					}
					if(status==2)
					{
						if(selected)//选中状态要设置底色
						{
						  if(selected_bk!=null)
						  {
						    gc.setBackground(selected_bk);
						    gc.fillRectangle(0, 0, c.getBounds().width, c.getBounds().height);
						  }
						  if(selectedbkimg!=null)
						  {
							  gc.drawImage(selectedbkimg, 0, 0,selectedbkimg.getBounds().width,selectedbkimg.getBounds().height,0,0,c.getBounds().width, c.getBounds().height);
						  }
						}
						else if(press_bk!=null)
						{
							gc.setBackground(press_bk);
							gc.fillRectangle(0, 0, c.getBounds().width, c.getBounds().height);
						}
						if(pressimg!=null)
						  gc.drawImage(pressimg, imgx, imgy);
						else gc.drawImage(normalimg, imgx, imgy);
						if(selectedimg!=null&&selected)
					    {
						  gc.drawImage(selectedimg, imgx, imgy);
						}
						
						
						gc.setForeground(PressFontColor);
					}
					
					if(title!=null)
					{
						gc.drawString(title, tx, ty, true);
					}
				}
			}
		});
		// TODO Auto-generated constructor stub
	}
    
    public void setHMargin(int margin)
    {
    	this.HMargin=margin;
    }
    
    public void setVMargin(int margin)
    {
    	this.VMargin=margin;
    }

    public void drawbk(boolean drawbk)
    {
      this.drawbk = drawbk;	
    }
    
    public void setSize(int width,int height)
    {
    	this.Width = width;
    	this.Height = height;
    }
    
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        this.redraw();
  	}
    
    public void setOnclick(OnclickListener Onclick) {
      this.Onclick = Onclick;		
	}
    
    public void setOnButtonclick(OnButtonclickListener Onclick) {
        this.Onbtclick = Onclick;		
  	}
    
    public void setOnLayout(LayoutInterface Onlayout) {
        this.OnLayout = Onlayout;		
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
		GC gc = new GC(this);
		if(Width<=0||Height<=0)
		  WrapContent=true;
		if(Tposition==TPosition.CENTER)
		  WrapContent=true;
//		if(Width>0&&Height>0)
		{
			if(!WrapContent&&Width>0&&Height>0)
			{
				this.setBounds(0,0,Width,Height);
				if(OnLayout!=null)
				  OnLayout.Layout(Width,Height);
				return;
			}
			int imgh = normalimg.getBounds().height;
			int imgw = normalimg.getBounds().width;
			int titleh = 0;
			int tw = 0;
			if(font!=null)
				gc.setFont(font);
			if(title!=null)
			{
				titleh = getStringHeightInternal(gc);
				tw = getStringWidth(gc,title.toCharArray(),0,title.length());					
			}
			if(Tposition==TPosition.LEFT||Tposition==TPosition.RIGHT)
			{
				if(imgw+HMargin*2+tw!=Width||imgh+VMargin*2!=Height)
				{
				  ImgButton.this.setSize(imgw+HMargin*2+tw,imgh+VMargin*2);
				}
			}
			else if(Tposition==TPosition.TOP||Tposition==TPosition.BOTTOM)
			{
				if((imgw>tw?imgw:tw)+HMargin*2!=Width||imgh+VMargin*2+titleh!=Height)
				{
				  ImgButton.this.setSize((imgw>tw?imgw:tw)+HMargin*2,imgh+VMargin*2+titleh);
				}
			}
			else if(Tposition==TPosition.CENTER)
			{
				if((imgw>tw?imgw:tw)+HMargin*2!=Width||imgh+VMargin*2!=Height)
				{
				  ImgButton.this.setSize((imgw>tw?imgw:tw)+HMargin*2,imgh+VMargin*2);
				}
			}
			this.setBounds(0,0,Width,Height);
			if(OnLayout!=null)
			  OnLayout.Layout(Width,Height);
			gc.dispose();
			return;
		}
	}
	
	public void setSelected(boolean selected)
	{
		this.selected=selected;
		/*if(selected)
		{
			normalimg = temppressimg;
			pressimg = tempnormalimg;
		}
		else
		{
			normalimg = tempnormalimg;
			pressimg = temppressimg;
		}*/
//		status=1;
		this.redraw();
	}
	
	public void setState(int state)
	{
		status=state;
	}
	
	public boolean getSelected()
	{
		return selected;
	}
	
	public void setNormalimg(Image img)
	{
		normalimg = img;
	}
	
	public void sethotimg(Image img)
	{
		hotimg = img;
	}
	
	public void setdisimg(Image img)
	{
		disableimg = img;
	}
	
	public void setpressimg(Image img)
	{
		pressimg = img;
	}
	
	public void setSelectedbkimg(Image img)
	{
		this.selectedbkimg = img;
	}
	
	public void setSelectedimg(Image img)
	{
		this.selectedimg = img;
	}
	
	public void setSelectedbk(Color bk)
	{
		this.selected_bk = bk;
	}
	
	public void setbackgroundimg(Image img)
	{
		normalimg = img;
		hotimg = img;
		disableimg = img;
		pressimg = img;
		this.redraw();
	}
	
	public void setNormabk(Color normalbk)
	{
		normal_bk = normalbk;
	}
	
	public void sethotbk(Color hotbk)
	{
		hot_bk = hotbk;
	}
	
	public void setdisimg(Color disbk)
	{
		disable_bk = disbk;
	}
	
	public void setpressbk(Color pressbk)
	{
		press_bk = pressbk;
	}

	public void setFontColor(Color fontcolor)
	{
//		this.FontColor = fontcolor;
	}
	
	public void setNormalFontColor(Color fontcolor)
	{
		this.NormalFontColor = fontcolor;
	}
	
	public void setHotFontColor(Color fontcolor)
	{
		this.HotFontColor = fontcolor;
	}
	
	public void setPressFontColor(Color fontcolor)
	{
		this.PressFontColor = fontcolor;
	}
	
	public void setTalignment(TAlignment alignment)
	{
		Talignment = alignment;
	}
	
	public void setTposition(TPosition position)
	{
		Tposition = position;
	}
	
	public void setWrapContent(boolean wrap)
	{
		WrapContent = wrap;
	}
}
