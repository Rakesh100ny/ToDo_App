package com.bridgelabz.todo.utility;

public class Response 
{
	private boolean success;
	private String message;

	public Response()
	{}
	
	public Response(boolean success, String message) {
		this.success = success;
		this.message = message;
	}

	public boolean isSuccess() {
		return success;
	}

    public String getMessage() {
		return message;
	}


}
