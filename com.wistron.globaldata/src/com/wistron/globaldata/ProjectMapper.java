package com.wistron.globaldata;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ProjectMapper {

	public int addProject(Project project);
	
	public List<Project> projectList(HashMap<String,String> searchMap);
	
	public int getTotalCount(HashMap<String,String> searchMap);
	
	public int delProjectById(String id);
	
	public int updateProjectName(Project project);
	
	public String getCommandById(String pid);
	
	public int updateCommandById(Map<String,String> paraMap);
	
	public int updateProjectVideo(Map<String,String> paraMap);
	
	public String getVideoPathById(String pid);
	
	public int modifyStatus(Map<String,String> paraMap);
}
