package com.school.sba.request_dto;

import com.school.sba.enums.UserRole;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {

	@NotNull(message = "UserName cannot be null!!!")
	@NotBlank(message = "UserName cannot be blank!!!")
	@Pattern(regexp = "^[a-zA-Z0-9]+$", message = "UserName must be alphanumeric only")
	private String userName;
	
	@NotNull(message = "User FirstName cannot be null!!!")
	@NotBlank(message = "User FirstName cannot be blank!!!")
	@Pattern(regexp = "^[A-Z][a-zA-Z]*$", message = "User FirstName must be only alphabates & must start with Uppercase")
	private String firstName;
	
	@NotNull(message = "User LasttName cannot be null!!!")
	@NotBlank(message = "User LasttName cannot be blank!!!") 
	@Pattern(regexp = "^[A-Z][a-zA-Z]*$", message = "User LastName must be only alphabates & must start with Uppercase")
	private String lastName;
	
	
	@Min(value = 6000000000L, message = "ContactNo must start with >=6000000000L")
	@Max(value = 9999999999L, message = "ContactNo must not be greater than 10 digit")
	private long contactNo;
	
	@NotNull(message = "User Email cannot be null!!!")
	@NotBlank(message = "User Email cannot be blank!!!")
	@Email(regexp = "[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+\\.[a-z]{2,}", message = "Invalid Email ")
	private String email;
	
	@NotNull(message = "User password cannot be null!!!")
	@NotBlank(message = "User password cannot be blank!!!")
	@Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters") 
	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", message = "Password must"
			+ " contain at least one uppercase letter, one lowercase letter, one number, one special character")
	private String password;
	
	@NotNull(message = "UserRole cannot be null!!!")
//	@NotBlank(message = "UserRole cannot be blank!!!")  //!!! not applicable for enum
//	@Pattern(regexp = "^[A-Z]+$", message = "UserRole must be Uppercase")  //!!! not applicable for enum
	private UserRole userRole;
	
}
