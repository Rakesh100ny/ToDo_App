package com.bridgelabz.todo.noteservice.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.bridgelabz.todo.noteservice.error.UserErrorResponse;
import com.bridgelabz.todo.noteservice.exception.NoteNotFoundException;

@ControllerAdvice
public class NoteExceptionHandler {

	@ExceptionHandler(NoteNotFoundException.class)

	public ResponseEntity<UserErrorResponse> handleNoteNotFoundException(RuntimeException ex) {
		UserErrorResponse errorResponse = new UserErrorResponse();
		errorResponse.setErrorCode(404);
		errorResponse.setErrorMessage(ex.getMessage());
		return new ResponseEntity<UserErrorResponse>(errorResponse, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<UserErrorResponse> handleGenericException(Exception ex) {
		UserErrorResponse errorResponse = new UserErrorResponse();
		errorResponse.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		errorResponse.setErrorMessage(ex.getMessage());
		return new ResponseEntity<UserErrorResponse>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
