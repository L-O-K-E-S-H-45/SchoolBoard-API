package com.school.sba.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.school.sba.entities.School;

public interface SchoolRepository extends JpaRepository<School, Integer> {
	
	List<School> findByIsDeletedTrue();

}
