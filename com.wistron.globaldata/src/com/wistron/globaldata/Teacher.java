package com.wistron.globaldata;

public class Teacher {
	//id 主键
	private int id;
	//number
	private String number;
	//school
	private String school;
	//grade
	private String grade;
	//tclass
	private String tclass;
	//授课数量
	private int subjectCount;
	//name
	private String name;
	//subject
	private String subject ;
	//password
	private String password;
	
	public int getSubjectCount() {
		return subjectCount;
	}
	public void setSubjectCount(int subjectCount) {
		this.subjectCount = subjectCount;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getSchool() {
		return school;
	}
	public void setSchool(String school) {
		this.school = school;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getTclass() {
		return tclass;
	}
	public void setTclass(String tclass) {
		this.tclass = tclass;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public String toString() {
		return "Teacher [id=" + id + ", number=" + number + ", school=" + school + ", grade=" + grade + ", tclass="
				+ tclass + ", name=" + name + ", subject=" + subject + ", password=" + password + "]";
	}
	
}
