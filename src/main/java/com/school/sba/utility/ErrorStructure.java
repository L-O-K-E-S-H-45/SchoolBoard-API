package com.school.sba.utility;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorStructure {
	
	private int status;
	private String message;
	private String rootCause;

}
