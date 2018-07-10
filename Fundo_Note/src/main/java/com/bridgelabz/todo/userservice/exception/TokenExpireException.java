package com.bridgelabz.todo.userservice.exception;

public class TokenExpireException extends RuntimeException
{
	private static final long serialVersionUID = 1L;
	private String errorMessage;
	 
    public TokenExpireException() {
	}
 
    public TokenExpireException(String errorMessage) {
         this.errorMessage = errorMessage;
    }
 
    public String getErrorMessage() {
        return errorMessage;
    }

}
