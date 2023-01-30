package com.cerner.PatientApp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 
 * CustomExceptionHandler class handles the RecordNotFoundException.
 * 
 * @author Akash Tangde
 */
@ControllerAdvice
public class CustomExceptionHandler {

	/**
	 * This method is used to handle RecordNotFoundException and return error
	 * response with message and status code.
	 * 
	 * @param ine RecordNotFoundException object.
	 * @return ResponseEntity<ErrorResponse> error response with message and status
	 *         code.
	 */
	@ExceptionHandler(RecordNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleException(RecordNotFoundException ine) {
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setMessage(ine.getMessage());
		errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.NOT_FOUND);
	}
}
