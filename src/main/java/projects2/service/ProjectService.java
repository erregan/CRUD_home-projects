package projects2.service;

import java.util.List;
import java.util.NoSuchElementException;
import projects2.dao.ProjectDao;
import projects2.entity.Projects2;
import projects2.exception.DbException2;

public class ProjectService {
	private ProjectDao projectDao = new ProjectDao();
	
	

	public Projects2 addProject(Projects2 project) {
		return projectDao.insertProject(project);
	}



	public List<Projects2> fetchAllProjects() {
		return projectDao.fetchAllProjects();
	}



	public Projects2 fetchProjectById(Integer projectId) {
		return projectDao.fetchProjectById(projectId).orElseThrow(() -> new NoSuchElementException(
				"Project with project ID=" + projectId + " does not exist"));
	}



	public void modifyProjectDetails(Projects2 project) {
		if (!projectDao.modifyProjectDetails(project)) {
			throw new DbException2("Project with ID=" + project.getProjects2Id() + " does not exist.");
		}
		
	}



	public void deleteProjects2(Integer projects2Id) {
		if (!projectDao.deleteProject(projects2Id)) {
			throw new DbException2("Project with ID=" + projects2Id + " does not exist.");
		
		
	}

}}
