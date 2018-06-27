package com.wistron.web.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wistron.globaldata.Student;
import com.wistron.globaldata.Users;
import com.wistron.globaldata.UsersMapper;

public class LoginController {
	
	SqlSession sqlSession;
	
	public SqlSession getSqlSession() {
		return sqlSession;
	}

	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	
	@RequestMapping("/user/public/login")
	public ModelAndView login(HttpServletRequest req, HttpServletResponse resp){
		String loginname = req.getParameter("username");
		String password = req.getParameter("password");
		String msg = "";
		UsersMapper mapper = sqlSession.getMapper(UsersMapper.class);
		int count = mapper.exitUserName(loginname);
		Student student = new Student();
		student.setCno("2");
		student.setGrade("3");
		student.setSname("周杰伦");
		student.setSno("139074023");
		ModelAndView mv = new ModelAndView();
		String view = "login";
		if(count>0){
			String pwd = mapper.getPasswordByName(loginname);
			if(pwd.equals(password)){
				msg = "登录成功" ;
				HttpSession session = req.getSession();
				session.setAttribute("student", student);
				view = "redirect:/teaching/index.do";
			}else{
				msg = "密码不正确！";
				mv.addObject("msg", msg);
			}
		}else{
			msg = "用户名不存在！";
			mv.addObject("msg", msg);
		}
		mv.setViewName(view);
		return mv;
	}

	@RequestMapping("/user/public/register")
	public ModelAndView as(HttpServletRequest req, HttpServletResponse resp){
		String loginname = req.getParameter("username");
		String password = req.getParameter("password");
		String email = req.getParameter("email");
		String tel = req.getParameter("tel");
		String realname = req.getParameter("realname");
		Users user = new Users();
		user.setLoginName(loginname);
		user.setPassWord(password);
		user.setEmail(email);
		user.setPhone(tel);
		user.setRealname(realname);
		UsersMapper mapper = sqlSession.getMapper(UsersMapper.class);
		int res = mapper.saveUser(user);
		ModelAndView mv = new ModelAndView();
		mv.setViewName("login");
		return mv;
	}
	
	@RequestMapping("/user/public/test")
	@ResponseBody
	public Map<String,String> test(){
		System.out.println("hello");
		Map<String,String> resMap = new HashMap<String,String>();
		resMap.put("res", "success");
		resMap.put("msg", "hello world");
		return resMap;
	}
}
