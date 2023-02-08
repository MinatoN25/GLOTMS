package com.glotms.ticketservice.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.glotms.ticketservice.dto.ProjectDTO;
import com.glotms.ticketservice.enums.Role;
import com.glotms.ticketservice.enums.Status;
import com.glotms.ticketservice.exception.ProjectAlreadyExistException;
import com.glotms.ticketservice.exception.ProjectDoesNotExistException;
import com.glotms.ticketservice.exception.UnauthorizedException;
import com.glotms.ticketservice.model.Project;
import com.glotms.ticketservice.model.User;
import com.glotms.ticketservice.repository.ProjectRepository;
import com.glotms.ticketservice.repository.UserRepository;

@Service
public class ProjectServiceImpl implements ProjectService {

	private ProjectRepository projectRepository;
	private UserRepository userRepository;

	@Autowired
	public ProjectServiceImpl(ProjectRepository projectRepository, UserRepository userRepository) {
		this.projectRepository = projectRepository;
		this.userRepository= userRepository;
	}

	@Override
	public Project createProject(ProjectDTO projectDto, String loggedInUser) {
		if (!projectRepository.findById(projectDto.getProjectCode()).isPresent()) {
			Project project = ProjectDTO.convertToEntity(projectDto);
			project.setCreatedOn(LocalDateTime.now());
			project.setCreatedBy(loggedInUser);
			projectRepository.save(project);
			return project;
		}
		
		throw new ProjectAlreadyExistException("Project already exists with this project code");
	}

	@Override
	public Project editProject(ProjectDTO projectDto, String loggedInUser) {
		if (projectRepository.findById(projectDto.getProjectCode()).isPresent()) {
			Project project = ProjectDTO.convertToEntity(projectDto);
			project.setUpdatedOn(LocalDateTime.now());
			project.setUpdatedBy(loggedInUser);
			return projectRepository.save(project);
		}
		throw new ProjectDoesNotExistException("Project with this project code does not exist");
	}

	@Override
	public Project grantProjectAccess(String projectCode, ProjectDTO projectDto, String loggedInUser) {
		adminSuperAdminAccess(loggedInUser);
		Optional<Project> project = projectRepository.findById(projectCode);
		if (project.isPresent()) {
			
			Set<String> users = project.get().getUsers();
			if(users == null) {
				users = new HashSet<>();
			}
			users.addAll(projectDto.getUsers());
			project.get().setUsers(users);
			return projectRepository.save(project.get());
		}
		throw new ProjectDoesNotExistException("Project with this project code does not exist");
	}
	
	
	private boolean adminSuperAdminAccess(String userEmail) {
		User loggedInUser = userRepository.findByUserEmail(userEmail);
		if (loggedInUser != null) {
			if ((loggedInUser.getRole().equals(Role.ADMIN) || loggedInUser.getRole().equals(Role.SUPER_ADMIN))
					&& loggedInUser.getStatus().equals(Status.ACTIVE)) {
				return true;
			} else {
				throw new UnauthorizedException("Unauth");
			}
		}
		return false;

	}

}
