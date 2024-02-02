package com.school.sba.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.school.sba.entities.School;
import com.school.sba.entities.User;
import com.school.sba.enums.UserRole;

public interface UserRepository extends JpaRepository<User, Integer> {
	
	Optional<User> findByUserName(String userName);
	
	boolean existsByUserRole(UserRole userRole);
	
	List<User> findByUserRole(UserRole userRole);
	
	User findByEmail(String userEmail);
	
	User findByContactNo(long userContactNo);
	
	//******************
	List<User> findByUserRoleAndAcademicPrograms_ProgramId(UserRole userRole, int programId);
	
	// 
	List<User> findByIsDeletedTrue();
	
	boolean existsBySchool(School school);
	
	List<User> findBySchoolAndUserRoleNot(School school, UserRole userRole);
	
}
