package com.school.sba.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.school.sba.request_dto.SubjectRequest;
import com.school.sba.response_dto.AcademicProgramResponse;
import com.school.sba.service.SubjectService;
import com.school.sba.utility.ResponseStructure;

@RestController
public class SubjectController {
	
	@Autowired
	private SubjectService subjectService;
	
	@PostMapping("/academic-programs/{programId}/subjects")
	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> saveSubjects(
			@PathVariable int programId, @RequestBody SubjectRequest subjectRequest){
		return subjectService.saveSubjects(programId,subjectRequest);
	}
	
	@PutMapping("/academic-programs/{programId}/subjects")
	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> updateSubjectsToAcademy(
			@PathVariable int programId, @RequestBody SubjectRequest subjectRequest	){
		return subjectService.updateSubjectsToAcademy(programId, subjectRequest);
	}

}
