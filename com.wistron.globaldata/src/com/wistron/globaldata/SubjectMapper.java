package com.wistron.globaldata;

import java.util.HashMap;
import java.util.List;

public interface SubjectMapper {
	
	public List<Subject> getSubjectList(HashMap searchMap);
	
	public int getSubjectCount(HashMap searchMap);
	
	public int getStudentCountById(int id);
	
	public int addSubject(Subject subject);
	
	public int updateSubjectById(Subject subject);
	
	public int delSubjectById(int id);
	
	//删除课程后修改学生表中subjectid字段
	public int updateStudentSubjectId(int id);
	
	public List<Subject> getSubject();
}
