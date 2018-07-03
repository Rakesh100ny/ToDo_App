package com.bridgelabz.todo.noteservice.exception;

public class NoteNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String errorMessage;

	
	public NoteNotFoundException(String errorMessage) {

		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
	
	
}
