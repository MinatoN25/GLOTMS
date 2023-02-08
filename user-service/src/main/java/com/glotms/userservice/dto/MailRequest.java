package com.glotms.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MailRequest {

	private String firstName;
	private String userEmail;
	private String otp;
	private String password;

	public MailRequest(String name, String userEmail) {
		super();
		this.firstName = name;
		this.userEmail = userEmail;
	}

	public MailRequest(String name, String userEmail, String otp) {
		this(name,userEmail);
		this.otp = otp;
	}

}
