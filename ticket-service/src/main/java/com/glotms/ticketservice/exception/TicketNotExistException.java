package com.glotms.ticketservice.exception;

public class TicketNotExistException extends RuntimeException {
	private final String errorMessage;

	private static final long serialVersionUID = 1L;

	public TicketNotExistException(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return this.errorMessage;
	}
}
