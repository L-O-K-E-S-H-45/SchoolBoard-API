package com.school.sba.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.school.sba.request_dto.ScheduleRequest;
import com.school.sba.response_dto.ScheduleResponse;
import com.school.sba.service.ScheduleService;
import com.school.sba.utility.ResponseStructure;

@RestController
public class ScheduleController {
	
	@Autowired
	private ScheduleService scheduleService;
	
	@PostMapping("/schools/{schoolId}/schedules")
	public ResponseEntity<ResponseStructure<ScheduleResponse>> saveSchedule(@PathVariable int schoolId, @RequestBody ScheduleRequest scheduleRequest){
		return scheduleService.saveSchedule(schoolId,scheduleRequest);
	}
	
	@GetMapping("/schools/{schoolId}/schedules")
	public ResponseEntity<ResponseStructure<ScheduleResponse>> findScheduleBySchoolId(@PathVariable int schoolId){
		return scheduleService.findScheduleBySchoolId(schoolId);
	}
	
	@PutMapping("/schedules/{scheduleId}")
	public ResponseEntity<ResponseStructure<ScheduleResponse>> updateScheduleByScheduleId(@PathVariable int scheduleId, @RequestBody ScheduleRequest scheduleRequest){
		return scheduleService.updateScheduleByScheduleId(scheduleId,scheduleRequest);
	}
	

}
