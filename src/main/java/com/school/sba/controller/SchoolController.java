package com.school.sba.controller;

import java.util.List;

import javax.xml.transform.ErrorListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.school.sba.request_dto.SchoolRequest;
import com.school.sba.response_dto.SchoolResponse;
import com.school.sba.service.SchoolService;
import com.school.sba.utility.ResponseStructure;

import jakarta.validation.Valid;

@RestController
public class SchoolController {
	
	@Autowired
	private SchoolService schoolService;
	
	@PostMapping("/users/{userId}/schools")
	public ResponseEntity<ResponseStructure<SchoolResponse>> saveschool(@PathVariable int userId, @RequestBody @Valid SchoolRequest schoolRequest){
		return schoolService.saveSchool(userId,schoolRequest);
	}
	
	@PutMapping("/schools/{schoolId}")
	public ResponseEntity<ResponseStructure<SchoolResponse>> updateSchool(@RequestBody SchoolRequest request,
			@PathVariable	int schoolId	){
		return schoolService.updateSchool(request, schoolId);
	}
	
	@PutMapping("/schools/{schoolId}")
	public ResponseEntity<ResponseStructure<SchoolResponse>> findSchoolById(@PathVariable int schoolId){
		return schoolService.findSchoolById(schoolId);
	}
	
	@GetMapping("/schools")
	public ResponseEntity<ResponseStructure<List<SchoolResponse>>> fetchAllSchools(){
		return schoolService.fetchAllSchools();
	}
	
	@DeleteMapping("/schools/{schoolId}")
	public ResponseEntity<ResponseStructure<SchoolResponse>> deleteSchool(@PathVariable int schoolId){
		return schoolService.deleteSchool(schoolId);
	}

}





