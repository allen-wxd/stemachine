package com.windows.library.control;

import java.util.ArrayList;

import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;


public class FrameStack extends Composite {
	ArrayList<Composite> Items = new ArrayList();
	private int currentindex;
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public FrameStack(Composite parent, int style) {
		super(parent, style);
		setLayout(new FormLayout());
        Items.clear();
        currentindex=-1;
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	public void AddItem(Composite com)
	{
		Items.add(com);
		currentindex=0;
	}
	
	public void showItem(int index)
	{
		if(index>=Items.size())return;
		currentindex=index;
		for(Composite com:Items)
			com.setVisible(false);
		Items.get(index).setVisible(true);
	}
	
	public Composite getItem(int index)
	{
		return Items.get(index);
	}
	
	public Composite getCurrent()
	{
		return Items.get(currentindex);
	}
	
	public int getCount()
	{
		return Items.size();
	}
	
	public int getCurrentIndex()
	{
		return currentindex;
	}
}
