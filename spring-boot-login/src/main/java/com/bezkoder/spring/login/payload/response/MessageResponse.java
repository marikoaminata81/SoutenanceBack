package com.bezkoder.spring.login.payload.response;

public class MessageResponse {
	private String message;

	public MessageResponse(String message, boolean b) {
	    this.message = message;
	  }

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
