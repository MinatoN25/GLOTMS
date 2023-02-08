package com.glotms.userservice.exception;

public class UserNotFoundException extends RuntimeException {
	private final String errorMessage;

	private static final long serialVersionUID = 1L;

	public UserNotFoundException(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return this.errorMessage;
	}
}
