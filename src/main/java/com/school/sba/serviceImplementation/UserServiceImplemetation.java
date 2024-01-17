package com.school.sba.serviceImplementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.school.sba.entities.User;
import com.school.sba.enums.UserRole;
import com.school.sba.exception.UnAuthourizedRegistrationException;
import com.school.sba.exception.UserNotFoundByIdException;
import com.school.sba.repository.UserRepository;
import com.school.sba.request_dto.UserRequest;
import com.school.sba.response_dto.UserResponse;
import com.school.sba.service.UserService;
import com.school.sba.utility.ResponseStructure;

@Service
public class UserServiceImplemetation implements UserService {
	/*
	 * the @RequestBody annotation is used to bind the incoming JSON request body to the User object.
	 */
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private ResponseStructure<UserResponse> structure;
	
	private User mapRequestToUserObject(UserRequest userRequest) {
		return User.builder()
				.userName(userRequest.getUserName())
				.firstName(userRequest.getFirstName())
				.lastName(userRequest.getLastName())
				.contactNo(userRequest.getContactNo())
				.email(userRequest.getEmail())
				.password(userRequest.getPassword())
				.userRole(userRequest.getUserRole())
				.build();
	}
	
	private UserResponse mapUserObjectToUserResponse(User user) {
		return UserResponse.builder()
				.userId(user.getUserId())
				.userName(user.getUserName())
				.firstName(user.getFirstName())
				.lastName(user.getLastName())
				.contactNo(user.getContactNo())
				.email(user.getEmail())
				.userRole(user.getUserRole())
				.school(user.getSchool())
				.build();
	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> saveUser(UserRequest userRequest) {
//		if (userRequest.getUserRole()==UserRole.ADMIN  && 
//				userRepo.findByUserRole(userRequest.getUserRole()).size()==1) {
//			throw new UnAuthourizedRegistrationException("Failed to save User!!!");
//		}
		
		if (userRequest.getUserRole()==UserRole.ADMIN  && 
				userRepo.existsByUserRole(userRequest.getUserRole())) {
			throw new UnAuthourizedRegistrationException("Failed to save User!!!");
		}
		
		User user = userRepo.save(mapRequestToUserObject(userRequest));
		
		structure.setStatus(HttpStatus.CREATED.value());
		structure.setMessage("User saved successfully!!!");
		structure.setData(mapUserObjectToUserResponse(user));
		
		return new ResponseEntity<ResponseStructure<UserResponse>>(structure,HttpStatus.CREATED);
		
	}

	@Override
	public ResponseEntity<ResponseStructure<List<UserResponse>>> findAllUsers() {
		ResponseStructure<List<UserResponse>> structure = new ResponseStructure<>();
		List<UserResponse> responses = new ArrayList<>();
		List<User> users = userRepo.findAll();
		if (!users.isEmpty()) {
			users.forEach((user)->{
				if (user.isDeleted()==false) {
					UserResponse response = mapUserObjectToUserResponse(user);
					responses.add(response);
				}
			});
			structure.setStatus(HttpStatus.FOUND.value());
			structure.setMessage("Users found successfully!!!");
			structure.setData(responses);
			return new ResponseEntity<ResponseStructure<List<UserResponse>>>(structure,HttpStatus.FOUND);
		} else {
//			throw new UsersNotFoundException("Users not found!!!");
			structure.setStatus(HttpStatus.NOT_FOUND.value());
			structure.setMessage("Users not found!!!!");
			structure.setData(responses);
			return new ResponseEntity<ResponseStructure<List<UserResponse>>>(structure,HttpStatus.NOT_FOUND);
		}
	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> deleteUser(int userId) {
		User user = new User();
		User user1 = userRepo.findById(userId)
				.orElseThrow(()-> new UserNotFoundByIdException("Failed to delete User!!!"));
		if(user1.isDeleted()==false) {
			user.setUserId(user1.getUserId());
			user.setUserName(user1.getUserName());
			user.setFirstName(user1.getFirstName());
			user.setLastName(user1.getLastName());
			user.setContactNo(user1.getContactNo());
			user.setEmail(user1.getEmail());
			user.setPassword(user1.getPassword());
			user.setUserRole(user1.getUserRole());
			user.setDeleted(true);
			user1 = userRepo.save(user);
		
			structure.setStatus(HttpStatus.OK.value());
			structure.setMessage("User deleted successfully!!!");
			structure.setData(mapUserObjectToUserResponse(user1));
			return new ResponseEntity<ResponseStructure<UserResponse>>(structure,HttpStatus.OK);
		} else {
			throw new UserNotFoundByIdException("Failed to delete User!!!");
		}
	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> findUserById(int userId) {
		User user = userRepo.findById(userId).orElseThrow(
				()-> new UserNotFoundByIdException("Failed to find User!!!"));
		if (user.isDeleted()==true)
			throw new UserNotFoundByIdException("Failed to find User!!!");
		
		structure.setStatus(HttpStatus.FOUND.value());
		structure.setMessage("User found successfully!!!");
		structure.setData(mapUserObjectToUserResponse(user));
		return new ResponseEntity<ResponseStructure<UserResponse>>(structure,HttpStatus.FOUND);
	}

}









