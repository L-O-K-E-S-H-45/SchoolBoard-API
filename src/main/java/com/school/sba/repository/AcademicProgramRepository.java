package com.school.sba.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.school.sba.entities.AcademicProgram;
import com.school.sba.entities.School;

public interface AcademicProgramRepository extends JpaRepository<AcademicProgram, Integer> {
	
	List<AcademicProgram> findByIsDeletedTrue();
	
	boolean existsBySchool(School school);
	
	List<AcademicProgram> findBySchool(School school);
	
	List<AcademicProgram> findByAutoRepeatScheduleTrue();
	
}
