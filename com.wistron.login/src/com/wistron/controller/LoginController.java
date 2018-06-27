package com.wistron.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestMapping;

import com.windows.library.util.Util;
import com.wistron.dao.StudentDao;
import com.wistron.dao.SuperUserDao;
import com.wistron.dao.TeacherDao;
import com.wistron.globaldata.Student;

public class LoginController {
	TeacherDao teacherdao;
	StudentDao studentdao;
	SuperUserDao superuserdao;
   public TeacherDao getTeacherdao() {
		return teacherdao;
	}
	public void setTeacherdao(TeacherDao teacherdao) {
		this.teacherdao = teacherdao;
	}
	public StudentDao getStudentdao() {
		return studentdao;
	}
	public void setStudentdao(StudentDao studentdao) {
		this.studentdao = studentdao;
	}
	
	public SuperUserDao getSuperuserdao() {
		return superuserdao;
	}
	public void setSuperuserdao(SuperUserDao superuserdao) {
		this.superuserdao = superuserdao;
	}
	//普通用户登录
	@RequestMapping("/login.action")
		public void loginMethod(String password,String username,HttpServletResponse resp,HttpServletRequest req) 
				throws  IOException{
		String error=null; 
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter out =resp.getWriter();
		String pw2=studentdao.selectPassWordByName(username);
		String pw=teacherdao.selectPassWordByName(username);
		String sign;
		List<Student> ls1=new ArrayList<Student>();
		Enumeration em = req.getSession().getAttributeNames();
		while(em.hasMoreElements()){
			req.getSession().removeAttribute(em.nextElement().toString());
		}

				try {
					   //若用户名存在
			    	   if(pw!=null||pw2!=null){
			    		   //若密码正确
			    		   if(pw!=null&&pw.equals(password)){
			    			       
			    				   sign="ok1"; 
			    		           req.getSession().setAttribute("teacher", username); 
			    		          
			    		    	   
			    	       }else if(pw2!=null&&pw2.equals(password)){
			    	    	   sign="ok"; 
		    		           req.getSession().setAttribute("student", username); 
			    		   }//登陆成功
			    		   else{
			    			   
			    			   sign="error1";
			    			    //返回错误1，密码不正确！
			    		   }
			    		}else{
			    			  sign="error2";
			    			 
			    		   //返回错误2，用户不存在！！
			    	    }
			    	   out.print(sign);
			    	   out.close();
			}catch(Exception e)
			   {
				   Util.writeLog(e, "");
			   }
		 
		}
	//超级用户登录
	@RequestMapping("/superlogin.action")
	public void superLoginMethod(String password,String username,HttpServletResponse resp,HttpServletRequest req) 
			throws  IOException{
		try
		{
		    String error=null; 
		    String pw = superuserdao.getAdminPW(username);
		    resp.setContentType("text/html;charset=utf-8");
			PrintWriter out =resp.getWriter();
		    if(pw!=null){
			    if(pw.equals(password)) {
			    	error="ok3";
			    	req.getSession().setAttribute("superuser ", username); 
			    }else {
			      error="error1";	
			    }
		    }
		    out.print(error);
	    	out.close();
		}catch(Exception e)
		   {
			   Util.writeLog(e, "");
		   }
		
	}
	//普通用户密码修改
	   @RequestMapping("/update.action")
		public void updateMethod(String password,String username,String newpass,HttpServletResponse resp,HttpServletRequest req) throws IOException{
		//判断用户名和密码是否正确
		 //若不正确返回修改密码失败
		   //若正确则进行密码的更改，并提示更改成功请重新登陆
		   try {
			    String error=null; 
				resp.setContentType("text/html;charset=utf-8");
				PrintWriter out =resp.getWriter();
				String pw=teacherdao.selectPassWordByName(username);
				String pw2=studentdao.selectPassWordByName(username);
			    System.out.println(pw);
			    System.out.println(pw2);
				
		 	   if(pw!=null||pw2!=null){
		 		   //先判断老师，若密码正确且新密码不为空
		 		       if(pw!=null&&pw.equals(password)){
		 		    	   HashMap<String,String> hm=new HashMap<String, String>();
		 		    	   hm.put("newpass", newpass);
		 		    	   hm.put("username", username);
		 		    	   teacherdao.updatePWByName(hm);  
		 			    req.getSession().setAttribute("teacher", null);
		 			    //若 error=ok则密码修改成功
		 			    //若返回error=nook则密码修改失败
		 			    error="ok";
		 			   //登陆成功
		 		       }else if(pw2!=null&&pw2.equals(password)){
		 		    	  HashMap<String,String> hm=new HashMap<String, String>(); 
		 		    	   hm.put("newpass", newpass);
		 		    	   hm.put("username", username);
		 		    	   studentdao.updatePWByName(hm);  
		 			    req.getSession().setAttribute("student", null);
		 			    error="ok"; 
		 		       }else{
		 		    	  error="nook";  
		 		       }
		 		}else{
		 		 error="nook";
		 			
		 		}
	
		 	   System.out.println(error);
		 	   out.print(error);
		 	   out.close();
		   }catch(Exception e)
		   {
			   Util.writeLog(e, "");
		   }
	    }
	 //用户退出
	   @RequestMapping("/logout.action")
		public void logoutMethod(HttpServletRequest req,HttpServletResponse resp) throws IOException{
		   try
		   {
			   resp.setContentType("text/html;charset=utf-8");
			   PrintWriter out =resp.getWriter();
			   req.getSession().removeAttribute("student");
			   req.getSession().removeAttribute("teacher");
			   out.print("ok");
		   }catch(Exception e)
		   {
			   Util.writeLog(e, "");
		   }
		}
		
}
