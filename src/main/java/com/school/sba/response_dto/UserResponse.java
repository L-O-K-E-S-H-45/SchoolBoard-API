package com.school.sba.response_dto;

import java.util.List;

import org.springframework.stereotype.Component;

import com.school.sba.entities.AcademicProgram;
import com.school.sba.entities.School;
import com.school.sba.entities.Subject;
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
	
//	private School school; // giving infinite loop during response between school & schedule
	
	private List<Integer> academicProgramIds;
	
	private List<Subject> subjects; 

}
