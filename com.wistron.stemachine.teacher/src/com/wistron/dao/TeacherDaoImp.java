package com.wistron.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

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
	public String getTeaNameByNumber(String number) {
		// TODO Auto-generated method stub
		return sqlsession.selectOne("teacher.getNameById", number);
	}

}
