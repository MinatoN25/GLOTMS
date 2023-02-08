package com.glotms.userservice.model;

import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.glotms.userservice.enums.Gender;
import com.glotms.userservice.enums.Role;
import com.glotms.userservice.enums.Status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "User")
public class User {
	
	@Transient
	public static final String SEQUENCE_NAME = "user_sequence";

	@Id
	private int userId;

	private Role role;

	private Gender gender;
	
	private String firstName;
	private String lastName;
	
	private String userEmail;
	
	private Status status;
	
	private Binary image;
	
	@Transient
	private String password;

}