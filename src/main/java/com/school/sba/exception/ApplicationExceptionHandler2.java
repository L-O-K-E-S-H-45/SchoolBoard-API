package com.school.sba.exception;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.school.sba.utility.ErrorStructure;

//@RestControllerAdvice
public class ApplicationExceptionHandler2 extends ResponseEntityExceptionHandler {
	
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, 
			HttpStatus status, WebRequest request	){
		List<ObjectError> allErrors=ex.getAllErrors();
		HashMap<String, String> errors = new HashMap<>();
		for (ObjectError error : allErrors) {
			FieldError fieldError = (FieldError) error;
			String errormessage=fieldError.getDefaultMessage();
			String fieldName=fieldError.getField();
			errors.put(fieldName, errormessage);
		}
		return new ResponseEntity<Object>(errors, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorStructure> schoolNotFoundById(SchoolNotfoundByIdException ex){
		ErrorStructure  structure = new ErrorStructure();
		structure.setStatus(HttpStatus.NOT_FOUND.value());
		structure.setMessage(ex.getMessage());
		structure.setRootCause("School does not exist with requested Id!!!");
		
		return new ResponseEntity<ErrorStructure>(structure,HttpStatus.NOT_FOUND);
	}
	
	public ResponseEntity<ErrorStructure> scheduleNotFoundById(ScheduleNotFoundByIdException ex){
		ErrorStructure structure = new ErrorStructure();
		structure.setStatus(HttpStatus.NOT_FOUND.value());
		structure.setMessage(ex.getMessage());
		structure.setRootCause("Schedule does not exist with requested Id!!!");
		
		return new ResponseEntity<ErrorStructure>(structure,HttpStatus.NOT_FOUND);
	}
}
















