package com.wistron.dao;

import org.apache.ibatis.session.SqlSession;

public class SuperUserDaoImp implements SuperUserDao {
   
	SqlSession sqlsession;
	public SqlSession getSqlsession() {
		return sqlsession;
	}

	public void setSqlsession(SqlSession sqlsession) {
		this.sqlsession = sqlsession;
	}
	@Override
	public String getAdminPW(String name) {
		// TODO Auto-generated method stub
		return sqlsession.selectOne("admin.getAdminPWByName", name);
	}

}
