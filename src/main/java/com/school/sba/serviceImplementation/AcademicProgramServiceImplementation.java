package com.school.sba.serviceImplementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.school.sba.entities.AcademicProgram;
import com.school.sba.entities.Subject;
import com.school.sba.enums.ProgramType;
import com.school.sba.exception.IllegalRequestException;
import com.school.sba.exception.SchoolNotfoundByIdException;
import com.school.sba.repository.AcademicProgramRepository;
import com.school.sba.repository.SchoolRepository;
import com.school.sba.request_dto.AcademicProgramRequest;
import com.school.sba.response_dto.AcademicProgramResponse;
import com.school.sba.service.AcademicProgramService;
import com.school.sba.utility.ResponseStructure;

@Service
public class AcademicProgramServiceImplementation implements AcademicProgramService {
	
	@Autowired
	private AcademicProgramRepository academicProgramRepo;
	
	@Autowired
	private SchoolRepository schoolRepo;
	
	@Autowired
	private ResponseStructure<AcademicProgramResponse> structure;

	private AcademicProgram mapRequestToAcademicProgram(AcademicProgramRequest academicProgramRequest) {
		return AcademicProgram.builder()
				.programType(academicProgramRequest.getProgramType())
				.programName(academicProgramRequest.getProgramName())
				.programBeginsAt(academicProgramRequest.getProgramBeginsAt())
				.programEndsAt(academicProgramRequest.getProgramEndsAt())
				.build();
	}

	public AcademicProgramResponse mapObjectToAcademicProgramResponse(AcademicProgram academicProgram) {
		List<String> subjectList = new ArrayList<>();
		if (academicProgram.getSubjects()!=null) {
			academicProgram.getSubjects().forEach(subject->{
				subjectList.add(subject.getSubjectName());
			});
		}
		
		return AcademicProgramResponse.builder()
				.programId(academicProgram.getProgramId())
				.programType(academicProgram.getProgramType())
				.programName(academicProgram.getProgramName())
				.programBeginsAt(academicProgram.getProgramBeginsAt())
				.programEndsAt(academicProgram.getProgramEndsAt())
				.subjects(subjectList)
				.build();
	}

	@Override
	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> saveAcademicProgram(int schoolId,
			 AcademicProgramRequest academicProgramRequest) {
		return schoolRepo.findById(schoolId)
				.map(school->{
					if ((academicProgramRequest.getProgramType()==(ProgramType.PRIMARY)  && 
							academicProgramRequest.getProgramName().charAt(0)>='1' && 
							academicProgramRequest.getProgramName().charAt(0)<='7' &&
							!(academicProgramRequest.getProgramName().charAt(1)>='0' && 
							academicProgramRequest.getProgramName().charAt(1)<='9')) ||
						(academicProgramRequest.getProgramType().equals(ProgramType.SECONDARY) &&
							(academicProgramRequest.getProgramName().charAt(0)=='8' || 
							academicProgramRequest.getProgramName().charAt(0)=='9'  ||
							academicProgramRequest.getProgramName().substring(0, 2).equals("10"))) ||
						(academicProgramRequest.getProgramType().equals(ProgramType.HIGHER) && 
							(academicProgramRequest.getProgramName().substring(0, 2).equals("11") ||
							academicProgramRequest.getProgramName().substring(0, 2).equals("12"))	)	) {
						
					AcademicProgram academicProgram = mapRequestToAcademicProgram(academicProgramRequest);
					academicProgram.setSchool(school);
					academicProgram = academicProgramRepo.save(academicProgram);
					school.setSchoolId(schoolId);
					school.setAcademicPrograms(academicProgramRepo.findAll());
					school = schoolRepo.save(school);
					structure.setStatus(HttpStatus.CREATED.value());
					structure.setMessage("AcademicProgram saved successfully!!!");
					structure.setData(mapObjectToAcademicProgramResponse(academicProgram));
					return new ResponseEntity<ResponseStructure<AcademicProgramResponse>>(structure,HttpStatus.CREATED);
					}
					else 
						throw new IllegalRequestException("Failed to save AcademicProgram b/z Invalid ProgramName!!!");
				})
				.orElseThrow(()-> new SchoolNotfoundByIdException("Failed to save AcademicProgram!!!"));
	}

	@Override
	public ResponseEntity<ResponseStructure<List<AcademicProgramResponse>>> findAllAcademicProgramBySchoolId(
			int schoolId) {
		return schoolRepo.findById(schoolId)
				.map(school->{
					ResponseStructure<List<AcademicProgramResponse>> structure = new ResponseStructure<>();
					List<AcademicProgramResponse> academicProgramResponses=new ArrayList<>();
					List<AcademicProgram> academicPrograms=school.getAcademicPrograms();
					if (!academicPrograms.isEmpty()) {
						academicPrograms.forEach(academicProgram->{
							academicProgramResponses.add(mapObjectToAcademicProgramResponse(academicProgram));
						});
						structure.setStatus(HttpStatus.FOUND.value());
						structure.setMessage("Academic-Program list found successfully!!!");
						structure.setData(academicProgramResponses);
						return new ResponseEntity<ResponseStructure<List<AcademicProgramResponse>>>(structure,HttpStatus.FOUND);
					} else {
						structure.setStatus(HttpStatus.NOT_FOUND.value());
						structure.setMessage("Academic-Program list is empty!!!");
						structure.setData(academicProgramResponses);
						return new ResponseEntity<ResponseStructure<List<AcademicProgramResponse>>>(structure,HttpStatus.NOT_FOUND);
					}
				})
				.orElseThrow(()-> new SchoolNotfoundByIdException("Failed to find all AcademicProgram!!!"));
	}


}






