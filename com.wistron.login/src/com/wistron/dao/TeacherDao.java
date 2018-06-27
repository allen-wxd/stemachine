package com.wistron.dao;

import java.util.HashMap;
import java.util.List;

import com.wistron.globaldata.Student;
import com.wistron.globaldata.Subject;
import com.wistron.globaldata.Teacher;

public interface TeacherDao {
	 String selectPassWordByName(String username );
	 int updatePWByName(HashMap<String,String> hm);
	// String selectNameById(int username);
	 List<Teacher> selectTeacher(String number);
	 
	 public int addTeacher(Teacher teacher);
	 
	 public List<Teacher> getTeacherList(HashMap searchMap);
	 
	 public int getTeacherCount(HashMap map);
	 
	 public int updateTeacherById(Teacher teacher);
	 
	 public int delTeacherById(int id);
	 
	//查询教师信息列表，无分页，用于导出Excel数据
	 public List<Teacher> queryTeacherList(HashMap<String,String> searchMap);
	 //判断教师编号是否重复
	 public int tnoExist(String tno);
	 
	 public List<Subject> getSubjectIdByid(String id);
	 
	 //删除教师后删除所教授课程信息
	 public int delSubjectByTeacher(int id);
	 
}
