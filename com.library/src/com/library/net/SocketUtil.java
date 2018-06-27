package com.library.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class SocketUtil {
  
  public interface HandleMessage
  {
		abstract public void Handle(String message);
  }
  public SocketUtil() {  }
  
  public void CreateSocketServer(int Port) throws IOException
  {
      //定义一个ServerSocket监听在端口8899上  
      ServerSocket server = new ServerSocket(Port);  
      while (true) {  
         //server尝试接收其他Socket的连接请求，server的accept方法是阻塞式的  
         Socket socket = server.accept();  
         //每接收到一个Socket就建立一个新的线程来处理它  
         new Thread(new Task(socket)).start();  
      }
   }  
     
   /** 
    * 用来处理Socket请求的 
    */  
   static class Task implements Runnable {  
   
      private Socket socket;  
        
      public Task(Socket socket) {  
         this.socket = socket;  
      }  
        
      public void run() {  
         try {  
            handleSocket();  
         } catch (Exception e) {  
         }  
      }  
        
      /** 
       * 跟客户端Socket进行通信 
      * @throws Exception 
       */  
      private void handleSocket() throws Exception {
         BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));  
         StringBuilder sb = new StringBuilder();  
         String temp;  
         int index;  
         while ((temp=br.readLine()) != null) {  
            System.out.println(temp);
            if ((index = temp.indexOf("eof")) != -1) {//遇到eof时就结束接收  
             sb.append(temp.substring(0, index));  
                break;  
            }  
            sb.append(temp);  
         }  
         System.out.println("客户端: " + sb);  
         //读完后写一句  
         Writer writer = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");  
         writer.write("你好，客户端。");  
         writer.write("eof\n");  
         writer.flush();  
         writer.close();  
         br.close();  
         socket.close();  
      } 
  }
   
  public void Sendto(String host,int port,String msg) throws UnknownHostException, IOException
  {
	try {
		 Socket client = new Socket(host, port);  
	      //建立连接后就可以往服务端写数据了  
	     Writer writer = new OutputStreamWriter(client.getOutputStream(), "UTF-8");  
	     writer.write(msg);  
	     writer.flush();
	     writer.close(); 
	     client.close();
	} catch (UnknownHostException e) {
		// TODO Auto-generated catch block
	} catch (IOException e) {
		// TODO Auto-generated catch block
	}
	
  }
  
  public void CreateClient(String host,int port) throws UnknownHostException, IOException
  {
	     //与服务端建立连接  
	     Socket client = new Socket(host, port);  
	      //建立连接后就可以往服务端写数据了  
	     Writer writer = new OutputStreamWriter(client.getOutputStream(), "UTF-8");  
	     writer.write("你好，服务端。");  
	     writer.write("eof\n");  
	     writer.flush();  
	      //写完以后进行读操作  
	     BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream(), "UTF-8"));  
	      //设置超时间为10秒  
	     client.setSoTimeout(10*1000);  
	      StringBuffer sb = new StringBuffer();  
	      String temp;  
	      int index;  
	      try 
	      {  
	         while ((temp=br.readLine()) != null) 
	         {  
	            if ((index = temp.indexOf("eof")) != -1) 
	            {  
	                sb.append(temp.substring(0, index));  
	                break;  
	            }  
	            sb.append(temp);  
	         }  
	      } catch (SocketTimeoutException e) 
	      {  
	         System.out.println("数据读取超时。");  
	      }  
	      System.out.println("服务端: " + sb);  
	      writer.close();  
	      br.close();  
	      client.close();  
  }
}