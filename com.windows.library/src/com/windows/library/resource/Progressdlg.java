package com.windows.library.resource;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.Rectangle2D;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.windows.library.util.Util;

public class Progressdlg extends JDialog {

	private JPanel contentPane;
private String[] res = {"progress_1.png",
		"progress_2.png",
		"progress_3.png",
		"progress_4.png",
		"progress_5.png",
		"progress_6.png",
		"progress_7.png",
		"progress_8.png"};

private Image[] resimg=new Image[8];
private int index=-1;
private Timer timer;
private Graphics2D g2d;
private String Message="";//百分比信息
private String Message1="努力加载中......";//提示信息

private static Progressdlg frame;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new Progressdlg();
					frame.setVisible(false);
				} catch (Exception e) {
					Util.writeLog(e, "Progressdlg.main");
				}
			}
		});
	}
	
	public static void showDialog()
	{
		if(frame==null)
		{
			frame = new Progressdlg();	
		}
		int width = Toolkit.getDefaultToolkit().getScreenSize().width; //得到当前屏幕分辨率的高
		int height = Toolkit.getDefaultToolkit().getScreenSize().height;//得到当前屏幕分辨率的宽
		frame.setBounds(0,0,width,height); 
		frame.setBackground(new Color(0,0,0,100));
		frame.setVisible(true);
		frame.Message("");
		frame.Message1("努力加载中......");
		frame.setAlwaysOnTop(true);
	}
	
	public static void hideDialog()
	{
		if(frame==null)
		{
			frame = new Progressdlg();	
		}
		frame.setVisible(false);
		frame.Message("");
		frame.Message1("努力加载中......");
	}
	
	public static boolean isshow()
	{
		if(frame!=null)
		  return frame.isVisible();
		return false;
	}
	
	public static void Message(String message)
	{
		if(frame!=null)
		  frame.setMessage(message);
	}
	
	public static void Message1(String message)
	{
		if(frame!=null)
		  frame.setMessage1(message);
	}
	
	public void Clear()
	{
		if (!isOpaque()) 
		{
            Graphics gg = g2d.create();
            try {
                if (gg instanceof Graphics2D) {
                    gg.setColor(getBackground());
                    ((Graphics2D)gg).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));
                    gg.fillRect(0, 0, getWidth(), getHeight());
                }
            } finally {
                gg.dispose();
            }
        }
	}
	
	public static void destroy()
	{
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
		}
		if(frame!=null)
		{
			frame.closetimer();
			frame.dispose();
			frame=null;
		}
		  
	}
	
    public void closetimer()
    {
    	if(timer!=null)
    	{
    		timer.cancel();
    		timer=null;
    	}
    }

	/**
	 * Create the frame.
	 */
	public Progressdlg() {
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		setUndecorated(true);
		
		
		for(int i=0;i<8;i++)
		{
			resimg[i]=IconCache.Hinstance().getawtimg(res[i]);
		}
		index=-1;
		timer = new Timer();
        timer.schedule(new TimerTask(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(frame!=null&&frame.isVisible())
				{
				  index=(index+1)%8;
		    	  repaint();
				}
			}}, 0, 100); 
//        this.dispose();
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void setMessage(String msg)
	{
		this.Message=msg;
	}
	
	public void setMessage1(String msg)
	{
		this.Message1=msg;
	}
	
	public void update(Graphics g)
	{
		paint(g);
	}
	
	public void paint(Graphics g)
	{
		g2d = (Graphics2D)g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		if(index<0)
		{
			index=0;
		}
		Clear();
		{
		  Image img = resimg[index];
		  if(img!=null)
		  {
			  img = new ImageIcon(img).getImage();
		      g2d.drawImage(img, getWidth()/2-img.getWidth(null)/2, getHeight()/2-img.getHeight(null)/2, null);
		      if(!Message.isEmpty())
		      {
			      Font font=new Font("Dialog",Font.BOLD,18);
		          g2d.setFont(font);
		          FontMetrics fm2 = g.getFontMetrics();  
		          Rectangle2D rec2=fm2.getStringBounds(Message, g2d);
		          g2d.setColor(new Color(97,97,97));//设置当前绘图颜色
			      g2d.drawString(Message, getWidth()/2-(int)rec2.getWidth()/2, getHeight()/2-9);
		      }
		      if(!Message1.isEmpty())
		      {
		    	  Font font=new Font("微软雅黑",Font.BOLD,18);
		          g2d.setFont(font);
		          FontMetrics fm2 = g.getFontMetrics();  
		          Rectangle2D rec2=fm2.getStringBounds(Message1, g2d);
		          if(index<4)
		              g2d.setColor(new Color(97,97,97));//设置当前绘图颜色
		          else
		        	  g2d.setColor(new Color(167,167,167));//设置当前绘图颜色 
			      g2d.drawString(Message1, getWidth()/2-(int)rec2.getWidth()/2, getHeight()/2+100);
		      }
		  }
		}
	}
}
