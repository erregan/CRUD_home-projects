package projects2.service;

import projects2.dao.ProjectDao;
import projects2.entity.Project;

public class ProjectService {
	private ProjectDao projectDao = new ProjectDao();
	
	

	public Project addProject(Project project) {
		return projectDao.insertProject(project);
	}

}
