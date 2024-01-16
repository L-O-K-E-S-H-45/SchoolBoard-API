package com.school.sba.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SchoolRequest {
	
//	@NotNull(message = "Student Name cannot be Null!!")
	private String schoolName;
	private long contactNo;
	private String emailId;
	private String address;

}
