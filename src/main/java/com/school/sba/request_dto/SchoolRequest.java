package com.school.sba.request_dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SchoolRequest {
	
	@NotNull(message = "School Name cannot be null!!")
	@NotBlank(message = "School Name cannot be blank!!")
	@Pattern(regexp = "^([A-Z][a-z]*)(?:\\s[A-Z][a-z]*)$", message = "School Name must start with Uppercaase letter")
	private String schoolName;
	
	@Min(value = 6000000000L, message = "ContactNo must be >=6000000000L")
	@Max(value = 9999999999L, message = "ContactNo must not be greater than 10 digit")
	private long schoolContactNo;
	
	@NotNull(message = "School Email cannot be null!!")
	@NotBlank(message = "School Email cannot be blank!!")
	@Email(regexp = "[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+\\.[a-z]{2,}", message = "Invalid Email ")
	private String schoolEmail;
	
	@NotNull(message = "School Address cannot be null!!")
	@NotBlank(message = "School Address cannot be blank!!")
	private String schoolAddress;
	
}
