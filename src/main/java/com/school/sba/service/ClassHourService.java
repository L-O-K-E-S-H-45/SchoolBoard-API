package com.school.sba.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.school.sba.request_dto.ClassHourRequest;
import com.school.sba.request_dto.ExcelRequestDto;
import com.school.sba.response_dto.ClassHourResponse;
import com.school.sba.utility.ResponseStructure;

public interface ClassHourService {

	ResponseEntity<ResponseStructure<String>> generateClassHours(int programId, ClassHourRequest classHourRequest);

	ResponseEntity<ResponseStructure<List<ClassHourResponse>>> updateClassHour(List<ClassHourRequest> classHourRequests);

	ResponseEntity<ResponseStructure<String>> autoRepeatTimeTable(int programId);

	ResponseEntity<ResponseStructure<String>> writeToExcell(int programId, ExcelRequestDto excelRequestDto);

	ResponseEntity<?> writeToExcel(int programId, LocalDate fromDate, LocalDate toDate, MultipartFile file) throws IOException;

}
