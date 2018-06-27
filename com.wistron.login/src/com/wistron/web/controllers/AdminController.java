package com.wistron.web.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.ibatis.session.SqlSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.windows.library.util.Util;
import com.wistron.dao.TeacherDao;
import com.wistron.globaldata.Student;
import com.wistron.globaldata.StudentMapper;
import com.wistron.globaldata.Subject;
import com.wistron.globaldata.SubjectMapper;
import com.wistron.globaldata.Teacher;
import com.wistron.web.util.ExcelUtil;
/**
 * 用于管理学生及老师用户信息
 * @author s1710001
 *
 */
public class AdminController {

	SqlSession sqlSession;
	TeacherDao teacherdao;
	
	public TeacherDao getTeacherdao() {
		return teacherdao;
	}

	public void setTeacherdao(TeacherDao teacherdao) {
		this.teacherdao = teacherdao;
	}

	public SqlSession getSqlSession() {
		return sqlSession;
	}

	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	//查询学生信息列表
	@RequestMapping("/user/private/studentList")
	public void studentList(HttpServletRequest req, HttpServletResponse resp){
		PrintWriter writer = null;
		try {
			String limit =req.getParameter("limit");
			String offset =req.getParameter("offset");
			String sname = req.getParameter("sname");
			String grade = req.getParameter("grade");
			String cno = req.getParameter("cno");
			String sort = req.getParameter("sort");
			String order = req.getParameter("sortOrder");
			String subject = req.getParameter("subject");
			if(sort == null) {
				sort = "sno";
			}
			StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
			HashMap<String,String> searchMap = new HashMap<String,String>();
			searchMap.put("limit", limit);
			searchMap.put("offset", offset);
			searchMap.put("sname", sname);
			searchMap.put("grade", grade);
			searchMap.put("cno", cno);
			searchMap.put("subject", subject);
			searchMap.put("sort", sort);
			searchMap.put("order", order);
			List<Student> list = mapper.getStudentList(searchMap);
			Map<String, Object> resMap = new HashMap<String, Object>();
			int total = mapper.getStudentCount(searchMap);
			resMap.put("total", total);
			resMap.put("rows", list);
			String json = JSON.toJSONString(resMap,true);
			resp.setContentType("application/json;charset=UTF-8");
			
			 writer = resp.getWriter();
			 writer.write(json);
		} catch (Exception e) {
			Util.writeLog(e, "");
		}finally{
			if(writer!=null)
			  writer.close();
		}
	}
	//删除学生信息
	@RequestMapping("/user/private/studentDel")
	public void studentDel(HttpServletRequest req, HttpServletResponse resp){
		PrintWriter writer = null;
		try {
			String ids = req.getParameter("ids");
			String idArr[] = ids.split(",");
			Map<String, Object> resMap = new HashMap<String, Object>();
			StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
			int res = 0;
			for(int i=0;i<idArr.length;i++){
				res = mapper.delStudentById(Integer.parseInt(idArr[i]));
			}
			if(res>0){
				resMap.put("status", "success");
			}else{
				resMap.put("status", "fail");
			}
			String json = JSON.toJSONString(resMap,true);
			resp.setContentType("application/json;charset=UTF-8");
			
		
			 writer = resp.getWriter();
			 writer.write(json);
		} catch (Exception e) {
			Util.writeLog(e, "");
		}finally{
			if(writer!=null)
			  writer.close();
		}
	}
	//增加或修改学生信息
	@RequestMapping("/user/private/studentAddOrEdit")
	public void addOrEditStudent(HttpServletRequest req, HttpServletResponse resp){
		int res;
		PrintWriter writer = null;
		try {
			String sname = req.getParameter("sname");
			String sno = req.getParameter("sno");
			String id = req.getParameter("id");
			String cno = req.getParameter("cno");
			String grade = req.getParameter("grade");
			String password = req.getParameter("password");
			String seatnum = req.getParameter("seatnum");
			Student student = new Student();
			student.setSname(sname);
			student.setCno(cno);
			student.setGrade(grade);
			student.setSno(sno);
			student.setSeatNum(seatnum);
			student.setPassword(password);
			Map<String, Object> resMap = new HashMap<String, Object>();
			StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
			if(id==null||"".equals(id)){
				res = mapper.addStudent(student);
			}else{
				student.setId(Integer.parseInt(req.getParameter("id")));
				res = mapper.updateStudentById(student);
			}
			String restr = "";
			if(res>0){
				restr = "success";
			}else{
				restr = "fail";
			}
			resMap.put("status", restr);
			String json = JSON.toJSONString(resMap,true);
			resp.setContentType("application/json;charset=UTF-8");
		
			 writer = resp.getWriter();
			 writer.write(json);
		} catch (Exception e) {
			Util.writeLog(e, "");
		}finally{
			if(writer!=null)
			  writer.close();
		}
	}
	//批量增加学生用户将
	public String addStudentByList(List<List<String>> list){
		StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
		Student student = null;
		int success = 0;
		int fail = 0;
		String restr = "";
		Map<String, Object> resMap = new HashMap<String, Object>();
		if (list != null) {
			for (int i = 1; i < list.size(); i++) {
				student = new Student();
				List<String> cellList = list.get(i);
				int snoExist = mapper.snoExist(cellList.get(0));
				int tnoExist = teacherdao.tnoExist(cellList.get(0));
				if(snoExist > 0 || tnoExist>0) {
					continue;
				}else {
					student.setSno(cellList.get(0));
					student.setGrade(cellList.get(1));
					student.setCno(cellList.get(2));
					student.setSname(cellList.get(3));
					student.setSeatNum(cellList.get(4));
					student.setPassword("123456");
					try {
						int res = mapper.addStudent(student);
						if(res>0){
							success++;
						}
					}catch (Exception e) {
						fail++;
					}
				}
				
			}
			resMap.put("success", success);
			resMap.put("fail", fail);
			String json = JSON.toJSONString(resMap,true);
			restr = json;
		}
		return restr;
	}
	//批量增加教师用户信息
	public String addTeacherByList(List<List<String>> list){
		Teacher teacher = null;
		int success = 0;
		int fail = 0;
		String restr = "";
		Map<String, Object> resMap = new HashMap<String, Object>();
		StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
		if (list != null) {
			for (int i = 1; i < list.size(); i++) {
				teacher = new Teacher();
				List<String> cellList = list.get(i);
				int snoExist = mapper.snoExist(cellList.get(0));
				int tnoExist = teacherdao.tnoExist(cellList.get(0));
				if(snoExist > 0 || tnoExist>0) {
					continue;
				}else {
					teacher.setNumber(cellList.get(0));
					teacher.setSchool(cellList.get(1));
					teacher.setGrade(cellList.get(2));
					teacher.setTclass(cellList.get(3));
					teacher.setName(cellList.get(4));
					teacher.setSubject(cellList.get(5));
					teacher.setPassword("123456");
					try {
						int res = teacherdao.addTeacher(teacher);
						if(res>0){
							success++;
						}
						
					}catch (Exception e) {
						fail++;
					}
				}
			}
			resMap.put("success", success);
			resMap.put("fail", fail);
			String json = JSON.toJSONString(resMap,true);
			restr = json;
		}
		return restr;
	}
	//查询老师用户列表
	@RequestMapping("/user/private/teacherList")
	public void teacherList(HttpServletRequest req, HttpServletResponse resp){
		PrintWriter writer = null;
		try {
			String limit =req.getParameter("limit");
			String offset =req.getParameter("offset");
			String tname = req.getParameter("tname");
			String sort = req.getParameter("sort");
			String order = req.getParameter("sortOrder");
			if(sort == null) {
				sort = "number";
			}
			HashMap<String,String> searchMap = new HashMap<String,String>();
			searchMap.put("limit", limit);
			searchMap.put("offset", offset);
			searchMap.put("tname", tname);
			searchMap.put("sort", sort);
			searchMap.put("order", order);
			List<Teacher> list = teacherdao.getTeacherList(searchMap);
			Map<String, Object> resMap = new HashMap<String, Object>();
			int total = teacherdao.getTeacherCount(searchMap);
			resMap.put("total", total);
			resMap.put("rows", list);
			String json = JSON.toJSONString(resMap,true);
			resp.setContentType("application/json;charset=UTF-8");
			
		
			 writer = resp.getWriter();
			 writer.write(json);
		} catch (Exception e) {
			Util.writeLog(e, "");
		}finally{
			if(writer!=null)
			  writer.close();
		}
	}
	//增加或修改教师信息
	@RequestMapping("/user/private/teacherAddOrEdit")
	public void addOrEditTeacher(HttpServletRequest req, HttpServletResponse resp){
		int res;
		PrintWriter writer = null;
		try {
			String name = req.getParameter("name");
			String number = req.getParameter("number");
			String id = req.getParameter("id");
			String cno = req.getParameter("cno");
			String grade = req.getParameter("grade");
			String password = req.getParameter("password");
			Teacher teacher = new Teacher();
			teacher.setName(name);
			teacher.setNumber(number);
			teacher.setTclass(cno);
			teacher.setGrade(grade);
			teacher.setPassword(password);
			Map<String, Object> resMap = new HashMap<String, Object>();
			if(id==null||"".equals(id)){
				res = teacherdao.addTeacher(teacher);
			}else{
				teacher.setId(Integer.parseInt(req.getParameter("id")));
				res = teacherdao.updateTeacherById(teacher);
			}
			String restr = "";
			if(res>0){
				restr = "success";
			}else{
				restr = "fail";
			}
			resMap.put("status", restr);
			String json = JSON.toJSONString(resMap,true);
			resp.setContentType("application/json;charset=UTF-8");
		
			 writer = resp.getWriter();
			 writer.write(json);
		} catch (Exception e) {
			Util.writeLog(e, "");
		}finally{
			if(writer!=null)
			  writer.close();
		}
	}
	//删除教师信息
	@RequestMapping("/user/private/teacherDel")
	public void teacherDel(HttpServletRequest req, HttpServletResponse resp){
		PrintWriter writer = null;
		try {
			String ids = req.getParameter("ids");
			String idArr[] = ids.split(",");
			Map<String, Object> resMap = new HashMap<String, Object>();
			int res = 0;
			for(int i=0;i<idArr.length;i++){
				teacherdao.delSubjectByTeacher(Integer.parseInt(idArr[i]));
				res = teacherdao.delTeacherById(Integer.parseInt(idArr[i]));
			}
			if(res>0){
				resMap.put("status", "success");
			}else{
				resMap.put("status", "fail");
			}
			String json = JSON.toJSONString(resMap,true);
			resp.setContentType("application/json;charset=UTF-8");
		
			 writer = resp.getWriter();
			 writer.write(json);
		} catch (Exception e) {
			Util.writeLog(e, "");
		}finally{
			if(writer!=null)
			  writer.close();
		}
	}
	/***
	 * 查看教师授课详情
	 * @param req
	 * @param resp
	 */
	@RequestMapping("/user/private/detailSubject")
	public void detailSubject(HttpServletRequest req, HttpServletResponse resp) {
		PrintWriter writer = null;
		try {
			String tno = req.getParameter("tno");
			StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
			List<Subject> subjectId = teacherdao.getSubjectIdByid(tno);
			Map<String,String> subjectCount = null;
			List<Student> studentList = null;
			List resList = new ArrayList<>();
			for (Subject subject : subjectId) {
				studentList = new ArrayList<Student>();
				subjectCount = new HashMap<String,String>();
				studentList = mapper.getSubjectCount(Integer.toString(subject.getId()));
				int count = 0;
				String grade = "";
				String cno = "";
				subjectCount.put("subjectname", subject.getSubjectName());
				for (int i=0;i<studentList.size();i++) {
					count += studentList.get(i).getCount();
					if(i==studentList.size()-1){
						grade += studentList.get(i).getGrade();
						cno += studentList.get(i).getCno();
	            	}else{
	            		grade += studentList.get(i).getGrade()+",";
	    				cno += studentList.get(i).getCno()+",";
	            	}
					if(grade.length()>3) {
						grade = grade.substring(0, 3)+"...";
						cno = cno.substring(0, 3)+"...";
					}
				}
				subjectCount.put("count", Integer.toString(count));
				if("".equals(grade)) {
					grade = "---";
					cno = "---";
				}
				subjectCount.put("grade", grade);
				subjectCount.put("cno", cno);
				resList.add(subjectCount);
			}
			String json = JSON.toJSONString(resList,true);
			resp.setContentType("application/json;charset=UTF-8");
			writer = resp.getWriter();
			writer.write(json);
		} catch (Exception e) {
			Util.writeLog(e, "");
		}finally{
			if(writer!=null)
			  writer.close();
		}
	}
	
