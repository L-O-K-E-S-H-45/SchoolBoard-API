package com.school.sba.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.school.sba.entities.AcademicProgram;
import com.school.sba.entities.ClassHour;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.time.LocalDateTime;

public interface ClassHourRepository extends JpaRepository<ClassHour, Integer> {

	boolean existsByRoomNoAndClassBeginsAtBetween(int roomNo, LocalDateTime classHourBeginsAt,LocalDateTime classHourEndsAt);

	boolean existsByAcademicProgram(AcademicProgram academicProgram);
	
	List<ClassHour> findByAcademicProgram(AcademicProgram academicProgram);
	
	@Query("SELECT ch FROM ClassHour ch WHERE ch.academicProgram = :academicProgram "+
			"ORDER BY ch.classHourId DESC LIMIT :lastNrecords")
	List<ClassHour> findLastNRecordsByAcademicProgram(AcademicProgram academicProgram, int lastNrecords);
}
