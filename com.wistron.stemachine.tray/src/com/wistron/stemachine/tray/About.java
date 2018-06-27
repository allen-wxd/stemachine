package com.wistron.stemachine.tray;

import com.windows.library.resource.IconCache;
import com.windows.library.resource.MonitorResource;
import com.windows.library.resource.ZLResource;
import com.windows.library.util.Util;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.HttpURLConnection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.MyButton.ImgButton;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;

public class About extends Dialog {

	protected Object result;
	protected Shell shell;
	private Composite Title;
	private Image logoimg;
	private ImgButton activation;
    private Label activationstatus;
    public static About instance;
	
	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public About(Shell parent) {
		super(parent, SWT.DIALOG_TRIM);
		instance=this;
//		Thread.UncaughtExceptionHandler				
	}

	/**
	 * Open the dialog.
	 * 
	 * @return the result
	 */
	public Object open() {
		createContents();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		instance=null;
		return result;
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
	
	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), SWT.APPLICATION_MODAL|SWT.DOUBLE_BUFFERED);
		shell.setBackground(SWTResourceManager.getColor(255, 255, 255));
		shell.setBackgroundMode(SWT.INHERIT_FORCE);
		shell.setModified(true);
//        Display display = getParent().getDisplay();
        logoimg = IconCache.Hinstance().getimg("icon_STEMachine_72.png");
		shell.setText("About");
		shell.setLayout(new FormLayout());
//		GetTimePeriod();
		String title=ZLResource.resource("toolbar").getResource("about").getValue()+" WiStemachine";
		Title = new Composite(shell,SWT.NONE);
		Title.setLayout(new FormLayout());
		FormData fd_lblNewLabel = new FormData();
		fd_lblNewLabel.top = new FormAttachment(0);
		fd_lblNewLabel.left = new FormAttachment(0);
		fd_lblNewLabel.bottom = new FormAttachment(0,0);
		fd_lblNewLabel.right = new FormAttachment(100);
		Title.setLayoutData(fd_lblNewLabel);
		Title.setBackground(new Color(null,82,146,226));
		
		Label lblNewLabel = new Label(Title, SWT.NONE);
		lblNewLabel.setFont(SWTResourceManager.getFont("微软雅黑", 11, SWT.NORMAL));
		lblNewLabel.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		fd_lblNewLabel = new FormData();
		fd_lblNewLabel.top = new FormAttachment(0);
		fd_lblNewLabel.left = new FormAttachment(0, 10);
		lblNewLabel.setLayoutData(fd_lblNewLabel);
		lblNewLabel.setText(title);
		int height=Util.getStringHeightInternal(new GC(lblNewLabel));
		((FormData)Title.getLayoutData()).bottom.offset=height;
		
		Label lblWiclassmaker = new Label(shell, SWT.NONE);
		lblWiclassmaker.setText("WiStemachine 10-Wx v1.00 (180330.003-T)");
		lblWiclassmaker.setForeground(SWTResourceManager.getColor(97, 152, 221));
		lblWiclassmaker.setFont(SWTResourceManager.getFont("微软雅黑", 15, SWT.NORMAL));
		FormData fd_lblWiclassmaker = new FormData();
		fd_lblWiclassmaker.top = new FormAttachment(Title, 50,SWT.BOTTOM);
		fd_lblWiclassmaker.left = new FormAttachment(0, 50+logoimg.getBounds().width+10);
		lblWiclassmaker.setLayoutData(fd_lblWiclassmaker);
		//Copyright © 2001-2018.Wistron Corporation All Rights Peserved.
		Label lblCopyrightWistronCorporation = new Label(shell, SWT.NONE);
		lblCopyrightWistronCorporation.setText("Copyright ©2018.Wistron Corporation.");
		lblCopyrightWistronCorporation.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		lblCopyrightWistronCorporation.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
		FormData fd_lblCopyrightWistronCorporation = new FormData();
		fd_lblCopyrightWistronCorporation.top = new FormAttachment(lblWiclassmaker, 12);
		fd_lblCopyrightWistronCorporation.left = new FormAttachment(lblWiclassmaker, 0, SWT.LEFT);
		lblCopyrightWistronCorporation.setLayoutData(fd_lblCopyrightWistronCorporation);
		
		Label lblwistronCorporation = new Label(shell, SWT.NONE);
		lblwistronCorporation.setText("All rights reserved.");
		lblwistronCorporation.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		lblwistronCorporation.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
		FormData fd_lblwistronCorporation = new FormData();
		fd_lblwistronCorporation.top = new FormAttachment(lblCopyrightWistronCorporation, 0);
		fd_lblwistronCorporation.left = new FormAttachment(lblWiclassmaker, 0, SWT.LEFT);
		lblwistronCorporation.setLayoutData(fd_lblwistronCorporation);
		
		ImgButton ok = new ImgButton(shell,SWT.TRANSPARENT);
		ok.setNormalimg(IconCache.Hinstance().getimg("about_but_normal.png"));
		ok.sethotimg(IconCache.Hinstance().getimg("about_but_over.png"));
		ok.setpressimg(IconCache.Hinstance().getimg("about_but_click.png"));
		ok.setSize(110, 62);
		ok.setTposition(ImgButton.TPosition.CENTER);
