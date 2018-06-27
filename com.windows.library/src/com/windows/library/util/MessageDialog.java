package com.windows.library.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;

import com.windows.library.resource.MonitorResource;
import com.windows.library.resource.ZLResource;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
public class MessageDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private String Message;
	private String Title;
		
	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public MessageDialog(Shell parent) {
		super(parent, SWT.CLOSE);
//		Thread.UncaughtExceptionHandler
		setText("");
		Message="";
		Title="";
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
		shell = new Shell(getParent(), SWT.BORDER | SWT.TITLE|SWT.APPLICATION_MODAL|SWT.DOUBLE_BUFFERED);
		shell.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		shell.setBackgroundMode(SWT.INHERIT_FORCE);
		shell.setModified(true);
		
        Display display = getParent().getDisplay();
		
		shell.setText(Title);
		shell.setLayout(new FormLayout());
		
		Label lblNewLabel = new Label(shell, SWT.NONE);
//		lblNewLabel.setFont(SWTResourceManager.getFont("宋体", 19, SWT.NORMAL));
		FormData fd_lblNewLabel = new FormData();
		fd_lblNewLabel.top = new FormAttachment(0, 24);
		fd_lblNewLabel.left = new FormAttachment(0, 20);
		lblNewLabel.setLayoutData(fd_lblNewLabel);
		lblNewLabel.setText(Message);
		
		Button btnNewButton_1 = new Button(shell, SWT.NONE);
//		btnNewButton_1.setFont(SWTResourceManager.getFont("宋体", 9, SWT.NORMAL));
		btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				result=-1;
				shell.close();
			}
		});
		FormData fd_btnNewButton_1 = new FormData();
		fd_btnNewButton_1.top = new FormAttachment(100, -40);
		fd_btnNewButton_1.left = new FormAttachment(100, -70);
		fd_btnNewButton_1.right = new FormAttachment(100, -10);
		fd_btnNewButton_1.bottom = new FormAttachment(100, -10);
		btnNewButton_1.setLayoutData(fd_btnNewButton_1);
		btnNewButton_1.setText(ZLResource.resource("app").getResource("cancel").getValue());
		
		Button btnNewButton = new Button(shell, SWT.NONE);
//		btnNewButton.setFont(SWTResourceManager.getFont("宋体", 9, SWT.NORMAL));
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				result=0;
				shell.close();
			}
		});
		FormData fd_btnNewButton = new FormData();
		fd_btnNewButton.top = new FormAttachment(btnNewButton_1, 0,SWT.TOP);
		fd_btnNewButton.bottom = new FormAttachment(btnNewButton_1, 0,SWT.BOTTOM);
		fd_btnNewButton.left = new FormAttachment(btnNewButton_1, -70 ,SWT.LEFT);
		fd_btnNewButton.right = new FormAttachment(btnNewButton_1, -10 ,SWT.LEFT);
		btnNewButton.setLayoutData(fd_btnNewButton);
		btnNewButton.setText(ZLResource.resource("app").getResource("ok").getValue());
		
        shell.layout();
		
//		shell.setSize(428, 142);
		shell.setSize(161+35+lblNewLabel.getBounds().width, 142);
		int maxwidth=MonitorResource.getBounds().width;
		shell.setLocation(maxwidth/2+maxwidth / 4 - shell.getSize().x / 2, MonitorResource.getMonitor(0).getClientArea().height / 2
				- shell.getSize().y / 2);
//		shell.setLocation(maxwidth - shell.getSize().x , MonitorResource.getMonitor(0).getClientArea().height / 2
//				- shell.getSize().y / 2);
		shell.setVisible(true);
		shell.open();
		
	}
	
	public void setMessage(String message)
	{
		this.Message=message;
	}
	
	public void setTitle(String title)
	{
		this.Title=title;
	}
	
	public void Close() {
		MessageDialog.this.shell.close();

	}
}
