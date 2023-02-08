package com.glotms.userservice.exception;

public class OTPExpiredException extends RuntimeException {
	private final String errorMessage;

	private static final long serialVersionUID = 1L;

	public OTPExpiredException(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return this.errorMessage;
	}
}
