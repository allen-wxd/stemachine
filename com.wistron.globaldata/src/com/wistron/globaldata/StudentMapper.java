package com.wistron.globaldata;

import java.util.HashMap;
import java.util.List;

public interface StudentMapper {

	public int addStudent(Student student);
	
	public List<Student> getStudentList(HashMap searchMap);
	
	public int getStudentCount(HashMap map);
	
	public int delStudentById(int id);
	
	public int updateStudentById(Student student);
	
	public Student getStudentBySno(String sno);
	
	public String getNameByTno(String tno);
	
	//查询学生信息列表，无分页，用于导出Excel数据
	public List<Student> queryStudentList(HashMap<String,String> searchMap);
	
	public List<String> getCnoByGrade(String grade);
	
	//通过学生id查询学生选课id
	public String getSubjectId(String id);
	//通过学生id修改选课id
	public int updateSubjectidById(String id,String subjectids);
	
	public int snoExist(String sno);
	
	public List<Student> getSubjectCount(String subjectId);
	
	public List<String> getGrade();
	
}
