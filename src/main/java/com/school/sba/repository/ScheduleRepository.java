package com.school.sba.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.school.sba.entities.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {

}
