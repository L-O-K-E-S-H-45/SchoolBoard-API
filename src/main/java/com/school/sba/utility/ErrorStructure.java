package com.school.sba.utility;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
public class ErrorStructure {
	
	private int status;
	private String message;
	private String rootCause;

}
