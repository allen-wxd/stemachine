/*
 
 */

package com.windows.library.service;

import org.eclipse.swt.graphics.Image;

public abstract class Constants {
	public static String DEFAULT_DIRECTORY = "";
	public static String CURRENT_DIRECTORY = "";	
	public static String CURRENT_DIRECTORY1 = "";
	public static String WORKING_DIRECTORY = "";
	public static String BOOKS_DIRECTORY = "/sdcard/eBooks";
	public static String BOOKS_DIRECTORY1 = "/s-space/eBooks";
	//public static String BOOKS_DIRECTORY = "/data/data/ebooks";
	public static String BOOKS_MusicDIRECTORY = "/sdcard/Musics";
	public static String BOOKS_PhotoDIRECTORY = "/sdcard/Photos";
	public static String BOOKS_MusicDIRECTORY1 = "/s-space/Musics";
	public static String BOOKS_PhotoDIRECTORY1 = "/s-space/Photos";	
	public static String BOOKS_INTERACTIVE = "/sdcard/interactive";
	public static String CACHE_DIRECTORY =  "/data/data/com.wistron.android.wieducation/.ebookreader";
	public static String CACHE_COVER = "res\\covor";
	public static String CACHE_MEDIA = "/data/data/com.wistron.android.wieducation/media";
	public static String CLASSMAKER_DIRECTORY = "";
	public static String CLASSMAKER_DIRECTORY_CONTENT = "";
	public static String CLASSMAKER_DIRECTORY_THUM = "";
	public static String CLASSMAKER_DIRECTORY_QUIZ = "";
	public static String CLASSMAKER_DIRECTORY_QUIZ_TEMP = "";
	public static String CLASSMAKER_NOTES_DIRECTORY = "";
	public static String CLASSMAKER_VTHUM_DIRECTORY = "";
	public static String DOUBLEPLAY="N";
	public static String CURRENT_PLAYFILE = "";
	public static String SERVERIP = "";
	public static String SERVERNAME = "";
	public static int CURRENTINDEX=0;
	
	public static enum ScreenOrientation {
		LANDSCAPE,PORTRAIT
    }
	public static ScreenOrientation Orientation=ScreenOrientation.LANDSCAPE;
	
	public static Image screenimage;
}