package com.glotms.ticketservice.dto;

import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.glotms.ticketservice.model.Project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDTO {

	@NotNull(message = "Project code can not be null")
	private String projectCode;
	@NotNull(message = "Project title can not be null")
	@NotBlank(message = "Project title can not be blank")
	private String projectTitle;
	private String projectDescription;
	private Set<String> users;

	public static Project convertToEntity(ProjectDTO projectDto) {
		Project project = new Project();
		project.setProjectCode(projectDto.getProjectCode());
		project.setProjectDescription(projectDto.getProjectDescription());
		project.setProjectTitle(projectDto.getProjectTitle());

		project.setUsers(projectDto.getUsers());

		return project;
	}
}
