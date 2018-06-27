package com.wistron.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.wistron.globaldata.Student;
import com.wistron.globaldata.Subject;
import com.wistron.globaldata.Teacher;

public class TeacherDaoImp implements TeacherDao {
    SqlSession sqlsession;
	
	public SqlSession getSqlsession() {
		return sqlsession;
	}

	public void setSqlsession(SqlSession sqlsession) {
		this.sqlsession = sqlsession;
	}
	@Override
	public String selectPassWordByName(String username) {
		// TODO Auto-generated method stub
		return sqlsession.selectOne("teacher.getPassWordByName", username);
	}

	@Override
	public int updatePWByName(HashMap hm) {
		// TODO Auto-generated method stub
		return sqlsession.update("teacher.updatePWByName", hm);
	}

//	@Override
//	public String selectNameById(int username) {
//		// TODO Auto-generated method stub
//		return sqlsession.selectOne("teacher.getNameById", username);
//	}

	@Override
	public List<Teacher> selectTeacher(String number) {
		// TODO Auto-generated method stub
		return sqlsession.selectList("teacher.getTeacherByNumber", number);
	}

	@Override
	public int addTeacher(Teacher teacher) {
		
		return sqlsession.insert("teacher.addTeacher", teacher);
	}

	@Override
	public List<Teacher> getTeacherList(HashMap searchMap) {
		
		return sqlsession.selectList("teacher.getTeacherList", searchMap);
		
	}

	@Override
	public int getTeacherCount(HashMap map) {
		
		return sqlsession.selectOne("teacher.getTeacherCount", map);
		
	}

	@Override
	public int updateTeacherById(Teacher teacher) {
		
		return sqlsession.update("teacher.updateTeacherById", teacher);
	}

	@Override
	public int delTeacherById(int id) {

		return sqlsession.delete("teacher.delTeacherById", id);
		
	}

	@Override
	public List<Teacher> queryTeacherList(HashMap<String, String> searchMap) {
		
		return sqlsession.selectList("teacher.queryTeacherList", searchMap);
		
	}

	@Override
	public int tnoExist(String tno) {

		return sqlsession.selectOne("teacher.tnoExist", tno);
		
	}

	@Override
	public List<Subject> getSubjectIdByid(String id) {
		
		return sqlsession.selectList("teacher.getSubjectIdByid", id);
		
	}

	@Override
	public int delSubjectByTeacher(int id) {
		
		return sqlsession.delete("teacher.delSubjectByTeacher", id);
	}

}
