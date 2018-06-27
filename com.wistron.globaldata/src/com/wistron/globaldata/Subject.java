package com.wistron.globaldata;
/***
 * 对应课程表
 * @author s1710001
 *
 */
public class Subject {
	
	private int id;
	private String subjectName;
	private String teacherId;
	private String teacherName;
	private int count;
	
	public String getTeacherName() {
		return teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public String getTeacherId() {
		return teacherId;
	}
	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}
	@Override
	public String toString() {
		return "Subject [id=" + id + ", subjectName=" + subjectName + ", teacherId=" + teacherId + ", teacherName="
				+ teacherName + ", count=" + count + "]";
	}
	
}
