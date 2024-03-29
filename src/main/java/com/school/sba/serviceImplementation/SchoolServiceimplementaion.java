package com.school.sba.serviceImplementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.school.sba.entities.AcademicProgram;
import com.school.sba.entities.ClassHour;
import com.school.sba.entities.Schedule;
import com.school.sba.entities.School;
import com.school.sba.entities.User;
import com.school.sba.enums.UserRole;
import com.school.sba.exception.IllegalRequestException;
import com.school.sba.exception.SchoolAlreadyExistException;
import com.school.sba.exception.SchoolNotfoundByIdException;
import com.school.sba.exception.UnAuthourizedUserException;
import com.school.sba.exception.UserNotFoundByIdException;
import com.school.sba.repository.AcademicProgramRepository;
import com.school.sba.repository.ClassHourRepository;
import com.school.sba.repository.ScheduleRepository;
import com.school.sba.repository.SchoolRepository;
import com.school.sba.repository.UserRepository;
import com.school.sba.request_dto.SchoolRequest;
import com.school.sba.response_dto.SchoolResponse;
import com.school.sba.service.SchoolService;
import com.school.sba.utility.ResponseStructure;

@Service
public class SchoolServiceimplementaion implements SchoolService  {
	
	@Autowired
	private SchoolRepository schoolRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private ScheduleRepository scheduleRepo;
	
	@Autowired
	private AcademicProgramRepository academicProgramRepo;
	
	@Autowired
	private ClassHourRepository classHourRepo;
	
	@Autowired
	private ResponseStructure<SchoolResponse> structure;
	
	private School mapRequestToSchoolObject(SchoolRequest schoolRequest) {
		return School.builder()
				.schoolName(schoolRequest.getSchoolName())
				.schoolContactNo(schoolRequest.getSchoolContactNo())
				.schoolEmail(schoolRequest.getSchoolEmail())
				.schoolAddress(schoolRequest.getSchoolAddress())
				.build();
	}
	
	private SchoolResponse mapSchoolObjectToSchoolResponse(School school) {
		return SchoolResponse.builder()
				.schoolId(school.getSchoolId())
				.schoolName(school.getSchoolName())
				.schoolContactNo(school.getSchoolContactNo())
				.schoolEmail(school.getSchoolEmail())
				.schoolAddress(school.getSchoolAddress())
				.build();
	}

	@Override
	public ResponseEntity<ResponseStructure<SchoolResponse>> saveSchool(SchoolRequest schoolRequest) {
		String userName=SecurityContextHolder.getContext().getAuthentication().getName();
		return userRepo.findByUserName(userName)
				.map(u->{
					if (u.getUserRole().equals(UserRole.ADMIN)) {
						if (u.getSchool()==null) {
							School school = mapRequestToSchoolObject(schoolRequest);
							school = schoolRepo.save(school);
							u.setSchool(school);
							userRepo.save(u);
							structure.setStatus(HttpStatus.CREATED.value());
							structure.setMessage("School saved successfully!!!");
							structure.setData(mapSchoolObjectToSchoolResponse(school));
							return new ResponseEntity<ResponseStructure<SchoolResponse>>(structure,HttpStatus.CREATED);
							} else 
							throw new SchoolAlreadyExistException("Failed to save School!!!");
						
					} else
						throw new UnAuthourizedUserException("Failed to save School!!!");
				})
				.orElseThrow(()-> new UserNotFoundByIdException("Failed to save School!!!"));
		
	}

	@Override
	public ResponseEntity<ResponseStructure<SchoolResponse>> findSchoolById(int schoolId) {
		return schoolRepo.findById(schoolId)
				.map(school->{
					structure.setStatus(HttpStatus.FOUND.value());
					structure.setMessage("School found successfully!!!");
					structure.setData(mapSchoolObjectToSchoolResponse(school));
					return new ResponseEntity<ResponseStructure<SchoolResponse>>(structure,HttpStatus.FOUND);
				})
				.orElseThrow(()-> new SchoolNotfoundByIdException("School did not found!!!"));
	}

	@Override
	public ResponseEntity<ResponseStructure<SchoolResponse>> updateSchool(SchoolRequest request, int schoolId) {
		return schoolRepo.findById(schoolId)
				.map(school->{
					School school1=mapRequestToSchoolObject(request);
					school1.setSchoolId(schoolId);
					school1=schoolRepo.save(school1);
					structure.setStatus(HttpStatus.OK.value());
					structure.setMessage("School updated successfully!!!");
					structure.setData(mapSchoolObjectToSchoolResponse(school1));
					return new ResponseEntity<ResponseStructure<SchoolResponse>>(structure,HttpStatus.OK);
				})
				.orElseThrow(()-> new SchoolNotfoundByIdException("School data did not updated!!!"));

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
				response.setSchoolId(school.getSchoolId());
				response.setSchoolContactNo(school.getSchoolContactNo());
				response.setSchoolEmail(school.getSchoolEmail());
				response.setSchoolAddress(school.getSchoolAddress());
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
		return schoolRepo.findById(schoolId)
				.map(school->{
					if (school.isDeleted())
						throw new IllegalRequestException("School Already deleted!!!");
					school.setDeleted(true);
					school = schoolRepo.save(school);
					structure.setStatus(HttpStatus.OK.value());
					structure.setMessage("School deleted successfully!!!");
					structure.setData(mapSchoolObjectToSchoolResponse(school));
					return new ResponseEntity<ResponseStructure<SchoolResponse>>(structure,HttpStatus.OK);
				})
				.orElseThrow(()-> new SchoolNotfoundByIdException("School is not deleted!!!"));

	}
	
	
	public void permanentlyDeleteSchool() {
		System.out.println("----permanentlyDeleteSchool() -> STARTS --------");
		List<School> schools = schoolRepo.findByIsDeletedTrue();
		System.out.println("SchoolSize => "+ schools.size());
		
		schools.forEach(school->{
			
			List<AcademicProgram> academicPrograms = school.getAcademicPrograms();
			academicPrograms.forEach(program -> classHourRepo.deleteAll(program.getClassHours()));
			academicProgramRepo.deleteAll(academicPrograms);
			
			List<User> users = userRepo.findBySchoolAndUserRoleNot(school, UserRole.ADMIN);
			if (!users.isEmpty()) userRepo.deleteAll(users);
			
			userRepo.findByUserRole(UserRole.ADMIN).forEach(user -> {
				if(user.getSchool() == school) {
					user.setSchool(null);
					userRepo.save(user);
				}
			});
			
			schoolRepo.delete(school);
		});

		System.out.println("----permanentlyDeleteSchool() -> ENDS --------");
	}
	

}






