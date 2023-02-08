package com.glotms.ticketservice.exception;

public class ProjectAlreadyExistException extends RuntimeException {

	private final String errorMessage;

	private static final long serialVersionUID = 1L;

	public ProjectAlreadyExistException(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return this.errorMessage;
	}
}
