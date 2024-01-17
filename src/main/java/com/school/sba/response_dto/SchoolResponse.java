package com.school.sba.response_dto;

import org.springframework.stereotype.Component;

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
public class SchoolResponse {
	
	private int schoolId;
	private String schoolName;
	private long schoolContactNo;
	private String schoolEmail;
	private String schoolAddress;

}
