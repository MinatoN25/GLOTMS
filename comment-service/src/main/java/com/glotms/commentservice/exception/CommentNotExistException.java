package com.glotms.commentservice.exception;

public class CommentNotExistException extends RuntimeException {
	
	private final String errorMessage;

	private static final long serialVersionUID = 1L;

	public CommentNotExistException(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return this.errorMessage;
	}

}
