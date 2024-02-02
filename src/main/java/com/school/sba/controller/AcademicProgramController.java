package com.school.sba.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.school.sba.enums.UserRole;
import com.school.sba.request_dto.AcademicProgramRequest;
import com.school.sba.request_dto.SubjectRequest;
import com.school.sba.response_dto.AcademicProgramResponse;
import com.school.sba.response_dto.UserResponse;
import com.school.sba.service.AcademicProgramService;
import com.school.sba.utility.ResponseStructure;

import jakarta.validation.Valid;

@RestController
public class AcademicProgramController {
	
	@Autowired
	private AcademicProgramService academicProgramService;
	
	@PostMapping("/schools/{schoolId}/academic-programs")
	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> saveAcademicProgram(@PathVariable int schoolId,
			@RequestBody @Valid AcademicProgramRequest academicProgramRequest){
		return academicProgramService.saveAcademicProgram(schoolId,academicProgramRequest);
	}
	
	@GetMapping("/schools/{schoolId}/academic-programs")
	public ResponseEntity<ResponseStructure<List<AcademicProgramResponse>>> findAllAcademicProgramBySchoolId(@PathVariable int schoolId){
		return academicProgramService.findAllAcademicProgramBySchoolId(schoolId);
	}

	@PutMapping("/academic-programs/{programId}/users/{userId}")
	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> setUserToAcademyProgram(@PathVariable int programId, @PathVariable int userId){
		return academicProgramService.setUserToAcademyProgram(programId,userId);
	}
	
	@DeleteMapping("/academic-programs/{programId}")
	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> deleteAcademyProgram(@PathVariable int programId){
		return academicProgramService.deleteAcademyProgram(programId);
	}
	
	@PutMapping("/academic-programs/{programId}")
	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> updateAcademicProgram(
			@PathVariable int programId, @RequestBody AcademicProgramRequest academicProgramRequest){
		return academicProgramService.updateAcademicProgram(programId,academicProgramRequest);
	}
	
	@PutMapping("/academic_programs/{programId}")
	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> setAutoReapeatSchedule(
			@PathVariable int programId,@RequestParam("autoRepeatSchedule") boolean autoRepeatSchedule){
		return academicProgramService.setAutoReapeatSchedule(programId,autoRepeatSchedule);
	}
	
}




