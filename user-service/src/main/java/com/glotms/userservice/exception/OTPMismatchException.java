package com.glotms.userservice.exception;

public class OTPMismatchException extends RuntimeException {
	private final String errorMessage;

	private static final long serialVersionUID = 1L;

	public OTPMismatchException(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return this.errorMessage;
	}
}
