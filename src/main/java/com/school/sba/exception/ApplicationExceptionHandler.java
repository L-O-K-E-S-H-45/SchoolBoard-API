package com.school.sba.exception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
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

@RestControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {
	
	private ResponseEntity<Object> structure(HttpStatus status, String message, Object rootCause){
		return new ResponseEntity<Object>(Map.of(
				"rootCause",rootCause,
				"message",message,
				"status",status.value()
				),status);
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		List<ObjectError> allErrors = ex.getAllErrors();
		Map<String, String> errors = new HashMap<>();
		allErrors.forEach((error)->{
			errors.put(((FieldError) error).getField(), ((FieldError) error).getDefaultMessage());
		});
		return structure(HttpStatus.BAD_REQUEST,"Failed to save data", errors);
	}
	
	@ExceptionHandler(UnAuthourizedRegistrationException.class)
	public ResponseEntity<Object> handleUnAuthourizedRegistrationException(UnAuthourizedRegistrationException ex){
		return structure(HttpStatus.NOT_ACCEPTABLE, ex.getMessage(), "Admin already exist!!!");
	}
	
	@ExceptionHandler(UserNotFoundByIdException.class)
	public ResponseEntity<Object> handleUserNotFoundByIdException(UserNotFoundByIdException ex){
		return structure(HttpStatus.NOT_FOUND, ex.getMessage(), "User does not exist for requested ID!!!");
	}
	
	@ExceptionHandler(UnAuthourizeduserException.class)
	public ResponseEntity<Object> handleUnAuthourizeduserException(UnAuthourizeduserException ex){
		return structure(HttpStatus.UNAUTHORIZED, ex.getMessage(), "User is not a ADMIN");
	}
	
	@ExceptionHandler(SchoolAlreadyExistException.class)
	public ResponseEntity<Object> handleSchoolAlreadyExistException(SchoolAlreadyExistException ex){
		return structure(HttpStatus.NOT_ACCEPTABLE, ex.getMessage(), "School already exists!!!");
	}
	
	@ExceptionHandler(IllegalRequestException.class)
	public ResponseEntity<Object> handleIlligalRequestException(IllegalRequestException ex){
		return structure(HttpStatus.BAD_REQUEST, ex.getMessage(), "Illigal Request!!!");
	}
	
	@ExceptionHandler(SchoolNotfoundByIdException.class)
	public ResponseEntity<Object> handleSchoolNotfoundByIdException(SchoolNotfoundByIdException ex){
		return structure(HttpStatus.NOT_FOUND, ex.getMessage(), "School does not exist for requested Id!!!");
	}
	
	@ExceptionHandler(ScheduleNotFoundByIdException.class)
	public ResponseEntity<Object> handleScheduleNotFoundByIdException(ScheduleNotFoundByIdException ex){
		return structure(HttpStatus.NOT_FOUND, ex.getMessage(), "Schedule does not exist for requested Id!!!");
	}
	
	@ExceptionHandler(AcademicProgramNotFoundByIdException.class)
	public ResponseEntity<Object> handleAcademicProgramNotFoundByIdException(AcademicProgramNotFoundByIdException ex){
		return structure(HttpStatus.NOT_FOUND, ex.getMessage(), "Academic-Program does not exist for requested Id!!!");
	}

}











