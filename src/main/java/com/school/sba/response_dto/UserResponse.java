package com.school.sba.response_dto;

import org.springframework.stereotype.Component;

import com.school.sba.entities.School;
import com.school.sba.enums.UserRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {

	private int userId;
	private String userName;
	private String firstName;
	private String lastName;
	private long contactNo;
	private String email;
	private UserRole userRole;
	
	private School school;

}
