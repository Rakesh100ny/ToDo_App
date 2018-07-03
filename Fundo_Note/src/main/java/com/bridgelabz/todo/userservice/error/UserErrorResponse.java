package com.bridgelabz.todo.userservice.error;

public class UserErrorResponse
{
	private int errorCode;
    private String errorMessage;
    
    public int getErrorCode() {
        return errorCode;
    }
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
    public String getErrorMessage() {
        return errorMessage;
    }
    
	public void setErrorMessage(String message) {
		this.errorMessage = message;
		
	}
	
 
}
