package com.glotms.ticketservice.exception;

public class ProjectDoesNotExistException extends RuntimeException {

	private final String errorMessage;

	private static final long serialVersionUID = 1L;

	public ProjectDoesNotExistException(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return this.errorMessage;
	}
}