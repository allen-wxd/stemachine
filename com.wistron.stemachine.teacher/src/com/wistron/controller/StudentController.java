package com.wistron.controller;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.windows.library.util.UncaughtExceptionHandler;
import com.windows.library.util.Util;
import com.wistron.dao.StudentDao;
import com.wistron.globaldata.Student;

public class StudentController {
	StudentDao studentdao;
	public StudentDao getStudentdao() {
		return studentdao;
	}
	public void setStudentdao(StudentDao studentdao) {
		this.studentdao = studentdao;
	}
	@RequestMapping("/student/queryStudent.taction")
	public void queryStudenr(Student st,HttpServletResponse resp,
			HttpServletRequest req)
	{   
		resp.setContentType("application/json; charset=utf-8");
		List<Student> list=new ArrayList<Student>();
		
	    try {
	    	list=studentdao.selectStudentByGrade(st);
			String ls=JSONObject.toJSON(list).toString();
			PrintWriter out =resp.getWriter();
			out.print(ls);
			out.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Util.writeLog(e, "");
		}
	}
	
	@RequestMapping("/student/searchStudent.taction")
	public void searchStudent(Student st,HttpServletResponse resp,
			HttpServletRequest req)
	{       
		try {
			resp.setContentType("application/json; charset=utf-8");
			List<Student> list=new ArrayList<Student>();
			list=studentdao.selectStudentByName(st);
			String ls=JSONObject.toJSON(list).toString();
		    System.out.println(ls);
	    
			PrintWriter out =resp.getWriter();
			out.print(ls);
			out.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Util.writeLog(e, "");
		}
	}
	@RequestMapping("/student/orderStudent.taction")
	public void orderStudent(String grade,String cno,String name,String span,HttpServletResponse resp,
			HttpServletRequest req)
	{
		try {
			resp.setContentType("application/json; charset=utf-8");
			List<Student> list=new ArrayList<Student>();
			HashMap<String,String> params=new HashMap<String,String>();
			 params.put("grade", grade);
			 params.put("cno", cno);
			 params.put("name", name);
			if(span.equals("namespan")){
				list=studentdao.orderStudentByName(params);
			}else{
				list=studentdao.orderStudentById(params);
			}
			
			String ls=JSONObject.toJSON(list).toString();
		    System.out.println(ls);
	    
			PrintWriter out =resp.getWriter();
			out.print(ls);
			out.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Util.writeLog(e, "");
		}
	}
	
}
