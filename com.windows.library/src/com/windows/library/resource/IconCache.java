/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.windows.library.resource;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.program.*;
import org.eclipse.swt.widgets.*;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.util.*;

/**
 * Manages icons for the application.
 * This is necessary as we could easily end up creating thousands of icons
 * bearing the same image.
 */
public class IconCache {	    
	public final String[] stockImageLocations = {
		
	};
	private String[] res = {"progress_1.png",
			"progress_2.png",
			"progress_3.png",
			"progress_4.png",
			"progress_5.png",
			"progress_6.png",
			"progress_7.png",
			"progress_8.png"};
	public Image stockImages[];
	
	// Stock cursors
	public final int
		cursorDefault = 0,
		cursorWait = 1;
	public Cursor stockCursors[];
	// Cached icons
	private int screenwidth;
	private int screenheight;
	
    public static IconCache Icon;
    
    private HashMap<String, Image> map = new HashMap<String, Image>();
    private HashMap<String, java.awt.Image> awtimgmap = new HashMap<String, java.awt.Image>();
	
	static public IconCache Hinstance()
	{
		if(Icon!=null)return Icon;
		else
		{
			return new IconCache();
		}
	}
	
	public IconCache() {
		Icon=this;
		map.clear();
		awtimgmap.clear();
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

		screenwidth = screen.width;
		screenheight = screen.height;
		
		initResources();
	}
	/**
	 * Loads the resources
	 * 
	 * @param display the display
	 */
	public void initResources() {
		for(int i=0;i<8;i++)
		{
			getawtimg(res[i]);
		}
	}
	/**
	 * Frees the resources
	 */
	public void freeResources() {
		map.clear();
		awtimgmap.clear();
		Icon = null;
	}
	/**
	 * Creates a stock image
	 * 
	 * @param display the display
	 * @param path the relative path to the icon
	 */
	private Image createStockImage(Display display, String path) {
//		InputStream stream = IconCache.class.getResourceAsStream (path);
//		ImageData imageData = new ImageData (stream);
//		ImageData mask = imageData.getTransparencyMask ();
//		Image result = new Image (display, imageData, mask);
		String screensize = "-"+screenwidth+"x"+screenheight;
		Image result = null;
		String language = ZLResource.getLanguage().toLowerCase();
		if(language.contains("sg")||language.contains("cn"))
		{
			language="zh-cn";
			String languagepath = path.replace("drawable", "drawable-"+language+screensize);
			File file = new File(languagepath);
			if(file.exists())
			{
				result = new Image (display,languagepath);
			}
			else if(!file.exists())
			{
				languagepath = path.replace("drawable", "drawable-"+language);
				file = new File(languagepath);
				if(file.exists())
				{
					result = new Image (display,languagepath);
				}
				else result = new Image (display,path);
			}
			
		}
		
		else if(language.contains("tw"))
		{
			language="zh-tw";
			String languagepath = path.replace("drawable", "drawable-"+language+screensize);
			File file = new File(languagepath);
			if(file.exists())
			{
				result = new Image (display,languagepath);
			}
			else if(!file.exists())
			{
				languagepath = path.replace("drawable", "drawable-"+language);
				file = new File(languagepath);
				if(file.exists())
				{
					result = new Image (display,languagepath);
				}
				else result = new Image (display,path);
			}
			
		}
		else if(language.contains("en"))
		{
			language="en";
			String languagepath = path.replace("drawable", "drawable-"+language+screensize);
			File file = new File(languagepath);
			if(file.exists())
			{
				result = new Image (display,languagepath);
			}
			else if(!file.exists())
			{
				languagepath = path.replace("drawable", "drawable-"+language);
				file = new File(languagepath);
				if(file.exists())
				{
					result = new Image (display,languagepath);
				}
				else result = new Image (display,path);
			}
			
		}
		else result = new Image (display,path);
//		try {
//			stream.close ();
//		} catch (IOException e) {
//			e.printStackTrace ();
//		}
		return result;
	}
	
	public Image getimg(String name) {

		Image img = map.get(name);
		if (img != null)
			return img;
		else {
			String path = System.getProperty("user.dir")+"\\res\\drawable\\" + name;
			String language = ZLResource.getLanguage().toLowerCase();
			String languagepath = path.replace("drawable", "drawable-" + language);
			File file = new File(languagepath);
			if (file.exists()) {
				img = new Image(null,languagepath);
			} else if (language.contains("zh")) {
				language = "zh-tw";
				languagepath = path.replace("drawable", "drawable-" + language);
				file = new File(languagepath);
				if (file.exists()) {
					img = new Image(null,languagepath);
				} else
					img = new Image(null,path);
			} else if (language.contains("en")) {
				language = "en";
				languagepath = path.replace("drawable", "drawable-" + language);
				file = new File(languagepath);
				if (file.exists()) {
					img = new Image(null,languagepath);
				} else
					img = new Image(null,path);
			} else
				img = new Image(null,path);
			map.put(name, img);
			return img;
		}
	}
	
	public java.awt.Image getawtimg(String name) {

		java.awt.Image img = awtimgmap.get(name);
		if(img != null)
		  return img;
		else {
			String path = System.getProperty("user.dir")+"\\res\\drawable\\" + name;
			String language = ZLResource.getLanguage().toLowerCase();
			String languagepath = path.replace("drawable", "drawable-" + language);
			File file = new File(languagepath);
			if (file.exists()) {
				img = Toolkit.getDefaultToolkit().getImage(languagepath);
			} else if (language.contains("zh")) {
				language = "zh-tw";
				languagepath = path.replace("drawable", "drawable-" + language);
				file = new File(languagepath);
				if (file.exists()) {
					img = Toolkit.getDefaultToolkit().getImage(languagepath);
				} else
					img = Toolkit.getDefaultToolkit().getImage(path);
			} else if (language.contains("en")) {
				language = "en";
				languagepath = path.replace("drawable", "drawable-" + language);
				file = new File(languagepath);
				if (file.exists()) {
					img = Toolkit.getDefaultToolkit().getImage(languagepath);
				} else
					img = Toolkit.getDefaultToolkit().getImage(path);
			} else
				img = Toolkit.getDefaultToolkit().getImage(path);
			awtimgmap.put(name, img);
			return img;
		}
	}
}
