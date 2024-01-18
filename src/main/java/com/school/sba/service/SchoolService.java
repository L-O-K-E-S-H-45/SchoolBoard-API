package com.school.sba.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.school.sba.request_dto.SchoolRequest;
import com.school.sba.response_dto.SchoolResponse;
import com.school.sba.utility.ResponseStructure;

public interface SchoolService {
	
	ResponseEntity<ResponseStructure<SchoolResponse>> saveSchool(int userId, SchoolRequest schoolRequest);
	
	ResponseEntity<ResponseStructure<SchoolResponse>> updateSchool(SchoolRequest request, int schoolId);
	
	ResponseEntity<ResponseStructure<List<SchoolResponse>>> fetchAllSchools();
	
	ResponseEntity<ResponseStructure<SchoolResponse>> deleteSchool(int schoolId);

	ResponseEntity<ResponseStructure<SchoolResponse>> findSchoolById(int schoolId);

}
