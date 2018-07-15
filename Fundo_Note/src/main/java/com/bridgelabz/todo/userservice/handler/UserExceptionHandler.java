package com.bridgelabz.todo.userservice.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.bridgelabz.todo.userservice.error.UserErrorResponse;
import com.bridgelabz.todo.userservice.exception.EmailIdAlreadyExistException;
import com.bridgelabz.todo.userservice.exception.TokenExpireException;
import com.bridgelabz.todo.userservice.exception.UserNotFoundException;

@ControllerAdvice
public class UserExceptionHandler 
{

	 @ExceptionHandler(UserNotFoundException.class)
	    public ResponseEntity<UserErrorResponse> handleUserNotFoundException(RuntimeException exception) {
	        UserErrorResponse errorResponse = new UserErrorResponse();
	        errorResponse.setErrorCode(400);
	        errorResponse.setErrorMessage(exception.getMessage());
	        return new ResponseEntity<UserErrorResponse>(errorResponse, HttpStatus.NOT_FOUND);
	    }
	 
	 
	 @ExceptionHandler(TokenExpireException.class)
	    public ResponseEntity<UserErrorResponse> handleTokenExpireException(RuntimeException exception)  {
	        UserErrorResponse errorResponse = new UserErrorResponse();
	        errorResponse.setErrorCode(400);
	        errorResponse.setErrorMessage(exception.getMessage());
	        return new ResponseEntity<UserErrorResponse>(errorResponse, HttpStatus.NOT_FOUND);
	    }
	 
	 
	 @ExceptionHandler(EmailIdAlreadyExistException.class)
	 public ResponseEntity<UserErrorResponse> handleEmailIdAlreadyExistException(RuntimeException exception)
	 {
	  UserErrorResponse errorResponse=new UserErrorResponse();
	  errorResponse.setErrorCode(HttpStatus.PRECONDITION_FAILED.value());
	  errorResponse.setErrorMessage(exception.getMessage());
	  return new ResponseEntity<UserErrorResponse>(errorResponse,HttpStatus.NOT_ACCEPTABLE);
	 }
	  
	/*    @ExceptionHandler(Exception.class)
	    public ResponseEntity<UserErrorResponse> handleGenericException(Exception exception) {
	        UserErrorResponse errorResponse = new UserErrorResponse();
	        errorResponse.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
	        errorResponse.setErrorMessage(exception.getMessage());
	        return new ResponseEntity<UserErrorResponse>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	    }*/

}