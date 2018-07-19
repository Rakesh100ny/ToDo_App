package com.bridgelabz.todo.noteservice.exception;

public class UnauthorizedException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	private String errorMessage;

	
	public UnauthorizedException(String errorMessage) {

		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
	
	
}