package com.wistron.stemachine.tray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class StartupManager{
    
	public interface OnRuningListener
	{
		abstract public void runing(String message);
	}
	private ServerSocket applLockSocket;
	private OnRuningListener Onruning;
	public int Port;
	public void setRuningListener(OnRuningListener Onruning) {
	      this.Onruning = Onruning;		
		}
	
    public StartupManager(int port)
    {
    	this.Port = port;
    }
    
	 public boolean isRunning(String message)
	 {

	   try{ applLockSocket=new ServerSocket(Port, 50, InetAddress.getByName( "127.0.0.1" ) );
	   
	        handleSocketUnBound();  //

	        return false;
	      }
	   catch( java.net.BindException e )    //already running
	        {
	          handleSocketBound( message );  //

	          return true;
	        } 
	   catch( IOException e )  //Other Error
	        {
	          return false;
	        }
	 }

	 public void handleSocketBound( String message )
	 {
	   Socket socket;

	   try{ socket=new Socket( InetAddress.getByName( "127.0.0.1" ), Port );

	        PrintWriter writer=new PrintWriter(new OutputStreamWriter( socket.getOutputStream() ) );

	        writer.println( ( message!=null&&message.length()>0 ) ? message : "" );

	        writer.flush();
	      }
	   catch( UnknownHostException e )
	        {
//	          ...
	        }
	   catch( IOException e )
	        {
//	          ...
	        }
	 }
	 
	 private void handleSocketUnBound()
	 {
	   listen();  //转向，为了对称好看？还是为了兼容？
	 }

	 /**
	  * Listen for incoming messages.看看接受的连接发送了什么内容
	  */
	 private void listen()
	 {
	   //Run the Server inside a Thread
	   ExtendedThread server=new ExtendedThread()  //extends from Thread
	   {
	     public void run()
	     {
	       while( !isStopped()&&!isInterrupted() )
	            {
	              BufferedReader buffReader=null;

	              try{ //Read a single line from the Socket
	                   Socket socket=applLockSocket.accept();

	                   buffReader=new BufferedReader( new InputStreamReader( socket.getInputStream() ) );

	                   final String message=buffReader.readLine();

	                   socket.close();
					   if(Onruning!=null)
							Onruning.runing(message);
	                   //Check the received message
	                   /*if( ( message!=null&&message.length()>0 )&&GUI.display!=null&&!GUI.display.isDisposed() )  //激活某个GUI的代码在此，我们进去看看
	                     {
	                       
	                     }*/
	                 }
	              catch( IOException e )
	                   {
//	                     ...
	                   }
	              finally{ //关闭流
	                     }
	            }
	     }
	   };

	   server.setDaemon( true );

	   server.setName( "Startup Manager Thread" );

	   server.start();
	 }
	 
	 public void close()
	 {
		 try {
			if(applLockSocket!=null)
			{
			  applLockSocket.close();
			  applLockSocket=null;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
	
}
