package com.library.core.network;
import java.io.File;import java.io.IOException;
import java.io.InputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
public class HttpUpload 
{	
  private static final String UPLOADURL = "http://192.168.0.181:8099/upload";	
  private static final String KEY_USERNAME = "username";	
  private static final String KEY_MD5ORSHA1 = "md5orsha1";	
  private static final String KEY_FILE = "myfile";	
  private String username;	
  private String md5orsha1;	
  private String filePath;	
  public String getUsername() 
  {		
    return username;	
  }	
  public void setUsername(String username) 
  {		
    this.username = username;	
  }	
  public String getMd5orsha1() 
  {		
    return md5orsha1;	
  }	
  public void setMd5orsha1(String md5orsha1) 
  {		
    this.md5orsha1 = md5orsha1;	
  }	
  public String getFilePath() 
  {		
    return filePath;	
  }	
  public void setFilePath(String filePath) 
  {		
    this.filePath = filePath;	
  }	
  public HttpUpload(String username, String md5orsha1, String filePath) 
  {		
    super();		
    this.username = username;		
    this.md5orsha1 = md5orsha1;		
    this.filePath = filePath;	
  }	
  public String postFile() throws ClientProtocolException, IOException 
  {		
    FileBody fileBody = null;		
    HttpClient httpclient = new DefaultHttpClient();		
    HttpPost httppost = new HttpPost(UPLOADURL);		
    File file = new File(filePath);		
    if (file != null) 
    {			
      fileBody = new FileBody(file);		
    }		
    StringBody usernameStringBody = new StringBody(username);		
    StringBody md5orsha1StringBody = new StringBody(md5orsha1);		
    MultipartEntity reqEntity = new MultipartEntity();		
    reqEntity.addPart(KEY_USERNAME, usernameStringBody);		
    reqEntity.addPart(KEY_MD5ORSHA1, md5orsha1StringBody);		
    reqEntity.addPart(KEY_FILE, fileBody);		
    httppost.setEntity(reqEntity);		
    System.out.println("执行: " + httppost.getRequestLine());	
    HttpResponse response = httpclient.execute(httppost);		
    System.out.println("statusCode is "				+ response.getStatusLine().getStatusCode());		
    HttpEntity resEntity = response.getEntity();		
    System.out.println("----------------------------------------");		
    System.out.println(response.getStatusLine());		
    if (resEntity != null) 
    {
    	System.out.println("返回长度: " + resEntity.getContentLength());			
		System.out.println("返回类型: " + resEntity.getContentType());			
		InputStream in = resEntity.getContent();			
		String returnValue = "http://willvvv.iteye.com/blog/inputStream2String(in)";			
		System.out.println("returnValue:" + returnValue);			
		return returnValue;		
    }		
    if (resEntity != null) 
    {
    			resEntity.consumeContent();
 		}		
 		return null;	
  }
  	
  public static String inputStream2String(InputStream in) throws IOException 
  {		
     StringBuffer out = new StringBuffer();		
     byte[] b = new byte[4096];		
     for (int n; (n = in.read(b)) != -1;) 
     {
     			out.append(new String(b, 0, n));		
     }		
     return out.toString();	
  }
  	
  public static void main(String[] args) throws ClientProtocolException,IOException 
  {
  		String username = "10000";		
  		String md5orsha1 = "11111111111111111111111111111112";		
  		String filePath = "e:\\b.jpg";		
  		HttpUpload hu = new HttpUpload(username, md5orsha1, filePath);		
  		System.out.println(hu.postFile());	
  }
}