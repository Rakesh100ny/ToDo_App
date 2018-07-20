package com.bridgelabz.todo.label.exception;

public class UnauthorizedLabelException extends RuntimeException {

	private static final long serialVersionUID = -1653649020148430608L;
	private String errorMessage;

	public UnauthorizedLabelException(String errorMessage) {

		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

}
