package com.wistron.web.controllers;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.ibatis.session.SqlSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.windows.library.util.Util;
import com.wistron.globaldata.PageVo;
import com.wistron.globaldata.Project;
import com.wistron.globaldata.ProjectMapper;
import com.wistron.globaldata.Student;
import com.wistron.globaldata.StudentMapper;
import com.wistron.util.CheckUtil;
import com.wistron.util.DateUtil;
import com.wistron.util.IniReader;
import com.wistron.util.PakageUtil;
import com.wistron.util.PingUtil;
/****
 * 用于学生项目的创建、修改、上传代码等功能
 * @author s1710001
 *
 */
public class StemachineController {
	
	SqlSession sqlSession;
	private String pid = "";
	
	public SqlSession getSqlSession() {
		return sqlSession;
	}

	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	/***
	 * 跳转到学生项目的操作页
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping("/stemachine/index")
	public ModelAndView index(HttpServletRequest req, HttpServletResponse resp){
		try {
			String sno = (String)req.getSession().getAttribute("student");
			StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
			Student student = mapper.getStudentBySno(sno);
			ModelAndView mv = new ModelAndView();
			mv.setViewName("index");
			mv.addObject("student", student);
			return mv;
		}
		catch(Exception e)
		{
			Util.writeLog(e, "");
			return null;
		}
	}
	/**
	 * 教师查看学生详情页
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping("/stemachine/studentdetail")
	public ModelAndView studentDetail(HttpServletRequest req, HttpServletResponse resp){
		try {
			ModelAndView mv = new ModelAndView();
			String sno = req.getParameter("sno");
			String tno = (String)req.getSession().getAttribute("teacher");
			StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
			String name = mapper.getNameByTno(tno);
			mv.addObject("teacher_name", name);
			Student student = mapper.getStudentBySno(sno);
			mv.addObject("sno", sno);
			mv.addObject("student", student);
			mv.setViewName("studentdetail");
			return mv;
		}
		catch(Exception e)
		{
			Util.writeLog(e, "");
			return null;
		}
	}
	/**
	 * 修改或新增项目
	 * @param req
	 * @param resp
	 */
	@RequestMapping("/stemachine/addOrUpdate")
	public void addProject(HttpServletRequest req, HttpServletResponse resp){
		PrintWriter writer = null;
		try {
			HttpSession session = req.getSession();
			String sno = (String)session.getAttribute("student");
			StudentMapper studentmapper = sqlSession.getMapper(StudentMapper.class);
			Student student = studentmapper.getStudentBySno(sno); 
			String pname = req.getParameter("pname");
			String pid = req.getParameter("pid");
			Map<String,Integer> resMap = new HashMap<String,Integer>();
			Project project = new Project();
			ProjectMapper mapper = sqlSession.getMapper(ProjectMapper.class);
			int res = 0;
			if("".equals(pid)){
				project.setPname(pname);
				project.setSno(student.getSno());
				project.setStatus(1);
				res = mapper.addProject(project);
			}else{
				project.setPid(Integer.parseInt(pid));
				project.setPname(pname);
				res = mapper.updateProjectName(project);
			}
			resMap.put("status", res);
			String json = JSON.toJSONString(resMap,true);
			resp.setContentType("application/json;charset=UTF-8");
			 writer = resp.getWriter();
			 writer.write(json);
		} catch (Exception e) {
			Util.writeLog(e, "");
		}finally{
			writer.close();
		}
	}
	
