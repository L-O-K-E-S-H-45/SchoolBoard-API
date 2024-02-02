package com.school.sba.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.school.sba.request_dto.AcademicProgramRequest;
import com.school.sba.response_dto.AcademicProgramResponse;
import com.school.sba.response_dto.UserResponse;
import com.school.sba.utility.ResponseStructure;

import jakarta.validation.Valid;

public interface AcademicProgramService {

	ResponseEntity<ResponseStructure<AcademicProgramResponse>> saveAcademicProgram(int schoolId,
			 AcademicProgramRequest academicProgramRequest);

	ResponseEntity<ResponseStructure<List<AcademicProgramResponse>>> findAllAcademicProgramBySchoolId(int schoolId);

	ResponseEntity<ResponseStructure<AcademicProgramResponse>> setUserToAcademyProgram(int programId, int userId);

	ResponseEntity<ResponseStructure<AcademicProgramResponse>> deleteAcademyProgram(int programId);

	ResponseEntity<ResponseStructure<AcademicProgramResponse>> updateAcademicProgram(int programId,
			AcademicProgramRequest academicProgramRequest);

	ResponseEntity<ResponseStructure<AcademicProgramResponse>> setAutoReapeatSchedule(int programId,
			boolean autoRepeatSchedule);

}
