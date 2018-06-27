package com.wistron.globaldata;

public class Student {
	
	private Integer id;
	private String sno;
	private String sname;
	private String cno;
	private String grade;
	int count;
	private String password;
	//座位号
	private String seatNum;
	
	public String getSeatNum() {
		return seatNum;
	}

	public void setSeatNum(String seatNum) {
		this.seatNum = seatNum;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Student(){
		
		
	}
	
	public Student(Integer id, String sno, String sname, String cno, String grade, String password) {
		super();
		this.id = id;
		this.sno = sno;
		this.sname = sname;
		this.cno = cno;
		this.grade = grade;
		this.password = password;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getSno() {
		return sno;
	}
	public void setSno(String sno) {
		this.sno = sno;
	}
	public String getSname() {
		return sname;
	}
	public void setSname(String sname) {
		this.sname = sname;
	}
	public String getCno() {
		return cno;
	}
	public void setCno(String cno) {
		this.cno = cno;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	
	@Override
	public String toString() {
		return "Student [sno=" + sno + ", id=" + id + ", grade=" + grade + ", sname=" + sname + ", cno=" + cno
				+ ", password=" + password + "]";
	}
}
