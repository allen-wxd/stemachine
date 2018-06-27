package com.windows.library.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Composite;

public class TaskProgress extends Dialog {
	public interface TaskOper {
		abstract public void run();
	}
	protected Object result;
	protected Shell shell;
	public String Message;
	private FormData fd_lblNewLabel_1;
	private TaskOper toper;
//	private Composite composite;
	private AsyncTask<Void,Integer,Void> SendTask;
	
	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public TaskProgress(Shell parent) {
		super(parent, SWT.NONE);
		setText("SWT Dialog");
	}
	
	public void setTOper(TaskOper oper)
	{
		this.toper = oper;
	}

	/**
	 * Open the dialog.
	 * 
	 * @return the result
	 */
	public Object open() {
		createContents();
		Display display = getParent().getDisplay();
		
		shell.setText("About");
		shell.setLayout(new FormLayout());

//		ImageLoader loader  =   new  ImageLoader();  
//        ImageData[] imageDatas  =  loader.load(Constants.WORKING_DIRECTORY+"res\\drawable\\waiting.gif");  
//         if  (imageDatas.length  ==   0 )  
//             return null;
//         else   if  (imageDatas.length  ==   1 ) {  
//            ic.setImage(imageDatas[ 0 ]);  
//        }  else  {  
//            ic.setImages(imageDatas, imageDatas.length);  
//        }
		CLabel lblNewLabel = new CLabel(shell, SWT.NONE);
		lblNewLabel.setFont(SWTResourceManager.getFont("楷体", 16, SWT.BOLD));
		lblNewLabel.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		fd_lblNewLabel_1 = new FormData();
		fd_lblNewLabel_1.left = new FormAttachment(0, 10);
		fd_lblNewLabel_1.top = new FormAttachment(0, 66);
		lblNewLabel.setLayoutData(fd_lblNewLabel_1);
		lblNewLabel.setText(Message);
		
//		composite = new Composite(shell, SWT.NONE);
//		FormData fd_composite = new FormData();
//		fd_composite.top = new FormAttachment(0, 35);
//		fd_composite.left = new FormAttachment(0, 10);
//		fd_composite.bottom = new FormAttachment(0, 130);
//		fd_composite.right = new FormAttachment(0, 100);
//		composite.setLayoutData(fd_composite);
//		composite.setLayout(new FormLayout());
//		
//		ImgViewer ic  =   new  ImgViewer(composite);
//		ic.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_DARK_SHADOW));
//		FormData fd_toolBar = new FormData();
//		ic.setLayoutData(fd_toolBar);
//
//		fd_toolBar.top = new FormAttachment(0,0);
//		fd_toolBar.left = new FormAttachment(0,0);
//		
//	    String string  = "res\\drawable\\waiting.gif";  
//
//	    ImageLoader loader  =   new  ImageLoader();  
//	    ImageData[] imageDatas  =  loader.load(string);  
//	     if  (imageDatas.length  ==   0 )  
//	         return null;  
//	     else   if  (imageDatas.length  ==   1 ) {  
//	        ic.setImage(imageDatas[ 0 ]);  
//	    }  else  {  
//	        ic.setImages(imageDatas, loader.repeatCount);  
//	    }  
//
//	    ic.pack();
		
		shell.open();
		shell.layout();
		shell.setSize(lblNewLabel.getBounds().width+80, 172);
		shell.setLocation(display.getClientArea().width / 2 - shell.getSize().x / 2, display.getClientArea().height / 2
				- shell.getSize().y / 2);
		WaitintTask();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}
	


	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), SWT.BORDER | SWT.APPLICATION_MODAL);
		shell.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		shell.setBackgroundMode(SWT.INHERIT_FORCE);
		shell.setSize(0, 0);
		Display display = getParent().getDisplay();
		shell.setLocation(display.getClientArea().width / 2 - shell.getSize().x / 2, display.getClientArea().height / 2
				- shell.getSize().y / 2);
		shell.setModified(true);

	}
	
	public void Close() {
//		composite.dispose();
		SendTask.cancel(true);
		this.shell.close();
		this.shell.dispose();
	}
	
	public void WaitintTask()
	{
		SendTask = new AsyncTask<Void,Integer,Void>() {
			@Override
			protected Void doInBackground(Void... params) {
					try {
						Thread.sleep(10);
						if(toper!=null)
						  toper.run();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				TaskProgress.this.Close();
				while(!SendTask.isCancelled());
				
			}

			@Override
			protected void onCancelled() {
			}

			@Override
			protected void onProgressUpdate(Integer... values) {
			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
			}
		};

		SendTask.execute();
	}
}
