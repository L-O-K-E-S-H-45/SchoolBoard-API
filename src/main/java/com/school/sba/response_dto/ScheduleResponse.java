package com.school.sba.response_dto;

import java.time.Duration;
import java.time.LocalTime;

import org.springframework.stereotype.Component;

import com.school.sba.entities.School;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ScheduleResponse {
	
	private int scheduleId;
	private LocalTime opensAt;
	private LocalTime closesAt;
	private int classHoursPerDay;
	private int classHourLengthInMinutes;
	private LocalTime breakTime;
	private int breakLengthInMinutes;
	private LocalTime lunchTime;
	private int lunchLengthInMinutes;
	
}
