package com.school.sba.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.school.sba.request_dto.UserRequest;
import com.school.sba.response_dto.UserResponse;
import com.school.sba.utility.ResponseStructure;

public interface UserService {

	ResponseEntity<ResponseStructure<UserResponse>> saveUser(UserRequest userRequest);
	
	ResponseEntity<ResponseStructure<List<UserResponse>>> findAllUsers();

	ResponseEntity<ResponseStructure<UserResponse>> deleteUser(int userId);

	ResponseEntity<ResponseStructure<UserResponse>> findUserById(int userId);

	ResponseEntity<ResponseStructure<UserResponse>> assignSubjectsToTeacher(int subjectId, int userId);

}
