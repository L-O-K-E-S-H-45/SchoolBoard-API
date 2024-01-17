package com.school.sba.service;

import org.springframework.http.ResponseEntity;

import com.school.sba.request_dto.ScheduleRequest;
import com.school.sba.response_dto.ScheduleResponse;
import com.school.sba.utility.ResponseStructure;

public interface ScheduleService {

	ResponseEntity<ResponseStructure<ScheduleResponse>> saveSchedule(int schoolId, ScheduleRequest scheduleRequest);

	ResponseEntity<ResponseStructure<ScheduleResponse>> findScheduleBySchoolId(int schoolId);

	ResponseEntity<ResponseStructure<ScheduleResponse>> updateScheduleByScheduleId(int scheduleId,
			ScheduleRequest scheduleRequest);

}
