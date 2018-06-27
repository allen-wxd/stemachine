package com.windows.library.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import com.windows.library.resource.ZLResource;
import com.windows.library.service.Constants;
import com.windows.library.util.Util;

import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Transform;

public class LandScreen extends Shell {

	private static LandScreen shell;
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Display display = Display.getDefault();
			shell = new LandScreen(display);
			shell.open();
			shell.layout();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} catch (Exception e) {
			Util.writeLog(e, "LandScreen.txt");
		}
	}
	
	public static void Destroy()
	{
		if(shell!=null&&!shell.isDisposed())
		{
		  shell.close();
		  shell=null;
		}
	}
	
	public int getStringWidth(GC gc, char[] string, int offset, int length) {
		int width = 0;
		for (int i = 0; i < length; i++) {
			width += gc.getAdvanceWidth(string[offset + i]);
		}
		width = (int) (width + 0.5f);
		return width;
	}

	protected int getStringHeightInternal(GC gc) {
		return (int) (gc.getFontMetrics().getHeight() + 0.5f);
	}
	
	public static boolean isShowing()
	{
	    if(shell!=null&&!shell.isDisposed())
	      return shell.isVisible();
	    return false;
	}
	
	public static void show()
	{
//		if(shell==null||shell.isDisposed())
		{
			try {
				Display display = Display.getDefault();
				shell = new LandScreen(display);
				shell.open();
				shell.layout();
				while (!shell.isDisposed()) {
					if (!display.readAndDispatch()) {
						display.sleep();
					}
				}
			} catch (Exception e) {
				Util.writeLog(e, "LandScreen.txt");
			}
		}
//		shell.setVisible(true);
//		shell.redraw();
	}

	/**
	 * Create the shell.
	 * @param display
	 */
	public LandScreen(Display display) {
		super(display, SWT.APPLICATION_MODAL|SWT.NO_TRIM);
		createContents();
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("SWT Application");
//		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//		setSize(screenSize.width, screenSize.height);
		this.setMaximized(true);
//		this.setLocation(0, 0);
		
		addControlListener(new ControlListener()
		{
			@Override
			public void controlMoved(ControlEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void controlResized(ControlEvent e) {
				// TODO Auto-generated method stub
//				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//				if(shell!=null&&!shell.isDisposed())
//				{
//					if(screenSize.width>screenSize.height)
//					{
//						LandScreen.this.close();
//						shell=null;
//					}
//				}
			}
		});
		
		addPaintListener(new PaintListener() {
			public void paintControl(final PaintEvent event) {
				GC gc = event.gc;
				gc.setFont(SWTResourceManager.getFont("宋体", 18, SWT.BOLD));
				String text = ZLResource.resource("Exercise").getResource("ScreenOrientation").getValue();
				int twidth = getStringWidth(gc, text.toCharArray(), 0, text.length());
				int theight = getStringHeightInternal(gc);
				if(Constants.screenimage!=null)
				{
//					transform.translate(rect.width/2, rect.height/2);
//					transform.rotate(rotateSpinner.getSelection());
//					transform.translate(-rect.width/2, -rect.height/2);
					Transform transform = new Transform(null); 
					transform.translate(LandScreen.this.getBounds().width/2, LandScreen.this.getBounds().height/2);
					 transform.rotate(90); 
					 transform.translate(-LandScreen.this.getBounds().height/2,-LandScreen.this.getBounds().width/2);
					 gc.setTransform(transform); 
					 gc.drawImage(Constants.screenimage, 0, 0); 
					 gc.setAdvanced(false);
					 {
						 gc.setBackground(new Color(null,255,255,255));
						 gc.fillRoundRectangle((LandScreen.this.getBounds().width-twidth)/2-30, (LandScreen.this.getBounds().height-theight)/2-50, twidth+60, theight+110, 20, 20);
						 gc.setLineWidth(8);
						 gc.setForeground(new Color(null,34,177,76));
//						 gc.drawRectangle((LandScreen.this.getBounds().width-twidth)/2-10, (LandScreen.this.getBounds().height-theight)/2-50, twidth+80, theight+110);
						 gc.drawRoundRectangle((LandScreen.this.getBounds().width-twidth)/2-30, (LandScreen.this.getBounds().height-theight)/2-50, twidth+60, theight+110, 20, 20);
						 gc.setLineWidth(1);
						 gc.setForeground(new Color(null,0,0,0));
					 }
					 gc.drawText(text, (LandScreen.this.getBounds().width-twidth)/2, (LandScreen.this.getBounds().height-theight)/2,true);
					 transform.dispose(); 
				}     				
			}
		});
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
