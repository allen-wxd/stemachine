package com.wistron.dao;

import java.util.HashMap;
import java.util.List;

import com.wistron.globaldata.Student;

public interface StudentDao {
	 String selectPassWordByName(String username );
	 int updatePWByName(HashMap hm);
	 String selectNameById(int username);
	 List<Student> selectStudentByGrade(Student student);
	 List<Student> selectStudentByName(Student student);
	 List<Student> orderStudentByName(Student student);
	 List<Student> orderStudentById(Student student);
	 Student selectStudentByKey(String name);
}
