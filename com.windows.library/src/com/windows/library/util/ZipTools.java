package com.windows.library.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarInputStream;
import org.apache.tools.tar.TarOutputStream;
//import org.apache.commons.lang.StringUtils;
import org.apache.tools.zip.*;

public class ZipTools {
	    private static int bufSize;    //size of bytes 
	    private static byte[] buf; 
	    private static int readedBytes;
	    
	    
	public static void main(String[] args) {   

	    

	    // 这里是调用压缩的代码   

	     //zip("k://test", "k://test.rar"); 

	    // 这里是调用解压缩的代码   

	     //unZip("k://test.rar", "k://test1");
	}     


	public  static void zip(String inputFile, String zipFileName) {
		bufSize = 512; 
        buf = new byte[bufSize]; 
		zip(new File(inputFile), zipFileName);
//        Compress(inputFile,zipFileName);

	}

	private static void zip(File inputFile, String zipFileName) {

		try {
			FileOutputStream out = new FileOutputStream(new String(zipFileName.getBytes("utf-8"))); // 创建文件输出对象out,提示:注意中文支持
			ZipOutputStream zOut = new ZipOutputStream(out);

			System.out.println("压缩-->开始");

			zip(zOut, inputFile, "");

			System.out.println("压缩-->结束");

			zOut.close();

		} catch (Exception e) {

			Util.writeLog(e, "ZipTools.zip");

		}

	}

	private static void zip(ZipOutputStream zOut, File file, String base) {

		try {

			System.out.println("正在压缩-->" + file.getName());

			if (file.isDirectory()) {

				File[] listFiles = file.listFiles();

				zOut.putNextEntry(new ZipEntry(base + "/"));

				base = (base.length() == 0 ? "" : base + "/");

				for (int i = 0; i < listFiles.length; i++) {

					byte [] bytes = listFiles[i].getName().getBytes( ); 
					zip(zOut, listFiles[i], base + new String(bytes,"UTF-8"));

					// System.out.println(new
					// String(listFiles[i].getName().getBytes("gb2312")));

				}

			} else {

				if (base.isEmpty()) {

					base = new String(file.getName().getBytes( ),"UTF-8");

				}

				zOut.putNextEntry(new ZipEntry(base));

				System.out.println(file.getPath() + "," + base);

				FileInputStream in = new FileInputStream(file);

				while((readedBytes = in.read(buf))>0){ 

                    zOut.write(buf , 0 , readedBytes); 

                } 


				in.close();

			}

		} catch (Exception e) {

			Util.writeLog(e, "ZipTools.zip1");

		}

	}

	private static void createDirectory(String directory, String subDirectory) {

		String dir[];

		File fl = new File(directory);

		try {

			if (subDirectory.isEmpty() && fl.exists() != true)

				fl.mkdir();

			else if (!(subDirectory.isEmpty())) {

				dir = subDirectory.replace("//","/").split("/");

				for (int i = 0; i < dir.length; i++) {

					File subFile = new File(directory + File.separator + dir[i]);

					if (subFile.exists() == false)

						subFile.mkdir();

					directory += File.separator + dir[i];

				}

			}

		} catch (Exception ex) {

			System.out.println(ex.getMessage());

		}

	}

