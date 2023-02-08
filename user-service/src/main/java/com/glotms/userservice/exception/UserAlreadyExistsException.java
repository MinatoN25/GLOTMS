package com.glotms.userservice.exception;

public class UserAlreadyExistsException extends RuntimeException {
	
	private final String errorMessage;

	private static final long serialVersionUID = 1L;

	public UserAlreadyExistsException(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return this.errorMessage;
	}
	

}
