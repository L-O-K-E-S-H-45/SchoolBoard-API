package com.school.sba.exception;

public class SchoolNotfoundByIdException extends RuntimeException {
	
	private String message;
	public SchoolNotfoundByIdException(String message) {
		super(message);
	}

}
