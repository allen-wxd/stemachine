package com.wistron.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	public static String getNowTime(){
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(d);
	}
	public static void main(String[] args) {
		System.out.println(getNowTime());
	}
}
