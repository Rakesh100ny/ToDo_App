package com.bridgelabz.todo.userservice.exception;

public class TokenExpireException extends RuntimeException
{

	private static final long serialVersionUID = -1689017632281795211L;
	private String errorMessage;
	 
    public TokenExpireException()
    {
     super();	
	}
 
    public TokenExpireException(String errorMessage) {
        super(errorMessage);
    	this.errorMessage = errorMessage;
    }
 
    public String getErrorMessage() {
        return errorMessage;
    }

}
