package com.school.sba.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class SubjectNotFoundByIdException extends RuntimeException {
	
	private String message;

}
