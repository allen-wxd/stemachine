package com.wistron.globaldata;

public class Users {

	private String loginName;
	private String passWord;
	private String phone;
	private String email;
	private String realname;
	
	public Users(){}
	public Users(String loginName, String passWord, String phone, String email, String realname) {
		super();
		this.loginName = loginName;
		this.passWord = passWord;
		this.phone = phone;
		this.email = email;
		this.realname = realname;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	
	
}
