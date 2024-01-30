package com.school.sba.response_dto;

import java.time.LocalTime;
import java.util.List;

import com.school.sba.enums.ClassStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClassHourResponse {
	
	private int classHourId;
	private int userId;
	private int subjectId;
	private int roomNo;

}
