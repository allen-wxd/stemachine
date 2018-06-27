package com.windows.library.util;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.JDialog;

//import org.eclipse.swt.SWT;
//import org.eclipse.swt.graphics.Color;
//import org.eclipse.wb.swt.SWTResourceManager;

import com.windows.library.resource.ZLResource;

public class LandScreen_awt extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static BufferedImage  bi;
	private static LandScreen_awt dialog;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			dialog = new LandScreen_awt();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
			dialog.repaint();

		} catch (Exception e) {
			Util.writeLog(e, "LandScreen_awt.main.txt");
		}
	}
	
	public static boolean Showing()
	{
		if(dialog!=null)
		  return dialog.isVisible();
		return false;
	}

	/**
	 * Create the dialog.
	 */
	public LandScreen_awt() {
		setUndecorated(true);
//		com.sun.awt.AWTUtilities.setWindowOpacity(this, 0.2f);
		int width = Toolkit.getDefaultToolkit().getScreenSize().width; // 得到当前屏幕分辨率的高
		int height = Toolkit.getDefaultToolkit().getScreenSize().height;// 得到当前屏幕分辨率的宽
		setBounds(0, 0, width, height);
		getRootPane ().setOpaque (true);
//		setBackground (new Color (0, 0, 0, 50));
		getContentPane().setLayout(new BorderLayout());
		setAlwaysOnTop(true);
		
		addComponentListener(new ComponentAdapter(){
			@Override public void componentResized(ComponentEvent e){
			    // write you code here
//				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//				if(screenSize.width>screenSize.height)
//				{
//					Destroy();
//				}
		}});
	}
	
	public static void Destroy()
	{
		if(dialog!=null)
		{
			dialog.dispose();
			dialog=null;
		}
	}
	
	public int getStringHeight(String str, Font font) {
		AffineTransform atf = new AffineTransform();
		FontRenderContext frc = new FontRenderContext(atf, true,true);
        if (str == null || str.isEmpty() || font == null) {
            return 0;
        }
        return (int) font.getStringBounds(str, frc).getHeight();

    }

    public int getStringWidth(String str, Font font) {
    	AffineTransform atf = new AffineTransform();
		FontRenderContext frc = new FontRenderContext(atf, true,true);
        if (str == null || str.isEmpty() || font == null) {
            return 0;
        }
        return (int) font.getStringBounds(str, frc).getWidth();
    }
	
	public void paint(Graphics g)
	{
		super.paint(g);
    	Graphics2D g2d = (Graphics2D)g;
    	g2d.setFont(new Font ( "宋体" , Font.BOLD ,26 ));
		String text = ZLResource.resource("Exercise").getResource("ScreenOrientation").getValue();
		int twidth = getStringWidth(text, g2d.getFont());
		int theight = getStringHeight(text, g2d.getFont());
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		if(bi!=null)
		{
			AffineTransform transformemp = g2d.getTransform();
			AffineTransform transform = new AffineTransform(); 
			transform.translate(this.getBounds().width/2, this.getBounds().height/2);
			transform.rotate(Math.PI / 2.0);
			transform.translate(-this.getBounds().height/2,-this.getBounds().width/2);
			g2d.setTransform(transform);
			
			
//			g2d.InterpolationMode = System.Drawing.Drawing2D.InterpolationMode.NearestNeighbor; 
//			g2d.PixelOffsetMode = System.Drawing.Drawing2D.PixelOffsetMode.Half;
//			g2d.SmoothingMode = Drawing2D.SmoothingMode.HighQuality;
//			Graphics.SmoothingMode = Drawing2D.SmoothingMode.HighQuality;
			g2d.drawImage(bi, 0, 0, null);
			g2d.setTransform(transformemp);
			 {
				 g2d.setColor(new Color(255,255,255));
				 g2d.fillRoundRect((this.getBounds().width-twidth)/2-30, (this.getBounds().height-theight)/2-50, twidth+60, theight+110, 20, 20);

				 g2d.setStroke(new BasicStroke(8.0f));
				 g2d.setColor(new Color(34,177,76));
//				 g2d.drawRect((this.getBounds().width-twidth)/2-10, (this.getBounds().height-theight)/2-50, twidth+80, theight+110);
				 g2d.drawRoundRect((this.getBounds().width-twidth)/2-30, (this.getBounds().height-theight)/2-50, twidth+60, theight+110,20,20);
				 g2d.setStroke(new BasicStroke(1.0f));
				 g2d.setColor(new Color(0,0,0));
			 }
			g2d.drawString(text, (this.getBounds().width-twidth)/2, (this.getBounds().height-theight)/2+theight);
			transform=null;
		}
	}

}
