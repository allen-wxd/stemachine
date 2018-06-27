package com.wistron.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.SqlSession;

import com.wistron.globaldata.Student;

public class StudentDaoImp implements StudentDao {
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
		return sqlsession.selectOne("student.getPassWordByName", username);
	}

	@Override
	public int updatePWByName(HashMap hm) {
		// TODO Auto-generated method stub
		return sqlsession.update("student.updatePWByName", hm);
	}

	@Override
	public String selectNameById(int username) {
		// TODO Auto-generated method stub
		return sqlsession.selectOne("student.getNameById",username);
	}

	@Override
	public List<Student> selectStudentByGrade(Student student) {
		// TODO Auto-generated method stub
		return sqlsession.selectList("student.getStudentsByGrade",student);
	
	}

	@Override
	public List<Student> selectStudentByName(Student student) {
		// TODO Auto-generated method stub
		return sqlsession.selectList("student.getStudentByName",student);
	}

	@Override
	public List<Student> orderStudentByName(HashMap<String,String> params) {
		// TODO Auto-generated method stub
		return sqlsession.selectList("student.orderByName",params);
	}

	@Override
	public List<Student> orderStudentById(HashMap<String,String> params) {
		// TODO Auto-generated method stub
		return sqlsession.selectList("student.orderById",params);
	}

	@Override
	public List<Student> selectGandCBySubId(String number) {
		// TODO Auto-generated method stub
		return sqlsession.selectList("student.getGradeAndClassBySubId",number);
	}

}
