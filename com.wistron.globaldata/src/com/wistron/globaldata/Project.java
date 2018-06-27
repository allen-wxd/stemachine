package com.wistron.globaldata;

import java.util.Date;

public class Project {

	private int pid;
	private String pname;
	private String sno;//学生编号
	private String code;//代码
	private String videoUrl;//视屏地址
	private String submitDate;//提交时间
	private String startTime;//代码执行开始时间
	private String endTime;//代码执行结束时间
	private int status;//代码执行状态，1表示尚未发布，2表示等待回传，3表示未播放，4表示已播放，5表示结果出错
	
	public Project(){
		
	}
	public Project(int pid, String pname, String sno, String code, String videoUrl, String submitDate, String startTime,
			String endTime, int status) {
		super();
		this.pid = pid;
		this.pname = pname;
		this.sno = sno;
		this.code = code;
		this.videoUrl = videoUrl;
		this.submitDate = submitDate;
		this.startTime = startTime;
		this.endTime = endTime;
		this.status = status;
	}
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	public String getSno() {
		return sno;
	}
	public void setSno(String sno) {
		this.sno = sno;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getVideoUrl() {
		return videoUrl;
	}
	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}
	public String getSubmitDate() {
		return submitDate;
	}
	public void setSubmitDate(String submitDate) {
		this.submitDate = submitDate;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEngTime() {
		return endTime;
	}
	public void setEngTime(String engTime) {
		this.endTime = engTime;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
}
