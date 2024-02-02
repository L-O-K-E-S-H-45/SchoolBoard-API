package com.school.sba.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.school.sba.entities.Schedule;
import com.school.sba.entities.School;

public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {
	
	boolean existsBySchool(School school);

	Schedule findBySchool(School school);
	
}
