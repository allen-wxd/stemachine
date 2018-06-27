package com.wistron.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

public class SubjectDaoImp implements SubjectDao {
	   SqlSession sqlsession;
		
		public SqlSession getSqlsession() {
			return sqlsession;
		}

		public void setSqlsession(SqlSession sqlsession) {
			this.sqlsession = sqlsession;
		}

	@Override
	public List selectSubjectId(String number) {
		// TODO Auto-generated method stub
		return sqlsession.selectList("subject.getSubjectIdsByNumber",number);
	}

}
