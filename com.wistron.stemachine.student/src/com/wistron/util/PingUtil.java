package com.wistron.util;

import java.net.InetAddress;

public class PingUtil {

	 public static boolean ping(String ipAddress) throws Exception {  
        int  timeOut =  5000 ;  //超时应该在3钞以上          
        boolean status = InetAddress.getByName(ipAddress).isReachable(timeOut);     // 当返回值是true时，说明host是可用的，false则不可。  
        return status;  
	 } 
	
	 public static void main(String[] args) {
		try {
			System.out.println("result---->"+PingUtil.ping("10.43.148.132"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
