package com.app.exception;

/* Creating the exception class to display to the presentation layer.*/
public class BusinessException extends Exception {

	public BusinessException() {
		super();
	}

	public BusinessException(final String message) {
		super(message);
	}

}