	public static void unZip(String zipFileName, String outputDirectory) {

		try {

			ZipFile zipFile = new ZipFile(new String(zipFileName.getBytes("utf-8")));

			java.util.Enumeration e = zipFile.getEntries();

			ZipEntry zipEntry = null;

			createDirectory(outputDirectory, "");

			while (e.hasMoreElements()) {

				zipEntry = (ZipEntry) e.nextElement();

				System.out.println("正在解压: " + zipEntry.getName());

				String name = null;

				if (zipEntry.isDirectory()) {

					name = zipEntry.getName();

					name = name.substring(0, name.length() - 1);

					File f = new File(outputDirectory + File.separator + name);

					f.mkdir();

					System.out.println("创建目录：" + outputDirectory
							+ File.separator + name);

				} else {

					String fileName = zipEntry.getName();

					fileName = fileName.replace("//", "/");

					// System.out.println("测试文件1：" +fileName);

					if (fileName.indexOf("/") != -1) {

						createDirectory(outputDirectory, fileName.substring(0,
								fileName.lastIndexOf

								("/")));

						fileName = fileName.substring(

						fileName.lastIndexOf("/") + 1, fileName.length());

					}

					File f = new File(outputDirectory + File.separator
							+ zipEntry.getName());

					f.createNewFile();

					InputStream in = zipFile.getInputStream(zipEntry);

					FileOutputStream out = new FileOutputStream(f);

					byte[] by = new byte[1024*2];

					int c;

					while ((c = in.read(by)) != -1) {

						out.write(by, 0, c);

					}
					out.close();

					in.close();

				}
			}
			zipFile.close();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());

		}

	}
	//***********************************Tar**********************************************************
	public  static void CreateTar(String inputFile, String zipFileName) {
        File srcFile = new File(inputFile);//要归档的文件对象
		
		File targetTarFile = new File(zipFileName);//归档后的文件名
		
		TarOutputStream out = null;
		
		boolean boo = false;//是否压缩成功
		
		try{
			out = new TarOutputStream(new BufferedOutputStream(new FileOutputStream(targetTarFile)),"UTF-8");
			
			tar(srcFile, out, "", true);
			
			boo = true;
			
			//归档成功			
		}catch(IOException ex){
			throw new RuntimeException(ex);
		}finally{
			
			try{
				if(out!=null)
					out.close();
			}catch(IOException ex){
				throw new RuntimeException("关闭Tar输出流出现异常",ex);
			}finally{
				//清理操作
				if(!boo && targetTarFile.exists())//归档不成功,
					targetTarFile.delete();
				
			}
			
		}

	}
	
	/**
	 * 归档tar文件
	 * @param file 归档的文件对象
	 * @param out 输出tar流
	 * @param dir 相对父目录名称
	 * @param boo 是否把空目录归档进去
	 */
	public static void tar(File file,TarOutputStream out,String dir,boolean boo) throws IOException{
		dir="";
		if(file.isDirectory()){//是目录
			
			File []listFile = file.listFiles();//得出目录下所有的文件对象
			
			if(listFile.length == 0 && boo){//空目录归档
				
				out.putNextEntry(new TarEntry(new String(file.getName().getBytes("UTF-8") + "/")));//将实体放入输出Tar流中
				
				System.out.println("归档." + file.getName() + "/");
				
				return;
			}else{
				
				for(File cfile: listFile){
					
					tar(cfile,out,file.getName() + "/",boo);//递归归档
				}
			}
			
		}else if(file.isFile()){//是文件
			
			System.out.println("归档." + file.getName() + "/");
			
			byte[] bt = new byte[2048*2];
			
            TarEntry ze = new TarEntry(new String(file.getName().getBytes("UTF-8")));//构建tar实体
            //设置压缩前的文件大小
            ze.setSize(file.length());
            
            //ze.setName(file.getName());//设置实体名称.使用默认名称
            
            out.putNextEntry(ze);////将实体放入输出Tar流中
            
            FileInputStream fis = null;
            
            try{
            	
            	fis = new FileInputStream(file);
            	
            	int i=0;
                
                while((i = fis.read(bt)) != -1) {//循环读出并写入输出Tar流中
  
                    out.write(bt, 0, i);
                }

            }catch(IOException ex){
            	throw new IOException("写入归档文件出现异常",ex);
            }finally{
            	
            	try{
            		if (fis != null)
            			fis.close();//关闭输入流
            		out.closeEntry();
            	}catch(IOException ex){
            		
            		throw new IOException("关闭输入流出现异常");
            	}

            }           
		}
		
	}
	
	public static void unTar(File file,String outputDir) throws IOException {  
        
        
        TarInputStream tarIn = null;  
          
        try{  
              
            tarIn = new TarInputStream(new FileInputStream(file),1024 * 4);  
              
            createDirectory(outputDir,null);//创建输出目录   
              
            TarEntry entry = null;  
              
            while( (entry = tarIn.getNextEntry()) != null ){  
                  
                if(entry.isDirectory()){//是目录   
                      
                    createDirectory(outputDir,entry.getName());//创建空目录   
                      
                }else{//是文件   
                      
                    File tmpFile = new File(outputDir + "/" + entry.getName());  
                      
                    createDirectory(tmpFile.getParent() + "/",null);//创建输出目录   
                      
                    OutputStream out = null;  
                    if(!tmpFile.exists())
                    {
	                    try{  
	                      
	                        out = new FileOutputStream(tmpFile);  
	                          
	                        int length = 0;  
	                          
	                        byte[] b = new byte[1024 * 4];  
	                          
	                        while((length = tarIn.read(b)) != -1){  
	                            out.write(b, 0, length);  
	                        }  
	                      
	                    }catch(IOException ex){  
	                        throw ex;  
	                    }finally{  
	                          
	                        if(out!=null)  
	                            out.close();  
	                    }  
                    }
                }  
            }  
              
        }catch(IOException ex){  
            throw new IOException("解压归档文件出现异常",ex);  
        } finally{  
            try{  
                if(tarIn != null){  
                    tarIn.close();  
                }  
            }catch(IOException ex){  
                throw new IOException("关闭tarFile出现异常",ex);  
            }  
        }  
          
    }  
}