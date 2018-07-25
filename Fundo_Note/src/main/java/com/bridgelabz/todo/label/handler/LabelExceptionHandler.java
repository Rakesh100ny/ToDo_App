package com.bridgelabz.todo.label.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.bridgelabz.todo.label.exception.LabelAlreadyExistException;
import com.bridgelabz.todo.label.exception.LabelNotFoundException;
import com.bridgelabz.todo.noteservice.exception.UnauthorizedException;
import com.bridgelabz.todo.label.error.UserErrorResponse;

@ControllerAdvice
public class LabelExceptionHandler 
{
	@ExceptionHandler(LabelNotFoundException.class)
	public ResponseEntity<UserErrorResponse> handleLabelNotFoundException(LabelNotFoundException ex) {
		UserErrorResponse errorResponse = new UserErrorResponse();
		errorResponse.setErrorCode(404);
		errorResponse.setErrorMessage(ex.getMessage());
		return new ResponseEntity<UserErrorResponse>(errorResponse, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(LabelAlreadyExistException.class)
	public ResponseEntity<UserErrorResponse> handleLabelAlreadyExistException(LabelAlreadyExistException ex) {
		UserErrorResponse errorResponse = new UserErrorResponse();
		errorResponse.setErrorCode(HttpStatus.PRECONDITION_FAILED.value());
		errorResponse.setErrorMessage(ex.getMessage());
		return new ResponseEntity<UserErrorResponse>(errorResponse, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<UserErrorResponse> handleUnauthorizedException(UnauthorizedException ex) {
		UserErrorResponse errorResponse = new UserErrorResponse();
		errorResponse.setErrorCode(HttpStatus.PRECONDITION_FAILED.value());
		errorResponse.setErrorMessage(ex.getMessage());
		return new ResponseEntity<UserErrorResponse>(errorResponse, HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<UserErrorResponse> handleGenericException(Exception ex) {
		UserErrorResponse errorResponse = new UserErrorResponse();
		errorResponse.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		errorResponse.setErrorMessage(ex.getMessage());
		return new ResponseEntity<UserErrorResponse>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
