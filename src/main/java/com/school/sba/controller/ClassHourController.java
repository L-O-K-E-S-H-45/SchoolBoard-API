package com.school.sba.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.school.sba.request_dto.ClassHourRequest;
import com.school.sba.response_dto.ClassHourResponse;
import com.school.sba.service.ClassHourService;
import com.school.sba.utility.ResponseStructure;

@RestController
public class ClassHourController {

	@Autowired
	private ClassHourService classHourService;
	
	@PostMapping("/academic-program/{programId}/class-hours")
	public ResponseEntity<ResponseStructure<String>> generateClassHours(
			@PathVariable int programId, @RequestBody ClassHourRequest classHourRequest){
		return classHourService.generateClassHours(programId,classHourRequest);
	}
	
	@PutMapping("/class-hours")
	public ResponseEntity<ResponseStructure<List<ClassHourResponse>>> updateClassHour(
			@RequestBody List<ClassHourRequest> classHourRequests){
		return classHourService.updateClassHour(classHourRequests);
	}
	
	@PostMapping("/academic-program/{programId}/autorepeat-schedule")
	public ResponseEntity<ResponseStructure<String>> autoRepeatTimeTable(@PathVariable int programId){
		return classHourService.autoRepeatTimeTable(programId);
	}
}






