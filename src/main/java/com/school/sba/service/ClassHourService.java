package com.school.sba.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.school.sba.request_dto.ClassHourRequest;
import com.school.sba.response_dto.ClassHourResponse;
import com.school.sba.utility.ResponseStructure;

public interface ClassHourService {

	ResponseEntity<ResponseStructure<String>> generateClassHours(int programId, ClassHourRequest classHourRequest);

	ResponseEntity<ResponseStructure<List<ClassHourResponse>>> updateClassHour(List<ClassHourRequest> classHourRequests);

}
