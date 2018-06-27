package com.windows.library.util;

import java.util.ArrayList;

import org.eclipse.swt.graphics.Image;

/**
 * 全局变量
 * 
 * @author Jack qp 
 * 
 */

public class GData {

	public static int        SMARTPAPER_QUESTION_UNANSWER_COUNT = 0;  
	public static int        SMARTPAPER_MODE_ALL = 0;
	public static int        SMARTPAPER_MODE_WRONG = 1;
	public static int        SMARTPAPER_MODE_FAVORITE = 2;
	public static int        SMARTPAPER_MODE_VIEW = 3;
	public static int        SMARTPAPER_MODE_ALL_UNKNOW = 4;
	
	public static final int   MESSAGE_SCREEN_CUT_PASTE = 98;
	public static final int   MESSAGE_SCREEN_CUT_PICTURE = 99;
	public static final int   MESSAGE_SCREEN_CUT = 100;
	public static final int   MESSAGE_SCREEN_CUT_CANCEL = 101;
	public static final int   MESSAGE_TEXTWORD = 102;
	public static final int   MESSAGE_RECORDER_CANCEL = 103;
	public static final int   MESSAGE_MP3 = 200;
	public static final int   MESSAGE_FONT = 201;
	public static final int   MESSAGE_PRESS_CUTSCREEN_CANCEL = 299;
	public static final int   MESSAGE_PRESS_CUTSCREEN = 300;
	public static final int   MESSAGE_PRESS_PICTURE = 301;
	public static final int   MESSAGE_CUTGALLERY_PICTURE = 302;
	public static final int   MESSAGE_DELETE_LOCAL = 401;
	public static final int   MESSAGE_DELETE_CLOUD = 402;
	public static final int   MESSAGE_DELETE_ALL = 403;
	
	public static final int   MESSAGE_WORD_OBJECT = 501;
	
	//录音
//	public static String    PATH_CURRENT_FILE = "Unknow File";
	
//	public static int   screenWidth = 1280;
//	public static int   screenHeight = 752;
	
	public static enum pencolor {
        BLACK, RED, BLUE,GREEN
    }
	
	public static enum paintstyle {
		PEN,ERASER;
	}
}
