package com.wistron.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wistron.globaldata.Student;

public interface StudentDao {
	 String selectPassWordByName(String username );
	 int updatePWByName(HashMap hm);
	 String selectNameById(int username);
	 List<Student> selectStudentByGrade(Student student);
	 List<Student> selectStudentByName(Student student);
	 
	 List<Student> orderStudentByName(HashMap<String,String> params);
	 List<Student> orderStudentById(HashMap<String,String> params);
	 
	 List<Student> selectGandCBySubId(String number);
}
