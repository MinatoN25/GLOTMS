package com.glotms.ticketservice.service;

import java.util.List;

import com.glotms.ticketservice.dto.ProjectDTO;
import com.glotms.ticketservice.model.Project;

public interface ProjectService {
	
	Project createProject(ProjectDTO projectDto, String loggedInUser);
	Project editProject(ProjectDTO projectDto, String loggedInUser);
	Project grantProjectAccess(String projectCode, ProjectDTO projectDto, String loggedInUser);

}
