package com.bridgelabz.todo.userservice.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.bridgelabz.todo.userservice.error.UserErrorResponse;
import com.bridgelabz.todo.userservice.exception.UserNotFoundException;

@ControllerAdvice
public class UserExceptionHandler 
{

	 @ExceptionHandler(UserNotFoundException.class)
	    public ResponseEntity<UserErrorResponse> handleUserNotFoundException(Exception ex) {
	        UserErrorResponse errorResponse = new UserErrorResponse();
	        errorResponse.setErrorCode(400);
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