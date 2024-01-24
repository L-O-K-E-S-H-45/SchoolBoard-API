package com.school.sba.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.school.sba.request_dto.UserRequest;
import com.school.sba.response_dto.UserResponse;
import com.school.sba.utility.ResponseStructure;

import jakarta.validation.Valid;

public interface UserService {


	ResponseEntity<ResponseStructure<UserResponse>> registerAdmin(UserRequest userRequest);

	ResponseEntity<ResponseStructure<UserResponse>> addOtherUser(UserRequest userRequest);
	
	ResponseEntity<ResponseStructure<List<UserResponse>>> findAllUsers();

	ResponseEntity<ResponseStructure<UserResponse>> deleteUser(int userId);

	ResponseEntity<ResponseStructure<UserResponse>> findUserById(int userId);

	ResponseEntity<ResponseStructure<UserResponse>> assignSubjectsToTeacher(int subjectId, int userId);

}
