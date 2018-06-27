package com.wistron.globaldata;

public interface UsersMapper {

	public String getPasswordByName(String loginname);
	
	public int exitUserName(String loginname);
	
	public int saveUser(Users user);
}
