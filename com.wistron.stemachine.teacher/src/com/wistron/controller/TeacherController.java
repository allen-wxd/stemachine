package com.wistron.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.windows.library.util.Util;
import com.wistron.dao.StudentDao;
import com.wistron.dao.SubjectDao;
import com.wistron.dao.TeacherDao;
import com.wistron.globaldata.Student;

public class TeacherController {
	TeacherDao teacherdao;
	SubjectDao subjectdao;
	StudentDao studentdao;
	public TeacherDao getTeacherdao() {
		return teacherdao;
	}

	public void setTeacherdao(TeacherDao teacherdao) {
		this.teacherdao = teacherdao;
	}
	
	public SubjectDao getSubjectdao() {
		return subjectdao;
	}

	public void setSubjectdao(SubjectDao subjectdao) {
		this.subjectdao = subjectdao;
	}
	
    public StudentDao getStudentdao() {
		return studentdao;
	}

	public void setStudentdao(StudentDao studentdao) {
		this.studentdao = studentdao;
	}

	@RequestMapping("/teacher/queryTeacher.taction")
	public void queryTeacher(HttpServletResponse resp,
			HttpServletRequest req)
	{  
		try 
		{
			String number1 = (String)req.getSession().getAttribute("teacher");
			resp.setContentType("application/json; charset=utf-8");
			String number=null;
			List<Integer> list=new ArrayList<>();
			List<Student> st=new ArrayList<>();
			HashSet<String> hs=new HashSet<>(); 
			list=subjectdao.selectSubjectId(number1);
			for (Integer ls : list) {
				number=ls.toString();
				st=studentdao.selectGandCBySubId(number);
				for (Student st1 : st) {
					hs.add(JSONObject.toJSONString(st1));
				}
			}
	    
			PrintWriter out =resp.getWriter();
			out.print(hs);
			out.close();
		} catch (Exception e) {
			Util.writeLog(e, "");
		}
		
		
	}
    @RequestMapping("/teacher/toQueryTeacher")
    public ModelAndView index(HttpServletRequest req, HttpServletResponse resp){
    	try 
		{
	    	String name1 = (String)req.getSession().getAttribute("teacher");
	    	String name=teacherdao.getTeaNameByNumber(name1);
	    	ModelAndView mv = new ModelAndView();
			mv.setViewName("/teacherpage");
			mv.addObject("name", name);
			return mv;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Util.writeLog(e, "");
			return null;
		}
	}
}