	/**
	 * 查询项目列表
	 * @param req
	 * @param resp
	 */
	@RequestMapping("/stemachine/projectList")
	public void projectList(HttpServletRequest req, HttpServletResponse resp){
		PrintWriter writer = null;
		try {
			HttpSession session = req.getSession();
			String sno  = "";
			if(session.getAttribute("student")==null){
				sno = req.getParameter("sno");
			}else{
				sno = (String)session.getAttribute("student");
			}
			String limit =req.getParameter("limit");
			String offset =req.getParameter("offset");
			String pname = req.getParameter("pname");
			PageVo pageVo = new PageVo();
			pageVo.setToPageNo(Integer.parseInt(offset));
			offset = Integer.toString(pageVo.getTopageNo());
			Map<String,Object> resMap = new HashMap<String,Object>();
			ProjectMapper mapper = sqlSession.getMapper(ProjectMapper.class);
			HashMap<String,String> searchMap = new HashMap<String,String>();
			searchMap.put("limit", limit);
			searchMap.put("offset", offset);
			searchMap.put("pname", pname);
			searchMap.put("sno", sno);
			List<Project> list = mapper.projectList(searchMap);
			resMap.put("rows", list);
			int count = mapper.getTotalCount(searchMap);
			pageVo.setCount(count);
			pageVo.setTotalCount(count);
			resMap.put("page", pageVo);
			String json = JSON.toJSONString(resMap,true);
			resp.setContentType("application/json;charset=UTF-8");
			
		
			 writer = resp.getWriter();
			 writer.write(json);
		} catch (Exception e) {
			Util.writeLog(e, "");
		}finally{
			writer.close();
		}
	}
	/**
	 * 删除项目
	 * @param req
	 * @param resp
	 */
	@RequestMapping("/stemachine/del")
	public void delProject(HttpServletRequest req, HttpServletResponse resp){
		PrintWriter writer = null;
		try {
			String path = System.getProperty("user.dir");
			String pid = req.getParameter("pid");
			Map<String,Object> resMap = new HashMap<String,Object>();
			ProjectMapper mapper = sqlSession.getMapper(ProjectMapper.class);
			String videoPath = mapper.getVideoPathById(pid);
			videoPath = path +"\\com.wistron.stemachine.student\\WebContent\\"+videoPath;
			File video = new File(videoPath);
  		    if(video.exists()) {
  		    	video.delete();
  		    }
			int res = mapper.delProjectById(pid);
			resMap.put("status", res);
			String json = JSON.toJSONString(resMap,true);
			resp.setContentType("application/json;charset=UTF-8");
		    writer = resp.getWriter();
			writer.write(json);
		} catch (Exception e) {
			Util.writeLog(e, "");
		}finally{
			writer.close();
		}
	}
	/**
	 * 获取项目代码
	 * @param req
	 * @param resp
	 */
	@RequestMapping("/stemachine/getCommand")
	public void getCommand(HttpServletRequest req, HttpServletResponse resp){
		PrintWriter writer = null;
		try {
			String pid = req.getParameter("pid");
			Map<String,Object> resMap = new HashMap<String,Object>();
			ProjectMapper mapper = sqlSession.getMapper(ProjectMapper.class);
			String code = mapper.getCommandById(pid);
			resMap.put("command", code);
			String json = JSON.toJSONString(resMap,true);
			resp.setContentType("application/json;charset=UTF-8");
			
		
			 writer = resp.getWriter();
			 writer.write(json);
		} catch (Exception e) {
			Util.writeLog(e, "");
		}finally{
			writer.close();
		}
	}
	/**
	 * 更新项目代码
	 * @param req
	 * @param resp
	 */
	@RequestMapping("/stemachine/updateCommand")
	public void updateCommand(HttpServletRequest req, HttpServletResponse resp){
		PrintWriter writer = null;
		try {
			
			String code = req.getParameter("code");
			pid = req.getParameter("pid");
			String temp = req.getParameter("temp");
			Map<String,String> paraMap = new HashMap<String,String>();
			paraMap.put("code", code);
			paraMap.put("pid", pid);
			paraMap.put("time", DateUtil.getNowTime());
			Map<String,String> resMap = new HashMap<String,String>();
			ProjectMapper mapper = sqlSession.getMapper(ProjectMapper.class);
			int res = mapper.updateCommandById(paraMap);
			String exeRes = "";
		 	if(res>0 && "1".equals(temp)) {
		 		String path = System.getProperty("user.dir");
		 		String videoPath = mapper.getVideoPathById(pid);
		 		videoPath = path +"\\com.wistron.stemachine.student\\WebContent\\"+videoPath;
				File video = new File(videoPath);
	  		    if(video.exists()) {
	  		    	video.delete();
	  		    }
		 		exeRes = execute(code);
		 		if("1".equals(exeRes)) {
		 			resMap.put("status", "0");//网络不通畅
		 		}else if("ok".equals(exeRes)){
		 			resMap.put("status","1");//执行成功
		 		}else {
		 			resMap.put("status","2");//执行失败
		 		}
		 	}else {
		 		if(res>0) {
		 			resMap.put("status","1");//执行成功
		 		}else {
		 			resMap.put("status","2");//执行失败
		 		}
		 	}
			String json = JSON.toJSONString(resMap,true);
			resp.setContentType("application/json;charset=UTF-8");
			writer = resp.getWriter();
			writer.write(json);
		} catch (Exception e) {
			Util.writeLog(e, "");
		}finally{
			writer.close();
		}
	}
	@RequestMapping("/stemachine/getVideoPath")
	public void getVideoPath(HttpServletRequest req, HttpServletResponse resp) {
		PrintWriter writer = null;
		try {
			String pid = req.getParameter("pid");
			ProjectMapper mapper = sqlSession.getMapper(ProjectMapper.class);
			String videoPath = mapper.getVideoPathById(pid);
			Map<String,Object> resMap = new HashMap<String,Object>();
			resMap.put("url", videoPath);
			String json = JSON.toJSONString(resMap,true);
			resp.setContentType("application/json;charset=UTF-8");
			writer = resp.getWriter();
			writer.write(json);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			writer.close();
		}
		
	}
	/***
	 * 将python代码发送到树莓派执行
	 * @param code
	 * @return
	 */
	public String execute(String code) {
		String boundary = "*****";
		DataOutputStream ds = null;
		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader reader = null;
		StringBuffer resultBuffer = new StringBuffer();
		String tempLine = null;
		try {
			String path = System.getProperty("user.dir")+"\\com.wistron.stemachine.student\\WebContent\\config\\machineConfig.ini";
			IniReader iniReader = new IniReader(path);
			String ip = iniReader.getProperty("ip");
			String port = iniReader.getProperty("port");
			boolean status = PingUtil.ping(ip);
			if(status) {
				URL url = new URL("http://"+ip+":"+port+"/machine/python.exec");
				URLConnection urlConnection = url.openConnection();
				HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
				httpURLConnection.setDoInput(true);
				httpURLConnection.setDoOutput(true);
				httpURLConnection.setUseCaches(false);
				httpURLConnection.setRequestMethod("POST");
				httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
				httpURLConnection.setRequestProperty("Charset", "UTF-8");
				httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data;charset=UTF-8; boundary=" + boundary);
				ds = new DataOutputStream(httpURLConnection.getOutputStream());
				ds.write(code.getBytes());
				ds.flush();
				if (httpURLConnection.getResponseCode() >= 300) {
					throw new Exception(
							"HTTP Request is not success, Response code is " + httpURLConnection.getResponseCode());
				}

				if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
					inputStream = httpURLConnection.getInputStream();
					inputStreamReader = new InputStreamReader(inputStream);
					reader = new BufferedReader(inputStreamReader);
					tempLine = null;
					resultBuffer = new StringBuffer();
					while ((tempLine = reader.readLine()) != null) {
						resultBuffer.append(tempLine);
					}
				}
			}else {
				return "1";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (ds != null) {
				try {
					ds.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (inputStreamReader != null) {
				try {
					inputStreamReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return resultBuffer.toString();
	}
	
	@RequestMapping("/stemachine/getVideo")
	public void getVideo(HttpServletRequest req, HttpServletResponse resp) {
		try {
			String path = System.getProperty("user.dir");
			path = path +"\\com.wistron.stemachine.student\\WebContent\\video\\";
        	path = path+"temp.mp4";
        	File sourceFile = new File(path);
 	        if(sourceFile.exists()) {
 	        	sourceFile.delete();
 	        }
    	    ServletInputStream sis;
			sis = req.getInputStream();
	        FileOutputStream fos = new FileOutputStream(path);
	
	        byte[] media = new byte[1024];
	
	        int length = sis.read(media, 0, 1024);
	
	        while(length>0)
	
	        {
	            fos.write(media, 0, length);
	
	            length = sis.read(media, 0, 1024);       
	        }
	        fos.close();
	        sis.close();
	        String url = format(path);
	        Map<String,String> paraMap = new HashMap<String,String>();
			paraMap.put("path", url);
			paraMap.put("pid", pid);
			paraMap.put("status", "3");
	        ProjectMapper mapper = sqlSession.getMapper(ProjectMapper.class);
	        mapper.updateProjectVideo(paraMap);
    	} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String format(String videoPath)
	{
		String path = System.getProperty("user.dir");
		String toolpath = path +"\\com.wistron.stemachine.student\\WebContent\\tool\\ffmpeg.exe";
		String targetFile = path +"\\com.wistron.stemachine.student\\WebContent\\video\\";
		Date d = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
    	String filename = df.format(d);
    	targetFile = targetFile+filename+".mp4";
    	String url = "";
		if(new File(videoPath).exists())
		{
			File recordfile=new File(targetFile);
  		    if(recordfile.exists())recordfile.delete();
	    	String[] cmdA = new String[]{toolpath,"-i", videoPath, "-y", targetFile};
	    	try{
	    		System.out.println("-----视屏文件转化开始-----");
	    		long startTime = System.currentTimeMillis();
	            Runtime rt = Runtime.getRuntime();
	            Process proc = rt.exec(cmdA); 
	            final InputStream is1 = proc.getInputStream();   
	            //获取进城的错误流  
	            final InputStream is2 = proc.getErrorStream();  
	            //启动两个线程，一个线程负责读标准输出流，另一个负责读标准错误流  
	            new Thread() {  
	               public void run() {  
	                  BufferedReader br1 = new BufferedReader(new InputStreamReader(is1));  
	                   try {  
	                       String line1 = null;  
	                       while ((line1 = br1.readLine()) != null) {  
	                             if (line1 != null){}  
	                         }  
	                   } catch (IOException e) {  
	                        e.printStackTrace();  
	                   }  
	                   finally{  
	                        try {  
	                          is1.close();  
	                        } catch (IOException e) {  
	                           e.printStackTrace();  
	                       }  
	                     }  
	                   }  
	                }.start();  
	                                           
	              new Thread() {   
	                 public void  run() {   
	                  BufferedReader br2 = new  BufferedReader(new  InputStreamReader(is2));   
	                     try {   
	                        String line2 = null ;   
	                        while ((line2 = br2.readLine()) !=  null ) {   
	                             if (line2 != null){}  
	                        }   
	                      } catch (IOException e) {   
	                            e.printStackTrace();  
	                      }   
	                     finally{  
	                        try {  
	                            is2.close();  
	                        } catch (IOException e) {  
	                            e.printStackTrace();  
	                        }  
	                      }  
	                   }   
	                 }.start();    
                proc.waitFor();
	            proc.destroy();
	            long endTime = System.currentTimeMillis();
	            System.out.println("-----视屏文件转化结束-----");
	            System.out.println("用时"+(endTime-startTime)+"ms");
	            url = "/video/"+filename+".mp4";
	        }catch (Exception e){
	        	e.printStackTrace();
	        }finally {
	        	
	        }
		}
		return url;
	}
	/**
	 * 通过txt文件上传代码
	 * @param req
	 * @param resp
	 * @throws IOException
	 */
	@RequestMapping("/stemachine/uploadCommand")
	public void uploadCode(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		PrintWriter writer = null;
        InputStream in = null;
        InputStreamReader reader = null;
		try {
			resp.setCharacterEncoding("utf-8");
			Map<String,String> resMap = new HashMap<String,String>();
	        ModelAndView mv = new ModelAndView();
	        //1、创建一个DiskFileItemFactory工厂  
	        DiskFileItemFactory factory = new DiskFileItemFactory();  
	        //2、创建一个文件上传解析器  
	        ServletFileUpload upload = new ServletFileUpload(factory);  
	        //解决上传文件名的中文乱码  
	        upload.setHeaderEncoding("UTF-8");   
	        upload.setSizeMax(1024 * 1024 * 5);//设置上传的文件总的大小不能超过5M
	        StringBuilder sb  =null;
	        resp.setContentType("application/json;charset=UTF-8");
    	
			List<FileItem> items = upload.parseRequest(req);
			for (FileItem item : items) {  
                in = item.getInputStream(); 
                reader = new InputStreamReader(in);
                char[] bytes = new char[12];
                int len = 0;
                sb = new StringBuilder();
                while((len=reader.read(bytes))!=-1){
                	String str = new String(bytes,0,len);
                	sb.append(str);
                }
			}
			resMap.put("command", sb.toString());
			String json = JSON.toJSONString(resMap,true);
			writer = resp.getWriter();
			writer.write(json);
		} catch (Exception e) {
			Util.writeLog(e, "");
		}finally{
			writer.close();
			in.close();
			reader.close();
		} 
	}
	/**
	 * 代码的语法检查
	 * @param command
	 * @param resp
	 */
	@RequestMapping("/stemachine/checkCommand")
	public void compliceMathord(String command ,HttpServletResponse resp) 
	{    
		 PrintWriter out = null;
		 String result = "";
		 try {
			 String path= PakageUtil.pakageCommand(command);
		     if(path!=null){
		       result=CheckUtil.checkCommand(path);
			  }else{
				result="请重新点击检测";
			  }
		      if("".equals(result)){
		    	  result = "success";
		      }
		      result = result.replace("w.py:", "#");
		      out = resp.getWriter();
		      out.print(result);
		 	} catch (Exception e) {
		 		Util.writeLog(e, "");
		 	}finally{
		 		out.close();
		 		PakageUtil.file.delete();
		 	}
	}
	@RequestMapping("/stemachine/modifyStatus")
	public void modifyStatus(HttpServletRequest req,HttpServletResponse resp) 
	{    
		PrintWriter writer = null;
		try {
			
			String pid = req.getParameter("pid");
			String status = req.getParameter("status");
			Map<String,String> paraMap = new HashMap<String,String>();
			if("2".equals(status)) {
				paraMap.put("time", DateUtil.getNowTime());
			}
			paraMap.put("status", status);
			paraMap.put("pid", pid);
			Map<String,Integer> resMap = new HashMap<String,Integer>();
			ProjectMapper mapper = sqlSession.getMapper(ProjectMapper.class);
			int res = mapper.modifyStatus(paraMap);
			resMap.put("status", res);
			String json = JSON.toJSONString(resMap,true);
			resp.setContentType("application/json;charset=UTF-8");
			writer = resp.getWriter();
			writer.write(json);
		} catch (Exception e) {
			Util.writeLog(e, "");
		}finally{
			writer.close();
		}
	}
	
}
