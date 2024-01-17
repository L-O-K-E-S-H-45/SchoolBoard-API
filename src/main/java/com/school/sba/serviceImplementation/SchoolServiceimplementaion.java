package com.school.sba.serviceImplementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.school.sba.entities.School;
import com.school.sba.exception.SchoolNotfoundByIdException;
import com.school.sba.repository.SchoolRepository;
import com.school.sba.request_dto.SchoolRequest;
import com.school.sba.response_dto.SchoolResponse;
import com.school.sba.service.SchoolService;
import com.school.sba.utility.ResponseStructure;

@Service
public class SchoolServiceimplementaion implements SchoolService  {
	
	@Autowired
	private SchoolRepository schoolRepo;

	@Override
	public ResponseEntity<ResponseStructure<SchoolResponse>> saveSchool(SchoolRequest request) {
		School school = new School();
		school.setSchoolName(request.getSchoolName());
		school.setContactNo(request.getContactNo());
		school.setEmailId(request.getEmailId());
		school.setAddress(request.getAddress());
		
		school=schoolRepo.save(school);
		
		SchoolResponse response = new SchoolResponse();
		response.setSchoolName(school.getSchoolName());
		response.setContactNo(school.getContactNo());
		response.setEmailId(school.getEmailId());
		response.setAddress(school.getAddress());
		
		ResponseStructure<SchoolResponse> structure = new ResponseStructure<>();
		structure.setStatus(HttpStatus.CREATED.value());
		structure.setMessage("School data saved successfully!!!");
		structure.setData(response);
		
		return new ResponseEntity<ResponseStructure<SchoolResponse>>(structure,HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<ResponseStructure<SchoolResponse>> updateSchool(SchoolRequest request, int schoolId) {
		Optional<School> optionals = schoolRepo.findById(schoolId);
		if (optionals.isPresent()) {
			School school = optionals.get();
			school.setSchoolName(request.getSchoolName());
			school.setContactNo(request.getContactNo());
			school.setEmailId(request.getEmailId());
			school.setAddress(request.getAddress());
			
			school = schoolRepo.save(school);
			
			SchoolResponse response = new SchoolResponse();
			response.setSchoolName(school.getSchoolName());
			response.setContactNo(school.getContactNo());
			response.setEmailId(school.getEmailId());
			response.setAddress(school.getAddress());
			
			ResponseStructure<SchoolResponse> structure = new ResponseStructure<>();
			structure.setStatus(HttpStatus.ACCEPTED.value());
			structure.setMessage("school data updated successfully!!!");
			structure.setData(response);
			
			return new ResponseEntity<ResponseStructure<SchoolResponse>>(structure,HttpStatus.ACCEPTED);
		}
		throw new SchoolNotfoundByIdException("School data did not updated!!!");
	}

	@Override
	public ResponseEntity<ResponseStructure<List<SchoolResponse>>> fetchAllSchools() {
		List<School> scList= schoolRepo.findAll();
		ResponseStructure<List<SchoolResponse>> structure = new ResponseStructure<>();
		List<SchoolResponse> scResList=new ArrayList<>();
		if (!scList.isEmpty()) {
			for (School school : scList) {
				SchoolResponse response = new SchoolResponse();
				response.setSchoolName(school.getSchoolName());
				response.setContactNo(school.getContactNo());
				response.setEmailId(school.getEmailId());
				response.setAddress(school.getAddress());
				
				scResList.add(response);
			}
			
			structure.setStatus(HttpStatus.FOUND.value());
			structure.setMessage("Schools list found!!!");
			structure.setData(scResList);
			return new ResponseEntity<ResponseStructure<List<SchoolResponse>>>(structure,HttpStatus.FOUND);
			
		}
		structure.setStatus(HttpStatus.NOT_FOUND.value());
		structure.setMessage("Schools list is empty!!!");
		structure.setData(scResList);
		return new ResponseEntity<ResponseStructure<List<SchoolResponse>>>(structure,HttpStatus.NOT_FOUND);
	}

	@Override
	public ResponseEntity<ResponseStructure<SchoolResponse>> deleteSchool(int schoolId) {
		Optional<School> optional = schoolRepo.findById(schoolId);
		if (optional.isPresent()) {
			School school = optional.get();
			
			schoolRepo.delete(school);
			
			SchoolResponse response = new SchoolResponse();
			response.setSchoolName(school.getSchoolName());
			response.setContactNo(school.getContactNo());
			response.setEmailId(school.getEmailId());
			response.setEmailId(school.getEmailId());
			
			ResponseStructure<SchoolResponse> structure = new ResponseStructure<>();
			structure.setStatus(HttpStatus.FOUND.value());
			structure.setMessage("School data deleted successfullY!!!");
			structure.setData(response);
			
			return new ResponseEntity<ResponseStructure<SchoolResponse>>(structure,HttpStatus.FOUND);
		}
		throw new SchoolNotfoundByIdException("School data did not deleted!!!");
	}

}






