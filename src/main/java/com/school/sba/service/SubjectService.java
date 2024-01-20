package com.school.sba.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.school.sba.entities.Subject;
import com.school.sba.request_dto.SubjectRequest;
import com.school.sba.response_dto.AcademicProgramResponse;
import com.school.sba.response_dto.SubjectResponse;
import com.school.sba.utility.ResponseStructure;

public interface SubjectService {

	ResponseEntity<ResponseStructure<AcademicProgramResponse>> saveSubjects(int programId,
			SubjectRequest subjectRequest);

	ResponseEntity<ResponseStructure<AcademicProgramResponse>> save_update_SubjectsToAcademy(int programId,
			SubjectRequest subjectRequest);

	ResponseEntity<ResponseStructure<List<Subject>>> findAllSubjects();

}
