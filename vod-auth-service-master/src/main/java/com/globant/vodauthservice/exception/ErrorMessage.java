package com.globant.vodauthservice.exception;

/**
 * @author anuruddh.yadav
 *
 */
public class ErrorMessage {

	private int statusCode;
	
	private String type;
	
	private String message;

	
	public ErrorMessage(int statusCode, String type,String message) {
		this.statusCode = statusCode;
		this.type=type;
		this.message = message;
	}

	
	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
