package com.glotms.ticketservice.model;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Project {

	@Id
	private String projectCode;
	private String projectTitle;
	private String projectDescription; 
	private String createdBy;
	private String updatedBy;
	private LocalDateTime createdOn;
	private LocalDateTime updatedOn;
	private Set<String> users;
}