	//导入Excel表格
	public String ImportExcel(String path,String role){
		ExcelUtil excelUtil = new ExcelUtil();
		List<List<String>> list = excelUtil.read(path);
		if("0".equals(role)){
			return addStudentByList(list);
		}else{
			return addTeacherByList(list);
		}
		
	}
	//通过Excel导入用户信息
	@RequestMapping("/user/private/upload")
	public void upload(HttpServletRequest request, HttpServletResponse response)throws IOException{
		String role = "";
		request.setCharacterEncoding("utf-8");  
        response.setCharacterEncoding("utf-8"); 
        //1、创建一个DiskFileItemFactory工厂  
        DiskFileItemFactory factory = new DiskFileItemFactory();  
        //2、创建一个文件上传解析器  
        ServletFileUpload upload = new ServletFileUpload(factory);  
        //解决上传文件名的中文乱码  
        upload.setHeaderEncoding("UTF-8");   
        upload.setSizeMax(1024 * 1024 * 5);//设置上传的文件总的大小不能超过5M  
        String fileName="";
        String folder = System.getProperty("java.io.tmpdir");
        PrintWriter writer = null;
        Map<String, Object> resMap = new HashMap<String, Object>();
        try {  
            // 1. 得到 FileItem 的集合 items  
            List<FileItem> items = upload.parseRequest(request);  
  
            // 2. 遍历 items:  
            for (FileItem item : items) {  
                // 若是一个一般的表单域, 打印信息  
                if (item.isFormField()) {  
                    String name = item.getFieldName();  
                    String value = item.getString("utf-8");
                    role = value;
                }  
                // 若是文件域则把文件保存到 e:\\files 目录下.  
                else {  
                    fileName = item.getName();  
                    InputStream in = item.getInputStream();  
                    byte[] buffer = new byte[1024];  
                    int len = 0;  
                    File file = new File(folder);
                    if(!file.exists()){
                    	file.mkdir();
                    }
                    fileName = folder + fileName;//文件最终上传的位置  
                    OutputStream out = new FileOutputStream(fileName);  
  
                    while ((len = in.read(buffer)) != -1) {  
                        out.write(buffer, 0, len);  
                    }  
  
                    out.close();  
                    in.close(); 
                    String res = ImportExcel(fileName,role);
                    File excelFile = new File(fileName);
                    if(excelFile.exists()){
                    	excelFile.delete();
                    }
            		response.setContentType("application/json;charset=UTF-8");
        			writer = response.getWriter();
            		writer.write(res);
                }  
            }  
  
        } catch (Exception e) {  
        	Util.writeLog(e, "");
        } 
	}
	//导出数据生成Excel
	@RequestMapping("/user/private/exportExcel")
	public void exportExcel(HttpServletRequest request, HttpServletResponse response){
		String path = "";
		try{
			response.setContentType("application/json;charset=UTF-8");
			String type = request.getParameter("type");
			HashMap<String,String> searchMap = new HashMap<String,String>();
			String grade = request.getParameter("grade");
			String subjectId = request.getParameter("subjectid");
			String folder = System.getProperty("java.io.tmpdir");
			searchMap.put("grade", grade);
			List<String> cells = null;
			List dataList = null;
			String tablename = "";
			
			//根据查询条件导出学生信息
			if("0".equals(type)){
				String sname = request.getParameter("sname");
				searchMap.put("sname", sname);
				if(subjectId!=null) {
					searchMap.put("subjectid", subjectId);
					String tname = request.getParameter("tname");
					String subjectname = request.getParameter("subjectname");
					tablename = tname+"老师"+subjectname+"选课信息表";
				}else {
					tablename="学生表";
				}
				StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
				dataList = mapper.queryStudentList(searchMap);
				cells = new ArrayList<String>();
				cells.add("年级");
				cells.add("班级");
				cells.add("姓名");
				cells.add("学籍号");
				cells.add("学号");
				cells.add("密码");
			}else{
				String tname = request.getParameter("tname");
				searchMap.put("tname", tname);
				tablename = "教师表";
				dataList = teacherdao.queryTeacherList(searchMap);
				cells = new ArrayList<String>();
				cells.add("教师编号");
				cells.add("学校名称");
				cells.add("年级");
				cells.add("班级");
				cells.add("姓名");
				cells.add("科目");
				cells.add("密码");
			}
			String filename = ExcelUtil.exportExcel(tablename, cells, dataList, type);
			response.setHeader("Content-Disposition", "attachment;filename=" + filename);
			path = folder+filename;
		
			InputStream in = new FileInputStream(path);
			OutputStream out = response.getOutputStream();
			// 写文件
			int b;
			while ((b = in.read()) != -1) {
				out.write(b);
			}
			in.close();
			out.close();
		}catch(Exception e){
			Util.writeLog(e, "");
		}finally{
			 File file = new File(path);
		     if (!file.exists()) {
		    	 System.out.println("删除文件失败:" + path + "不存在！");
		     }else {
		    	 file.delete();
		     }
		}
		
	}
	//通过年级获得班级列表,用于动态添加下拉项
	@RequestMapping("/user/private/getCnoByGrade")
	public void getCnoByGrade(HttpServletRequest request, HttpServletResponse response){
		PrintWriter writer = null;
		try {
			String grade = request.getParameter("grade");
			StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
			List<String> cnoList = mapper.getCnoByGrade(grade);
			String json = JSON.toJSONString(cnoList,true);
			response.setContentType("application/json;charset=UTF-8");
		
			 writer = response.getWriter();
			 writer.write(json);
		} catch (Exception e) {
			Util.writeLog(e, "");
		}finally{
			if(writer!=null)
			  writer.close();
		}
	}
	//查询课程信息列表
	@RequestMapping("/user/private/subjectList")
	public void subjectList(HttpServletRequest req, HttpServletResponse resp){
		PrintWriter writer = null;
		try {
			String limit =req.getParameter("limit");
			String offset =req.getParameter("offset");
			String subjectName = req.getParameter("subjectname");
			SubjectMapper mapper = sqlSession.getMapper(SubjectMapper.class);
			HashMap<String,String> searchMap = new HashMap<String,String>();
			searchMap.put("limit", limit);
			searchMap.put("offset", offset);
			searchMap.put("subjectname", subjectName);
			int subjectId = 0;
			List<Subject> list = mapper.getSubjectList(searchMap);
			for (Subject subject : list) {
				subjectId = subject.getId();
				subject.setCount(mapper.getStudentCountById(subjectId));
			}
			Map<String, Object> resMap = new HashMap<String, Object>();
			int total = mapper.getSubjectCount(searchMap);
			resMap.put("total", total);
			resMap.put("rows", list);
			String json = JSON.toJSONString(resMap,true);
			resp.setContentType("application/json;charset=UTF-8");
		
			 writer = resp.getWriter();
			 writer.write(json);
		} catch (Exception e) {
			Util.writeLog(e, "");
		}finally{
			if(writer!=null)
			  writer.close();
		}
	}
	
