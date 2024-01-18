package com.school.sba.request_dto;

import java.time.LocalDate;

import com.school.sba.enums.ProgramType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AcademicProgramRequest {
	
	private ProgramType programType;
	private String programName;
	private LocalDate programBeginsAt;
	private LocalDate programEndsAt;

}
