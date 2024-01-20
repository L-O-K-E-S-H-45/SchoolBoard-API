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
import com.school.sba.response_dto.SubjectResponse;
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
	private ResponseStructure<SubjectResponse> subjectResponseStructure;

	@Autowired
	private ResponseStructure<List<Subject>> subjectsObjectStructure;

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

	public Subject getSubjectBySubjectName(String subjectName){
		return subjectRepo.findBySubjectName(subjectName.toLowerCase()).map(subject->{
			return subject;
		}).orElseGet(()->{
			return subjectRepo.save(Subject.builder()
					.subjectName(subjectName.toLowerCase())
					.build());
		});

	}
	
	@Override
	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> save_update_SubjectsToAcademy(
			int programId, SubjectRequest subjectRequest){
		return academicProgramRepo.findById(programId)
				.map(academy->{
					List<Subject> subjects=(academy.getSubjects()!=null)?academy.getSubjects():new ArrayList();
					List<Subject> newSubjects=new ArrayList();
					subjectRequest.getSubjectNames().forEach(name->{
						boolean isPresent=false;
						for (Subject subject : subjects) {
							if (name.equalsIgnoreCase(subject.getSubjectName())) {
								newSubjects.add(subject);
								isPresent=true;
								break;
							}
						}
						if (!isPresent)
							newSubjects.add(subjectRepo.findBySubjectName(name.toLowerCase())
									.orElseGet(()->subjectRepo.save(Subject.builder().subjectName(name.toLowerCase()).build())));
					});
					academy.setSubjects(newSubjects);
					academy=academicProgramRepo.save(academy);
					structure.setStatus(HttpStatus.OK.value());
					structure.setMessage("New subject List saved or updated to Academin-Program");
					structure.setData(academicProgramServiceImpl.mapObjectToAcademicProgramResponse(academy));
					return new ResponseEntity<ResponseStructure<AcademicProgramResponse>>(structure,HttpStatus.OK);
				})
				.orElseThrow(()-> new AcademicProgramNotFoundByIdException("Failed to save or update the Subjects!!!"));
	}

	@Override
	public ResponseEntity<ResponseStructure<List<Subject>>> findAllSubjects() {
		List<Subject> subjects= (subjectRepo.findAll()!=null)?subjectRepo.findAll():new ArrayList<>();
		if (!subjects.isEmpty()) {
			subjectsObjectStructure.setStatus(HttpStatus.FOUND.value());
			subjectsObjectStructure.setMessage("Subjects List is found successfully!!!!");
			subjectsObjectStructure.setData(subjects);
			return new ResponseEntity<ResponseStructure<List<Subject>>>(subjectsObjectStructure,HttpStatus.FOUND);
		} else {
			subjectsObjectStructure.setStatus(HttpStatus.NOT_FOUND.value());
			subjectsObjectStructure.setMessage("Subjects List is not empty!!!!");
			subjectsObjectStructure.setData(subjects);
			return new ResponseEntity<ResponseStructure<List<Subject>>>(subjectsObjectStructure,HttpStatus.NOT_FOUND);
		}

	}

}











