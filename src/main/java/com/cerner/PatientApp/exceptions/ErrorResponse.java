package com.cerner.PatientApp.exceptions;

/**
 * ErrorResponse class represents the error details that can occur during the API execution.
 *
 * @author Akash Tangde
 */
public class ErrorResponse {

	/**
	 * status code of the error
	 */
	private int status;
	
	/**
	 * message describing the error
	 */
	private String message;
	
	public ErrorResponse(int status, String message) {
		this.status = status;
		this.message = message;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public ErrorResponse() {
	}
}
