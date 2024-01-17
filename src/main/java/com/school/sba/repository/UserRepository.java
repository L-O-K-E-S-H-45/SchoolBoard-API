package com.school.sba.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.school.sba.entities.User;
import com.school.sba.enums.UserRole;

public interface UserRepository extends JpaRepository<User, Integer> {
	
	List<User> findByUserRole(UserRole userRole);
	
	User findByEmail(String userEmail);
	
	User findByContactNo(long suerContactNo);

}
