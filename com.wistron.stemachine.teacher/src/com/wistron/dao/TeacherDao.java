package com.wistron.dao;

import java.util.HashMap;
import java.util.List;

import com.wistron.globaldata.Teacher;

public interface TeacherDao {
	 String selectPassWordByName(String username );
	 int updatePWByName(HashMap<String,String> hm);
	// String selectNameById(int username);
	 List<Teacher> selectTeacher(String number);
	 String getTeaNameByNumber(String number);
}
