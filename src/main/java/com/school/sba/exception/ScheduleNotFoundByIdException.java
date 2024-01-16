package com.school.sba.exception;

public class ScheduleNotFoundByIdException extends RuntimeException {
	
	private String message;
	public ScheduleNotFoundByIdException(String message) {
		super(message);
	}

}
