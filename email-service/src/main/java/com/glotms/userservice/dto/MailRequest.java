package com.glotms.userservice.dto;

import lombok.Data;

@Data
public class MailRequest {
	
	private String firstName;
	private String userEmail;
	private String otp;
	private String subject;
	private String password;

}
