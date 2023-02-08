package com.glotms.ticketservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.glotms.ticketservice.dto.ProjectDTO;
import com.glotms.ticketservice.exception.ProjectAlreadyExistException;
import com.glotms.ticketservice.exception.ProjectDoesNotExistException;
import com.glotms.ticketservice.service.ProjectService;

import io.jsonwebtoken.Claims;

@RestController
@RequestMapping("/api/v1")
public class ProjectController {

	private ProjectService projectService;

	@Autowired
	public ProjectController(ProjectService projectService) {
		this.projectService = projectService;
	}
	
	@PostMapping("/createProject")
	public ResponseEntity<?> createProject(@RequestBody ProjectDTO projectDTO, @RequestAttribute Claims claims){
		try {
			return new ResponseEntity<>(projectService.createProject(projectDTO, claims.getSubject()), HttpStatus.CREATED);
		}catch(ProjectAlreadyExistException p) {
			return new ResponseEntity<>(p.getErrorMessage(),HttpStatus.CONFLICT);
		}catch(Exception e ) {
			return new ResponseEntity<>("Conflict occured", HttpStatus.CONFLICT);
		}
	}
	
	@PutMapping("/updateProject")
	public ResponseEntity<?> updateProject(@RequestBody ProjectDTO projectDTO, @RequestAttribute Claims claims){
		try {
			return new ResponseEntity<>(projectService.editProject(projectDTO, claims.getSubject()), HttpStatus.CREATED);
		}catch(ProjectDoesNotExistException p) {
			return new ResponseEntity<>(p.getErrorMessage(),HttpStatus.CONFLICT);
		}catch(Exception e ) {
			return new ResponseEntity<>("Conflict occured", HttpStatus.CONFLICT);
		}
	}
	
	@PutMapping("/grantProjectAccess")
	public ResponseEntity<?> grantProjectAccess(@RequestParam String projectCode,@RequestBody ProjectDTO projectDTO, @RequestAttribute Claims claims){
		try {
			return new ResponseEntity<>(projectService.grantProjectAccess(projectCode, projectDTO, claims.getSubject()), HttpStatus.CREATED);
		}catch(ProjectDoesNotExistException p) {
			return new ResponseEntity<>(p.getErrorMessage(),HttpStatus.CONFLICT);
		}catch(Exception e ) {
			return new ResponseEntity<>("Conflict occured", HttpStatus.CONFLICT);
		}
	}
}
