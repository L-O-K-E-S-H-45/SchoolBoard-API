package com.school.sba.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.school.sba.entities.ClassHour;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.time.LocalDateTime;


public interface ClassHourRepository extends JpaRepository<ClassHour, Integer> {

	boolean existsByRoomNoAndClassBeginsAtBetween(int roomNo, LocalDateTime classHourBeginsAt,LocalDateTime classHourEndsAt);
}
