package com.wistron.globaldata;

public class FileInfoData {

	private int fileId;
	private String fileName;
	private String filePath;
	public int getFileId() {
		return fileId;
	}
	public FileInfoData(int fileId, String fileName, String filePath) {
		super();
		this.fileId = fileId;
		this.fileName = fileName;
		this.filePath = filePath;
	}
	public FileInfoData(){
		
	}
	public void setFileId(int fileId) {
		this.fileId = fileId;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
}
