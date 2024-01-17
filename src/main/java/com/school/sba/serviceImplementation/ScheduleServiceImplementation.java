package com.school.sba.serviceImplementation;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.school.sba.entities.Schedule;
import com.school.sba.entities.School;
import com.school.sba.exception.IllegalRequestException;
import com.school.sba.exception.ScheduleNotFoundByIdException;
import com.school.sba.exception.SchoolNotfoundByIdException;
import com.school.sba.repository.ScheduleRepository;
import com.school.sba.repository.SchoolRepository;
import com.school.sba.request_dto.ScheduleRequest;
import com.school.sba.response_dto.ScheduleResponse;
import com.school.sba.service.ScheduleService;
import com.school.sba.utility.ResponseStructure;


@Service
public class ScheduleServiceImplementation implements ScheduleService {
	
	@Autowired
	private ScheduleRepository scheduleRepo;
	
	@Autowired
	private SchoolRepository schoolRepo;
	
	@Autowired
	private ResponseStructure<ScheduleResponse> structure;

	private Schedule mapRequestToScheduleObject(ScheduleRequest scheduleRequest) {
		return Schedule.builder()
				.opensAt(scheduleRequest.getOpensAt())
				.closesAt(scheduleRequest.getClosesAt())
				.classHoursPerDay(scheduleRequest.getClassHoursPerDay())
				.classHourLengthInMilSec(Duration.ofMinutes(scheduleRequest.getClassHourLengthInMinutes()))
				.breakTime(scheduleRequest.getBreakTime())
				.breakLengthInMilSecs(Duration.ofMinutes(scheduleRequest.getBreakLengthInMinutes()))
				.lunchTime(scheduleRequest.getLunchTime())
				.lunchLengthInMilSecs(Duration.ofMinutes(scheduleRequest.getLunchLengthInMinutes()))
				.build();
	}

	private ScheduleResponse mapScheduleObjectToScheduleResponse(Schedule schedule) {
		return ScheduleResponse.builder()
				.scheduleId(schedule.getScheduleId())
				.opensAt(schedule.getOpensAt())
				.closesAt(schedule.getClosesAt())
				.classHoursPerDay(schedule.getClassHoursPerDay())
				.classHourLengthInMinutes((int)schedule.getClassHourLengthInMilSec().toMinutes())
				.breakTime(schedule.getBreakTime())
				.breakLengthInMinutes((int)schedule.getBreakLengthInMilSecs().toMinutes())
				.lunchTime(schedule.getLunchTime())
				.lunchLengthInMinutes((int)schedule.getLunchLengthInMilSecs().toMinutes())
				.build();
	}
	
	@Override
	public ResponseEntity<ResponseStructure<ScheduleResponse>> saveSchedule(int schoolId,
			ScheduleRequest scheduleRequest) {
		return schoolRepo.findById(schoolId)
				.map(school->{
					if (school.getSchedule()==null) {
						Schedule schedule = mapRequestToScheduleObject(scheduleRequest);
						schedule.setSchool(school);
						schedule = scheduleRepo.save(schedule);
						school.setSchedule(schedule);
						schoolRepo.save(school);
						structure.setStatus(HttpStatus.CREATED.value());
						structure.setMessage("School Schedule for "+school.getSchoolName()+" saved successfully!!!");
						structure.setData(mapScheduleObjectToScheduleResponse(schedule));
						return new ResponseEntity<ResponseStructure<ScheduleResponse>>(structure,HttpStatus.CREATED);
					} else
						throw new IllegalRequestException("Failed to save Schedule!!!");
				}).orElseThrow(()-> new SchoolNotfoundByIdException("Failed to save Schedule!!!"));
	}

	@Override
	public ResponseEntity<ResponseStructure<ScheduleResponse>> findScheduleBySchoolId(int schoolId) {
		return schoolRepo.findById(schoolId)
				.map(school->{
					structure.setStatus(HttpStatus.FOUND.value());
					structure.setMessage("Schedule for "+school.getSchoolName()+" found successfully!!!");
					structure.setData(mapScheduleObjectToScheduleResponse(school.getSchedule()));
					return new ResponseEntity<ResponseStructure<ScheduleResponse>>(structure,HttpStatus.FOUND);
				})
				.orElseThrow(()-> new SchoolNotfoundByIdException("Failed to find Schedule!!!"));
	}

	@Override
	public ResponseEntity<ResponseStructure<ScheduleResponse>> updateScheduleByScheduleId(int scheduleId,
			ScheduleRequest scheduleRequest) {
		return scheduleRepo.findById(scheduleId)
				.map(schedule->{
					Schedule schedule1 = mapRequestToScheduleObject(scheduleRequest);
					schedule1.setScheduleId(scheduleId);
					School school = schedule.getSchool();
					schedule1.setSchool(school);
					schedule1=scheduleRepo.save(schedule1);
					school.setSchedule(schedule1);
					schoolRepo.save(school);
					structure.setStatus(HttpStatus.OK.value());
					structure.setMessage("Schedule updated successfully!!!");
					structure.setData(mapScheduleObjectToScheduleResponse(schedule1));
					return new ResponseEntity<ResponseStructure<ScheduleResponse>>(structure,HttpStatus.OK);
					
				})
				.orElseThrow(()-> new ScheduleNotFoundByIdException("Failed to Update Schedule!!!"));
	}


}