	/***
	 * 用于获取教师下拉列表
	 * @param req
	 * @param resp
	 */
	@RequestMapping("/user/private/getTeacher")
	public void getTeacher(HttpServletRequest req, HttpServletResponse resp) {
		PrintWriter writer = null;
		try {
			HashMap<String,String> searchMap = new HashMap<String,String>();
			List<Teacher> list = teacherdao.queryTeacherList(searchMap);
			String json = JSON.toJSONString(list,true);
			resp.setContentType("application/json;charset=UTF-8");
			
			 writer = resp.getWriter();
			 writer.write(json);
		} catch (Exception e) {
			Util.writeLog(e, "");
		}finally{
			if(writer!=null)
			  writer.close();
		}
	}
	/***
	 * 新增或修改课程信息
	 * @param req
	 * @param resp
	 */
	@RequestMapping("/user/private/subjectAddOrEdit")
	public void subjectAddOrEdit(HttpServletRequest req, HttpServletResponse resp) {
		int res;
		PrintWriter writer = null;
		try {
			String name = req.getParameter("subname");
			String number = req.getParameter("number");
			String id = req.getParameter("id");
			Subject subject = new Subject();
			subject.setSubjectName(name);
			subject.setTeacherId(number);
			Map<String, Object> resMap = new HashMap<String, Object>();
			SubjectMapper mapper = sqlSession.getMapper(SubjectMapper.class);
			if(id==null||"".equals(id)){
				res = mapper.addSubject(subject);
			}else{
				subject.setId(Integer.parseInt(req.getParameter("id")));
				res = mapper.updateSubjectById(subject);
			}
			String restr = "";
			if(res>0){
				restr = "success";
			}else{
				restr = "fail";
			}
			resMap.put("status", restr);
			String json = JSON.toJSONString(resMap,true);
			resp.setContentType("application/json;charset=UTF-8");
		
			 writer = resp.getWriter();
			 writer.write(json);
		} catch (Exception e) {
			Util.writeLog(e, "");
		}finally{
			if(writer!=null)
			  writer.close();
		}
	}
	/***
	 * 删除课程信息
	 * @param req
	 * @param resp
	 */
	@RequestMapping("/user/private/subjectDel")
	public void subjectDel(HttpServletRequest req, HttpServletResponse resp) {
		PrintWriter writer = null;
		try {
			String ids = req.getParameter("ids");
			String idArr[] = ids.split(",");
			Map<String, Object> resMap = new HashMap<String, Object>();
			SubjectMapper mapper = sqlSession.getMapper(SubjectMapper.class);
			int res = 0;
			for(int i=0;i<idArr.length;i++){
				res = mapper.delSubjectById(Integer.parseInt(idArr[i]));
				if(res>0) {
					mapper.updateStudentSubjectId(Integer.parseInt(idArr[i]));
				}
			}
			if(res>0){
				resMap.put("status", "success");
			}else{
				resMap.put("status", "fail");
			}
			String json = JSON.toJSONString(resMap,true);
			resp.setContentType("application/json;charset=UTF-8");
		
			 writer = resp.getWriter();
			 writer.write(json);
		} catch (Exception e) {
			Util.writeLog(e, "");
		}finally{
			if(writer!=null)
			  writer.close();
		}
	}
	/****
	 * 获取课程列表
	 * @param req
	 * @param resp
	 */
	@RequestMapping("/user/private/getSubject")
	public void getSubject(HttpServletRequest req, HttpServletResponse resp) {
		PrintWriter writer = null;
		try {
			SubjectMapper mapper = sqlSession.getMapper(SubjectMapper.class);
			List<Subject> list = mapper.getSubject();
			String json = JSON.toJSONString(list,true);
			resp.setContentType("application/json;charset=UTF-8");
		
			 writer = resp.getWriter();
			 writer.write(json);
		} catch (Exception e) {
			Util.writeLog(e, "");
		}finally{
			if(writer!=null)
			  writer.close();
		}
	}
	/***
	 * 获取年级列表，用于填充下拉列表
	 * @param req
	 * @param resp
	 */
	@RequestMapping("/user/private/getGrade")
	public void getGrade(HttpServletRequest req, HttpServletResponse resp) {
		PrintWriter writer = null;
		try {
			StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
			List<String> gradeList = mapper.getGrade();
			String json = JSON.toJSONString(gradeList,true);
			resp.setContentType("application/json;charset=UTF-8");
			
		
			 writer = resp.getWriter();
			 writer.write(json);
		} catch (Exception e) {
			Util.writeLog(e, "");
		}finally{
			if(writer!=null)
			  writer.close();
		}
	}
	/***
	 * 获取多个学生相同的课程id
	 * @param req
	 * @param resp
	 */
	@RequestMapping("/user/private/getSubjectId")
	public void getSubjectId(HttpServletRequest req, HttpServletResponse resp) {
		PrintWriter writer = null;
		try {
			StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
			String ids = req.getParameter("ids");
			String idArr[] = ids.split(",");
			Collection exists = null;
			Collection notexists = null;
			List<String> idList1 = new ArrayList<String>();
			List<String> idList2 = null;
			List<String> idCpmpare = null;
			//获得第一个学生的课程id
			String subId = mapper.getSubjectId(idArr[0]);
			if(subId==null) {
				subId = "";
			}
			String arr[] = subId.split(",");
			for(int j = 0 ; j<arr.length ; j++) {
				if(!"".equals(arr[j])) {
					idList1.add(arr[j]);
				}
			}
			//比对相同元素
			if(idArr.length>1) {
				idCpmpare = idList1;
				for(int i = 1 ; i<idArr.length ; i++) {
					subId = mapper.getSubjectId(idArr[i]);
					if(subId==null) {
						subId = "";
					}
					idList2 = new ArrayList<String>();
					String arr1[] = subId.split(",");
					for(int j = 0 ; j<arr1.length ; j++) {
						if(!"".equals(arr1[j])) {
							idList2.add(arr1[j]);
						}
					}
					//System.out.println("common-->"+idCpmpare);
					//System.out.println("list2-->"+idList2);
					exists=new ArrayList<String>(idList2);
					notexists=new ArrayList<String>(idList2);
					exists.removeAll(idCpmpare);
					notexists.removeAll(exists);
					idCpmpare = (List<String>) notexists;
					//System.out.println("共同元素为："+idCpmpare);
				}
			}else {
				idCpmpare = idList1;
			}
			String json = JSON.toJSONString(idCpmpare,true);
			//System.out.println(json);
			resp.setContentType("application/json;charset=UTF-8");
			
		
			 writer = resp.getWriter();
			 writer.write(json);
		} catch (Exception e) {
			Util.writeLog(e, "");
		}finally{
			if(writer!=null)
			  writer.close();
		}
	}
	/***
	 * 保存学生的选课id
	 * @param req
	 * @param resp
	 */
	@RequestMapping("/user/private/matchSubject")
	public void matchSubject(HttpServletRequest req, HttpServletResponse resp) {
		PrintWriter writer = null;
		try {
			String subjectids = req.getParameter("subjectid"); //课程id
			String ids = req.getParameter("ids");  //学生id
			StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
			String idArr[] = ids.split(",");
			String status = "";
			int res = 0;
			for(int i=0;i<idArr.length;i++) {
				res = mapper.updateSubjectidById(idArr[i],subjectids);
			}
			if(res>0){
				status =  "success";
			}else{
				status = "fail";
			}
			String json = JSON.toJSONString(status,true);
			resp.setContentType("application/json;charset=UTF-8");
		
			 writer = resp.getWriter();
			 writer.write(json);
		} catch (Exception e) {
			Util.writeLog(e, "");
		}finally{
			if(writer!=null)
		      writer.close();
		}
	}
	/***
	 * 判断用户名是否存在，避免用户名重复
	 * @param req
	 * @param resp
	 */
	@RequestMapping("/user/private/exist")
	public void userNameExist(HttpServletRequest req, HttpServletResponse resp) {
		PrintWriter writer = null;
		try {
			String sno = req.getParameter("txt_sno");
			String tno = req.getParameter("txt_number");
			int res1 = 0;
			int res2 = 0;
			int count = 0;
			boolean result = true;
			StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
			if(!"".equals(sno) && null!=sno) {
				res1 = teacherdao.tnoExist(sno);
				res2 = mapper.snoExist(sno);
				if(res1==1 || res2==1) {
					count = 1; 
				}
			}else if(!"".equals(tno) && null!=tno) {
				res1 = teacherdao.tnoExist(tno);
				res2 = mapper.snoExist(tno);
				if(res1==1 || res2==1) {
					count = 1; 
				}
			}
			if(count==0) {
				result = true;
			}else {
				result = false;
			}
			Map<String, Boolean> map = new HashMap<>();
		    map.put("valid", result);
			String json = JSON.toJSONString(map,true);
			resp.setContentType("application/json;charset=UTF-8");
		
		
			 writer = resp.getWriter();
			 writer.write(json);
		} catch (Exception e) {
			Util.writeLog(e, "");
		}finally{
			if(writer!=null)
			  writer.close();
		}
	}
}
