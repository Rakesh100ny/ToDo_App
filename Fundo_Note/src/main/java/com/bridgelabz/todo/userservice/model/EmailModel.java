package com.bridgelabz.todo.userservice.model;

import java.io.Serializable;

public class EmailModel implements Serializable {


	private static final long serialVersionUID = 1L;
	
	private String to;
	private String subject;
	private String url;

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
