package com.school.sba.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.school.sba.dtos.SchoolRequest;
import com.school.sba.dtos.SchoolResponse;
import com.school.sba.utility.ResponseStructure;

public interface SchoolService {
	
	ResponseEntity<ResponseStructure<SchoolResponse>> saveSchool(SchoolRequest request);
	
	ResponseEntity<ResponseStructure<SchoolResponse>> updateSchool(SchoolRequest request, int schoolId);
	
	ResponseEntity<ResponseStructure<List<SchoolResponse>>> fetchAllSchools();
	
	ResponseEntity<ResponseStructure<SchoolResponse>> deleteSchool(int schoolId);

}
