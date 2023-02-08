package com.glotms.searchservice.exception;

public class TicketNotFoundException extends RuntimeException {
	private final String errorMessage;

	private static final long serialVersionUID = 1L;

	public TicketNotFoundException(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return this.errorMessage;
	}
}
