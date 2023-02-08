package com.glotms.ticketservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.glotms.ticketservice.enums.Role;
import com.glotms.ticketservice.enums.Status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "User")
public class User {

	@Id
	private int userId;
	
	private String firstName;
	private String lastName;
	private Role role;
	private Status status;
	private String userEmail;
	

}