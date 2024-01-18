package com.school.sba.response_dto;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.school.sba.enums.ProgramType;

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
public class AcademicProgramResponse {
	
	private int programId;
	private ProgramType programType;
	private String programName;
	private LocalDate programBeginsAt;
	private LocalDate programEndsAt;

}