//		importppt.setTalignment(ImgButton.TAlignment.BOTTOM);
		ok.setWrapContent(true);
		ok.setTitle(ZLResource.resource("app").getResource("ok").getValue());
		ok.setFontColor(SWTResourceManager.getColor(0, 0, 0));
		ok.setFont(SWTResourceManager.getFont("微软雅黑", 11, SWT.NORMAL));
		ok.setOnLayout(new ImgButton.LayoutInterface() {
			
			@Override
			public void Layout(int width,int height) {
				// TODO Auto-generated method stub
					FormData fd_newcut = null;
					fd_newcut = (FormData) ok.getLayoutData();
					if(fd_newcut==null)
					{
						fd_newcut = new FormData();
						ok.setLayoutData(fd_newcut);
						fd_newcut.bottom = new FormAttachment(100, -80+height);
						fd_newcut.right = new FormAttachment(100, -150+width);
						fd_newcut.top = new FormAttachment(100,-80);
						fd_newcut.left = new FormAttachment(100,-150);
					}
					else
					{
						shell.layout();
					}
				}
		    });
		ok.Layout();
		
		ok.setOnclick(new ImgButton.OnclickListener() {
			@Override
			public void Onclick() {
				// TODO Auto-generated method stub
				Close();
			}
		});
		
		shell.layout();
		
		shell.addPaintListener(new PaintListener(){
			@Override
			public void paintControl(PaintEvent e) {
				// TODO Auto-generated method stub
				GC gc = e.gc;
				gc.drawImage(logoimg, lblWiclassmaker.getBounds().x-logoimg.getBounds().width-10, lblWiclassmaker.getBounds().y);
			}
	    });
		
		Listener listener = new Listener() {  
		    int startX, startY;  
		    public void handleEvent(Event e) {  
		        if (e.type == SWT.MouseDown && e.button == 1) {  
		            startX = e.x;  
		            startY = e.y;  
		        }  
		        if (e.type == SWT.MouseMove && (e.stateMask & SWT.BUTTON1) != 0) {  
		            Point p = shell.toDisplay(e.x, e.y);  
		            p.x -= startX;  
		            p.y -= startY;  
		            shell.setLocation(p);  
		        }  
		    }  
		};  
		Title.addListener(SWT.MouseDown, listener);  
		Title.addListener(SWT.MouseMove, listener);
		
		shell.setSize(50+logoimg.getBounds().width+10+lblWiclassmaker.getBounds().width+80, 280);
		shell.setLocation(MonitorResource.getMonitor(0).getClientArea().width / 2 - shell.getSize().x / 2, MonitorResource.getMonitor(0).getClientArea().height / 2
				- shell.getSize().y / 2);
		shell.setVisible(true);
		shell.open();			
	}
	
	public String Readinfo()
	{
		String path=System.getProperty("user.home")+"\\Documents\\";
		File file = new File(path+"1");
		if(!file.exists())return "0";
		String str = null;
		FileReader reader = null;
		try 
		{
            // read file content from file
//            StringBuffer sb= new StringBuffer("");
           
			reader = new FileReader(path+"1");
            BufferedReader br = new BufferedReader(reader);
            
           
            while((str = br.readLine()) != null) {
            	System.out.println(str);
            	break;
            }
		}
		catch(FileNotFoundException e) {
            e.printStackTrace();
            return "0";
        }
        catch(IOException e) {
            e.printStackTrace();
            return "0";
        }finally {
			try {
				reader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return str;
	}
	
	public void Close() {
		About.this.shell.close();
	}

}
