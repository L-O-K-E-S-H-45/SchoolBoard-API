package com.school.sba.serviceImplementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.school.sba.entities.Subject;
import com.school.sba.exception.AcademicProgramNotFoundByIdException;
import com.school.sba.exception.IllegalRequestException;
import com.school.sba.repository.AcademicProgramRepository;
import com.school.sba.repository.SubjectRepository;
import com.school.sba.request_dto.SubjectRequest;
import com.school.sba.response_dto.AcademicProgramResponse;
import com.school.sba.service.SubjectService;
import com.school.sba.utility.ResponseStructure;

@Service
public class SubjectServiceImplementation implements SubjectService {
	
	@Autowired
	private SubjectRepository subjectRepo;
	
	@Autowired
	private AcademicProgramRepository academicProgramRepo;
	
	@Autowired
	private ResponseStructure<AcademicProgramResponse> structure;
	
	@Autowired
	private AcademicProgramServiceImplementation academicProgramServiceImpl;
	
	@Override
	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> saveSubjects(int programId,
			SubjectRequest subjectRequest) {
		return academicProgramRepo.findById(programId) 
				.map(academicProgram->{  // found academic program
					List<Subject> subjects = new ArrayList<>();
					subjectRequest.getSubjectNames().forEach(name->{
						Subject subject1=subjectRepo.findBySubjectName(name.toLowerCase()).map(subject->{
							return subject;
						}).orElseGet(()->{
							Subject subject = new Subject();
							subject.setSubjectName(name.toLowerCase());
							subject=subjectRepo.save(subject);
							return subject;
						});
						subjects.add(subject1);
					});
					
					academicProgram.setSubjects(subjects);
					academicProgram = academicProgramRepo.save(academicProgram);
					
					structure.setStatus(HttpStatus.CREATED.value());
					structure.setMessage("Added subject List to Academic Program");
					structure.setData(academicProgramServiceImpl.mapObjectToAcademicProgramResponse(academicProgram));
					
					return new ResponseEntity<ResponseStructure<AcademicProgramResponse>>(structure,HttpStatus.CREATED);
					
				})
				.orElseThrow(()-> new AcademicProgramNotFoundByIdException("Failed to save Subjects!!!"));
	}
	
	public List<Subject> getSubjectObjectList(SubjectRequest subjectRequest){
		List<Subject> subjects=new ArrayList<>();
		subjectRequest.getSubjectNames().forEach(subName->{
			Subject subject1=subjectRepo.findBySubjectName(subName.toLowerCase()).map(subject->{
				return subject;
			}).orElseGet(()->{
				Subject subject = new Subject();
				subject.setSubjectName(subName.toLowerCase());
				subject=subjectRepo.save(subject);
				return subject;
			});
			subjects.add(subject1);
		});
		return subjects;
		
	}

	@Override
	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> updateSubjectsToAcademy(int programId,
			SubjectRequest newSubjectRequest) {
		return academicProgramRepo.findById(programId)
				.map(academicProgram->{
					List<Subject> existingSubObjList=academicProgram.getSubjects();
					if (!existingSubObjList.isEmpty()) {
						getSubjectObjectList(newSubjectRequest).forEach(newSubject->{
							if(!existingSubObjList.contains(newSubject))
								existingSubObjList.add(newSubject);
						});
						academicProgram.setProgramId(programId);
						academicProgram.setSubjects(existingSubObjList);
						academicProgram=academicProgramRepo.save(academicProgram);
						
						structure.setStatus(HttpStatus.OK.value());
						structure.setMessage("New subject List updated to Academin-Program");
						structure.setData(academicProgramServiceImpl.mapObjectToAcademicProgramResponse(academicProgram));
						return new ResponseEntity<ResponseStructure<AcademicProgramResponse>>(structure,HttpStatus.OK);
					} else 
						throw new IllegalRequestException("Failed to update Subjects b/z Academic-Program is not"
								+ " allocated with subjects!!!");
				})
				.orElseThrow(()-> new AcademicProgramNotFoundByIdException("Failed to update the Subjects!!!"));
	}

}











